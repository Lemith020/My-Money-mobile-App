package org.example.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.example.R;
import org.example.data.local.SqliteBudgetRepository;
import org.example.data.preference.PreferenceManager;
import org.example.domain.model.Budget;
import org.example.domain.repository.BudgetRepository;

public class SettingsFragment extends Fragment {

    private BudgetRepository budgetRepository;
    private PreferenceManager preferenceManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        budgetRepository = new SqliteBudgetRepository(requireContext());
        preferenceManager = new PreferenceManager(requireContext());

        EditText editMonthlyBudget = view.findViewById(R.id.edit_monthly_budget);
        EditText editDailyLimit = view.findViewById(R.id.edit_daily_limit);
        EditText editCurrencySymbol = view.findViewById(R.id.edit_currency_symbol);

        Budget budget = budgetRepository.getBudget();
        editMonthlyBudget.setText(String.valueOf(budget.getMonthlyBudget()));
        editDailyLimit.setText(String.valueOf(budget.getDailyLimit()));
        editCurrencySymbol.setText(preferenceManager.getCurrencySymbol());

        view.findViewById(R.id.button_save_settings).setOnClickListener(v -> {
            double monthlyBudget = parseOrZero(editMonthlyBudget.getText().toString());
            double dailyLimit = parseOrZero(editDailyLimit.getText().toString());
            String currencySymbol = editCurrencySymbol.getText().toString();

            budgetRepository.saveBudget(new Budget(monthlyBudget, dailyLimit));
            preferenceManager.setCurrencySymbol(currencySymbol.isEmpty() ? "Rs." : currencySymbol);

            Toast.makeText(requireContext(), "Settings saved", Toast.LENGTH_SHORT).show();
        });

        view.findViewById(R.id.button_reset_database).setOnClickListener(v -> {
            requireContext().deleteDatabase("my_money.db");
            Toast.makeText(requireContext(), "Database reset. Restart the app.", Toast.LENGTH_LONG).show();
        });
    }

    private double parseOrZero(String text) {
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}