package com.example.suberduberuber.Repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

// A class that serves to handle data structure specific logic and interactions with Fire store,  specifically for user related interactions.
public class UserRepository {

    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseFirestore myDb = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = myAuth.getCurrentUser();

    public Task<DocumentReference> saveUser(User user) {
        return myDb.collection("users")
                .add(user).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Log.d("SAVING_USER", "Failed saving user");
                    }
                });
    }

    public Query getCurrentUser() {
        return myDb.collection("users").whereEqualTo("email", currentUser.getEmail());
    }

    public Task<Void> updateCurrentUser(User user) {
        return myDb.collection("users")
                .document(currentUser.getEmail())
                .set(user);
    }
}
