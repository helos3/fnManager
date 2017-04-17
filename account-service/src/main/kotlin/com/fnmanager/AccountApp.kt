package com.fnmanager

import com.fnmanager.domain.orm.Accounts
import com.fnmanager.domain.orm.Items
import com.fnmanager.domain.orm.User
import com.fnmanager.domain.orm.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction
import spark.Spark.*

/**
 * Created by rushan on 2/15/2017.
 */



fun main(args: Array<String>) {
    Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver")

    var peter = transaction {
        create(Users, Accounts, Items)
        User.new("peter", {
            password = "someverydumbpass"
        })
    }


    get("/hw") {req, res -> peter.gson().toJson(peter)}


}
