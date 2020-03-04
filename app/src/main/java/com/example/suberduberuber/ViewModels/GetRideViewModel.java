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

    private MutableLiveData<Request> tempRequest = new MutableLiveData<Request>();

    public GetRideViewModel(Application application) {
        super(application);
        requestRepository = new RequestRepository();
        userRepository = new UserRepository();
    }

    public LiveData<Request> getTempRequest() {
        return tempRequest;
    }

    public void saveTempRequest(Request request) {
        tempRequest.setValue(request);
    }
}
