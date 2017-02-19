package com.fnmanager.domain.orm

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Table

/**
 * Created by rushan on 2/15/2017.
 */


enum class ItemType {
    ONCE_ONLY, DAILY, MONTHLY, YEARLY;
}


//object Items : IntIdTable
object Items : Table() {
    val id = varchar("id", 10).primaryKey() // Column<String>
    val type = enumeration("item_type", ItemType::class.java);
    val name = varchar("name", length = 50) // Column<String>


    //    val shit = enumeration()
//    val cityId = (integer("city_id") references Cities.id).nullable() // Column<Int?>
}