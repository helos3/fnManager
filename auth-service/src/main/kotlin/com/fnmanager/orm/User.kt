package com.fnmanager.orm

import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.jsonObject
import com.github.salomonbrys.kotson.registerTypeAdapter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import sun.security.provider.SHA
import sun.security.util.Password
import java.security.SecureRandom


object Users : IdTable<String>("account") {
    override val id = varchar("username", 20).entityId()
    val password = varchar("password", 255)
    val salt = varchar("salt", 255)
}

class User(username: EntityID<String>) : Entity<String>(username) {
    companion object : EntityClass<String, User>(Users) {
        @JvmStatic fun create(id: String, password: String): User =
            User.new(id) {
                salt = String(SecureRandom().generateSeed(32))
                this.password = password
            }

    }

    var password by Users.password
    var salt by Users.salt
    val login: String
        get() = this.id.value

//    override fun cre

    override fun toString(): String = "User: $login"

    override fun hashCode(): Int = 42 + login.hashCode() + password.hashCode()

    override fun equals(other: Any?): Boolean =
        when (other) {
            is User -> other.login == login && other.password == password
            else -> false
        }


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
