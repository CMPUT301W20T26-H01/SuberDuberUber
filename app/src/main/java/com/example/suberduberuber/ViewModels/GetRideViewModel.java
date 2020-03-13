package com.example.suberduberuber.ViewModels;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


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
    private MutableLiveData<User> currentUser = new MutableLiveData<User>();
    private MutableLiveData<ArrayList<Request>> usersRequests = new MutableLiveData<>();

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

    public void commitTempRequest() {
        requestRepository.saveRequest(tempRequest.getValue()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, e.toString());
            }
        });
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
}
