package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
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
                else {
                    requestLiveData.setValue(null);
                }
            }
        });
        return requestLiveData;
    }

    public LiveData<Request> getCurrentRequestOnEvent(User user) {
        requestRepository.getDriverCurrentRequest(user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
                    requestLiveData.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(Request.class));
                }
                else {
                    requestLiveData.setValue(null);
                }
            }
        });

        return requestLiveData;
    }

    public void updateRequest(Request request) {
        requestRepository.updateRequest(request);
    }

    public void removeObservers(LifecycleOwner owner) {
        requestLiveData.removeObservers(owner);
        requestLiveData = new MutableLiveData<>();
    }
}
