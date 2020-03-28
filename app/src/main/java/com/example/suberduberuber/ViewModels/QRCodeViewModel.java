package com.example.suberduberuber.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.Models.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.nio.file.attribute.UserDefinedFileAttributeView;

public class QRCodeViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private MutableLiveData<User> currentUser = new MutableLiveData<User>();

    public QRCodeViewModel(Application application) {
        super(application);
        userRepository = new UserRepository();
    }

    public LiveData<User> getCurrentUser() {
        userRepository.getCurrentUser().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    User user = documentSnapshot.toObject(User.class);
                    if (user.getDriver()) {
                        currentUser.setValue(documentSnapshot.toObject(Driver.class));
                    } else {
                        currentUser.setValue(documentSnapshot.toObject(Rider.class));
                    }
                }
            }
        });

        return currentUser;
    }

    public void setBalance(Double balance) {
        User user = this.currentUser.getValue();
        user.setBalance(balance);
        userRepository.updateCurrentUser(user);
    }

}
