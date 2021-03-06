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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * This is a ViewModelClass used to expose livedata to the fragments and activities that persists their
 * Life cycle changes. Viewmodels also serve to data security by giving views access ONLY to the database
 * opertaions that are relavent to them. Finally, if this viewmodel is shared between fragments it will
 * serve as a data scope in which data can persist fragment changes, allowing for a form of inter-fragment
 * communication more simple than intent extra passing. In this case this viewmodel is used for the Ride
 * requesting flow. It has a local request that is edited by multiple fragments until finally being
 * commited to the database.
 */
public class GetRideViewModel extends AndroidViewModel {

    private static final String TAG = "GET_RIDE_VIEWMODEL";

    private RequestRepository requestRepository;

    private MutableLiveData<Request> tempRequest = new MutableLiveData<Request>();
    private MutableLiveData<ArrayList<Request>> usersRequests = new MutableLiveData<>();
    private MutableLiveData<Request> currentRequest = new MutableLiveData<Request>();

    public GetRideViewModel(Application application) {
        super(application);
        requestRepository = new RequestRepository();
    }

    public LiveData<Request> getTempRequest() {
        return tempRequest;
    }

    public void saveTempRequest(Request request) {
        tempRequest.setValue(request);
    }

    public void cancelCurrentRequest(User currentUser) {
        requestRepository.getRidersCurrentRequest(currentUser).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
                    requestRepository.cancelRequest(queryDocumentSnapshots.getDocuments().get(0).toObject(Request.class));
                }
            }
        });
    }

    public LiveData<Request> getUsersCurrentRide(User user) {
        requestRepository.getRidersCurrentRequest(user).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(queryDocumentSnapshots != null && queryDocumentSnapshots.getDocuments().size() > 0) {
                    currentRequest.setValue(queryDocumentSnapshots.getDocuments().get(0).toObject(Request.class));
                }
            }
        });

        return currentRequest;
    }

    public void commitTempRequest() {
        requestRepository.saveRequest(tempRequest.getValue());
    }

    public LiveData<ArrayList<Request>> getCurrentUsersRequests(User user) {
        requestRepository.getUsersRequests(user).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    ArrayList<Request> requests = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : task.getResult()) {
                        requests.add(doc.toObject(Request.class));
                    }
                    usersRequests.setValue(requests);
                } else {
                    usersRequests.setValue(null);
                    Log.e(TAG, "Could not get user's requests.");
                }
            }
        });

        return usersRequests;
    }

    public void cancelRequest(Request request) {
        requestRepository.cancelRequest(request);
    }

    public void removeObservers(LifecycleOwner owner) {
        currentRequest.removeObservers(owner);
        currentRequest = new MutableLiveData<>();
    }

    public Request getCurrentRequest() {
        return this.currentRequest.getValue();
    }
}
