package com.example.suberduberuber.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.suberduberuber.Fragments.SelectDestinationFragment;
import com.example.suberduberuber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth myAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        myAuth = FirebaseAuth.getInstance();

        currentUser = myAuth.getCurrentUser();
    }

    // for QR Code Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, intent);
        }
    }
}

