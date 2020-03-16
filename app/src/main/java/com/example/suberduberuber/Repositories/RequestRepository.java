package com.example.suberduberuber.Repositories;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

public class RequestRepository {

    FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    public RequestRepository() {

    }

    /**
     * Save a new request in the collection "requests", named with the request id
     * @param request
     * @return
     */
    public Task<DocumentReference> saveRequest(Request request) {
        return myDb.collection("requests")
                .add(request);
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

    public void acceptRequest() {

    }
}
