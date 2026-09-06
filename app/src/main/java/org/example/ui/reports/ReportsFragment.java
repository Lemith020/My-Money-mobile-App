package org.example.ui.reports;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.R;
import org.example.data.local.SqliteTransactionRepository;
import org.example.data.preference.PreferenceManager;
import org.example.domain.model.Transaction;
import org.example.domain.model.TransactionType;
import org.example.domain.repository.TransactionRepository;
import org.example.util.CurrencyFormatter;
import org.example.util.DateUtils;

public class ReportsFragment extends Fragment {

    private TransactionRepository transactionRepository;
    private PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionRepository = new SqliteTransactionRepository(requireContext());
        preferenceManager = new PreferenceManager(requireContext());

        List<Transaction> transactions = transactionRepository.getAllTransactions();
        String currencySymbol = preferenceManager.getCurrencySymbol();

        TextView textMonth = view.findViewById(R.id.text_report_month);
        TextView textTotalExpense = view.findViewById(R.id.text_report_total_expense);
        TextView textTotalIncome = view.findViewById(R.id.text_report_total_income);
        LinearLayout container = view.findViewById(R.id.container_category_breakdown);

        textMonth.setText("This Month (" + DateUtils.getCurrentMonthName() + ")");

        double totalExpense = 0;
        double totalIncome = 0;
        Map<String, Double> categoryTotals = new HashMap<>();

        for (Transaction transaction : transactions) {
            if (transaction.getType() == TransactionType.EXPENSE) {
                totalExpense += transaction.getAmount();
                double current = categoryTotals.containsKey(transaction.getCategory()) ? categoryTotals.get(transaction.getCategory()) : 0;
                categoryTotals.put(transaction.getCategory(), current + transaction.getAmount());
            } else {
                totalIncome += transaction.getAmount();
            }
        }

        // --- Color Force Fix using SpannableString ---

        // Income (Green)
        String incomeFormatted = CurrencyFormatter.format(totalIncome, currencySymbol);
        SpannableString incomeSpannable = new SpannableString(incomeFormatted);
        incomeSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#10B981")), 0, incomeFormatted.length(), 0);
        textTotalIncome.setText(incomeSpannable);

        // Expense (Red)
        String expenseFormatted = CurrencyFormatter.format(totalExpense, currencySymbol);
        SpannableString expenseSpannable = new SpannableString(expenseFormatted);
        expenseSpannable.setSpan(new ForegroundColorSpan(Color.parseColor("#EF4444")), 0, expenseFormatted.length(), 0);
        textTotalExpense.setText(expenseSpannable);

        // Dynamic Category Rows
        container.removeAllViews();
        for (Map.Entry<String, Double> entry : categoryTotals.entrySet()) {
            TextView row = new TextView(requireContext());
            row.setText(entry.getKey() + ": " + CurrencyFormatter.format(entry.getValue(), currencySymbol));
            row.setTextColor(Color.parseColor("#1F2937"));
            row.setTextSize(14);
            row.setPadding(0, 12, 0, 12);
            container.addView(row);
        }

        view.findViewById(R.id.button_export).setOnClickListener(v ->
                new ExportPdfService(requireContext()).export(transactions, currencySymbol));
    }
}