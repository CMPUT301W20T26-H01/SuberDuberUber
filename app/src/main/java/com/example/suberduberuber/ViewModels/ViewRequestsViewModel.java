package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

/**
 * This is a ViewModelClass used to expose livedata to the fragments and activities that persists their
 * Life cycle changes. Viewmodels also serve to data security by giving views access ONLY to the database
 * opertaions that are relavent to them. Finally, if this viewmodel is shared between fragments it will
 * serve as a data scope in which data can persist fragment changes, allowing for a form of inter-fragment
 * communication more simple than intent extra passing. In this case the viewmodel will provide the
 * requests to populate a recycler view.
 */
public class ViewRequestsViewModel extends AndroidViewModel {

    private RequestRepository requestRepository;
    private UserRepository userRepository;
    private MutableLiveData<List<Request>> allRequests = new MutableLiveData<List<Request>>();

    public ViewRequestsViewModel(Application application) {
        super(application);
        requestRepository = new RequestRepository();
        userRepository = new UserRepository();
    }

    public LiveData<List<Request>> getAllRequests() {
        requestRepository.getAllRequests().addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (queryDocumentSnapshots != null) {
                    allRequests.setValue(queryDocumentSnapshots.toObjects(Request.class));
                }
            }
        });

        return allRequests;
    }

    public Query getRequestByPickupName(String pickup) {
        return requestRepository.getRequestByPickupName(pickup);
    }

    public void acceptRequest(Request request) {
        userRepository.getCurrentUser().get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Driver driver = documentSnapshot.toObject(Driver.class);

                request.accept(driver);
                requestRepository.updateRequest(request);
            }
        });
    }

    public void removeObservers(LifecycleOwner owner) {
        allRequests.removeObservers(owner);
        allRequests = new MutableLiveData<>();
    }
}
