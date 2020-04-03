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
import com.example.suberduberuber.Models.UserLocation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * This class serves to encapsulate all he interactions with the User Location collection in firebase. It keeps
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