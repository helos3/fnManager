package com.fnmanager.domain.orm

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Table

/**
 * Created by rushan on 2/19/2017.
 */
object Accounts : IdTable<String>("money_account") {

    val notes = varchar("notes", 20000)
    override val id = varchar("username", 20).entityId().references(Users.id)


}

class Account(username: EntityID<String>) : Entity<String>(username) {
    companion object : EntityClass<String, Account>(Accounts)

    var user by User referencedOn Accounts.id
    var notes by Accounts.notes
}

