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
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This is a ViewModelClass used to expose livedata to the fragments and activities that persists their
 * Life cycle changes. Viewmodels also serve to data security by giving views access ONLY to the database
 * opertaions that are relavent to them. Finally, if this viewmodel is shared between fragments it will
 * serve as a data scope in which data can persist fragment changes, allowing for a form of inter-fragment
 * communication more simple than intent extra passing. In this case this viewmodel is used for profile
 * related data acessing / editing.
 */public class ProfileViewModel extends AndroidViewModel {

    private static String TAG = "PROFILE_VIEW_MODEL";

    private UserRepository userRepository;

    private MutableLiveData<User> currentUser = new MutableLiveData<User>();

    public ProfileViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    // Get the current user and setup listening for live updates
    public LiveData<User> getCurrentUser() {
        userRepository.getCurrentUser().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e != null || queryDocumentSnapshots.getDocuments().isEmpty()) {
                    currentUser.setValue(null);
                    Log.e(TAG, "Could not get user.", e);
                }
                else {
                    currentUser.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(User.class));
                }
            }
        });

        return currentUser;
    }
}
