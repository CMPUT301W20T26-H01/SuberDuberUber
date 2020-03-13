package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

public class AuthViewModel extends AndroidViewModel {

    private UserRepository userRepository;

    private MutableLiveData<User> currentUser = new MutableLiveData<>();

    public AuthViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }
}
