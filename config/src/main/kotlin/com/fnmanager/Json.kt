package com.fnmanager

import com.github.salomonbrys.kotson.jsonArray
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.GsonBuilder
import com.natpryce.konfig.Configuration

fun gson() = GsonBuilder()
    .registerTypeAdapter<Configuration> {
        serialize { it.src.toJson() }
    }
    .create()


fun Configuration.toJson() = list()
    .takeIf { it.isNotEmpty() }
    ?.let {
        jsonObject("properties" to
            jsonObject(it
                .map { it.second }
                .fold(mutableMapOf<String, String>()) {res, map -> res.putAll(map); res}
                .map { it.key to it.value }
            ))

    } ?: jsonObject("error" to "no configuration found")


