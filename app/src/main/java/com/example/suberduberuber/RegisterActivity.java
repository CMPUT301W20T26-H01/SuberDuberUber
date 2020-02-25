package com.example.suberduberuber;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth myAuth;
    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;

    private Button registerButton;
    private Button signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_page);

        usernameField = findViewById(R.id.username_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        registerButton = findViewById(R.id.register_button);
        signinButton = findViewById(R.id.signin_button);

        myAuth = FirebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignin();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void attemptRegistration() {
        if(!registrationFormIsValid()) {
            return;
        }
        else {
            createAccount();
        }
    }

    private void attemptSignin() {
        if(!signinFormIsValid()) {
            return;
        }
        else {
            String email = emailField.getText().toString().trim();
            String password = passwordField.getText().toString().trim();
            signIn(email, password);
        }
    }

    private boolean registrationFormIsValid() {
        boolean valid = true;

        String userName = usernameField.getText().toString().trim();
        if (userName.isEmpty()) {
            usernameField.setError("Please enter a username.");
            valid = false;
        } else {
            usernameField.setError(null);
        }

        String email = emailField.getText().toString().trim();
        if (email.isEmpty()) {
            emailField.setError("Please enter a username");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString().trim();
        if (password.isEmpty()) {
            passwordField.setError("Please enter a password.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    private boolean signinFormIsValid() {
        boolean valid = true;

        String email = emailField.getText().toString().trim();
        if (email.isEmpty()) {
            emailField.setError("Please enter a username");
            valid = false;
        } else {
            emailField.setError(null);
        }

        String password = passwordField.getText().toString().trim();
        if (password.isEmpty()) {
            passwordField.setError("Please enter a password.");
            valid = false;
        } else {
            passwordField.setError(null);
        }

        return valid;
    }

    private void createAccount() {

        String email = emailField.getText().toString().trim();
        String password = emailField.getText().toString().trim();

        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "You're Signed Up", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(RegisterActivity.this, "Signup Failed.", Toast.LENGTH_SHORT).show();
                    Log.e("AUTH_ERROR: ", task.getException().toString());
                }
            }
        });
    }

    private void signIn(String email, String password) {
        myAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    goToDashboardPage();
                } else {
                    Toast.makeText(RegisterActivity.this, "Signin Failed", Toast.LENGTH_SHORT).show();
                    Log.e("FAILED_AUTH", task.getException().toString());
                }
            }
        });
    }

    private void goToDashboardPage() {
        goToDashboardPage();
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
    }


}
