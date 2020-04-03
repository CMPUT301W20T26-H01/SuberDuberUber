package com.example.suberduberuber.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * This class encapsulates all the interactions with the firebase database that are related to user
 * ratings. Main functionalities are to update the rating and number of ratings for a specific
 * user.
 */

public class RatingRepository {
    private static final String TAG = "RATING_REPOSITORY";

    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    String id;

    public void saveRating(String userUID, double rating) {
        myDb.collection("users")
                .document(userUID)
                .update("rating", rating)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    public void saveNumOfRatings(String userUID, int numOfRatings) {
        myDb.collection("users")
                .document(userUID)
                .update("numberOfRatings", numOfRatings)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, e.toString());
                    }
                });
    }

    public Task<QuerySnapshot> findId(String email) {
        return myDb.collection("users")
                .whereEqualTo("email", email)
                .limit(1)
                .get();
    }

}
