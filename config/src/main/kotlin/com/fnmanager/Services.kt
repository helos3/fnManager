package com.fnmanager

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.ConfigurationProperties
import org.jetbrains.kotlin.script.getFileName
import java.io.File
import java.nio.file.Files
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.concurrent.timer
import kotlin.concurrent.timerTask
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * Created by berlogic on 17.04.17.
 */
class PropertiesHandler {
    companion object {
        internal var cache: MutableMap<String, Configuration> = HashMap()

        fun handle(serviceName: String): Configuration =
                cache.computeIfAbsent(serviceName, { ab -> val abc by PropertiesDelegate(ab); abc })
    }
}


internal class PropertiesDelegate(fileName: String) {

    var config : Configuration? = null

    init {
        fixedRateTimer(name = "$fileName-file-updater", initialDelay = 0, period = 60000) {
            config = if (Date().time - File("/shared/$fileName.properties").lastModified() < 60000)
                ConfigurationProperties.fromFile(File("/shared/$fileName.properties"))
            else
                config
        }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>) : Configuration = config ?: ConfigurationMap()

}
