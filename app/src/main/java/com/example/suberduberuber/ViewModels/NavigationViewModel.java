package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.Ride;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.RideRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.QuerySnapshot;

public class NavigationViewModel extends AndroidViewModel {

    private RideRepository rideRepository;
    private MutableLiveData<Ride> rideLiveData = new MutableLiveData<Ride>();

    public NavigationViewModel(Application application) {
        super(application);
        rideRepository = new RideRepository();
    }

    public LiveData<Ride> getCurrentRide(User user) {
        rideRepository.getUserCurrentRide(user).addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null) {
                    rideLiveData.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(Ride.class));
                }
            }
        });

        return rideLiveData;
    }

}
