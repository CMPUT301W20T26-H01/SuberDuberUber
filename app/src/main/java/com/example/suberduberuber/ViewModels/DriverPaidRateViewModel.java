package com.example.suberduberuber.ViewModels;

/*
Copyright [2020] [SuberDuberUber]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.RatingRepository;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

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
