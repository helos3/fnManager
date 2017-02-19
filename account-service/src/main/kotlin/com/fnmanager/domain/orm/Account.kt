package com.fnmanager.domain.orm

import org.jetbrains.exposed.sql.Table

/**
 * Created by rushan on 2/19/2017.
 */
object Accounts : Table() {
    val notes = varchar("notes", 20000)
    val userName = varchar("userName", 20).primaryKey()
    val password = varchar("password", 20)

}