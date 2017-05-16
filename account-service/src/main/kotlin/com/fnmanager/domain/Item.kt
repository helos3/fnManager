package com.fnmanager.domain

import com.fnmanager.domain.Account
import com.fnmanager.domain.Accounts
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.kotlin.utils.addToStdlib.check
import org.jetbrains.kotlin.utils.sure

import java.util.*
import kotlin.reflect.KProperty

/**
 * Created by rushan on 2/15/2017.
 */

object Items : IdTable<Long>("item") {
    override val id: Column<EntityID<Long>> = long("id").autoIncrement().primaryKey().entityId()
    val type = reference("item_type", ItemTypes)
    val name = varchar("name", length = 50)
    val account = reference("account", Accounts)
    val amount = decimal("amount", 2, 2)

}

class Item(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, Item>(Items) {
        fun create(acc: Account, init: Item.() -> Unit) : Item = Item.new(init)
    }

    var account by Account referencedOn Items.account
    var name by Items.name
    var type by Items.type
    var amount by Items.amount


    override fun toString(): String =
            "Related account:${account.id.value}, name: $name, type: $type, amount: $amount"


    override fun hashCode(): Int = 42 + account.hashCode() + name.hashCode() + amount.hashCode() + type.hashCode()

}

