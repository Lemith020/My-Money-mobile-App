package org.example.domain.repository;

import org.example.domain.model.Transaction;
import java.util.List;

public interface TransactionRepository {
    long addTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    List<Transaction> getTodayTransactions(String todayDate);
    List<Transaction> getMonthlyTransactions(String currentMonth);
    double getTotalIncome();
    double getTotalExpense();
    double getTodayExpense(String todayDate);
    double getMonthlyExpense(String currentMonth);
    boolean deleteTransaction(long id);
}