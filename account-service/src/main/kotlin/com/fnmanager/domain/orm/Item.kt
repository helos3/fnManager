package com.fnmanager.domain.orm

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.kotlin.utils.addToStdlib.check

/**
 * Created by rushan on 2/15/2017.
 */


enum class ItemType {
    ONCE_ONLY, DAILY, MONTHLY, YEARLY;
}


//object Items : IntIdTable
object Items : Table() {

    val id = integer("id").primaryKey()
    val type = enumeration("item_type", ItemType::class.java);
    val name = varchar("name", length = 50) // Column<String>
//    val referencingAccount = varchar("account_id").references(Accounts.userName)//.check { Accounts. }

}

