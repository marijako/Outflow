package com.example.marijah.outflow.room_database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
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
