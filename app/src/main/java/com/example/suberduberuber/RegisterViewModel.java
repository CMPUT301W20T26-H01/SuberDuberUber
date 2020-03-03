package com.example.suberduberuber;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnFailureListener;

public class RegisterViewModel extends AndroidViewModel {

    private static String TAG = "IN_REGISTRATION_VIEWMODEL";

    private FirestoreRepository firestoreRepository;

    public RegisterViewModel(Application application) {
        super(application);
        firestoreRepository = new FirestoreRepository();
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
