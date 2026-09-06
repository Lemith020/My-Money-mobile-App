package org.example.ui.dashboard;

import android.view.View;
import android.widget.TextView;
import org.example.R;
import org.example.util.CurrencyFormatter;

public class BalanceCardComponent {

    private final TextView textAvailableBalance;
    private final TextView textMoneyReceived;
    private final String currencySymbol;

    public BalanceCardComponent(View root, String currencySymbol) {
        this.textAvailableBalance = root.findViewById(R.id.text_available_balance);
        this.textMoneyReceived = root.findViewById(R.id.text_money_received);
        this.currencySymbol = currencySymbol;
    }

    public void bind(double availableBalance, double moneyReceived) {
        textAvailableBalance.setText(CurrencyFormatter.format(availableBalance, currencySymbol));
        textMoneyReceived.setText("Money Received: " + CurrencyFormatter.format(moneyReceived, currencySymbol));
    }
}