package org.example.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.example.R;
import org.example.ui.dashboard.DashboardFragment;
import org.example.ui.reports.ReportsFragment;
import org.example.ui.settings.SettingsFragment;
import org.example.ui.transactions.AddTransactionDialog;
import org.example.ui.transactions.TransactionsFragment;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Android 13+ Notification Permission Request
        requestNotificationPermission();

        if (savedInstanceState == null) {
            loadFragment(new DashboardFragment());
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);


        MenuItem placeholderItem = bottomNavigationView.getMenu().findItem(R.id.nav_placeholder);
        if (placeholderItem != null) {
            placeholderItem.setEnabled(false);
        }

        // 3. Navigation Item Selection Logic
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                loadFragment(new DashboardFragment());
                return true;
            } else if (id == R.id.nav_expenses) {
                loadFragment(new TransactionsFragment());
                return true;
            } else if (id == R.id.nav_reports) {
                loadFragment(new ReportsFragment());
                return true;
            } else if (id == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
                return true;
            }
            return false;
        });

        // 4. Floating Center (+) Button Action
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> new AddTransactionDialog(this, () -> {

                Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (currentFragment instanceof DashboardFragment) {
                    loadFragment(new DashboardFragment());
                } else if (currentFragment instanceof TransactionsFragment) {
                    loadFragment(new TransactionsFragment());
                }
            }).show());
        }
    }

    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE
                );
            }
        }
    }

    private void loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }
    }
}