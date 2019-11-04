package com.example.marijah.outflow.room_database

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

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
