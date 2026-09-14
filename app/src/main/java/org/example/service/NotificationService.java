package org.example.service;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import org.example.data.preference.PreferenceManager;
import org.example.util.CurrencyFormatter;

public class NotificationService {

    private static final String CHANNEL_ID = "budget_alerts";
    private static final String CHANNEL_NAME = "Budget Alerts";
    private static final int NOTIFICATION_ID_DAILY_LIMIT = 1001;
    private static final int NOTIFICATION_ID_MONTHLY_SUMMARY = 1002;

    private final Context context;
    private final PreferenceManager preferenceManager;

    public NotificationService(Context context) {
        this.context = context;
        this.preferenceManager = new PreferenceManager(context);
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH); // High Importance
            channel.setDescription("Alerts for daily limit and monthly budget summary");

            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public void showDailyLimitExceededNotification(double todaySpent, double dailyLimit) {
        String currency = preferenceManager.getCurrencySymbol();
        String message = "You've spent " + CurrencyFormatter.format(todaySpent, currency)
                + " today, over your " + CurrencyFormatter.format(dailyLimit, currency) + " daily limit.";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setContentTitle("Limit Exceeded! ⚠️")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        notify(NOTIFICATION_ID_DAILY_LIMIT, builder);
    }

    public void showMonthlyBudgetSummaryNotification(double totalSpent, double monthlyBudget) {
        int percentUsed = monthlyBudget > 0 ? (int) Math.round((totalSpent / monthlyBudget) * 100) : 0;
        String currency = preferenceManager.getCurrencySymbol();
        String message = percentUsed + "% of your monthly budget used ("
                + CurrencyFormatter.format(totalSpent, currency) + " of "
                + CurrencyFormatter.format(monthlyBudget, currency) + ").";

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Monthly Budget Summary 📊")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        notify(NOTIFICATION_ID_MONTHLY_SUMMARY, builder);
    }

    private void notify(int id, NotificationCompat.Builder builder) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        // Android 13+ (API 33) Permission Check
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        manager.notify(id, builder.build());
    }
}