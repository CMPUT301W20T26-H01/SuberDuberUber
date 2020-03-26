package com.example.suberduberuber.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
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

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.Distance;
import com.google.maps.model.Duration;

import java.util.ArrayList;
import java.util.List;

public class ConfirmRouteFragment extends Fragment implements OnMapReadyCallback {

    private static final int DEFAULT_ZOOM = 400;
    private MapView mMapView;
    private GoogleMap mMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "Auto Complete Log";
    private GeoApiContext mGeoApiContext = null;
    private LatLngBounds latLngBounds;

    Button submitButton;
    TextView textView;
    TextView pickupTextView;
    TextView pickupTimeTextView;
    TextView destinationTextView;


    private GetRideViewModel getRideViewModel;
    private Request tempRequest;

    protected NavController navController;

    private long duration;
    private long distance;

    public ConfirmRouteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMapView = (MapView) view.findViewById(R.id.route_map);

        navController = Navigation.findNavController(view);

        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        tempRequest = getRideViewModel.getTempRequest().getValue();

        pickupTextView = view.findViewById(R.id.pickupLocation);
        destinationTextView = view.findViewById(R.id.destination);
        setTextViews();

        pickupTimeTextView = view.findViewById(R.id.pickupTime);
        pickupTimeTextView.setText(tempRequest.getTime().toString());

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Submit ride to database
                saveRequest();
                getRideViewModel.commitTempRequest();
                navController.navigate(R.id.action_confrimRouteFragment_to_ridePendingFragment2);
            }
        });
        initGoogleMap(savedInstanceState);
    }

    private void setTextViews() {
        if (tempRequest.getPath().getStartLocation().hasUniqueName) {
            pickupTextView.setText(tempRequest.getPath().getStartLocation().getLocationName());
        }
        else {
            pickupTextView.setText(tempRequest.getPath().getStartLocation().getAddress());
        }

        if (tempRequest.getPath().getDestination().hasUniqueName) {
            destinationTextView.setText(tempRequest.getPath().getDestination().getLocationName());
        }
        else {
            destinationTextView.setText(tempRequest.getPath().getDestination().getAddress());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveRequest() {
        getRideViewModel.saveTempRequest(tempRequest);
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
        calculateDirections();
    }

    private void calculateDirections() {
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                tempRequest.getPath().getDestination().getLatLng().latitude,
                tempRequest.getPath().getDestination().getLatLng().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(false);
        directions.origin(
                new com.google.maps.model.LatLng(
                        tempRequest.getPath().getStartLocation().getLatLng().latitude,
                        tempRequest.getPath().getStartLocation().getLatLng().longitude
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

    public void setRouteValues(DirectionsRoute route) {
        distance = route.legs[0].distance.inMeters;
        duration = route.legs[0].duration.inSeconds;
        tempRequest.getPath().generateEstimatedFare(distance, duration);
    }

    private List<LatLng> setBoundsAndGetRoute(List<com.google.maps.model.LatLng> decodedPath) {
        List<LatLng> newDecodedPath = new ArrayList<>();
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        for(com.google.maps.model.LatLng latLng : decodedPath){
            latLngBoundsBuilder.include(new LatLng(
                    latLng.lat,
                    latLng.lng));
            newDecodedPath.add(new LatLng(
                    latLng.lat,
                    latLng.lng
            ));
        }
        latLngBounds = latLngBoundsBuilder.build();
        return newDecodedPath;
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<LatLng> newDecodedPath = setBoundsAndGetRoute(decodedPath);
                    setRouteValues(route);

                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    mMap.addMarker(new MarkerOptions().position(tempRequest.getPath().getStartLocation().getLatLng())
                            .title("Start")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    mMap.addMarker(new MarkerOptions().position(tempRequest.getPath().getDestination().getLatLng())
                            .title("Destination"));
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
