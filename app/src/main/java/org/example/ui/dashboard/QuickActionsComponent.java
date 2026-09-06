
package org.example.ui.dashboard;

import android.view.View;
import org.example.R;

public class QuickActionsComponent {

    public interface Listener {
        void onAddExpense();
        void onExpenses();
        void onSummary();
        void onCategories();
    }

    public QuickActionsComponent(View root, Listener listener) {
        root.findViewById(R.id.action_add_expense).setOnClickListener(v -> listener.onAddExpense());
        root.findViewById(R.id.action_expenses).setOnClickListener(v -> listener.onExpenses());
        root.findViewById(R.id.action_summary).setOnClickListener(v -> listener.onSummary());
        root.findViewById(R.id.action_categories).setOnClickListener(v -> listener.onCategories());
    }
}