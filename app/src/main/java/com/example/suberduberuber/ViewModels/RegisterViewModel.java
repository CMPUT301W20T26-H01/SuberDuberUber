package com.example.suberduberuber.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnFailureListener;

/**
 * This is a ViewModelClass used to expose livedata to the fragments and activities that persists their
 * Life cycle changes. Viewmodels also serve to data security by giving views access ONLY to the database
 * opertaions that are relavent to them. Finally, if this viewmodel is shared between fragments it will
 * serve as a data scope in which data can persist fragment changes, allowing for a form of inter-fragment
 * communication more simple than intent extra passing. In this case this viewmodel is used for registration
 * and auth operations.
 */
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
