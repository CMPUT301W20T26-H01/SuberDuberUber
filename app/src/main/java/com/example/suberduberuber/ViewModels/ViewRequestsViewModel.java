package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class ViewRequestsViewModel extends AndroidViewModel {

    private RequestRepository requestRepository;
    private MutableLiveData<List<Request>> allRequests = new MutableLiveData<List<Request>>();

    public ViewRequestsViewModel(Application application) {
        super(application);
        requestRepository = new RequestRepository();
    }

    public LiveData<List<Request>> getAllRequests() {
        requestRepository.getAllRequests().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null) {
                    allRequests.setValue(queryDocumentSnapshots.toObjects(Request.class));
                }
            }
        });

        return allRequests;
    }
}
