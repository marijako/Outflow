package com.example.marijah.outflow.room_database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

import java.util.ArrayList

@Dao
interface ExpenseDao {
    @get:Query("select * from expenses")
    val expenseList: List<Expense>

    @Insert
    fun insertExpense(expense: Expense)

    @Delete
    fun deleteExpense(expense: Expense)
}
