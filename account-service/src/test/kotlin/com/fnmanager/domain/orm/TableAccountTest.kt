package com.fnmanager.domain.orm

import com.fnmanager.domain.orm.Accounts
import com.fnmanager.domain.orm.User
import com.fnmanager.domain.orm.Users
import com.fnmanager.domain.orm.Users.password
import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.EntityIDColumnType
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.statements.Statement
import org.jetbrains.exposed.sql.transactions.ThreadLocalTransactionManager
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sun.font.CreatedFontTracker
import kotlin.test.assertTrue

/**
 * Created by rushan on 2/21/2017.
 */

class TableAccountTest {


    @Before fun beforeAll() {
        Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    }

    @After fun afterAll() {
    }


    internal val initSchema = {
        create(Users, Accounts, Items)
    }


    @Test fun insertStatement() {
        TransactionManager.currentOrNew(1).eval {
            var peter = User.new("peter", {
                password = "someverydumbpass"
            })

            var ivan = User.new("ivan", {
                password = "somedumbdayuff"
            })
            assertTrue { User.all().count { true } == 2 }
        }
    }

//    @RunWith()
}
