package com.fnmanager

import com.natpryce.konfig.ConfigurationProperties
import org.jetbrains.kotlin.script.getFileName
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask
import kotlin.reflect.KProperty

/**
 * Created by berlogic on 17.04.17.
 */
class PropertiesHandler {


    internal var cache: MutableMap<String, ConfigurationProperties> = HashMap()

    fun handle(serviceName: String): ConfigurationProperties {
        cache.computeIfAbsent(serviceName, {
//            val props by PropertiesDelegate(it)
        })
//        return cache[serviceName]
    }

}


internal class PropertiesDelegate(fileName: String) {

    var config = ConfigurationProperties.fromFile(File("/shared/$fileName.properties"))

    init {
        fixedRateTimer(name = "$fileName-file-updater", initialDelay = 0, period = 60000) {
            config = if (Date().time - File("/shared/$fileName.properties").lastModified() < 60000)
                ConfigurationProperties.fromFile(File("/shared/$fileName.properties"))
            else
                config
        }
    }

    fun getValue() : ConfigurationProperties = config



}