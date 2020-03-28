package com.example.suberduberuber.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

/**
 * This class serves to encapsulate all he interactions with the Request collection in firebase. It keeps
 * The system modular and keeps all the queries in one place, so that if database changes occur it can be
 * fixed in one place. It exposes methods to whichever viewmodel needs to interact with this data class.
 */
public class RequestRepository {

    private static final String TAG = "REQUEST_REPOSITORY";

    FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    public RequestRepository() {

    }

    /**
     * Save a new request in the collection "requests", named with the request id
     *
     * @param request
     * @return
     */
    public void saveRequest(Request request) {
        myDb.collection("requests")
                .add(request)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    public Task<QuerySnapshot> getUsersRequests(User currentUser) {
        return myDb.collection("requests")
                .whereEqualTo("requestingUser.email", currentUser.getEmail())
                .whereEqualTo("status", "")
                .limit(10)
                .get();
    }

    public Query getAllRequests() {
        return myDb.collection("requests")
                .orderBy("time");
    }

    public Task<Void> acceptRequest(Request request) {
        return myDb.collection("requests").document(request.getRequestID()).update("status", "claimed");
    }

    public void cancelRequest(Request request) {
        myDb.collection("requests").document(request.getRequestID()).delete();
    }

    public void updateRequest(Request request) {
        myDb.collection("requests").document(request.getRequestID()).set(request);
    }

    public Query getRidersCurrentRequest(User user) {
        return myDb.collection("requests")
                .whereEqualTo("requestingUser.email", user.getEmail())
                .whereIn("status", Arrays.asList("PENDING_ACCEPTANCE", "IN_PROGRESS"))
                .limit(1);
    }

    public Query getDriverCurrentRequest(User user) {
        return myDb.collection("requests")
                .whereEqualTo("driver.email", user.getEmail())
                .whereEqualTo("status", "IN_PROGRESS")
                .limit(1);
    }

    public Query getRequestByPickupName(String name) {
        return myDb.collection("requests")
                .whereEqualTo("path.startLocation.locationName", name);
    }
}
