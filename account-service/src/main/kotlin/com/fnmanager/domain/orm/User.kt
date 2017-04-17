package com.fnmanager.domain.orm

import com.fnmanager.domain.orm.Users.primaryKey
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import feign.Feign
import feign.gson.GsonDecoder
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.min
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty

object Users : IdTable<String>("account") {
    override val id = varchar("username", 20).entityId()
    val password = varchar("password", 20)
}

class User(username: EntityID<String>) : Entity<String>(username) {
    companion object : EntityClass<String, User>(Users)

    var password by Users.password
    val login : String
            get() = this.id.value
//    val login by Users.id is bugged



    override fun toString(): String = "User: $login"

    override fun hashCode(): Int = 42 + login.hashCode() + password.hashCode()


    fun gson(): Gson {
        return GsonBuilder()
                .registerTypeAdapter<User> {
                    serialize {
                        jsonObject("login" to login, "password" to password)
                    }
                    deserialize {
                        User.findById(it.json["login"].asString)
                    }
                }
                .create()

    }
}


