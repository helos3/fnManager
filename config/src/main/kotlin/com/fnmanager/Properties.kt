package com.fnmanager

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.ConfigurationProperties
import java.io.File
import java.util.*
import kotlin.concurrent.fixedRateTimer
import kotlin.reflect.KProperty

/**
 * Created by berlogic on 19.04.17.
 */
object PropertiesRepository {
    internal var cache: MutableMap<String, Configuration> = HashMap()

    fun find(serviceName: String): Configuration =
            cache.computeIfAbsent(serviceName,
                    { filePrefix -> val config by PropertiesDelegate(filePrefix); config })
}

internal class PropertiesDelegate(fileName: String?,
                                  private var config: Configuration? = null) {
    init {
        fileName?.let { File("shared/$it.properties") }
                ?.takeIf { it.isFile }
                ?.let {
                    fixedRateTimer(name = "$fileName-file-updater", initialDelay = 0, period = 60000) {
                        config = if (Date().time - it.lastModified() < 60000)
                            ConfigurationProperties.fromFile(it)
                        else
                            config
                    }
                }
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): Configuration = config ?: ConfigurationMap()
}


