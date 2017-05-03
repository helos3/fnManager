package com.fnmanager

/**
 * Created by berlogic on 18.04.17.
 */
import com.ecwid.consul.v1.health.model.HealthService
import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import spark.Spark.*
import spark.Request
import spark.Spark
import java.util.*


fun registerControllers() {

    val gson by lazy { gson() }

    path("/registry") {
        post("/register", { req, res ->
            val params: Map<String, String> = gson().fromJson(req.body())
            val created = Registry.register(
                params["name"],
                req.ip(),
                params["tags"]?.split(",") ?: emptyList(),
                req.port().toString()
            )

            created?.let {
                res.status(400)
                """ Service with name ${it.name}
                    registered on
                    address ${it.address}:${it.port} """
            } ?: {
                res.status(400)
                "Error creating service"
            }
        }, gson::toJson)

        get("/:serviceName", { req, res ->

            val services = Registry.discover(
                req.params("serviceName"),
                req.queryParams("tags")?.split(":") ?: emptyList()
            )

            res.status(200)
            services
        }, gson::toJson)
    }
}