package com.example.suberduberuber.Activities;

/*
Copyright [2020] [SuberDuberUber]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.suberduberuber.Fragments.EditInformationFragment;
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
    private Button editButton;

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
        editButton = findViewById(R.id.editButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserDetails();
            }
        });

    }

    private void displayUserDetails(User user) {
        usernameView.setText(user.getUsername());
        emailView.setText(user.getEmail());
        phoneNumberView.setText(user.getPhone());
        ratingView.setText(String.format("%d", user.getRating()));

    }

    private void editUserDetails(){
        Bundle bundle = new Bundle();
        User user = profileViewModel.getCurrentUser().getValue();
        bundle.putSerializable("User",user);
        bundle.putString("Username",user.getUsername());
        bundle.putString("Email",user.getPhone());
        bundle.putString("Phone Number", user.getPhone());
        EditInformationFragment fragInfo = new EditInformationFragment();
        fragInfo.setArguments(bundle);
    }



}

