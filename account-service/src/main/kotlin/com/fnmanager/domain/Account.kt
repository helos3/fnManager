package com.fnmanager.domain

import com.fnmanager.domain.orm.*
import com.github.salomonbrys.kotson.jsonDeserializer
import com.github.salomonbrys.kotson.*
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import feign.Feign
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.kotlin.utils.addToStdlib.check
import org.jetbrains.kotlin.utils.addToStdlib.singletonOrEmptyList
import java.util.*

/**
 * Created by rushan on 2/19/2017.
 */
object Accounts : IdTable<String>("account") {

    override val id = varchar("username", 20).entityId()
    val notes = varchar("notes", 20000)


}

class Account(username: EntityID<String>) : Entity<String>(username) {
    companion object : EntityClass<String, Account>(Accounts)

    fun createItem(init: Item.() -> Unit): Item =
        Item.new{ account = this@Account }.also(init)

    fun createItemType(init: ItemType.() -> Unit): ItemType =
        ItemType.new{ account = this@Account }.also(init)

}