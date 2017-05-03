package com.fnmanager.registry

import com.github.salomonbrys.kotson.*
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import feign.*
import feign.gson.GsonDecoder
import javax.xml.ws.Service

/**
 * Created by berlogic on 25.04.17.
 */

data class ServiceData(val serviceName: String,
                       val address: String,
                       val port: String,
                       val tags: List<String>) {
    companion object {
        @JvmStatic fun gsonAdapter(): TypeAdapter<ServiceData> =
            typeAdapter {
                jsonDeserializer {
                    read {
                        ServiceData(it.json.obj["name"].asString,
                            it.json.obj["address"].asString,
                            it.json.obj["port"].asString,
                            it.json.obj["tags"].asString.split(","))
                    }
                }
            }
    }
}

internal interface Registry {
    @RequestLine("POST /registry/register")
    @Headers("Content-Type: application/json")
    @Body("%7B\"name\": \"{serviceName}\", \"tags\": \"{tags}\"%7D")
    fun register(@Param("serviceName") serviceName: String, @Param("tags") tags: String): String

    @RequestLine("GET /registry/{serviceName}")
    fun discover(@Param("serviceName") serviceName: String): List<ServiceData>

    companion object {
        fun connect(address: String): Registry = Feign.builder()
            .decoder(
                GsonDecoder(GsonBuilder()
                    .registerTypeAdapter<List<ServiceData>> {
                        deserialize {
                            it.json.obj["error"]?.let { listOf<ServiceData>() } ?:
                            it.json.obj["found services"].array.map {
                                ServiceData.gsonAdapter().fromJsonTree(it)
                            }
                        }
                    }
                    .create()))
            .logger(Logger.ErrorLogger())
            .logLevel(Logger.Level.BASIC)
            .target(Registry::class.java, address)
    }


}