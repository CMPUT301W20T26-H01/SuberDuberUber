package com.example.suberduberuber.ViewModels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.widget.TextView;

import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.ProfileViewModel;

public class ProfileActivity extends AppCompatActivity {

    private ProfileViewModel profileViewModel;

    private LiveData<User> currentUser;

    private TextView usernameView;
    private TextView emailView;
    private TextView ratingView;
    private TextView phoneNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Configure the view model provider factory to dish the same view model instance out to any version of this activity (therefore the viewmodel classes survive the activity being destroyed and remade)
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        usernameView = findViewById(R.id.username);
        emailView = findViewById(R.id.email);
        ratingView = findViewById(R.id.rating);
        phoneNumberView = findViewById(R.id.phone_number);

        profileViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                displayUserDetails(user);
            }
        });
    }

    private void displayUserDetails(User user) {
        usernameView.setText(user.getUsername());
        emailView.setText(user.getEmail());
        phoneNumberView.setText(user.getEmail());
        ratingView.setText(String.format("%d", user.getRating()));
    }
}
