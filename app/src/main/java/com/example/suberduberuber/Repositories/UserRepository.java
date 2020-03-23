package com.example.suberduberuber.Repositories;

import android.util.Log;
import androidx.annotation.NonNull;

import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * This class serves to encapsulate all he interactions with the User collection in firebase. It keeps
 * The system modular and keeps all the queries in one place, so that if database changes occur it can be
 * fixed in one place. It exposes methods to whichever viewmodel needs to interact with this data class.
 */
public class UserRepository {

    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    public void saveUser(User user) {
        myDb.collection("users")
                .document(myAuth.getCurrentUser().getUid())
                .set(user);
    }

    public DocumentReference getCurrentUser() {
        return myDb.collection("users")
                .document(myAuth.getCurrentUser().getUid());
    }

    public Task<Void> updateCurrentUser(User user) {
        return myDb.collection("users")
                .document(myAuth.getCurrentUser().getUid())
                .set(user);
    }

    public DocumentReference getUser(String uid) {
        return myDb.collection("users")
                .document(uid);
    }
}
