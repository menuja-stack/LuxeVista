package com.example.luxevista;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista.models.AppUser;
import com.example.luxevista.service.UserService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SignupActivity extends AppCompatActivity {
    private TextInputLayout nameLayout, emailLayout, addressLayout, passwordLayout, confirmPasswordLayout;
    private TextInputEditText nameInput, emailInput, addressInput, passwordInput, confirmPasswordInput;
    private Button signupButton;
    private TextView signinText;
    private UserService userService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);

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
        nameLayout = findViewById(R.id.nameLayout);
        emailLayout = findViewById(R.id.emailLayout);
        addressLayout = findViewById(R.id.addressLayout);
        passwordLayout = findViewById(R.id.passwordLayout);
        confirmPasswordLayout = findViewById(R.id.confirmPasswordLayout);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        addressInput = findViewById(R.id.addressInput);
        passwordInput = findViewById(R.id.passwordInput);
        confirmPasswordInput = findViewById(R.id.confirmPasswordInput);

        signupButton = findViewById(R.id.signupButton);
        signinText = findViewById(R.id.signinText);
    }

    private void setupClickListeners() {
        signupButton.setOnClickListener(v -> attemptSignup());

        signinText.setOnClickListener(v -> {
            finish(); // Goes back to SigninActivity
        });
    }

    private void attemptSignup() {
        // Reset errors
        nameLayout.setError(null);
        emailLayout.setError(null);
        addressLayout.setError(null);
        passwordLayout.setError(null);
        confirmPasswordLayout.setError(null);

        // Get values
        String name = nameInput.getText().toString().trim();
        String email = emailInput.getText().toString().trim();
        String address = addressInput.getText().toString().trim();
        String password = passwordInput.getText().toString();
        String confirmPassword = confirmPasswordInput.getText().toString();

        // Validate inputs
        if (!validateInputs(name, email, address, password, confirmPassword)) {
            return;
        }

        // Create new user
        AppUser newUser = new AppUser(name, email, address, password);

        // Attempt to register user
        if (userService.createUser(newUser)>=1) {
            Toast.makeText(this, "Registration successful! Let's Login", Toast.LENGTH_SHORT).show();

            finish();
        } else {
            Toast.makeText(this, "Email already registered", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateInputs(String name, String email, String address,
                                   String password, String confirmPassword) {
        boolean valid = true;

        // Validate name
        if (TextUtils.isEmpty(name)) {
            nameLayout.setError("Name is required");
            valid = false;
        } else if (name.length() < 2) {
            nameLayout.setError("Name must be at least 2 characters");
            valid = false;
        }

        // Validate email
        if (TextUtils.isEmpty(email)) {
            emailLayout.setError("Email is required");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError("Please enter a valid email address");
            valid = false;
        }

        // Validate address
        if (TextUtils.isEmpty(address)) {
            addressLayout.setError("Address is required");
            valid = false;
        } else if (address.length() < 5) {
            addressLayout.setError("Please enter a valid address");
            valid = false;
        }

        // Validate password
        if (TextUtils.isEmpty(password)) {
            passwordLayout.setError("Password is required");
            valid = false;
        } else if (password.length() < 6) {
            passwordLayout.setError("Password must be at least 6 characters");
            valid = false;
        }

        // Validate confirm password
        if (TextUtils.isEmpty(confirmPassword)) {
            confirmPasswordLayout.setError("Please confirm your password");
            valid = false;
        } else if (!password.equals(confirmPassword)) {
            confirmPasswordLayout.setError("Passwords do not match");
            valid = false;
        }

        return valid;
    }
}