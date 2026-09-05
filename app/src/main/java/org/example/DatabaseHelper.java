package org.example;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "my_money.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE expenses (id INTEGER PRIMARY KEY AUTOINCREMENT, amount REAL, category TEXT, note TEXT, date TEXT)");
        db.execSQL("CREATE TABLE settings (key TEXT PRIMARY KEY, value TEXT)");
        db.execSQL("INSERT INTO settings (key, value) VALUES ('daily_limit', '1000')");
        db.execSQL("INSERT INTO settings (key, value) VALUES ('monthly_budget', '30000')");
        db.execSQL("INSERT INTO settings (key, value) VALUES ('money_received', '30000')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        db.execSQL("DROP TABLE IF EXISTS settings");
        onCreate(db);
    }

    public long addExpense(double amount, String category, String note) {
        SQLiteDatabase db = getWritableDatabase();
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new java.util.Date());
        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("category", category);
        values.put("note", note);
        values.put("date", date);
        return db.insert("expenses", null, values);
    }

    public double getTodaySpent() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new java.util.Date());
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(amount) FROM expenses WHERE date LIKE ?", new String[]{today + "%"});
        double total = 0;
        if (c.moveToFirst() && !c.isNull(0)) total = c.getDouble(0);
        c.close();
        return total;
    }

    public double getYesterdaySpent() {
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.add(java.util.Calendar.DATE, -1);
        String yesterday = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(amount) FROM expenses WHERE date LIKE ?", new String[]{yesterday + "%"});
        double total = 0;
        if (c.moveToFirst() && !c.isNull(0)) total = c.getDouble(0);
        c.close();
        return total;
    }

    public double getMonthSpent() {
        String month = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new java.util.Date());
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(amount) FROM expenses WHERE date LIKE ?", new String[]{month + "%"});
        double total = 0;
        if (c.moveToFirst() && !c.isNull(0)) total = c.getDouble(0);
        c.close();
        return total;
    }

    public double getHighestDay() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT SUM(amount) as total FROM expenses GROUP BY substr(date,1,10) ORDER BY total DESC LIMIT 1", null);
        double total = 0;
        if (c.moveToFirst() && !c.isNull(0)) total = c.getDouble(0);
        c.close();
        return total;
    }

    public Map<String, Double> getCategoryTotals() {
        Map<String, Double> map = new HashMap<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT category, SUM(amount) FROM expenses GROUP BY category", null);
        while (c.moveToNext()) {
            map.put(c.getString(0), c.getDouble(1));
        }
        c.close();
        return map;
    }

    public double getSetting(String key) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT value FROM settings WHERE key = ?", new String[]{key});
        double val = 0;
        if (c.moveToFirst()) val = Double.parseDouble(c.getString(0));
        c.close();
        return val;
    }

    public void setSetting(String key, double value) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("value", String.valueOf(value));
        db.update("settings", values, "key = ?", new String[]{key});
    }
}