package com.example.suberduberuber.Repositories;

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

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;

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
                .limit(10)
                .get();
    }

    public Query getAllRequests() {
        return myDb.collection("requests")
                .whereEqualTo("status", "PENDING_ACCEPTANCE");
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
                .whereIn("status", Arrays.asList("PENDING_ACCEPTANCE", "ACCEPTED", "IN_PROGRESS"))
                .limit(1);
    }

    public Query getDriverCurrentRequest(User user) {
        return myDb.collection("requests")
                .whereEqualTo("driver.email", user.getEmail())
                .whereIn("status", Arrays.asList("ACCEPTED", "IN_PROGRESS"))
                .limit(1);
    }

    public Query getRequestByPickupName(String name) {
        return myDb.collection("requests")
                .whereEqualTo("path.startLocation.locationName", name);
    }
}
