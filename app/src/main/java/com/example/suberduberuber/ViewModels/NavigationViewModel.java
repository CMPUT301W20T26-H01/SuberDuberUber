package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.RideRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.QuerySnapshot;

public class NavigationViewModel extends AndroidViewModel {

    private RequestRepository requestRepository;
    private MutableLiveData<Request> requestLiveData = new MutableLiveData<Request>();

    public NavigationViewModel(Application application) {
        super(application);
        requestRepository = new RequestRepository();
    }

    public LiveData<Request> getCurrentRequest(User user) {

        requestRepository.getDriverCurrentRequest(user).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
                    requestLiveData.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(Request.class));
                }
            }
        });

        return requestLiveData;
    }

}
