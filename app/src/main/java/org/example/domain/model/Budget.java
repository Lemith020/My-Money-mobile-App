package org.example.domain.model;

public class Budget {
    private double monthlyBudget;
    private double dailyLimit;

    public Budget(double monthlyBudget, double dailyLimit) {
        this.monthlyBudget = monthlyBudget;
        this.dailyLimit = dailyLimit;
    }

    public double getMonthlyBudget() {
        return monthlyBudget;
    }

    public double getDailyLimit() {
        return dailyLimit;
    }
}
