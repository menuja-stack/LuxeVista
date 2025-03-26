package com.example.luxevista;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista.models.AppUser;
import com.example.luxevista.service.PreferencesManager;
import com.example.luxevista.service.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SigninActivity extends AppCompatActivity {
    private TextInputLayout emailLayout, passwordLayout;
    private TextInputEditText emailInput, passwordInput;
    private Button signinButton;
    private TextView signupText;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UserService
        userService = new UserService(this);

        // Initialize views
        initializeViews();

        // Set click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        emailLayout = findViewById(R.id.emailLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        signinButton = findViewById(R.id.signinButton);
        signupText = findViewById(R.id.signupText);
    }

    private void setupClickListeners() {
        signinButton.setOnClickListener(v -> attemptLogin());

        signupText.setOnClickListener(v -> {
            Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    private void attemptLogin() {
        // Reset errors
        emailLayout.setError(null);
        passwordLayout.setError(null);

        // Get values
        String email = emailInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();

        // Validate inputs
        if (!validateInputs(email, password)) {
            return;
        }

        // Attempt login
        AppUser user = userService.login(email, password);
        if (user != null) {
            PreferencesManager preferencesManager = new PreferencesManager(this);
            preferencesManager.saveUserId(user.getId());
            // Login successful
            Toast.makeText(this, "Welcome back, " + user.getName(), Toast.LENGTH_SHORT).show();
            // Navigate to main app screen
            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String email, String password) {
        boolean valid = true;

        // Check email
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Please enter a valid email address");
            valid = false;
        }

        // Check password
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            valid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            valid = false;
        }

        return valid;
    }
}