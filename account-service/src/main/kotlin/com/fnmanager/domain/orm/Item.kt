package com.fnmanager.domain.orm

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.kotlin.utils.addToStdlib.check
import org.jetbrains.kotlin.utils.sure

import java.util.*
import kotlin.reflect.KProperty

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
    companion object : EntityClass<String, Item>(Items) {
        fun create(init: Item.() -> Unit) : Item {

//            fun a() = new(init)
//            a().name="huy"
            val item = new(init)
            item.name="huy"
            return item
        }
    }

    var account by Account referencedOn Items.account
    var name by Items.name
    var type by Items.type
    var amount by Items.amount

    fun abc() : Item = new("adsa", {})



    override fun toString(): String =
            "Related account:${account.id.value}, name: $name, type: $type, amount: $amount"


    override fun hashCode(): Int = 42 + account.hashCode() + name.hashCode() + amount.hashCode() + type.hashCode()

}

