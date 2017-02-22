package com.fnmanager.domain.orm

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.kotlin.utils.addToStdlib.check

import java.util.*

/**
 * Created by rushan on 2/15/2017.
 */


enum class ItemType {
    ONCE_ONLY, DAILY, MONTHLY, YEARLY;
}

object Items : IdTable<String>("item") {
    override val id: Column<EntityID<String>> = varchar("uuid", 36).primaryKey().entityId().clientDefault {
        EntityID(UUID.randomUUID().toString(), Items)
    }
    val type = enumeration("item_type", ItemType::class.java);
    val name = varchar("name", length = 50) // Column<String>
    val account = reference("account", Accounts)
    val amount = integer("amount")
//    val referencingAccount = varchar("account_id").references(Accounts.userName)//.check { Accounts. }
}

class Account(username: EntityID<String>) : Entity<String>(username) {
    companion object : EntityClass<String, Account>(Accounts)
    var user by User referencedOn Accounts.id
    var notes by Accounts.notes
//    var incomes by Item referrersOn Items.account
}


class Item(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, Item>(Items)
    var account by Account referencedOn Items.account
    var name by Items.name
    var type by Items.type
    var amount by Items.amount


}

