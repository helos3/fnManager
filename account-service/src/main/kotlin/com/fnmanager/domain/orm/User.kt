package com.fnmanager.domain.orm

import com.fnmanager.domain.orm.Users.primaryKey
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.min

object Users : Table() {
    val userName = varchar("userName", 20).primaryKey()
    val password = varchar("password", 20)

}