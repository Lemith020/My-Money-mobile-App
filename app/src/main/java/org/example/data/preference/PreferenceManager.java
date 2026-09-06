package org.example.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static final String PREF_NAME = "student_money_prefs";
    private static final String KEY_MONTHLY_BUDGET = "monthly_budget";
    private static final String KEY_DAILY_LIMIT = "daily_limit";
    private static final String KEY_CURRENCY = "currency_symbol";

    private final SharedPreferences preferences;

    public PreferenceManager(Context context) {
        this.preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setMonthlyBudget(double budget) {
        preferences.edit().putFloat(KEY_MONTHLY_BUDGET, (float) budget).apply();
    }

    public double getMonthlyBudget() {
        return preferences.getFloat(KEY_MONTHLY_BUDGET, 30000.0f);
    }

    public void setDailyLimit(double limit) {
        preferences.edit().putFloat(KEY_DAILY_LIMIT, (float) limit).apply();
    }

    public double getDailyLimit() {
        return preferences.getFloat(KEY_DAILY_LIMIT, 1000.0f);
    }

    public void setCurrencySymbol(String symbol) {
        preferences.edit().putString(KEY_CURRENCY, symbol).apply();
    }

    public String getCurrencySymbol() {
        return preferences.getString(KEY_CURRENCY, "Rs.");
    }
}