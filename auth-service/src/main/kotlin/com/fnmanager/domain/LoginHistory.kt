package com.fnmanager.domain

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.IdTable
import org.joda.time.DateTime


object LoginHistory : IdTable<String>("login_history") {
    override val id = varchar("login", 20).entityId() references Users.id
    val date = datetime("date").default(DateTime.now())
}

class LoginEntry(user: User) : Entity<String>(user.id) {
    companion object : EntityClass<String, LoginEntry>(LoginHistory) {
        @JvmStatic fun create(user: User): LoginEntry {
            User.findById(user.login) ?: throw IllegalStateException("User already exists")
            return new(user.login) {}
        }
    }

    val login: String
        get() = this.id.value
    val date by LoginHistory.date

    override fun toString(): String = "User: $login, login date: $date"

    override fun hashCode(): Int = 42 + String.format("%s%s", login,  date.toString()).hashCode()

    override fun equals(other: Any?): Boolean =
        when (other) {
            is LoginEntry -> other.login == login && other.date == date
            else -> false
        }

}
