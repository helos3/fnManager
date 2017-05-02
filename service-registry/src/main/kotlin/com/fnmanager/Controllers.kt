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

    path("/registry") {
        post("/register", { req, res ->
            val params: Map<String, String> = toMapGson().fromJson(req.body())
            val created = Registry.register(
                    params["name"],
                req.ip(),
                    params["tags"]?.split(",") ?: emptyList(),
                req.port().toString()
            )
            val returnMsg = created?.let {
                """ Service with name ${it.name}
                    registered on
                    address ${it.address}:${it.port} """
            } ?: "Error creating service"
            res.status(200)
            jsonObject("message" to returnMsg).toString()
        })

        get("/:serviceName", { req, res ->

            val services = Registry.discover(
                    req.params("serviceName"),
                    req.queryParams("tags")?.split(":") ?: emptyList()
            )

            res.status(200)

            if (services.isEmpty()) jsonObject("Error" to "No services found").toString()
            else jsonObject(
                    "found services" to jsonArray(
                            services.map { it.toJson().toString() })).toString()
        })
    }
}