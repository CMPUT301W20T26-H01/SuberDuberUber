package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectDestinationFragment extends MapFullFragment {

    private GetRideViewModel getRideViewModel;
    private Request tempRequest;

    public SelectDestinationFragment() {
        // Required empty public constructor
    }

    /**
     * Get the viewmodel provider factory to dish out the viewmodel, creating a new instance only
     * if it is the first time that the activity starts
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTempRequest();
            }
        });
    }

    private void saveRequest() {
        getRideViewModel.saveTempRequest(tempRequest);
    }

    private void createTempRequest() {
        getRideViewModel.getCurrentUser().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            private static final String TAG = "SELECT_DESTINATION_FRAG";

            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    User user = documentSnapshot.toObject(User.class);
                    tempRequest = new Request(-1, user, new Path(), "12:00 AM", "initiated");
                    tempRequest.getPath().setDestination(new CustomLocation(null, currentPlace.getName(), null));
                    saveRequest();
                    navController.navigate(R.id.action_selectDestinationFragment_to_selectOriginFragment);
                } else {
                    Log.d(TAG, "Error getting current user document: ", task.getException());
                }
            }
        });
    }
}
