package com.fnmanager

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.registerTypeAdapter
import com.github.salomonbrys.kotson.typeAdapter
import com.google.gson.GsonBuilder

data class Credentials(val login: String, val password: String) {
    companion object {

        val gson = GsonBuilder()
            .registerTypeAdapter<Credentials> {
                deserialize {
                    Credentials(
                        it.json["login"].asString,
                        it.json["password"].asString
                    )
                }
            }
            .create()

        fun fromJson(json: String) : Credentials {
            return gson.fromJson<Credentials>(json)
        }

        fun from(loginAndPassword: String): Credentials {
            fun create(decoded: String): Credentials {
                val data = decoded.split(":")
                return Credentials(data[0], data[1])
            }
            val data = loginAndPassword.split(":")

            return when (data.size) {
                1 -> create(decodeBase64(loginAndPassword).toString(Charsets.UTF_8))
                2 -> create(loginAndPassword)
                else -> throw IllegalStateException("Failed to parse credentials")
            }
        }
    }

    fun areCorrect() = login.length > 3 && password.length > 5

}
