
package org.example.ui.transactions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import org.example.R;
import org.example.domain.model.Transaction;
import org.example.domain.model.TransactionType;
import org.example.util.CurrencyFormatter;
import org.example.util.DateUtils;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactions;
    private final String currencySymbol;

    public TransactionAdapter(List<Transaction> transactions, String currencySymbol) {
        this.transactions = transactions;
        this.currencySymbol = currencySymbol;
    }

    public void updateData(List<Transaction> newTransactions) {
        this.transactions = newTransactions;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction_row, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);
        holder.category.setText(transaction.getCategory());
        holder.note.setText(transaction.getNote());
        holder.date.setText(DateUtils.formatDate(transaction.getDate()));

        String amountText = CurrencyFormatter.format(transaction.getAmount(), currencySymbol);
        if (transaction.getType() == TransactionType.EXPENSE) {
            holder.amount.setText("- " + amountText);
            holder.amount.setTextColor(0xFFE53935);
        } else {
            holder.amount.setText("+ " + amountText);
            holder.amount.setTextColor(0xFF43A047);
        }
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView category;
        TextView note;
        TextView date;
        TextView amount;

        TransactionViewHolder(View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.text_row_category);
            note = itemView.findViewById(R.id.text_row_note);
            date = itemView.findViewById(R.id.text_row_date);
            amount = itemView.findViewById(R.id.text_row_amount);
        }
    }
}