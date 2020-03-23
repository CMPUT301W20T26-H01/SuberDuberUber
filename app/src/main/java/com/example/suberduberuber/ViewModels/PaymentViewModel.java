package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PaymentViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    private MutableLiveData<User> userFound = new MutableLiveData<User>();

    public PaymentViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    // Get a user and setup listening for live updates
    public LiveData<User> getUser(String uid) {
        userRepository.getUser(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null) {
                    userFound.setValue(documentSnapshot.toObject(User.class));
                }
            }
        });

        return userFound;
    }

}
