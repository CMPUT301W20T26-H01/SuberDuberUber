package com.example.suberduberuber.Fragments;

import android.content.Intent;
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
import android.widget.ImageButton;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.NavigationViewModel;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DriverNavigationFragment extends Fragment implements OnMapReadyCallback {

    private static final int DEFAULT_ZOOM = 150;
    private MapView mMapView;
    private GoogleMap mMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "Auto Complete Log";
    private GeoApiContext mGeoApiContext = null;
    private LatLngBounds latLngBounds;

    private NavigationViewModel navigationViewModel;
    private AuthViewModel authViewModel;

    protected NavController navController;
    private ImageButton doneRideButton;

    public DriverNavigationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_navigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.route_map);

        navController = Navigation.findNavController(view);

        doneRideButton = view.findViewById(R.id.done_button);

        navigationViewModel= new ViewModelProvider(requireActivity()).get(NavigationViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                navigationViewModel.getCurrentRequest(user).observe(getViewLifecycleOwner(), new Observer<Request>() {
                    @Override
                    public void onChanged(Request request) {
                        setLatLngBounds(request);
                        calculateDirections(request);
                    }
                });
            }
        });

        doneRideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToRecievePaymentFragment();
            }
        });

        initGoogleMap(savedInstanceState);
    }

    private void redirectToRecievePaymentFragment() {
        navController.navigate(R.id.action_driverNavigationFragment_to_QRDriverCodeFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void setLatLngBounds(Request request) {
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        latLngBoundsBuilder.include(request.getPath().getDestination().getLatLng());
        latLngBoundsBuilder.include(request.getPath().getStartLocation().getLatLng());
        latLngBounds = latLngBoundsBuilder.build();
    }

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
                addPolylinesToMap(result, request);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());

            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result, Request request){
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
                    mMap.addMarker(new MarkerOptions().position(request.getPath().getStartLocation().getLatLng())
                            .title(request.getPath().getStartLocation().getLocationName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.addMarker(new MarkerOptions().position(request.getPath().getDestination().getLatLng())
                            .title(request.getPath().getDestination().getLocationName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, DEFAULT_ZOOM));
                }
            }
        });
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
                    latLng.lng
            ));
        }
        latLngBounds = latLngBoundsBuilder.build();
        return newDecodedPath;
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

