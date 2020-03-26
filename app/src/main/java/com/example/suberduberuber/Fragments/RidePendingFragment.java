package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.google.android.gms.maps.MapView;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class RidePendingFragment extends Fragment {

    private NavController navController;
    private GetRideViewModel getRideViewModel;
    private AuthViewModel authViewModel;

    private User currentUser;
    private Request currentRequest;

    private MapView ride_request_live_route;
    private Button cancelButton;
    private TextView rideRequestStatus;

    private Button submitButton;


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

        currentUser = authViewModel.getCurrentUser().getValue();
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;
                updateRequestInfo();
            }
        });

        currentRequest = getRideViewModel.getUsersCurrentRide(currentUser).getValue();
        updateRequestInfo();

        cancelButton = view.findViewById(R.id.cancel_ride_request_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRideViewModel.getUsersCurrentRide(currentUser).observe(getViewLifecycleOwner(), new Observer<Request>() {
                    @Override
                    public void onChanged(Request request) {
                        currentRequest = request;
                        if (currentRequest != null) {
                            updateRequestInfo();
                            cancelRideRequest(currentRequest);
                            navController.navigate(R.id.action_ridePendingFragment_to_selectDestinationFragment);
                        }
                    }
                });
            }
        });


    }

    public void cancelRideRequest(Request request) {
        getRideViewModel.cancelRequest(request);
    }

    public void updateRequestInfo() {
        getRideViewModel.getUsersCurrentRide(currentUser).observe(getViewLifecycleOwner(), new Observer<Request>() {
            @Override
            public void onChanged(Request request) {
                currentRequest = request;
                if (Objects.equals(request.getStatus(), "IN_PROGRESS")) {
                    Toast.makeText(getContext(), request.getDriver().getUsername() + "Accepted this Ride!", Toast.LENGTH_LONG).show();
                    rideRequestStatus.setText("Waiting for " + request.getDriver().getUsername() + " to pick you up.");
                }
            }
        });
    }
}
