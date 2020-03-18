package com.example.suberduberuber.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

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
import com.example.suberduberuber.Models.Ride;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverNavigationFragment extends MapFullFragment {

    private GetRideViewModel getRideViewModel;
    private AuthViewModel authViewModel;
    private Request tempRequest;

    private DrawerLayout drawerLayout;

    private AppBarConfiguration appBarConfiguration;

    public DriverNavigationFragment() {
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

        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        drawerLayout = view.findViewById(R.id.drawer_layout);
    }
}

