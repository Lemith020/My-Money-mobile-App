package org.example.service;

import org.example.domain.repository.TransactionRepository;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FinancialCalculationService {
    private final TransactionRepository repository;

    public FinancialCalculationService(TransactionRepository repository) {
        this.repository = repository;
    }

    public double getAvailableBalance() {
        return repository.getTotalIncome() - repository.getTotalExpense();
    }

    public double getMoneyReceived() {
        return repository.getTotalIncome();
    }

    public double getTodaySpent() {
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        return repository.getTodayExpense(today);
    }

    public double getMonthlySpent() {
        String currentMonth = new SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(new Date());
        return repository.getMonthlyExpense(currentMonth);
    }
}
