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

import com.example.suberduberuber.Models.User;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This class serves to encapsulate all he interactions with the User collection in firebase. It keeps
 * The system modular and keeps all the queries in one place, so that if database changes occur it can be
 * fixed in one place. It exposes methods to whichever viewmodel needs to interact with this data class.
 */
public class UserRepository {

    static FirebaseAuth myAuth = FirebaseAuth.getInstance();
    static FirebaseFirestore myDb = FirebaseFirestore.getInstance();

    public void saveUser(User user) {
        myDb.collection("users")
                .document(myAuth.getCurrentUser().getUid())
                .set(user);
    }

    public static DocumentReference getCurrentUser() {
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

    public Task<Void> updateDeviceToken(String token) {
        return myDb.collection("users")
                .document(myAuth.getCurrentUser().getUid())
                .update("deviceToken", token);
    }

}
