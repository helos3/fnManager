package com.fnmanager.domain.orm

import com.fnmanager.domain.orm.Users.primaryKey
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.min

object Users : IdTable<String>("account") {
    override val id = varchar("username", 20).primaryKey().entityId()
    val password = varchar("password", 20)
}

class User(username: EntityID<String>) : Entity<String>(username) {
    companion object : EntityClass<String, User>(Users)

    var password by Users.password

}
