package com.fnmanager

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.Location
import java.io.File
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * Created by berlogic on 19.04.17.
 */
internal fun reduce(values: List<Pair<Any, Map<String, String>>>) =
    values
        .map { it.second }
        .fold(mutableMapOf<String, String>()) { res, map -> res.putAll(map); res }


fun Configuration.merge(another: Configuration): Configuration =
    ConfigurationMap(
        list()
            .let(::reduce)
            .also { it.putAll(another.list().let(::reduce)) }
    )


object PropertiesRepository {
    internal var cache: MutableMap<String, Configuration> = HashMap()
    internal val default: Configuration by PropertiesDelegate("application", false)

    fun find(serviceName: String): Configuration =
        cache.computeIfAbsent(serviceName,
            { filePrefix -> val config by PropertiesDelegate(filePrefix); config.merge(default) })
}

internal class PropertiesDelegate(fileName: String?,
                                  shared: Boolean = true,
                                  private var config: Configuration? = null) {
    init {
        fileName?.let { File("${shared.takeIf { it }?.let { "shared/" }}$it.properties") }
            ?.takeIf {it.isFile}
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

