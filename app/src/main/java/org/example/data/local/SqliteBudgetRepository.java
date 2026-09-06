package org.example.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import org.example.domain.model.Budget;
import org.example.domain.repository.BudgetRepository;

public class SqliteBudgetRepository implements BudgetRepository {

    private final DatabaseHelper databaseHelper;

    public SqliteBudgetRepository(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public Budget getBudget() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_BUDGET, null, null, null, null, null, DatabaseHelper.COLUMN_ID + " ASC", "1");
        double monthlyBudget = 0;
        double dailyLimit = 0;
        if (cursor.moveToFirst()) {
            monthlyBudget = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MONTHLY_BUDGET));
            dailyLimit = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DAILY_LIMIT));
        }
        cursor.close();
        db.close();
        return new Budget(monthlyBudget, dailyLimit);
    }

    @Override
    public void saveBudget(Budget budget) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MONTHLY_BUDGET, budget.getMonthlyBudget());
        values.put(DatabaseHelper.COLUMN_DAILY_LIMIT, budget.getDailyLimit());
        db.update(DatabaseHelper.TABLE_BUDGET, values, DatabaseHelper.COLUMN_ID + "=1", null);
        db.close();
    }
}
