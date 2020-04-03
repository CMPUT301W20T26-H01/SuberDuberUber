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
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.Transaction;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.TransactionRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;

/**
 * This is a viewmodel class to handle the payment for the rider side of the app. It stores the rider
 * and the driver of the scanned QR code to keep them accessible through the payment and rating
 * fragments. The main functionalities of this viewmodel are to update complete transactions between
 * a rider and driver.
 */

public class PaymentViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;

    private MutableLiveData<Rider> currentRider = new MutableLiveData<Rider>();
    private MutableLiveData<Driver> userFound = new MutableLiveData<Driver>();
    private MutableLiveData<User> currentUser = new MutableLiveData<User>();

    private MutableLiveData<Driver> currentDriver = new MutableLiveData<Driver>();
    private String currentDriverUID;

    public PaymentViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
        transactionRepository = new TransactionRepository();
        getCurrentRider();
    }

    public void setDriver(Driver driver) {
        currentDriver.setValue(driver);
    }

    public Driver getCurrentDriver() {
        return currentDriver.getValue();
    }

    public String getCurrentDriverUID() {
        return currentDriverUID;
    }

    public void setCurrentDriverUID(String uid) {
        currentDriverUID = uid;
    }

    // Get the current user and setup listening for live updates
    public void getCurrentRider() {
        userRepository.getCurrentUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    currentRider.setValue(documentSnapshot.toObject(Rider.class));
                }
            }
        });
    }

    // Get a user and setup listening for live updates
    public LiveData<Driver> getDriverByUID(String uid) {
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

    public void completeTransaction(double amount) {
        Rider rider = this.currentRider.getValue();
        Driver driver = this.currentDriver.getValue();

        double newPayingBalance = rider.getBalance() - amount;
        double newPaidBalance = driver.getBalance() + amount;

        transactionRepository.processTransaction(this.currentDriverUID, newPayingBalance, newPaidBalance);
        Transaction transaction = new Transaction(rider, driver, amount, new Date());
        transactionRepository.saveTransaction(transaction);

    }

    public void findCurrentUser(String uid) {
        userRepository.getUser(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    currentUser.setValue(documentSnapshot.toObject(User.class));
                }
            }
        });
    }

    public User getCurrentUser() {
        return this.currentUser.getValue();
    }

    public void sendFundsCompleteTransaction(User payingUser, User paidUser, String paidUserUID, double amount) {
        double newPayingBalance = payingUser.getBalance() - amount;
        double newPaidBalance = paidUser.getBalance() + amount;

        transactionRepository.processTransaction(paidUserUID, newPayingBalance, newPaidBalance);
        //Transaction transaction = new Transaction((Rider) payingUser, (Driver) paidUser, amount, new Date());
        //transactionRepository.saveTransaction(transaction);
    }

}
