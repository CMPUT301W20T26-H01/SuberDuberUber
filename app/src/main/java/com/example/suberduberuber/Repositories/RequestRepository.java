package com.example.suberduberuber.Repositories;

import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.Ride;
import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
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
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        request.setRequestID(documentReference.getId());
                        documentReference.set(request);
                    }
                })
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
                .limit(10)
                .get();
    }

    public Query getAllRequests() {
        return myDb.collection("requests");
    }

    public Task<Void> acceptRequest(Request request) {
        return myDb.collection("requests").document(request.getRequestID()).update("status", "claimed");
    }

    public Task<DocumentReference> createRide(Ride ride) {
        return myDb.collection("rides").add(ride);
    }

    public void cancelRequest(String requestID) {
        myDb.collection("requests").document(requestID).delete();
    }
}
