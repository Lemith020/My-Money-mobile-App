package org.example.ui.dashboard;

import android.graphics.Color;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.TextView;
import org.example.R;
import org.example.util.CurrencyFormatter;

public class DailyBudgetCardComponent {

    private final TextView textTodaySpent;
    private final TextView textTodayRemaining;
    private final TextView textDailyLimit;
    private final TextView textTodayStatus;
    private final String currencySymbol;

    public DailyBudgetCardComponent(View root, String currencySymbol) {
        this.textTodaySpent = root.findViewById(R.id.text_today_spent);
        this.textTodayRemaining = root.findViewById(R.id.text_today_remaining);
        this.textDailyLimit = root.findViewById(R.id.text_daily_limit);
        this.textTodayStatus = root.findViewById(R.id.text_today_status);
        this.currencySymbol = currencySymbol;
    }

    public void bind(double spent, double dailyLimit) {
        double remaining = dailyLimit - spent;

        textTodaySpent.setText(CurrencyFormatter.format(spent, currencySymbol));
        textDailyLimit.setText("Daily Limit: " + CurrencyFormatter.format(dailyLimit, currencySymbol));


        textTodayRemaining.clearAnimation();

        if (remaining >= 0) {
            // Normal Status (Green)
            textTodayRemaining.setTextColor(Color.parseColor("#10B981"));
            textTodayRemaining.setText(CurrencyFormatter.format(remaining, currencySymbol));

            textTodayStatus.setText("You're doing great!");
            textTodayStatus.setTextColor(Color.parseColor("#6B7280"));
        } else {
            // Exceeded Status (Red & Positive Exceeded Amount)
            double exceededAmount = Math.abs(remaining);

            // Red Color Direct Apply
            textTodayRemaining.setTextColor(Color.parseColor("#E53935"));
            textTodayRemaining.setText("⚠ " + CurrencyFormatter.format(exceededAmount, currencySymbol) + " Over");

            textTodayStatus.setText("Limit Exceeded!");
            textTodayStatus.setTextColor(Color.parseColor("#EF4444"));


            startBlinkAnimation(textTodayRemaining);
        }
    }

    private void startBlinkAnimation(View view) {
        AlphaAnimation blink = new AlphaAnimation(0.3f, 1.0f);
        blink.setDuration(500); // Blink Speed (0.5s)
        blink.setRepeatMode(Animation.REVERSE);
        blink.setRepeatCount(Animation.INFINITE);
        view.startAnimation(blink);
    }
}