package org.example.ui.transactions;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import org.example.R;
import org.example.data.local.SqliteTransactionRepository;
import org.example.data.preference.PreferenceManager;
import org.example.domain.model.Transaction;
import org.example.domain.repository.TransactionRepository;

public class TransactionsFragment extends Fragment {

    private TransactionRepository transactionRepository;
    private PreferenceManager preferenceManager;
    private TransactionAdapter adapter;
    private List<Transaction> allTransactions;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transactions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        transactionRepository = new SqliteTransactionRepository(requireContext());
        preferenceManager = new PreferenceManager(requireContext());
        allTransactions = transactionRepository.getAllTransactions();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_transactions);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TransactionAdapter(allTransactions, preferenceManager.getCurrencySymbol());
        recyclerView.setAdapter(adapter);

        EditText editSearch = view.findViewById(R.id.edit_search);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void filter(String query) {
        List<Transaction> filtered = new ArrayList<>();
        for (Transaction transaction : allTransactions) {
            if (transaction.getCategory().toLowerCase().contains(query.toLowerCase()) ||
                    transaction.getNote().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(transaction);
            }
        }
        adapter.updateData(filtered);
    }

    @Override
    public void onResume() {
        super.onResume();
        allTransactions = transactionRepository.getAllTransactions();
        adapter.updateData(allTransactions);
    }
}
