package com.example.suberduberuber.Repositories;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class RideRepository {

    FirebaseFirestore myDb;
    FirebaseAuth myAuth = FirebaseAuth.getInstance();

    public RideRepository() {
         myDb = FirebaseFirestore.getInstance();
         myAuth = FirebaseAuth.getInstance();
    }
}