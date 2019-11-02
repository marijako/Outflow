package com.example.marijah.outflow.room_database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = Expense.class, exportSchema = false, version = 1)
public abstract class ExpenseDatabase extends RoomDatabase {

    private static final String DB_NAME = "expense_db";
    private static ExpenseDatabase instance;

    public static synchronized ExpenseDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context, ExpenseDatabase.class, DB_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    public abstract ExpenseDao expenseDao();

}
