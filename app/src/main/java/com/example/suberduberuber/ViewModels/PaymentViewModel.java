package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class PaymentViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    private MutableLiveData<Rider> currentRider = new MutableLiveData<Rider>();
    private MutableLiveData<Driver> userFound = new MutableLiveData<Driver>();

    private MutableLiveData<Driver> currentDriver = new MutableLiveData<Driver>();

    public PaymentViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public LiveData<Driver> getDriver() {
        return currentDriver;
    }

    public void setDriver(Driver driver) {
        currentDriver.setValue(driver);
    }

    // Get the current user and setup listening for live updates
    public LiveData<Rider> getCurrentRider() {
        userRepository.getCurrentUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    currentRider.setValue(documentSnapshot.toObject(Rider.class));
                }
            }
        });

        return currentRider;
    }

    // Get a user and setup listening for live updates
    public LiveData<Driver> getDriver(String uid) {
        userRepository.getUser(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null) {
                    userFound.setValue(documentSnapshot.toObject(Driver.class));
                }
            }
        });

        return userFound;
    }

}
