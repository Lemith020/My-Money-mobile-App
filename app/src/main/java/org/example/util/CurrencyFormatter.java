
package org.example.util;

import java.util.Locale;

public class CurrencyFormatter {
    public static String format(double amount, String currencySymbol) {
        if (currencySymbol == null || currencySymbol.isEmpty()) {
            currencySymbol = "Rs.";
        }
        return String.format(Locale.getDefault(), "%s %,.2f", currencySymbol, amount);
    }
}