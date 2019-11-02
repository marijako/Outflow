package com.example.marijah.outflow.room_database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface ExpenseDao
{
    @Query("select * from expenses")
    List<Expense> getExpenseList();

    @Insert
    void insertExpense(Expense expense);

    @Delete
    void deleteExpense(Expense expense);
}
