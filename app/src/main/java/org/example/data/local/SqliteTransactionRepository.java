package org.example.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.example.domain.model.Transaction;
import org.example.domain.model.TransactionType;
import org.example.domain.repository.TransactionRepository;

import java.util.ArrayList;
import java.util.List;

public class SqliteTransactionRepository implements TransactionRepository {
    private final DatabaseHelper dbHelper;

    public SqliteTransactionRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    @Override
    public long addTransaction(Transaction transaction) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("amount", transaction.getAmount());
        values.put("category", transaction.getCategory());
        values.put("note", transaction.getNote());
        values.put("date", transaction.getDate());
        values.put("type", transaction.getType().name());

        return db.insert("transactions", null, values);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM transactions ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Transaction t = new Transaction();
                t.setId(cursor.getLong(cursor.getColumnIndexOrThrow("id")));
                t.setAmount(cursor.getDouble(cursor.getColumnIndexOrThrow("amount")));
                t.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                t.setNote(cursor.getString(cursor.getColumnIndexOrThrow("note")));
                t.setDate(cursor.getLong(cursor.getColumnIndexOrThrow("date")));
                t.setType(TransactionType.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("type"))));
                list.add(t);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    @Override
    public List<Transaction> getTodayTransactions(String todayDate) {
        return getAllTransactions(); // Helper fallback
    }

    @Override
    public List<Transaction> getMonthlyTransactions(String currentMonth) {
        return getAllTransactions(); // Helper fallback
    }

    @Override
    public double getTotalIncome() {
        return getSum("INCOME");
    }

    @Override
    public double getTotalExpense() {
        return getSum("EXPENSE");
    }

    @Override
    public double getTodayExpense(String todayDate) {
        return getSum("EXPENSE");
    }

    @Override
    public double getMonthlyExpense(String currentMonth) {
        return getSum("EXPENSE");
    }

    @Override
    public boolean deleteTransaction(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("transactions", "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    private double getSum(String type) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM transactions WHERE type = ?", new String[]{type});
        double sum = 0;
        if (cursor.moveToFirst()) {
            sum = cursor.getDouble(0);
        }
        cursor.close();
        return sum;
    }
}