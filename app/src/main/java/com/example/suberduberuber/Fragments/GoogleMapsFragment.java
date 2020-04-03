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

Fragment to handle all background GoogleMaps functions. Handles creating and updating the map for fragments
which extend this one. Provides extending fragments with functions to draw routes and set bounds. Maintains
all GoogleMaps functionality. Extended by several other fragments.
Sources include:
    GoogleMaps SKD Android Documentation - https://developers.google.com/maps/documentation/android-sdk/intro
    GooglePlaces SDK Android Documentation - https://developers.google.com/places/android-sdk/intro
    GoogleDirections API Documentation - https://developers.google.com/maps/documentation/directions/intro
    GoogleDirections code based on code from GitHub User mitchtabian - https://gist.github.com/mitchtabian/b8a2dee2804bd1a58c09b045515e430e
    GoogleMaps YouTube Playlist by User CodingWithMitch - https://www.youtube.com/watch?v=RQxY7rrZATU&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi
 */

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Services.PermissionsService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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


public abstract class GoogleMapsFragment extends Fragment implements OnMapReadyCallback {

    protected MapView mMapView;
    protected GoogleMap mMap;
    protected GeoApiContext mGeoApiContext = null;

    private String TAG = "Maps Services";
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    private static final int DEFAULT_ZOOM = 150;

    protected List<com.google.maps.model.LatLng> routePath;
    protected FusedLocationProviderClient fusedLocationProviderClient;
    protected GeoPoint driverLocation;

    protected PermissionsService permissionsService;
    public boolean locationPermissionGranted = false;


    public GoogleMapsFragment() {

    }

    protected  void dropMapMarker(LatLng latLng, String title, @Nullable String info) {
        if (info == null) {
            mMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .title(title)
            );
        }
        else {
            mMap.addMarker(
                    new MarkerOptions()
                            .position(latLng)
                            .title(title)
                            .snippet(info)
            );
        }
    }

    protected void setCameraOnBounds(LatLngBounds latLngBounds) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, DEFAULT_ZOOM));
    }

    protected LatLngBounds getLargeGeoBounds(Location location, double distanceFactor) {
        double latDist = distanceFactor;
        double longDist = distanceFactor/Math.cos(Math.toRadians(location.getLatitude()));
        LatLng min = new LatLng(location.getLatitude() - latDist, location.getLongitude() - longDist);
        LatLng max = new LatLng(location.getLatitude() + latDist, location.getLongitude() + longDist);
        LatLngBounds.Builder builder = LatLngBounds.builder();
        builder.include(min);
        builder.include(max);
        return builder.build();
    }

    protected void addRequestMarkersToMap(Request request) {
        mMap.addMarker(new MarkerOptions().position(request.getPath().getStartLocation().getLatLng())
                .title("Start")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).showInfoWindow();
        mMap.addMarker(new MarkerOptions().position(request.getPath().getDestination().getLatLng())
                .title("Destination"));
    }

    protected void calculateDirections(Request request) {
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
                drawPath(result, request);
                Log.d(TAG, "Drawing Path");
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage());
            }
        });
    }

    protected void drawPath(final DirectionsResult result, Request request){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    routePath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());
                    List<com.google.android.gms.maps.model.LatLng> newRoutePath = getRoute();
                    mMap.clear();
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newRoutePath));
                    polyline.setColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
                    addRequestMarkersToMap(request);
                }
            }
        });
    }

    private List<LatLng> getRoute() {
        List<com.google.android.gms.maps.model.LatLng> newRoutePath = new ArrayList<>();
        for(com.google.maps.model.LatLng latLng : routePath){
            newRoutePath.add(new com.google.android.gms.maps.model.LatLng(
                    latLng.lat,
                    latLng.lng));
        }
        setMapBounds();
        return newRoutePath;
    }

    protected void setMapBounds() {
        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
        for(com.google.maps.model.LatLng latLng : routePath){
            latLngBoundsBuilder.include(new com.google.android.gms.maps.model.LatLng(
                    latLng.lat,
                    latLng.lng));
        }
        if (driverLocation != null) {
            latLngBoundsBuilder.include(new com.google.android.gms.maps.model.LatLng(driverLocation.getLatitude(), driverLocation.getLongitude()));
        }
        LatLngBounds latLngBounds = latLngBoundsBuilder.build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, DEFAULT_ZOOM));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "onActivityResult: called.");
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if (locationPermissionGranted) {
                } else {
                    permissionsService.getLocationPermission();
                }
            }
        }
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


    public void initMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        if (mGeoApiContext == null) {
            mGeoApiContext = new GeoApiContext.Builder()
                    .apiKey(getString(R.string.google_map_api_key))
                    .build();
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
    }

    public void setupMap() {
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        setupMap();
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
        if(permissionsService.checkMapServices()) {
            if(!locationPermissionGranted) {
                permissionsService.getLocationPermission();
            }
        }
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
