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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Services.PermissionsService;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

/**
 * This activity uses the authModel to go through a login routine. It can check for valid form entries
 * and report errors if entries are invalid. It also supports Auto-login where a user is automatically
 * redirected to the home screen if they are still signed into a past session. The login will redirect
 * Riders and Drivers to different dashboard pages.
 */
public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth myAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    private AuthViewModel authViewModel;

    private EditText emailField;
    private EditText passwordField;

    private Button signinButton;
    private Button registerButton;


    public boolean locationPermissionGranted = false;

    private PermissionsService permissionsService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authViewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        signinButton = findViewById(R.id.signin_button);
        registerButton = findViewById(R.id.register_button);

        permissionsService = new PermissionsService(this, this);

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignin();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToRegisterPage();
            }
        });

        myAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    redirectUser();
                }
            }
        });
    }

    private void redirectToRegisterPage() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void attemptSignin() {
        if(emailIsValid() & passwordIsValid()) {
            String email = emailField.getText().toString().trim().toLowerCase();
            String password = passwordField.getText().toString().trim();
            signIn(email, password);
        }
        else {
            return;
        }
    }

    private void signIn(String email, String password) {
        myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Signin Failed", Toast.LENGTH_SHORT).show();
                    Log.e("FAILED_AUTH", task.getException().toString());
                }
            }
        });
    }

    boolean emailIsValid() {
        String email = emailField.getText().toString().trim();

        if(email.isEmpty()) {
            emailField.setError("Please enter your email.");
            return false;
        }
        else if(!Pattern.matches("^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$", email)) {
            emailField.setError("Invalid Email");
            return false;
        }

        emailField.setError(null);
        return true;
    }

    boolean passwordIsValid() {
        String password = passwordField.getText().toString().trim();

        if(password.isEmpty()) {
            passwordField.setError("A password is required.");
            return false;
        }
        else if(password.length() < 6) {
            passwordField.setError("The password must be at least 6 characters.");
            return false;
        }

        emailField.setError(null);
        return true;
    }

    private void redirectUser() {
        authViewModel.getCurrentUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if(user.getDriver()) {
                    goToDriverDashboard();
                }
                else {
                    goToRiderDashboard();
                }
            }
        });
    }

    private void goToRiderDashboard() {
        Intent intent = new Intent(this, RiderDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goToDriverDashboard() {
        Intent intent = new Intent(this, DriverDashboardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(permissionsService.checkMapServices()) {
            if(!locationPermissionGranted) {
                permissionsService.getLocationPermission();
            }
        }
    }

}
