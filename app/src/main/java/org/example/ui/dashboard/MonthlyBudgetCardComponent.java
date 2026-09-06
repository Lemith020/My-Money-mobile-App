package org.example.ui.dashboard;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import org.example.R;
import org.example.util.CurrencyFormatter;
import org.example.util.DateUtils;

public class MonthlyBudgetCardComponent {

    private final TextView textMonthLabel;
    private final TextView textMonthlySpent;
    private final TextView textMonthlyBudget;
    private final TextView textMonthlyRemaining;
    private final ProgressBar progressMonthlyBudget;
    private final TextView textPercentUsed;
    private final TextView textPercentLeft;
    private final String currencySymbol;

    public MonthlyBudgetCardComponent(View root, String currencySymbol) {
        this.textMonthLabel = root.findViewById(R.id.text_month_label);
        this.textMonthlySpent = root.findViewById(R.id.text_monthly_spent);
        this.textMonthlyBudget = root.findViewById(R.id.text_monthly_budget);
        this.textMonthlyRemaining = root.findViewById(R.id.text_monthly_remaining);
        this.progressMonthlyBudget = root.findViewById(R.id.progress_monthly_budget);
        this.textPercentUsed = root.findViewById(R.id.text_percent_used);
        this.textPercentLeft = root.findViewById(R.id.text_percent_left);
        this.currencySymbol = currencySymbol;
    }

    public void bind(double spent, double budget) {
        double remaining = budget - spent;
        int percentUsed = budget > 0 ? (int) ((spent / budget) * 100) : 0;
        int percentLeft = 100 - percentUsed;

        textMonthLabel.setText("THIS MONTH (" + DateUtils.getCurrentMonthName() + ")");
        textMonthlySpent.setText(CurrencyFormatter.format(spent, currencySymbol));
        textMonthlyBudget.setText(CurrencyFormatter.format(budget, currencySymbol));
        textMonthlyRemaining.setText(CurrencyFormatter.format(remaining, currencySymbol));
        progressMonthlyBudget.setProgress(percentUsed);
        textPercentUsed.setText(percentUsed + "% of budget used");
        textPercentLeft.setText(percentLeft + "% left");
    }
}
