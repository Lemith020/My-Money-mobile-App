package org.example;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Map;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends Activity {

    private DatabaseHelper db;
    private static final int REQ_ADD_EXPENSE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);

        View cardBalance = findViewById(R.id.cardBalance);
        View cardToday = findViewById(R.id.cardToday);
        View cardCategories = findViewById(R.id.cardCategories);
        View cardInsights = findViewById(R.id.cardInsights);
        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);

        animateCard(cardBalance, 100);
        animateCard(cardToday, 250);
        animateCard(cardCategories, 400);
        animateCard(cardInsights, 550);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MainActivity.this, AddExpenseActivity.class), REQ_ADD_EXPENSE);
            }
        });

        refreshData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_ADD_EXPENSE && resultCode == RESULT_OK) {
            refreshData();
        }
    }

    private void refreshData() {
        DecimalFormat fmt = new DecimalFormat("#,##0");

        double moneyReceived = db.getSetting("money_received");
        double dailyLimit = db.getSetting("daily_limit");
        double monthlyBudget = db.getSetting("monthly_budget");
        double monthSpent = db.getMonthSpent();
        double todaySpent = db.getTodaySpent();
        double yesterdaySpent = db.getYesterdaySpent();
        double balance = moneyReceived - monthSpent;
        double todayRemaining = dailyLimit - todaySpent;

        ((TextView) findViewById(R.id.tvBalance)).setText("Rs. " + fmt.format(balance));
        ((TextView) findViewById(R.id.tvMoneyReceived)).setText("Money Received: Rs. " + fmt.format(moneyReceived));

        ((TextView) findViewById(R.id.tvTodaySpent)).setText("Spent\nRs. " + fmt.format(todaySpent));
        ((TextView) findViewById(R.id.tvTodayRemaining))
                .setText("Remaining\nRs. " + fmt.format(Math.max(todayRemaining, 0)));

        int progress = monthlyBudget > 0 ? (int) ((monthSpent / monthlyBudget) * 100) : 0;
        ((android.widget.ProgressBar) findViewById(R.id.budgetProgress)).setProgress(Math.min(progress, 100));

        TextView tvBudgetStatus = findViewById(R.id.tvBudgetStatus);
        if (todayRemaining >= 0) {
            tvBudgetStatus.setText("🟢 Rs. " + fmt.format(todayRemaining) + " under budget");
        } else {
            tvBudgetStatus.setText("🔴 Rs. " + fmt.format(Math.abs(todayRemaining)) + " over budget");
        }

        ((TextView) findViewById(R.id.tvDayCompare))
                .setText("🔄 Yesterday: Rs. " + fmt.format(yesterdaySpent) + " | Today: Rs. " + fmt.format(todaySpent));

        TextView tvSavedMsg = findViewById(R.id.tvSavedMsg);
        double diff = yesterdaySpent - todaySpent;
        if (diff >= 0) {
            tvSavedMsg.setText("🎉 You saved Rs. " + fmt.format(diff) + " today!");
        } else {
            tvSavedMsg.setText("⚠️ You spent Rs. " + fmt.format(Math.abs(diff)) + " more than yesterday");
        }

        Map<String, Double> categoryTotals = db.getCategoryTotals();
        ((TextView) findViewById(R.id.tvFoodAmount))
                .setText("Rs. " + fmt.format(categoryTotals.getOrDefault("Food", 0.0)));
        ((TextView) findViewById(R.id.tvBoardingAmount))
                .setText("Rs. " + fmt.format(categoryTotals.getOrDefault("Boarding", 0.0)));
        ((TextView) findViewById(R.id.tvTransportAmount))
                .setText("Rs. " + fmt.format(categoryTotals.getOrDefault("Transport", 0.0)));
        ((TextView) findViewById(R.id.tvOtherAmount))
                .setText("Rs. " + fmt.format(categoryTotals.getOrDefault("Other", 0.0)));

        Calendar cal = Calendar.getInstance();
        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        double avgPerDay = dayOfMonth > 0 ? monthSpent / dayOfMonth : 0;
        double highestDay = db.getHighestDay();
        ((TextView) findViewById(R.id.tvMonthlyAvg))
                .setText("Average/day: Rs. " + fmt.format(avgPerDay) + "\nHighest day: Rs. " + fmt.format(highestDay));

        double daysLeft = avgPerDay > 0 ? balance / avgPerDay : 0;
        ((TextView) findViewById(R.id.tvMoneyLast)).setText("💡 Money can last ~" + fmt.format(daysLeft) + " days");
    }

    private void animateCard(View view, int delay) {
        AnimationSet set = new AnimationSet(true);
        TranslateAnimation slideUp = new TranslateAnimation(0, 0, 100, 0);
        slideUp.setDuration(500);
        AlphaAnimation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setDuration(500);
        set.addAnimation(slideUp);
        set.addAnimation(fadeIn);
        set.setStartOffset(delay);
        view.startAnimation(set);
    }
}