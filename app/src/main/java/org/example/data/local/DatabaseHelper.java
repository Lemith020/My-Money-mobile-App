
package org.example.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_money.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_NOTE = "note";
    public static final String COLUMN_DATE = "date";

    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CAT_NAME = "name";
    public static final String COLUMN_CAT_ICON = "icon";
    public static final String COLUMN_CAT_COLOR = "color";
    public static final String COLUMN_CAT_TYPE = "type";

    public static final String TABLE_BUDGET = "budget";
    public static final String COLUMN_MONTHLY_BUDGET = "monthly_budget";
    public static final String COLUMN_DAILY_LIMIT = "daily_limit";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TRANSACTIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TYPE + " TEXT NOT NULL, " +
                COLUMN_AMOUNT + " REAL NOT NULL, " +
                COLUMN_CATEGORY + " TEXT, " +
                COLUMN_NOTE + " TEXT, " +
                COLUMN_DATE + " INTEGER NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CAT_NAME + " TEXT NOT NULL, " +
                COLUMN_CAT_ICON + " TEXT, " +
                COLUMN_CAT_COLOR + " TEXT, " +
                COLUMN_CAT_TYPE + " TEXT NOT NULL)");

        db.execSQL("CREATE TABLE " + TABLE_BUDGET + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MONTHLY_BUDGET + " REAL NOT NULL, " +
                COLUMN_DAILY_LIMIT + " REAL NOT NULL)");

        insertDefaultCategories(db);
        insertDefaultBudget(db);
    }

    private void insertDefaultCategories(SQLiteDatabase db) {
        String[] expenseCategories = {"Food", "Transport", "Rent", "Shopping", "Other"};
        for (String name : expenseCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CAT_NAME, name);
            values.put(COLUMN_CAT_ICON, "");
            values.put(COLUMN_CAT_COLOR, "#7C3AED");
            values.put(COLUMN_CAT_TYPE, "EXPENSE");
            db.insert(TABLE_CATEGORIES, null, values);
        }
        String[] incomeCategories = {"Salary", "Allowance", "Freelancing", "Other"};
        for (String name : incomeCategories) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_CAT_NAME, name);
            values.put(COLUMN_CAT_ICON, "");
            values.put(COLUMN_CAT_COLOR, "#22C55E");
            values.put(COLUMN_CAT_TYPE, "INCOME");
            db.insert(TABLE_CATEGORIES, null, values);
        }
    }

    private void insertDefaultBudget(SQLiteDatabase db) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MONTHLY_BUDGET, 0);
        values.put(COLUMN_DAILY_LIMIT, 0);
        db.insert(TABLE_BUDGET, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        onCreate(db);
    }
}