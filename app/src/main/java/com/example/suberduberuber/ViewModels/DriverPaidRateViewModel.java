package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.RatingRepository;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This is a viewmodel class to handle the payment and ratings for the driver side of the app. It
 * stores the driver and rider in the current request to keep them accessible throughout each of
 * the fragments. The main functionalities of this viewmodel are to update the rider's rating and
 * complete the request process.
 */

public class DriverPaidRateViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private RatingRepository ratingRepository;
    private RequestRepository requestRepository;
    private MutableLiveData<Driver> currentDriver = new MutableLiveData<Driver>();
    private User rider;
    private Request request;

    public DriverPaidRateViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
        ratingRepository = new RatingRepository();
        requestRepository = new RequestRepository();
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

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return this.request;
    }

    public void updateUserRating(String riderID, double rating) {
        double newRating = ((getRider().getRating() * getRider().getNumberOfRatings()) + rating) / (getRider().getNumberOfRatings() + 1);

        ratingRepository.saveRating(riderID, newRating);
    }

    public void updateUserNumOfRatings(String riderID) {
        ratingRepository.saveNumOfRatings(riderID, getRider().getNumberOfRatings() + 1);
    }

    public void finishRequest() {
        this.request.complete();
        requestRepository.updateRequest(this.request);
    }

}
