package org.example.ui.transactions;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;
import org.example.R;
import org.example.data.local.SqliteBudgetRepository;
import org.example.data.local.SqliteTransactionRepository;
import org.example.domain.model.Budget;
import org.example.domain.model.Transaction;
import org.example.domain.model.TransactionType;
import org.example.domain.repository.BudgetRepository;
import org.example.domain.repository.TransactionRepository;
import org.example.service.FinancialCalculationService;
import org.example.service.NotificationService;

public class AddTransactionDialog {

    public interface OnTransactionSavedListener {
        void onSaved();
    }

    private final Context context;
    private final OnTransactionSavedListener listener;
    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final FinancialCalculationService calculationService;
    private final NotificationService notificationService;

    public AddTransactionDialog(Context context, OnTransactionSavedListener listener) {
        this.context = context;
        this.listener = listener;
        this.transactionRepository = new SqliteTransactionRepository(context);
        this.budgetRepository = new SqliteBudgetRepository(context);
        this.calculationService = new FinancialCalculationService(transactionRepository);
        this.notificationService = new NotificationService(context);
    }

    public void show() {
        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_transaction);

        RadioButton radioExpense = dialog.findViewById(R.id.radio_expense);
        EditText editAmount = dialog.findViewById(R.id.edit_amount);
        EditText editCategory = dialog.findViewById(R.id.edit_category);
        EditText editNote = dialog.findViewById(R.id.edit_note);

        dialog.findViewById(R.id.button_save_transaction).setOnClickListener(v -> {
            String amountText = editAmount.getText().toString();
            String category = editCategory.getText().toString();
            String note = editNote.getText().toString();

            if (amountText.isEmpty() || category.isEmpty()) {
                Toast.makeText(context, "Amount and Category are required", Toast.LENGTH_SHORT).show();
                return;
            }

            double amount = Double.parseDouble(amountText);
            TransactionType type = radioExpense.isChecked() ? TransactionType.EXPENSE : TransactionType.INCOME;

            Transaction transaction = new Transaction(0, type, amount, category, note, System.currentTimeMillis());
            transactionRepository.addTransaction(transaction);

            // --- Notification Check Logic ---
            if (type == TransactionType.EXPENSE) {
                double todaySpent = calculationService.getTodaySpent();
                Budget budget = budgetRepository.getBudget();

                if (budget != null && todaySpent > budget.getDailyLimit()) {
                    notificationService.showDailyLimitExceededNotification(todaySpent, budget.getDailyLimit());
                }
            }

            Toast.makeText(context, "Transaction saved", Toast.LENGTH_SHORT).show();
            listener.onSaved();
            dialog.dismiss();
        });

        dialog.show();

        // Dialog Width එක Screen එකේ 90% දක්වා පළල් කිරීම සහ Shadow Cutouts අයින් කිරීම
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(
                    (int) (context.getResources().getDisplayMetrics().widthPixels * 0.90),
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }
}