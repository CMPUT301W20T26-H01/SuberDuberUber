package com.example.suberduberuber.Fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;


import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.suberduberuber.Clients.UserClient;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.DriverLocationViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

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
    private DriverLocationViewModel driverLocationViewModel;

    private User currentUser;

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
    private List<com.google.maps.model.LatLng> decodedPath;
    private Marker driverMaker;
    private GeoPoint driverLocation;
    private double DISTANCE_FACTOR = 0.002;
    private LatLngBounds nearbyBounds;


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
        rideRequestStatus = view.findViewById(R.id.rideRequestStatus);

        navController = findNavController(view);
        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);
        driverLocationViewModel = new ViewModelProvider(requireActivity()).get(DriverLocationViewModel.class);

        currentUser = ((UserClient)(getActivity().getApplicationContext())).getUser();

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

        initGoogleMap(savedInstanceState);
    }

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
            if (decodedPath != null) {
                setBounds();
            }
        }
        catch (NullPointerException exception) {
            Log.i(TAG, "Null Pointer Exception");
        }
    }

    public void updateRequestInfo(Request request) {
        if (Objects.equals(request.getStatus(), "IN_PROGRESS")) {
            updateDriverLocation(request);
            rideRequestStatus.setText("Waiting for " + request.getDriver().getUsername() + " to pick you up.");
            rideRequestStatus.setClickable(true);
            rideRequestStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (request.getDriver() != null) {
                        Bundle bundle = new Bundle();
                        bundle.putString("driverEmail", request.getDriver().getEmail());
                        bundle.putString("driverName", request.getDriver().getUsername());
                        bundle.putString("driverPhone", request.getDriver().getPhone());
                        bundle.putInt("driverRating", (int) request.getDriver().getRating());
                        navController.navigate(R.id.action_ridePendingFragment_to_driverDetailsFragment, bundle);
                    }
                }
            });
        }
        nearbyBounds(request.getPath().getStartLocation().getLatLng());
        calculateDirections(request);
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
            } catch (NullPointerException exception) {
            }
        }
    };

    private void updateDriverLocation(Request request) {
            driverLocationViewModel.removeObserver(driverLocationObserver);
            driverLocationViewModel.getDriverLocation(request).observe(getViewLifecycleOwner(), driverLocationObserver);
    }

    private void driverNearby() {
        cancelButton.setClickable(false);
        cancelButton.setText("Get Ready, Your Driver is Close!");
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

    private void nearbyBounds(com.google.android.gms.maps.model.LatLng location) {
        double latDist = DISTANCE_FACTOR;
        double longDist = DISTANCE_FACTOR/Math.cos(Math.toRadians(location.latitude));
        com.google.android.gms.maps.model.LatLng min = new com.google.android.gms.maps.model.LatLng(location.latitude - latDist, location.longitude - longDist);
        com.google.android.gms.maps.model.LatLng max = new com.google.android.gms.maps.model.LatLng(location.latitude + latDist, location.longitude + longDist);
        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(min);
        builder.include(max);
        nearbyBounds = builder.build();
    }

    public void setBounds() {
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        for(com.google.maps.model.LatLng latLng : decodedPath){
            latLngBoundsBuilder.include(new com.google.android.gms.maps.model.LatLng(
                    latLng.lat,
                    latLng.lng));
        }
        if (driverLocation != null) {
            latLngBoundsBuilder.include(new com.google.android.gms.maps.model.LatLng(driverLocation.getLatitude(), driverLocation.getLongitude()));
        }
        latLngBounds = latLngBoundsBuilder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, DEFAULT_ZOOM));
    }

    private List<com.google.android.gms.maps.model.LatLng> getRoute() {
        List<com.google.android.gms.maps.model.LatLng> newDecodedPath = new ArrayList<>();
        for(com.google.maps.model.LatLng latLng : decodedPath){
            newDecodedPath.add(new com.google.android.gms.maps.model.LatLng(
                    latLng.lat,
                    latLng.lng));
        }

        setBounds();
        return newDecodedPath;
    }

    private void calculateDirections(Request request) {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                request.getPath().getDestination().getLatLng().latitude,
                request.getPath().getDestination().getLatLng().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(false);
        directions.origin(
                new com.google.maps.model.LatLng(
                        request.getPath().getStartLocation().getLatLng().latitude,
                        request.getPath().getStartLocation().getLatLng().longitude
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

                drawPath(result, request);
                Log.d(TAG, "Drawing Path");
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());
            }
        });
    }

    private void drawPath(final DirectionsResult result, Request request){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<com.google.android.gms.maps.model.LatLng> newDecodedPath = getRoute();
                    mMap.clear();
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    addMarkersToMap(request);
                }
            }
        });
    }

    private void addMarkersToMap(Request request) {
        mMap.addMarker(new MarkerOptions().position(request.getPath().getStartLocation().getLatLng())
                .title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(request.getPath().getDestination().getLatLng())
                .title("Destination"));
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
        getRideViewModel.getUsersCurrentRide(currentUser).observe(getViewLifecycleOwner(), new Observer<Request>() {
            @Override
            public void onChanged(Request request) {
                if(request != null) {
                    updateRequestInfo(request);
                }
            }
        });
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
