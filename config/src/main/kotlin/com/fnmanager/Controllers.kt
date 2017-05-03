package com.fnmanager

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.ConfigurationProperties
import org.jetbrains.kotlin.com.intellij.openapi.util.io.FileUtil
import org.jetbrains.kotlin.script.getFileName
import spark.Spark.get
import spark.Spark.path
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

fun registerControllers() {
    val gson by lazy { gson() }
    path("/configuration") {
        get("/:serviceName", { req, res ->
            PropertiesRepository.find(req.params("serviceName"))
        }, gson::toJson)
    }
}