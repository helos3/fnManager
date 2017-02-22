package com.fnmanager.domain.orm

import org.apache.catalina.realm.X509SubjectDnRetriever
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
    override val id: Column<EntityID<String>> = varchar("uuid", 36).entityId().clientDefault {
        EntityID(UUID.randomUUID().toString(), Items)
    }
    val type = enumeration("item_type", ItemType::class.java);
    val name = varchar("name", length = 50) // Column<String>
    val account = reference("account", Accounts)
    val amount = integer("amount")
}

class Item(id: EntityID<String>) : Entity<String>(id) {
    companion object : EntityClass<String, Item>(Items)

    var account by Account referencedOn Items.account
    var name by Items.name
    var type by Items.type
    var amount by Items.amount


    override fun toString(): String =
            "Related account:${account.id.value}, name: $name, type: $type, amount: $amount"


    override fun hashCode(): Int = 42 + account.hashCode() + name.hashCode() + amount.hashCode() + type.hashCode()
}

