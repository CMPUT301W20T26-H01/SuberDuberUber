package com.example.suberduberuber.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suberduberuber.Models.Ride;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.example.suberduberuber.ViewModels.NavigationViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class RidePendingFragment extends Fragment {

    private NavController navController;

    private GetRideViewModel getRideViewModel;
    private AuthViewModel authViewModel;

    private Button submitButton;

    private LinearLayout layout;
    private TextView qrCodeText;

    public RidePendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        submitButton = view.findViewById(R.id.submit_button);

        layout = view.findViewById(R.id.dest_layout);

        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                getRideViewModel.getUsersCurrentRide(user).observe(getViewLifecycleOwner(), new Observer<Ride>() {
                    @Override
                    public void onChanged(Ride ride) {
                        Toast.makeText(getActivity(), "Your ride was accepted!", Toast.LENGTH_SHORT).show();
                        redirectToNavigationPage();
                    }
                });
            }
        });
    }

    private void redirectToNavigationPage() {

    }
}
