package com.fnmanager

import com.ecwid.consul.v1.health.model.HealthService
import com.github.salomonbrys.kotson.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.orbitz.consul.model.health.ServiceHealth
import org.jetbrains.kotlin.types.checker.intersectTypes

fun JsonElement.toMap() = obj.entrySet().map { it.key to it.value.asString }.toMap()


fun gson() =
    GsonBuilder()
        .registerTypeAdapter<Map<String, String>> {
            deserialize { it.json.toMap() }
        }
        .registerTypeAdapter<List<ServiceHealth>> {
            serialize {
                it.src.takeIf { !it.isEmpty() }?.let {
                    jsonObject(
                        "found services" to jsonArray(
                            it.map { it.toJson() }))
                } ?:
                jsonObject("error" to "No services found")
            }
        }
        .registerTypeAdapter<String> {
            serialize { jsonObject("message" to it.src) }
        }
        .create()


fun ServiceHealth.toJson() =
    jsonObject("address" to service.address,
        "port" to service.port,
        "name" to service.service,
        "id" to service.id,
        "tags" to service.tags.fold("") { res, str -> "$res,$str" })

