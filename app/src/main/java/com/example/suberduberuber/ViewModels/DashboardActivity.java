package com.example.suberduberuber.ViewModels;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.suberduberuber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private TextView welcomeMessage;

    private Button profileButton;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        myAuth = FirebaseAuth.getInstance();

        currentUser = myAuth.getCurrentUser();

        welcomeMessage = findViewById(R.id.welcome_message);
        profileButton = findViewById(R.id.profile_button);

        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToProfilePage();
            }
        });

        sayHello();
    }

    private void sayHello() {
        String message = String.format("Hello %s, looking for a ride?", currentUser.getEmail());
        welcomeMessage.setText(message);
    }

    private void goToProfilePage() {
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}

