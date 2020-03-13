package com.example.suberduberuber.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.Models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

// A class that exposes all of the data and data manipulation methods relevant to the user profile page.
public class ProfileViewModel extends AndroidViewModel {

    private static String TAG = "PROFILE_VIEW_MODEL";

    private UserRepository userRepository;

    private MutableLiveData<User> currentUser = new MutableLiveData<User>();

    public ProfileViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    // Get the current user and setup listening for live updates
    public LiveData<User> getCurrentUser() {
        userRepository.getCurrentUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    currentUser.setValue(null);
                    Log.w(TAG, "Failed listen to current user.", e);
                } else {
                    currentUser.setValue(documentSnapshot.toObject(User.class));
                }
            }
        });

        return currentUser;
    }
}
