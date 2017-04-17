package com.fnmanager.domain.orm

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
object Accounts : IdTable<String>("money_account") {

    val notes = varchar("notes", 20000)
    override val id = varchar("username", 20).entityId().references(Users.id)

}

class Account(username: EntityID<String>) : Entity<String>(username) {
    companion object : EntityClass<String, Account>(Accounts)

    var user by User referencedOn Accounts.id
    var notes by Accounts.notes
    val incomesAndOutcomes by Item referrersOn Items.account

    override fun toString(): String = "User: ${id.value}, " +
            "incomes and outcomes: ${incomesAndOutcomes.map { "\n\t$it" }.reduce { s1, s2 -> s1 + s2 }}, " +
            "notes: $notes"

    override fun hashCode(): Int = user.hashCode() + notes.hashCode()



}

fun abc() {
//Feign.builder().decoder()
    GsonBuilder().registerTypeAdapter<Account> {
        serialize {
            jsonObject()
        }
    }
}

fun <T:Any> Transaction.eval(transactionBody: () -> T) {

    logger.addLogger(StdOutSqlLogger())
    Exceptional.of { transactionBody.invoke() }
            .on(false, { rollback() })
            .on(true, { commit() })
            .peek { close() }
            .orElseThrow()
}


