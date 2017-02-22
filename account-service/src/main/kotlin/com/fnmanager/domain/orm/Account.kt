package com.fnmanager.domain.orm

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.Transaction

/**
 * Created by rushan on 2/19/2017.
 */
object Accounts : IdTable<String>("money_account") {

    val notes = varchar("notes", 20000)
    override val id = varchar("username", 20).entityId().references(Users.id)


}

//class Account(username: EntityID<String>) : Entity<String>(username) {
//    companion object : EntityClass<String, Account>(Accounts)
//    var user by User referencedOn Accounts.id
//    var notes by Accounts.notes
//    var incomes by Item referrersOn Items.account
////    var incomes by  Item.backReferencedOn(Items.account)
//}


fun <T> Transaction.eval(transactionBody: () -> T) {

    logger.addLogger(StdOutSqlLogger())
    try {
        transactionBody.invoke()
    } catch (e: Exception) {
        rollback()
    }
    commit()
    close()

Account.new {
//    incomes
}


}
