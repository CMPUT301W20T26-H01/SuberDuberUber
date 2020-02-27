package com.example.suberduberuber;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private TextView welcomeMessage;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        myAuth = FirebaseAuth.getInstance();

        currentUser = myAuth.getCurrentUser();

        welcomeMessage = findViewById(R.id.welcome_message);

        sayHello();
    }

    private void sayHello() {
        String message = String.format("Hello %s, looking for a ride?", currentUser.getEmail());
        welcomeMessage.setText(message);
    }
}

