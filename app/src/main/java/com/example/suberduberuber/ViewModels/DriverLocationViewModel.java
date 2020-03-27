package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Models.UserLocation;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.UserLocationRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DriverLocationViewModel extends AndroidViewModel {

    private static final String TAG = "DRIVER_LOCATION_MODEL";

    private UserLocationRepository userLocationRepository;

    private MutableLiveData<GeoPoint> currentLocation = new MutableLiveData<GeoPoint>();

    public DriverLocationViewModel(Application application) {
        super(application);
        userLocationRepository = new UserLocationRepository();
    }

    public LiveData<GeoPoint> getDriverLocation(Request request) {
        if (request != null && request.getDriver() != null) {
            Driver driver = request.getDriver();
            userLocationRepository.getLocation(driver).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
                        currentLocation.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(UserLocation.class).getGeoPoint());
                    }
                }
            });
        }
        else {
            currentLocation = null;
        }
        return currentLocation;

    }
}
