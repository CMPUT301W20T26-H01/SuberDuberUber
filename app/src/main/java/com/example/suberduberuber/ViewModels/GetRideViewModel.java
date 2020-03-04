package com.example.suberduberuber.ViewModels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;


/**
 *  A ViewModel to be shared between all the driver ride-requesting-workflow fragments, for communicating
 *  the request between forms as its being built, etc.
 */
public class GetRideViewModel extends AndroidViewModel {

    private static final String TAG = "GET_RIDE_VIEWMODEL";

    private RequestRepository requestRepository;
    private UserRepository userRepository;

    private MutableLiveData<User> currentUser = new MutableLiveData<User>();
    private MutableLiveData<Request> currentRequest = new MutableLiveData<Request>();

    public GetRideViewModel(Application application) {
        super(application);
        requestRepository = new RequestRepository();
        requestRepository = new RequestRepository();
    }

    // Get the current user and setup listening for live updates
    public LiveData<User> getCurrentUser() {
        userRepository.getCurrentUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    currentUser.setValue(null);
                    Log.w(TAG, "Failed listen to current user.", e);
                } else {
                    currentUser.setValue(documentSnapshot.toObject(User.class));
                }
            }
        });

        return currentUser;
    }

    /**
     * Gets the first (only) request of the current user
     * @return updated live data
     */
    public LiveData<Request> getCurrentUsersRequest() {
        requestRepository.getUsersRequests(currentUser.getValue())
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        currentRequest.setValue(queryDocumentSnapshots.toObjects(Request.class).get(0));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        currentRequest.setValue(null);
                        Log.w(TAG, "Failed to get current users request", e);
                    }
                });

        return currentRequest;
    }

    public void saveRequest(Request request) {
        requestRepository.saveRequest(request)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Failed to save request", e);
                    }
                });
    }
}
