package com.example.suberduberuber.ViewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

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
import java.util.Objects;

public class DriverLocationViewModel extends AndroidViewModel {

    private static final String TAG = "DRIVER_LOCATION_MODEL";

    private UserLocationRepository userLocationRepository;

    private MutableLiveData<GeoPoint> currentLocation = new MutableLiveData<GeoPoint>();

    public DriverLocationViewModel(Application application) {
        super(application);
        userLocationRepository = new UserLocationRepository();
    }

    public LiveData<GeoPoint> getLiveLocation(User driver) {
        if (driver != null) {
            userLocationRepository.getLocation(driver).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
                        try {
                            currentLocation.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(UserLocation.class).getGeoPoint());
                        }
                        catch (NullPointerException exception) {
                            Log.i(TAG, "Null Pointer Exception");
                        }
                    }
                }
            });
        }
        else {
            currentLocation = null;
        }
        return currentLocation;
    }

    public LiveData<GeoPoint> getDriverLocation(Request request) {
        if (request != null && (request.getStatus().equals("ACCEPTED") || request.getStatus().equals("PENDING_ACCEPTANCE"))) {
            Driver driver = request.getDriver();
            userLocationRepository.getLocation(driver).addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
                        try {
                            currentLocation.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(UserLocation.class).getGeoPoint());
                        }
                        catch (NullPointerException exception) {
                            Log.i(TAG, "Null Pointer Exception");
                        }
                    }
                }
            });
        }

        return currentLocation;
    }

    public void removeObserver(Observer<GeoPoint> observer) {
        currentLocation.removeObserver(observer);
        currentLocation = new MutableLiveData<GeoPoint>();
    }
}
