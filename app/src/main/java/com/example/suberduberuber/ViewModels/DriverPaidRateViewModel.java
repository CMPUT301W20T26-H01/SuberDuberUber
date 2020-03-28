package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.RatingRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class DriverPaidRateViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private RatingRepository ratingRepository;
    private MutableLiveData<Driver> currentDriver = new MutableLiveData<Driver>();
    private User rider;

    public DriverPaidRateViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
        ratingRepository = new RatingRepository();
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

    public void setRider(User user) {
        this.rider = user;
    }

    public User getRider() {
        return this.rider;
    }

    public void updateUserRating(String riderID, double rating) {
        double newRating = ((getRider().getRating() * getRider().getNumberOfRatings()) + rating) / (getRider().getNumberOfRatings() + 1);

        ratingRepository.saveRating(riderID, newRating);

    }

}
