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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth myAuth;

    private FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    private EditText usernameField;
    private EditText emailField;
    private EditText passwordField;

    private Button registerButton;
    private Button signinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameField = findViewById(R.id.username_field);
        emailField = findViewById(R.id.email_field);
        passwordField = findViewById(R.id.password_field);

        registerButton = findViewById(R.id.register_button);
        signinButton = findViewById(R.id.signin_button);

        myAuth = FirebaseAuth.getInstance();
        myDb = FirebaseFirestore.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void attemptRegistration() {
        if(emailIsValid() & passwordIsValid() & usernameIsValid()) {
            createAccount();
        }
        else {
            return;
        }
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

    private boolean usernameIsValid() {
        String userName = usernameField.getText().toString().trim();

        if(userName.isEmpty()) {
            usernameField.setError("A unique username is required.");
            return false;
        }

        usernameField.setError(null);
        return true;
    }

    private void createAccount() {

        final String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        final String username = usernameField.getText().toString().trim();

        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    saveUserData(email, username);
                    Toast.makeText(RegisterActivity.this, "You're Signed Up", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        throw task.getException();
                    }
                    catch (FirebaseAuthEmailException e) {
                        Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    }
                    catch (FirebaseAuthWeakPasswordException e) {
                        Toast.makeText(RegisterActivity.this, "Invalid Password", Toast.LENGTH_SHORT).show();
                    }
                    catch (FirebaseAuthUserCollisionException e) {
                        Toast.makeText(RegisterActivity.this, "Email already in use.", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e) {
                        Log.e("EXCEPTION THROWN: ", e.toString());
                    }
                }
            }
        });
    }

    private void saveUserData(String email, String username) {
        User user = new Rider(username, email);

        myDb.collection("Users")
                .document(myAuth.getUid())
                .set(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            redirectToLogin();
                        }
                    }
                });
    }

    private void redirectToLogin() {

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
