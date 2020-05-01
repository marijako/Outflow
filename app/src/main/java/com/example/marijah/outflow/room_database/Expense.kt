package com.example.marijah.outflow.room_database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room creates a table for each class annotated with @Entity;
 * the fields in the class correspond to columns in the table.
 */

@Entity(tableName = "expenses")
class Expense {
    @PrimaryKey(autoGenerate = true)
    var key: Int = 0

    @ColumnInfo(name = "cost")
    var price: Int = 0

    var place: String = ""
    var category: String = ""
    var date: String = ""
    var comment: String = ""


    constructor( price: Int, category: String, place: String, date: String, comment: String) {
        this.price = price
        this.category = category
        this.place = place
        this.date = date
        this.comment = comment
    }
}
