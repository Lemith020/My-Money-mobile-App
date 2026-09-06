
package org.example.data.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.example.domain.model.Category;
import org.example.domain.model.TransactionType;
import org.example.domain.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

public class SqliteCategoryRepository implements CategoryRepository {

    private final DatabaseHelper databaseHelper;

    public SqliteCategoryRepository(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public List<Category> getCategoriesByType(TransactionType type) {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                DatabaseHelper.TABLE_CATEGORIES,
                null,
                DatabaseHelper.COLUMN_CAT_TYPE + " = ?",
                new String[]{type.name()},
                null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CAT_NAME));
                String icon = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CAT_ICON));
                String color = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CAT_COLOR));
                list.add(new Category(id, name, icon, color, type));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    @Override
    public long addCategory(Category category) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CAT_NAME, category.getName());
        values.put(DatabaseHelper.COLUMN_CAT_ICON, category.getIcon());
        values.put(DatabaseHelper.COLUMN_CAT_COLOR, category.getColor());
        values.put(DatabaseHelper.COLUMN_CAT_TYPE, category.getType().name());

        long id = db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
        db.close();
        return id;
    }
}
