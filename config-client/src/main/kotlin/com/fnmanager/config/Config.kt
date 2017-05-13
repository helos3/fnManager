package com.fnmanager.config

import com.github.salomonbrys.kotson.*
import com.google.gson.GsonBuilder
import com.natpryce.konfig.*
import feign.Feign
import feign.Logger
import feign.Param
import feign.RequestLine
import feign.gson.GsonDecoder
import java.io.File
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.reflect.KProperty

/**
 * Created by berlogic on 20.04.17.
 */


interface IMicroserviceConfig {
    fun loadProperty(key: String): String?
    fun loadIntProperty(key: String): Int?
    fun loadBoolProperty(key: String): Boolean?
}

class MicroserviceConfig(fileName: String) : IMicroserviceConfig {

    val localConfig by LocalPropertiesDelegate(fileName)
    val remoteConfig by RemotePropertiesDelegate(
        localConfig[Key("configuration.address", stringType)],
        localConfig[Key("configuration.service_name", stringType)])


    override fun loadProperty(key: String): String? =
        localConfig.getOrNull(Key(key, stringType)) ?:
            remoteConfig.getOrNull(Key(key, stringType))


    override fun loadIntProperty(key: String): Int? =
        localConfig.getOrNull(Key(key, intType)) ?:
            remoteConfig.getOrNull(Key(key, intType))


    override fun loadBoolProperty(key: String): Boolean? =
        localConfig.getOrNull(Key(key, booleanType)) ?:
            remoteConfig.getOrNull(Key(key, booleanType))

}


internal class RemotePropertiesDelegate(address: String, serviceName: String,
                                        private var config: Configuration? = null) {
    init {
        fixedRateTimer(name = "$serviceName-remote-updater", initialDelay = 0, period = 60000) {
            config = ConfigurationMap(Config.connect(address).getProperties(serviceName))
        }
    }


    operator fun getValue(thisRef: Any?, property: KProperty<*>): Configuration = config ?: ConfigurationMap()
}


internal class LocalPropertiesDelegate(fileName: String?,
                                       private var config: Configuration? = null) {
    init {
        fileName?.let(::File)
            ?.takeIf { it.isFile }
            ?.let {
                fixedRateTimer(name = "${it.name}-file-updater", initialDelay = 0, period = 60000) {
                    config = if (Date().time - it.lastModified() < 60000)
                        ConfigurationProperties.fromFile(it)
                    else
                        config
                }
            }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Configuration = config ?: ConfigurationMap()
}

internal interface Config {
    @RequestLine("GET /configuration/{serviceName}")
    fun getProperties(@Param("serviceName") serviceName: String): Map<String, String>

    companion object {
        fun connect(address: String): Config = Feign.builder()
            .decoder(GsonDecoder(GsonBuilder()
                .registerTypeAdapter<Map<String, String>> {
                    deserialize {
                        it.json.obj["error"]?.let { mapOf<String, String>() } ?:
                            it.json.asJsonObject.toMap()
                                .map { it.key to it.value.asString }
                                .toMap()
                    }
                }
                .create()))
            .logger(Logger.ErrorLogger())
            .logLevel(Logger.Level.BASIC)
            .target(Config::class.java, address)
    }
}