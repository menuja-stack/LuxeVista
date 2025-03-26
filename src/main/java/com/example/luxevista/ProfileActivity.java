package com.example.luxevista;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.luxevista.models.AppUser;
import com.example.luxevista.service.PreferencesManager;
import com.example.luxevista.service.UserService;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {
    private TextInputEditText nameInput, emailInput, addressInput;
    private Button updateButton, deleteAccountButton, logoutButton, myBookingsButton;

    private UserService userService;
    private PreferencesManager preferencesManager;
    private AppUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize services
        userService = new UserService(this);
        preferencesManager = new PreferencesManager(this);

        // Initialize views
        initializeViews();

        // Load current user details
        loadUserDetails();

        // Setup button listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        addressInput = findViewById(R.id.addressInput);

        updateButton = findViewById(R.id.updateButton);
        deleteAccountButton = findViewById(R.id.deleteAccountButton);
        logoutButton = findViewById(R.id.logoutButton);
        myBookingsButton = findViewById(R.id.myBookingsButton);
    }

    private void loadUserDetails() {
        // Get current user ID from preferences
        int userId = preferencesManager.getUserId();

        // Fetch user details
        currentUser = userService.getUser(userId);

        if (currentUser != null) {
            nameInput.setText(currentUser.getName());
            emailInput.setText(currentUser.getEmail());
            addressInput.setText(currentUser.getAddress());
        }
    }

    private void setupButtonListeners() {
        // Update Profile Button
        updateButton.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String address = addressInput.getText().toString().trim();

            if (validateInputs(name, address)) {
                // Update user details
                currentUser.setName(name);
                currentUser.setAddress(address);

                int result = userService.updateUser(currentUser);
                if (result > 0) {
                    Toast.makeText(this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to Update Profile", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Delete Account Button
        deleteAccountButton.setOnClickListener(v -> {
            showConfirmationDialog("Delete Account",
                    "Are you sure you want to delete your account? This action cannot be undone.",
                    (dialog, which) -> {
                        // Delete user account
                        userService.deleteUser(currentUser.getId());

                        // Clear preferences
                        preferencesManager.clearUserId();

                        // Redirect to Login
                        Intent intent = new Intent(this, SigninActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
            );
        });

        // Logout Button
        logoutButton.setOnClickListener(v -> {
            showConfirmationDialog("Logout",
                    "Are you sure you want to logout?",
                    (dialog, which) -> {
                        // Clear preferences
                        preferencesManager.clearUserId();

                        // Redirect to Login
                        Intent intent = new Intent(this, SigninActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
            );
        });

        // My Bookings Button
        myBookingsButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MyBookingsActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs(String name, String address) {
        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name cannot be empty");
            return false;
        }

        if (TextUtils.isEmpty(address)) {
            addressInput.setError("Address cannot be empty");
            return false;
        }

        return true;
    }

    private void showConfirmationDialog(String title, String message,
                                        DialogInterface.OnClickListener positiveAction) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Yes", positiveAction)
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }
}