package com.example.suberduberuber.Fragments;

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

****************************************************************************************************

Fragment to show information to the driver about the ride they have accepted. Allows the driver to see
a route to the pickup location of the rider and after confirming arrival displays a route to the drop
off location. After confirming drop off redirects to users wallet to accept payment. Extends GoogleMapsFragment.
Sources include:
    GoogleMaps SKD Android Documentation - https://developers.google.com/maps/documentation/android-sdk/intro
    GooglePlaces SDK Android Documentation - https://developers.google.com/places/android-sdk/intro
    GoogleMaps YouTube Playlist by User CodingWithMitch - https://www.youtube.com/watch?v=RQxY7rrZATU&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi
    Permission Services Code from GitHub User mitchtabian - https://gist.github.com/mitchtabian/2b9a3dffbfdc565b81f8d26b25d059bf
 */

import android.content.Intent;
import android.location.Location;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Services.PermissionsService;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.DriverPaidRateViewModel;
import com.example.suberduberuber.ViewModels.NavigationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverNavigationFragment extends GoogleMapsFragment {

    private FusedLocationProviderClient fusedLocationProviderClient;

    private Boolean showingPickupRoute = Boolean.TRUE;

    private NavigationViewModel navigationViewModel;
    private AuthViewModel authViewModel;
    private DriverPaidRateViewModel driverPaidRateViewModel;

    private Boolean reqSetup;

    protected NavController navController;

    private LinearLayout arrivedButton;
    private TextView navStatusBar;

    public DriverNavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_driver_navigation, container, false);
        mMapView = view.findViewById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        permissionsService = new PermissionsService(getContext(), getActivity());
        initMap(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reqSetup = false;

        navController = Navigation.findNavController(view);

        arrivedButton = view.findViewById(R.id.arrived_button);
        navStatusBar = view.findViewById(R.id.driverNavStatus);

        navigationViewModel= new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        driverPaidRateViewModel = new ViewModelProvider(requireActivity()).get(DriverPaidRateViewModel.class);

        showPickupRoute();

        arrivedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showingPickupRoute) {
                    showRideRoute();
                    setRequestToInProgress();
                } else {
                    redirectToRecievePaymentFragment();
                }
            }
        });
    }

    private void showRoute() {
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                navigationViewModel.getCurrentRequestOnEvent(user).observe(getViewLifecycleOwner(), new Observer<Request>() {
                    @Override
                    public void onChanged(Request request) {
                        // reqSetup accounts for the first onChange where request can == null from not being setup
                        if (request == null && reqSetup) {
                            reqSetup = false;
                            Toast.makeText(getActivity(), "Rider has cancelled the rider", Toast.LENGTH_SHORT).show();
                            navController.navigate(R.id.action_to_viewRequests);
                        }
                        else if (request != null) {
                            if (Objects.equals(request.getStatus(), "IN_PROGRESS")) {
                                showingPickupRoute = Boolean.FALSE;
                            }

                            driverPaidRateViewModel.setRequest(request);
                            driverPaidRateViewModel.setRider(request.getRequestingUser());
                            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (showingPickupRoute & location != null) {
                                        Request pickupRouteRequest = mockPickupRouteRequest(location, request);
                                        calculateDirections(pickupRouteRequest);
                                    } else {
                                        navStatusBar.setText("Route to drop off location");
                                        calculateDirections(request);
                                    }
                                }
                            });
                        }
                        reqSetup = true;
                    }
                });
            }
        });
    }

    private Request mockPickupRouteRequest(Location location, Request request) {
        Request pickupRouteRequest = new Request();
        CustomLocation startLocation = new CustomLocation();
        startLocation.setLatitude(location.getLatitude());
        startLocation.setLongitude(location.getLongitude());
        pickupRouteRequest.setPath(new Path(startLocation, request.getPath().getStartLocation()));
        return pickupRouteRequest;
    }

    private void setRequestToInProgress() {
        User user = authViewModel.getCurrentUser().getValue();
        Request request = navigationViewModel.getCurrentRequest(user).getValue();
        request.pickup();
        navigationViewModel.updateRequest(request);
    }

    private void showPickupRoute() {
        showingPickupRoute = Boolean.TRUE;
        showRoute();
    }

    private void showRideRoute() {
        mMap.clear();
        showingPickupRoute = Boolean.FALSE;
        showRoute();
    }

    private void redirectToRecievePaymentFragment() {
        navController.navigate(R.id.action_driverNavigationFragment_to_QRDriverCodeFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

}

