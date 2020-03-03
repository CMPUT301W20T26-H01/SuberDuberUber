package com.example.suberduberuber;

import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirestoreRepository {

    FirebaseAuth myAuth = FirebaseAuth.getInstance();
    FirebaseFirestore myDb = FirebaseFirestore.getInstance();
    FirebaseUser currentUser = myAuth.getCurrentUser();

    public Task<Void> saveUser(User user) {
        return myDb.collection("users")
                .document(currentUser.getEmail())
                .set(user);
    }

    public DocumentReference getCurrentUser() {
        return myDb.collection("users").document(currentUser.getEmail());
    }

    public Task<Void> updateCurrentUser(User user) {
        return myDb.collection("users")
                .document(currentUser.getEmail())
                .set(user);
    }
}
