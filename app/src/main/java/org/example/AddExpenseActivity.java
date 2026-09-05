package org.example;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class AddExpenseActivity extends Activity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        db = new DatabaseHelper(this);

        EditText etAmount = findViewById(R.id.etAmount);
        EditText etNote = findViewById(R.id.etNote);
        Spinner spCategory = findViewById(R.id.spCategory);
        Button btnSave = findViewById(R.id.btnSave);

        String[] categories = { "Food", "Boarding", "Transport", "Other" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                categories);
        spCategory.setAdapter(adapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountStr = etAmount.getText().toString().trim();
                if (amountStr.isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "Enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                double amount = Double.parseDouble(amountStr);
                String category = spCategory.getSelectedItem().toString();
                String note = etNote.getText().toString().trim();

                db.addExpense(amount, category, note);
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}