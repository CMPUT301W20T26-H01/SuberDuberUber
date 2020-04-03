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
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Models.UserLocation;
import com.example.suberduberuber.Repositories.UserLocationRepository;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

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
