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

import androidx.lifecycle.AndroidViewModel;

import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.Models.User;

/**
 * This is a ViewModelClass used to expose livedata to the fragments and activities that persists their
 * Life cycle changes. Viewmodels also serve to data security by giving views access ONLY to the database
 * opertaions that are relavent to them. Finally, if this viewmodel is shared between fragments it will
 * serve as a data scope in which data can persist fragment changes, allowing for a form of inter-fragment
 * communication more simple than intent extra passing. In this case this viewmodel is used for registration
 * and auth operations.
 */
public class RegisterViewModel extends AndroidViewModel {

    private static String TAG = "IN_REGISTRATION_VIEWMODEL";

    private UserRepository firestoreRepository;

    public RegisterViewModel(Application application) {
        super(application);
        firestoreRepository = new UserRepository();
    }

    public void createNewUser(User user) {
        firestoreRepository.saveUser(user);
    }
}
