package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.suberduberuber.Clients.UserClient;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.Services.PermissionsService;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.DriverLocationViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 */
public class RidePendingFragment extends GoogleMapsFragment {

    private NavController navController;
    private GetRideViewModel getRideViewModel;
    private AuthViewModel authViewModel;
    private DriverLocationViewModel driverLocationViewModel;
    private User currentUser;

    private Button cancelButton;
    private Button completeButton;
    private Button viewDriverDetailsButton;
    private LinearLayout waitingProgressBar;
    private TextView titleText;

    private static final String TAG = "Auto Complete Log";
    private Marker driverMaker;
    private double DISTANCE_FACTOR = 0.002;
    private LatLngBounds nearbyBounds;
    private LatLngBounds endBounds;

    public RidePendingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_ride_pending, container, false);
        mMapView = view.findViewById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        permissionsService = new PermissionsService(getContext(), getActivity());
        initMap(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = findNavController(view);
        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        driverLocationViewModel = new ViewModelProvider(requireActivity()).get(DriverLocationViewModel.class);

        currentUser = ((UserClient)(requireActivity().getApplicationContext())).getUser();

        waitingProgressBar = view.findViewById(R.id.waiting_loader);
        titleText = view.findViewById(R.id.title_text);

        completeButton = view.findViewById(R.id.complete_ride_request_button);
        completeButton.setVisibility(View.GONE);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_ridePendingFragment_to_scanqrcodeFragment);
            }
        });

        viewDriverDetailsButton = view.findViewById(R.id.driver_details_button);
        viewDriverDetailsButton.setVisibility(View.GONE);

        cancelButton = view.findViewById(R.id.cancel_ride_request_button);

        UserRepository.getCurrentUser().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    currentUser = task.getResult().toObject(User.class);
                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getRideViewModel.cancelCurrentRequest(currentUser);
                            getRideViewModel.removeObservers(getViewLifecycleOwner());
                            navController.navigate(R.id.action_ridePendingFragment_to_selectDestinationFragment);
                        }
                    });
                    getRideViewModel.getUsersCurrentRide(currentUser).observe(getViewLifecycleOwner(), new Observer<Request>() {
                        @Override
                        public void onChanged(Request request) {
                            if(request != null) {
                                updateRequestInfo(request);
                            }
                        }
                    });
                }
            }
        });
    }

    public void updateRequestInfo(Request request) {

        if(request.getStatus().equals("ACCEPTED")) {
            displayAcceptedState(request);
            updateDriverLocation(request);
        }

        else if(request.getStatus().equals("IN_PROGRESS")) {
            displayInProgressState(request);
            updateDriverLocation(request);
        }

        nearbyBounds = createGeoFenceBounds(request.getPath().getStartLocation().getLatLng());
        endBounds = createGeoFenceBounds(request.getPath().getDestination().getLatLng());
        calculateDirections(request);
    }

    private void displayAcceptedState(Request request) {
        waitingProgressBar.setVisibility(View.GONE);
        viewDriverDetailsButton.setVisibility(View.VISIBLE);
        titleText.setText("Ride Accepted!");
        completeButton.setText("Get ready, driver " + request.getDriver().getUsername() + " is nearby!");
        viewDriverDetailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (request.getDriver() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString("driverEmail", request.getDriver().getEmail());
                    bundle.putString("driverName", request.getDriver().getUsername());
                    bundle.putString("driverPhone", request.getDriver().getPhone());
                    bundle.putFloat("driverRating",  (float) request.getDriver().getRating());
                    navController.navigate(R.id.action_ridePendingFragment_to_driverDetailsFragment, bundle);
                }
            }
        });
    }

    private void displayInProgressState(Request request) {
        titleText.setVisibility(View.GONE);
        completeButton.setText("Almost There!");
        cancelButton.setVisibility(View.GONE);
        viewDriverDetailsButton.setVisibility(View.GONE);
        completeButton.setVisibility(View.VISIBLE);
    }

    private Observer<GeoPoint> driverLocationObserver = new Observer<GeoPoint>() {
        @Override
        public void onChanged(GeoPoint geoPoint) {
            try {
                driverLocation = geoPoint;
                if (geoPoint != null) {
                    updateDriverPin();
                }
                if (nearbyBounds != null) {
                    if (nearbyBounds.contains(new com.google.android.gms.maps.model.LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()))) {
                        driverNearby();
                    }
                }
                if (endBounds != null) {
                    if (endBounds.contains(new com.google.android.gms.maps.model.LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()))) {
                        driverEnd();
                    }
                }
            } catch (NullPointerException exception) {
            }
        }
    };

    private void updateDriverPin() {
        try {
            if (driverMaker != null) {
                driverMaker.remove();

            }
            if (driverLocation != null) {
                com.google.android.gms.maps.model.LatLng driveLatLng = new com.google.android.gms.maps.model.LatLng(driverLocation.getLatitude(), driverLocation.getLongitude());
                driverMaker = mMap.addMarker(new MarkerOptions().position(driveLatLng)
                        .title("Driver Is Here")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.car_icon))
                        .anchor(0.5f, 0.5f)
                        .zIndex(2.0f));
            }
            if (routePath != null) {
                setMapBounds();
            }
        }
        catch (NullPointerException exception) {
            Log.i(TAG, "Null Pointer Exception");
        }
    }

    private void updateDriverLocation(Request request) {
            driverLocationViewModel.removeObserver(driverLocationObserver);
            driverLocationViewModel.getDriverLocation(request).observe(getViewLifecycleOwner(), driverLocationObserver);
    }

    private void driverNearby() {
        titleText.setText("Driver is Nearby!");
        cancelButton.setVisibility(View.GONE);
        completeButton.setVisibility(View.VISIBLE);
        completeButton.setOnClickListener(null);
    }

    private void driverEnd() {
        completeButton.setVisibility(View.VISIBLE);
        titleText.setVisibility(View.VISIBLE);
        titleText.setText("Ride Completed");
        completeButton.setText("Pay Driver!");
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_ridePendingFragment_to_scanqrcodeFragment);
            }
        });
    }


    private LatLngBounds createGeoFenceBounds(com.google.android.gms.maps.model.LatLng location) {
        double latDist = DISTANCE_FACTOR;
        double longDist = DISTANCE_FACTOR/Math.cos(Math.toRadians(location.latitude));
        com.google.android.gms.maps.model.LatLng min = new com.google.android.gms.maps.model.LatLng(location.latitude - latDist, location.longitude - longDist);
        com.google.android.gms.maps.model.LatLng max = new com.google.android.gms.maps.model.LatLng(location.latitude + latDist, location.longitude + longDist);
        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(min);
        builder.include(max);
        return builder.build();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (currentUser != null) {
            getRideViewModel.getUsersCurrentRide(currentUser).observe(getViewLifecycleOwner(), new Observer<Request>() {
                @Override
                public void onChanged(Request request) {
                    if (request != null) {
                        updateRequestInfo(request);
                    }
                }
            });
        }
    }

}
