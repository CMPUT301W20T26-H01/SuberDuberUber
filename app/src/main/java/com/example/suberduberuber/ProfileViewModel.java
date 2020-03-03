package com.example.suberduberuber;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.suberduberuber.Models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileViewModel extends AndroidViewModel {

    private static String TAG = "PROFILE_VIEW_MODEL";

    private FirestoreRepository firestoreRepository;

    private MutableLiveData<User> currentUser = new MutableLiveData<User>();

    public ProfileViewModel(Application application) {
        super(application);
        firestoreRepository = new FirestoreRepository();
    }

    // get the current user and setup listening for live updates
    public LiveData<User> getCurrentUser() {
        firestoreRepository.getCurrentUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
