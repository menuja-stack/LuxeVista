package com.example.luxevista;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.luxevista.models.AppUser;
import com.example.luxevista.service.PreferencesManager;
import com.example.luxevista.service.UserService;

public class HomeActivity extends AppCompatActivity {
    private UserService userService;
    private PreferencesManager preferencesManager;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        userService = new UserService(this);
        preferencesManager = new PreferencesManager(this);

        initializeViews();
        setupWindowInsets();
        loadUserAndShowWelcome();
        showRoomsFragment();
        setupProfileButton();
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.welcomeText);
    }

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void loadUserAndShowWelcome() {
        int userId = preferencesManager.getUserId();
        if (userId != -1) {
            AppUser user = userService.getUser(userId);
            if (user != null) {
                welcomeText.setText("Welcome, " + user.getName());
            }
        }
    }

    private void showRoomsFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, new RoomsFragment())
                .commit();
    }

    private void setupProfileButton() {
        ImageButton profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Option 1: Open Profile Activity
                Intent profileIntent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profileIntent);

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        int userId = preferencesManager.getUserId();
        if (userId != -1) {
            AppUser user = userService.getUser(userId);
            if (user != null) {
                welcomeText.setText("Welcome, " + user.getName());
            }
        }
    }
}