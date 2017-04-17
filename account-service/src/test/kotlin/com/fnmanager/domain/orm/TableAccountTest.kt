package com.fnmanager.domain.orm

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fnmanager.domain.orm.Accounts
import com.fnmanager.domain.orm.Accounts.notes
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

    @Test fun gsonTest() {
        transaction {
            initSchema.invoke()
            var peter = User.new("peter", {
                password = "someverydumbpass"
            })
            print(peter.abc().toJson(peter))
        }
    }

    @Test fun insertStatement() {

        transaction {
            initSchema.invoke()
            var peter = User.new("peter", {
                password = "someverydumbpass"
            })


            var ivan = User.new("ivan", {
                password = "somedumbdayuff"
            })
            assertTrue { User.all().count() == 2 }

            val ivanAccount = Account.new(ivan.id.value, {

                notes = "blablabla"
            })

            Item.new {
                account = ivanAccount
                name = "idk"
                type = ItemType.DAILY
                amount = 5000
            }

            Item.new {
                account = ivanAccount
                name = "qwe"
                type = ItemType.DAILY
                amount = 5000
            }

            Item.new {
                account = ivanAccount
                name = "asd"
                type = ItemType.DAILY
                amount = 5000
            }
            Item.create{
                account = ivanAccount
                name = "asssssss"
                type = ItemType.DAILY
                amount = 5000
            }
            val temp = Item.create{
                account = ivanAccount
                name = "daym"
                type = ItemType.DAILY
                amount = 5000
            }
            temp.name = "pidor"


            assertTrue { ivanAccount.incomesAndOutcomes.count() == 5}
            ivanAccount.incomesAndOutcomes.toList().forEach { println(it.toString()) }
            println(ivanAccount.toString())
            println(ivan.id.value)




        }



    }

}
