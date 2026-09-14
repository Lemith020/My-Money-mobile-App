package org.example.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import org.example.R;
import org.example.data.local.SqliteBudgetRepository;
import org.example.data.local.SqliteTransactionRepository;
import org.example.data.preference.PreferenceManager;
import org.example.domain.model.Budget;
import org.example.domain.repository.BudgetRepository;
import org.example.domain.repository.TransactionRepository;
import org.example.service.FinancialCalculationService;
import org.example.ui.reports.ReportsFragment;
import org.example.ui.transactions.AddTransactionDialog;
import org.example.ui.transactions.TransactionsFragment;
import org.example.util.CurrencyFormatter;

public class DashboardFragment extends Fragment {

    private TransactionRepository transactionRepository;
    private BudgetRepository budgetRepository;
    private FinancialCalculationService calculationService;
    private PreferenceManager preferenceManager;

    private BalanceCardComponent balanceCardComponent;
    private DailyBudgetCardComponent dailyBudgetCardComponent;
    private MonthlyBudgetCardComponent monthlyBudgetCardComponent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Repositories & Services Setup
        transactionRepository = new SqliteTransactionRepository(requireContext());
        budgetRepository = new SqliteBudgetRepository(requireContext());
        calculationService = new FinancialCalculationService(transactionRepository);
        preferenceManager = new PreferenceManager(requireContext());

        // 1. Header Notification Bell Icon Click Setup
        View buttonNotification = view.findViewById(R.id.button_notification);
        if (buttonNotification != null) {
            buttonNotification.setOnClickListener(v -> showNotificationDialog());
        }

        // 2. Quick Actions Card Listeners Setup
        new QuickActionsComponent(view.findViewById(R.id.card_quick_actions), new QuickActionsComponent.Listener() {
            @Override
            public void onAddExpense() {
                new AddTransactionDialog(requireContext(), () -> loadData()).show();
            }

            @Override
            public void onExpenses() {
                navigateTo(new TransactionsFragment());
            }

            @Override
            public void onSummary() {
                navigateTo(new ReportsFragment());
            }

            @Override
            public void onCategories() {
                navigateTo(new ReportsFragment());
            }
        });

        // Load Initial Dashboard Data
        loadData();
    }

    private void showNotificationDialog() {
        Budget budget = budgetRepository.getBudget();
        double todaySpent = calculationService.getTodaySpent();
        double dailyLimit = budget != null ? budget.getDailyLimit() : 0;
        String currencySymbol = preferenceManager.getCurrencySymbol();

        String title;
        String alertMessage;

        if (dailyLimit > 0 && todaySpent > dailyLimit) {
            double exceeded = todaySpent - dailyLimit;
            title = "⚠️ Daily Limit Exceeded!";
            alertMessage = "You've spent " + CurrencyFormatter.format(todaySpent, currencySymbol) + " today.\n\n"
                    + "Over limit by " + CurrencyFormatter.format(exceeded, currencySymbol) + "!";
        } else {
            title = "✅ Budget Status";
            alertMessage = "You are currently within your daily budget limit.\n\n"
                    + "Today Spent: " + CurrencyFormatter.format(todaySpent, currencySymbol) + "\n"
                    + "Daily Limit: " + CurrencyFormatter.format(dailyLimit, currencySymbol);
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(title)
                .setMessage(alertMessage)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void navigateTo(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private void loadData() {
        if (getView() == null) return;


        String currencySymbol = preferenceManager.getCurrencySymbol();
        Budget budget = budgetRepository.getBudget();

        balanceCardComponent = new BalanceCardComponent(getView().findViewById(R.id.card_balance), currencySymbol);
        dailyBudgetCardComponent = new DailyBudgetCardComponent(getView().findViewById(R.id.card_today), currencySymbol);
        monthlyBudgetCardComponent = new MonthlyBudgetCardComponent(getView().findViewById(R.id.card_monthly), currencySymbol);

        // Calculations
        double monthlyBudgetAmount = budget != null ? budget.getMonthlyBudget() : 0;
        double monthlySpent = calculationService.getMonthlySpent();
        double todaySpent = calculationService.getTodaySpent();
        double dailyLimit = budget != null ? budget.getDailyLimit() : 0;


        double totalIncome = calculationService.getMoneyReceived();

        // Money Received  = monthlyBudgetAmount + totalIncome
        double moneyReceivedCardValue = monthlyBudgetAmount + totalIncome;


        // Available Balance  = (monthlyBudgetAmount + totalIncome) - monthlySpent
        double availableBalance = moneyReceivedCardValue - monthlySpent;

        // Bind data to components
        balanceCardComponent.bind(availableBalance, moneyReceivedCardValue);
        dailyBudgetCardComponent.bind(todaySpent, dailyLimit);
        monthlyBudgetCardComponent.bind(monthlySpent, moneyReceivedCardValue);
    }

    @Override
    public void onResume() {
        super.onResume();

        loadData();
    }
}