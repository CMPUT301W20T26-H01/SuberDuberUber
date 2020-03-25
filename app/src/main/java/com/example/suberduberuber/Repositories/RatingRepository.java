package com.example.suberduberuber.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RatingRepository {
    private static final String TAG = "RATING_REPOSITORY";

    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseFirestore myDb = FirebaseFirestore.getInstance();

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

}
