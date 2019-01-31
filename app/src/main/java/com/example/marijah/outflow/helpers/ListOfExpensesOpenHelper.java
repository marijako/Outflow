package com.example.marijah.outflow.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.marijah.outflow.models.ListOfExpensesItem;

public class ListOfExpensesOpenHelper extends SQLiteOpenHelper {


    // It's a good idea to always define a log tag like this.
    private static final String TAG = ListOfExpensesOpenHelper.class.getSimpleName();

    // has to be 1 first time or app will crash
    private static final int DATABASE_VERSION = 1;
    private static final String EXPENSES_TABLE = "word_entries";
    private static final String DATABASE_NAME = "wordlist";

    // Column names...
    public static final String KEY_ID = "_id";
    public static final String COST_COLUMN = "cost";
    public static final String CATEGORY_COLUMN = "category";
    public static final String WHERE_COLUMN = "where";
    public static final String DATE_COLUMN = "date";
    public static final String COMMENT_COLUMN = "comment";


    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;


    // ... and a string array of columns.
    private static final String[] COLUMNS = {KEY_ID, COST_COLUMN, CATEGORY_COLUMN, WHERE_COLUMN, DATE_COLUMN, COMMENT_COLUMN};


    private static final String EXPENSES_TABLE_CREATE = "CREATE TABLE " + EXPENSES_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY, " +
                    // id will auto-increment if no value passed
                    COST_COLUMN + " TEXT );";


    public ListOfExpensesOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EXPENSES_TABLE_CREATE);

        fillDatabaseWithData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(ListOfExpensesItem.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + EXPENSES_TABLE);
        onCreate(db);

    }


    private void fillDatabaseWithData(SQLiteDatabase db)
    {

        String[] words = {"2250", "5490", "450", "1200",
                "240", "760", "6080",
                "4500", "330","180",
                "9870"};


        ContentValues values = new ContentValues();

        for (int i=0; i < words.length; i++) {
            // Put column/value pairs into the container.
            // put() overrides existing values.
            values.put(COST_COLUMN, words[i]);
            db.insert(EXPENSES_TABLE, null, values);
        }


    }

    public ListOfExpensesItem query(int position) {

        String query = "SELECT  * FROM " + EXPENSES_TABLE +
                " ORDER BY " + KEY_ID + " DESC " +
                "LIMIT " + position + ",1";

        Cursor cursor = null;
        ListOfExpensesItem entry = new ListOfExpensesItem();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setmId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setmCost(cursor.getString(cursor.getColumnIndex(COST_COLUMN)));
        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return entry;
        }


    }






}
