package com.example.suberduberuber.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.suberduberuber.Models.Car;
import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.RegisterViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.regex.Pattern;

/**
 * This class allow a user to register as either a rider or a driver. If the user is a driver, it
 * will ask relavant information about their car.
 */
public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth myAuth;
    private FirebaseFirestore db;

    private RegisterViewModel registerViewModel;

    private EditText usernameField;
    private EditText emailField;
    private EditText phoneField;
    private EditText passwordField;
    private EditText passConf;

    private EditText carYear;
    private EditText carMake;
    private EditText carModel;
    private EditText carColor;
    private EditText carPlate;

    private TextView carInfo;

    private Switch isDriverSwitch;
    private boolean isDriver;

    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerViewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);

        usernameField = findViewById(R.id.username_field);
        emailField = findViewById(R.id.email_field);
        phoneField = findViewById(R.id.phone_field);
        phoneField.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        passwordField = findViewById(R.id.password_field);
        passConf = findViewById(R.id.password_field_confirm);

        carYear = findViewById(R.id.year_field);
        carMake = findViewById(R.id.make_field);
        carModel = findViewById(R.id.model_field);
        carColor = findViewById(R.id.color_field);
        carPlate = findViewById(R.id.plate_field);

        carInfo = findViewById(R.id.car_info);

        isDriverSwitch = findViewById(R.id.isDriver);
        isDriver = false;

        registerButton = findViewById(R.id.register_button);

        myAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        carInfo.setVisibility(View.GONE);
        carYear.setVisibility(View.GONE);
        carMake.setVisibility(View.GONE);
        carModel.setVisibility(View.GONE);
        carColor.setVisibility(View.GONE);
        carPlate.setVisibility(View.GONE);

        isDriverSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    isDriver = false;
                    carInfo.setVisibility(View.GONE);
                    carYear.setVisibility(View.GONE);
                    carMake.setVisibility(View.GONE);
                    carModel.setVisibility(View.GONE);
                    carColor.setVisibility(View.GONE);
                    carPlate.setVisibility(View.GONE);
                }
                if (isChecked) {
                    isDriver = true;
                    carInfo.setVisibility(View.VISIBLE);
                    carYear.setVisibility(View.VISIBLE);
                    carMake.setVisibility(View.VISIBLE);
                    carModel.setVisibility(View.VISIBLE);
                    carColor.setVisibility(View.VISIBLE);
                    carPlate.setVisibility(View.VISIBLE);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isUsernameUnique(usernameField.getText().toString().trim());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void attemptRegistration() {
        if (isDriver) {
            if(emailIsValid() & passwordIsValid() & usernameIsValid() & phoneIsValid() & carIsValid()) {
                createAccount();
            }
            else {
                return;
            }

        } else {
            if (emailIsValid() & passwordIsValid() & usernameIsValid() & phoneIsValid()) {
                createAccount();
            } else {
                return;
            }
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

    boolean phoneIsValid() {
        String phone = phoneField.getText().toString().trim().replaceAll("\\D", "");

        if (phone.isEmpty()) {
            phoneField.setError("Please enter your phone number.");
            return false;
        }
        else if (phone.length() != 10) {
            phoneField.setError("Invalid phone number. Must be 10 digits.");
            return false;
        }

        phoneField.setError(null);
        return true;
    }

    boolean passwordIsValid() {
        String password = passwordField.getText().toString().trim();
        String confirm = passConf.getText().toString().trim();

        if(password.isEmpty()) {
            passwordField.setError("A password is required.");
            return false;
        }
        else if(password.length() < 6) {
            passwordField.setError("The password must be at least 6 characters.");
            return false;
        }
        else if(!password.equals(confirm)) {
            passConf.setError("The passwords must match!");
            return false;
        }

        emailField.setError(null);
        return true;
    }

    private boolean usernameIsValid() {
        String userName = usernameField.getText().toString().trim();

        if(userName.isEmpty()) {
            usernameField.setError("A username is required");
            return false;
        }

        usernameField.setError(null);
        return true;
    }

    private boolean carIsValid() {
        String make = carMake.getText().toString().trim();
        String model = carModel.getText().toString().trim();
        String year = carYear.getText().toString().trim();
        String color = carColor.getText().toString().trim();
        String plate = carPlate.getText().toString().trim();

        boolean makeValid = true;
        boolean modelValid = true;
        boolean yearValid = true;
        boolean colorValid = true;
        boolean plateValid = true;

        if (make.isEmpty()) {
            carMake.setError("Car make is required");
            makeValid = false;
        }

        if (model.isEmpty()) {
            carModel.setError("Car model is required");
            modelValid = false;
        }

        if (year.isEmpty()) {
            carYear.setError("Car year is required");
            yearValid = false;
        }

        if (color.isEmpty()) {
            carColor.setError("Car color is required");
            colorValid = false;
        }

        if (plate.isEmpty()) {
            carPlate.setError("License plate is required");
            plateValid = false;
        }

        if (year.length() != 4) {
            carYear.setError("Year must be 4 digits");
            yearValid = false;
        }

        if (makeValid & modelValid & yearValid & colorValid & plateValid) {

            carMake.setError(null);
            carModel.setError(null);
            carYear.setError(null);
            carColor.setError(null);
            carPlate.setError(null);

            return true;
        }

        return false;
    }

    private void createAccount() {

        final String email = emailField.getText().toString().trim().toLowerCase();
        String password = passwordField.getText().toString().trim();
        final String username = usernameField.getText().toString().trim();
        final String phone = phoneField.getText().toString().trim().replaceAll("\\D", "");

        myAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    saveUserData(email, username, phone);
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

    private void saveUserData(String email, String username, String phone) {
        User user;
        if (isDriver) {
            Car car = new Car(carMake.getText().toString().trim(), carModel.getText().toString().trim(), carPlate.getText().toString().trim(), carColor.getText().toString().trim(), Integer.parseInt(carYear.getText().toString().trim()));
            user = new Driver(username, email, phone, car);

        } else {
            user = new Rider(username, email, phone);
        }

        registerViewModel.createNewUser(user);
        redirectToMain();
    }

    private void redirectToMain() {

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void isUsernameUnique(String username) {
        CollectionReference users = db.collection("users");
        Query query = users.whereEqualTo("username", username);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot qSnap = task.getResult();
                    if (qSnap.isEmpty()) {
                        usernameField.setError(null);
                        attemptRegistration();
                    } else {
                        usernameField.setError("Username already taken");
                    }
                }
            }
        });
    }
}
