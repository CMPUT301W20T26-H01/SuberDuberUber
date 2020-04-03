package com.example.suberduberuber.ViewModels;

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

import android.app.Application;

import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;

import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.Models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

/**
 * This is a ViewModelClass used to expose livedata to the fragments and activities that persists their
 * Life cycle changes. Viewmodels also serve to data security by giving views access ONLY to the database
 * opertaions that are relavent to them. Finally, if this viewmodel is shared between fragments it will
 * serve as a data scope in which data can persist fragment changes, allowing for a form of inter-fragment
 * communication more simple than intent extra passing. In this case this viewmodel is used for profile
 * related data acessing / editing.
 */public class ProfileViewModel extends AndroidViewModel {

    private static String TAG = "PROFILE_VIEW_MODEL";

    private UserRepository userRepository;

    private MutableLiveData<User> currentUser = new MutableLiveData<User>();
    private MutableLiveData<Driver> currentDriver = new MutableLiveData<Driver>();

    public ProfileViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    // Get the current user and setup listening for live updates
    public LiveData<User> getCurrentUser() {
        UserRepository.getCurrentUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    if(documentSnapshot.getBoolean("driver")){
                        currentDriver.setValue(documentSnapshot.toObject(Driver.class));}
                    currentUser.setValue(documentSnapshot.toObject(User.class));

                }
            }
        });

        return currentUser;
    }

    public void updateCurrentUser(User user){
        userRepository.updateCurrentUser(user);
    }


    public LiveData<Driver> getCurrentDriver(){
        return currentDriver;
    }
}
