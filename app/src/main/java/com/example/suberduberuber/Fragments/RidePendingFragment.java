package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 */
public class RidePendingFragment extends Fragment implements OnMapReadyCallback {

    private NavController navController;
    private GetRideViewModel getRideViewModel;
    private AuthViewModel authViewModel;

    private User currentUser;
    private Request currentRequest;

    private Button cancelButton;
    private Button completeButton;
    private TextView rideRequestStatus;

    private static final int DEFAULT_ZOOM = 150;
    private MapView mMapView;
    private GoogleMap mMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "Auto Complete Log";
    private GeoApiContext mGeoApiContext = null;
    private LatLngBounds latLngBounds;


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

        mMapView = (MapView) view.findViewById(R.id.ride_request_live_route);

        navController = findNavController(view);
        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        rideRequestStatus = view.findViewById(R.id.rideRequestStatus);

        currentUser = authViewModel.getCurrentUser().getValue();
        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                currentUser = user;
                updateRequestInfo();
            }
        });

        if (currentUser != null) {
            currentRequest = getRideViewModel.getUsersCurrentRide(currentUser).getValue();
            updateRequestInfo();
        }

        completeButton = view.findViewById(R.id.complete_ride_request_button);
        completeButton.setVisibility(View.GONE);
        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //on ride completion
            }
        });

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
        initGoogleMap(savedInstanceState);
    }

    public void cancelRideRequest(Request request) {
        getRideViewModel.cancelRequest(request);
    }

    public void updateRequestInfo() {
        getRideViewModel.getUsersCurrentRide(currentUser).observe(getViewLifecycleOwner(), new Observer<Request>() {
            @Override
            public void onChanged(Request request) {
                currentRequest = request;
                calculateDirections();
                if (Objects.equals(request.getStatus(), "IN_PROGRESS")) {
                    Toast.makeText(getContext(), request.getDriver().getUsername() + " accepted your ride!", Toast.LENGTH_SHORT).show();
                    completeButton.setVisibility(View.VISIBLE);
                    rideRequestStatus.setText("Waiting for " + request.getDriver().getUsername() + " to pick you up.");
                }
            }
        });
    }


    // BEGINS MAPS CODE
    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);

        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_map_api_key))
                    .build();
        }
    }

    private List<com.google.android.gms.maps.model.LatLng> setBoundsAndGetRoute(List<com.google.maps.model.LatLng> decodedPath) {
        List<com.google.android.gms.maps.model.LatLng> newDecodedPath = new ArrayList<>();
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        for(com.google.maps.model.LatLng latLng : decodedPath){
            latLngBoundsBuilder.include(new com.google.android.gms.maps.model.LatLng(
                    latLng.lat,
                    latLng.lng));
            newDecodedPath.add(new com.google.android.gms.maps.model.LatLng(
                    latLng.lat,
                    latLng.lng));
        }
        latLngBounds = latLngBoundsBuilder.build();
        return newDecodedPath;
    }

    private void calculateDirections() {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                currentRequest.getPath().getDestination().getLatLng().latitude,
                currentRequest.getPath().getDestination().getLatLng().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(false);
        directions.origin(
                new com.google.maps.model.LatLng(
                        currentRequest.getPath().getStartLocation().getLatLng().latitude,
                        currentRequest.getPath().getStartLocation().getLatLng().longitude
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
                addPolylinesToMap(result);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());

            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<com.google.android.gms.maps.model.LatLng> newDecodedPath = setBoundsAndGetRoute(decodedPath);

                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    mMap.addMarker(new MarkerOptions().position(currentRequest.getPath().getStartLocation().getLatLng())
                            .title("Start")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
                    mMap.addMarker(new MarkerOptions().position(currentRequest.getPath().getDestination().getLatLng())
                            .title("Destination")).showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, DEFAULT_ZOOM));
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }
        mMapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }
    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

}
