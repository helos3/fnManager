package com.fnmanager.domain

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.kotlin.js.backend.ast.JsLiteral
import java.util.*
import kotlin.properties.Delegates
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

object ItemTypes : IdTable<Long>("item_type") {
    override val id: Column<EntityID<Long>> = long("id").autoIncrement().primaryKey().entityId()
    val name = varchar("name", 50)
    val account = optReference("account", Accounts)
    val icon = varchar("icon_name", 50)
}

class ItemType(id: EntityID<Long>) : Entity<Long>(id) {
    companion object : EntityClass<Long, ItemType>(ItemTypes) {
        fun create(acc: Account?, init: ItemType.() -> Unit) : ItemType =
            ItemType.new(init).also{ it.account = acc }
    }
    // noid
    var name
        get() = ItemTypes.name.lookup()
        set(value) = account?.let { ItemTypes.name.setValue(this, ItemType::name, value) } ?: Unit
    var icon: String
        get() = ItemTypes.icon.lookup()
        set(value) = account?.let { ItemTypes.icon.setValue(this, ItemType::icon, value) } ?: Unit
    var account by Account optionalReferencedOn ItemTypes.account
}

