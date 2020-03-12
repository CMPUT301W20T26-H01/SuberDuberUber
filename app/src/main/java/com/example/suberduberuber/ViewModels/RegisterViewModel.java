package com.example.suberduberuber.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnFailureListener;

// A class that exposes all of the data and data manipulation methods relevant to the registration page.
public class RegisterViewModel extends AndroidViewModel {

    private static String TAG = "IN_REGISTRATION_VIEWMODEL";

    private UserRepository firestoreRepository;

    public RegisterViewModel(Application application) {
        super(application);
        firestoreRepository = new UserRepository();
    }

    public void createNewUser(User user) {
        firestoreRepository.saveUser(user).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
            }
        });
    }
}
