package com.fnmanager.utils

import com.fnmanager.config.IMicroserviceConfig
import com.fnmanager.config.MicroserviceConfig
import com.fnmanager.controller.AuthController
import com.fnmanager.controller.RegisterController
import spark.Spark
import spark.Spark.port

val config : IMicroserviceConfig = MicroserviceConfig("application.properties")

fun main(args: Array<String>) {
    config.loadIntProperty("server.port")?.let { Spark.port(it) }

    Spark.post( "/register",    RegisterController())
    Spark.get(  "/login",       AuthController())

}