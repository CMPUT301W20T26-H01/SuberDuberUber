package com.example.suberduberuber.ViewModels;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.suberduberuber.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth myAuth = FirebaseAuth.getInstance();

    private FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    private EditText emailField;
    private EditText passwordField;

    private Button registerButton;
    private Button signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        registerButton = findViewById(R.id.register_button);
        signinButton = findViewById(R.id.signin_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToRegisterPage();
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignin();
            }
        });
    }

    private void redirectToRegisterPage() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void attemptSignin() {
        if(emailIsValid() & passwordIsValid()) {
            String email = emailField.getText().toString().trim();
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
                if(task.isSuccessful()) {
                    goToDashboardPage();
                } else {
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

    private void goToDashboardPage() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
