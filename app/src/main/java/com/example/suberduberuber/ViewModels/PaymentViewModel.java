package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.Transaction;
import com.example.suberduberuber.Repositories.TransactionRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Date;

public class PaymentViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private TransactionRepository transactionRepository;

    private MutableLiveData<Rider> currentRider = new MutableLiveData<Rider>();
    private MutableLiveData<Driver> userFound = new MutableLiveData<Driver>();

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

}
