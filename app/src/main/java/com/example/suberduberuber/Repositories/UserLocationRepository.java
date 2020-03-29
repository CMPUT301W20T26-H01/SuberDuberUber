package com.example.suberduberuber.Repositories;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Models.UserLocation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

/**
 * This class serves to encapsulate all he interactions with the User collection in firebase. It keeps
 * The system modular and keeps all the queries in one place, so that if database changes occur it can be
 * fixed in one place. It exposes methods to whichever viewmodel needs to interact with this data class.
 */
public class UserLocationRepository {

    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseFirestore myDb = FirebaseFirestore.getInstance();
    DocumentReference locationReference = myDb.collection("User Location").document(myAuth.getCurrentUser().getUid());

    public void saveLocation(UserLocation userLocation) {
        locationReference.set(userLocation);
    }

    public Query getLocation(User user) {
        return myDb.collection("User Location")
                .whereEqualTo("user.email", user.getEmail())
                .limit(1);
    }

}