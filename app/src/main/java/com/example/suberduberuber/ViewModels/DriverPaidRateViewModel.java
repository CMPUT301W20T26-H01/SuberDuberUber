package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DriverPaidRateViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private MutableLiveData<Driver> currentDriver = new MutableLiveData<Driver>();

    public DriverPaidRateViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
        findCurrentDriver();
    }

    public void findCurrentDriver() {
        userRepository.getCurrentUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    currentDriver.setValue(documentSnapshot.toObject(Driver.class));
                }
            }
        });
    }

    public LiveData<Driver> getDriverLive() {
        userRepository.getCurrentUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot != null) {
                    currentDriver.setValue(documentSnapshot.toObject(Driver.class));
                }
            }
        });

        return currentDriver;
    }

    public Driver getCurrentDriver() {
        return this.currentDriver.getValue();
    }

    public DocumentReference userRepoGetUser() {
        return userRepository.getCurrentUser();
    }
}
