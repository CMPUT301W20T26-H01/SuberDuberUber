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

Fragment class built to display a GoogleMaps view and incorporate GooglePlaces Autocomplete feature
Class includes additional checking for location permissions
Implemented to be used with NavGraph
Extended by Other Fragments for SelectDestination and SelectOrigin
Sources include:
    GoogleMaps SKD Android Documentation - https://developers.google.com/maps/documentation/android-sdk/intro
    GooglePlaces SDK Android Documentation - https://developers.google.com/places/android-sdk/intro
    GoogleMaps YouTube Playlist by User CodingWithMitch - https://www.youtube.com/watch?v=RQxY7rrZATU&list=PLgCYzUzKIBE-SZUrVOsbYMzH7tPigT3gi
    Permission Services Code from GitHub User mitchtabian - https://gist.github.com/mitchtabian/2b9a3dffbfdc565b81f8d26b25d059bf
 */
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.DroppedPinPlace;
import com.example.suberduberuber.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.model.PlusCode;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.maps.GeoApiContext;

import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class MapFullFragment extends Fragment implements OnMapReadyCallback {
    private static final float DEFAULT_ZOOM = 18;
    private MapView mMapView;
    private GoogleMap mMap;
    private static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private static final String TAG = "Auto Complete Log";

    Button confirmButton;
    Button textView;
    Place currentPlace = null;
    Place initPlace = null;
    PlacesClient placesClient;
    LatLng mDefaultLocation = new LatLng(53.2734, -7.77832031);
    int AUTOCOMPLETE_REQUEST_CODE = 1;


    protected NavController navController;

    public boolean locationPermissionGranted = false;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    public static final int ERROR_DIALOG_REQUEST = 9001;
    public static final String ERROR_TAG = "Login Activity";

    public MapFullFragment() {
        // Required Empty Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map_full, container, false);
        mMapView = (MapView) view.findViewById(R.id.full_map);

        // Initialize GooglePlaces
        Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_map_api_key));
        placesClient = Places.createClient(getContext());

        confirmButton = view.findViewById(R.id.confirmButton);

        textView = view.findViewById(R.id.autocomplete);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set Fields to be returned by PlacesAutocomplete
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY, fields)
                        .setCountries(Arrays.asList("CA"))
                        .build(getContext());
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
            }
        });

        initGoogleMap(savedInstanceState);
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Get Autocomplete Results
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                currentPlace = Autocomplete.getPlaceFromIntent(data);
                //textView.setText(currentPlace.getName());
                if (currentPlace.getLatLng() != null) {
                    Marker currentMarker  =mMap.addMarker(new MarkerOptions().position(currentPlace.getLatLng()).title("Selected Location").snippet(currentPlace.getAddress()));
                    currentMarker.showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace.getLatLng(), DEFAULT_ZOOM));
                    //confirmButton.setVisibility(View.VISIBLE);
                }
                Log.i(TAG, "Place: " + currentPlace.getName() + ", " + currentPlace.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
        // Check For Location Permission
        else {
            super.onActivityResult(requestCode, resultCode, data);
            Log.d(ERROR_TAG, "onActivityResult: called.");
            switch (requestCode) {
                case PERMISSIONS_REQUEST_ENABLE_GPS: {
                    if (locationPermissionGranted) {
                    } else {
                        getLocationPermission();
                    }
                }
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }

    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }
        mMapView.onCreate(mapViewBundle);
        mMapView.getMapAsync(this);
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

    private void getDeviceLocation() {
        // Set Fields For Places
        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);
        FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
        // Gets Most Likely Current Location
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<FindCurrentPlaceResponse> placeResponse = placesClient.findCurrentPlace(request);
            placeResponse.addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    FindCurrentPlaceResponse response = task.getResult();
                    double probability = -1;
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Log.i(TAG, String.format("Place '%s' has likelihood: %f",
                                placeLikelihood.getPlace().getName(),
                                placeLikelihood.getLikelihood()));
                        if (placeLikelihood.getLikelihood() > probability) {
                            probability = placeLikelihood.getLikelihood();
                            initPlace = placeLikelihood.getPlace();
                        }
                    }
                    if (initPlace != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                initPlace.getLatLng(), DEFAULT_ZOOM));
                        Marker initMarker = mMap.addMarker(new MarkerOptions().position(initPlace.getLatLng()).title("Current Location").snippet(initPlace.getAddress()));
                        initMarker.showInfoWindow();
                    }
                } else {
                    Exception exception = task.getException();
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                    }
                }
            });
        }
        // Location Permission Missing
        else {
            getLocationPermission();
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                DroppedPinPlace droppedPinPlace = new DroppedPinPlace(latLng);
                if (droppedPinPlace.getDroppedPinPlace() != null) {
                    currentPlace = droppedPinPlace.getDroppedPinPlace();
                    Marker currentMarker  = mMap.addMarker(new MarkerOptions().position(currentPlace.getLatLng()).title("Selected Location").snippet(currentPlace.getAddress()));
                    currentMarker.showInfoWindow();
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace.getLatLng(), DEFAULT_ZOOM));
                }
                else {
                    Toast.makeText(getContext(), "Invalid Pin Location", Toast.LENGTH_LONG).show();
                }
            }
        });
        map.setMyLocationEnabled(true);
        map.getUiSettings().setZoomControlsEnabled(true);
        getDeviceLocation();

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
        if(checkMapServices()) {
            if(!locationPermissionGranted) {
                getLocationPermission();
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

    // ALL METHODS BELOW ARE FROM GITHUB
    // USER: mitchtabian
    // URL: https://gist.github.com/mitchtabian/2b9a3dffbfdc565b81f8d26b25d059bf
    // Code is to ask user for permission to use location services for maps
    private boolean checkMapServices(){
        if(isServicesOK()){
            if(isMapsEnabled()){
                return true;
            }
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("This application requires GPS to work properly, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled(){
        final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(ERROR_TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());

        if(available == ConnectionResult.SUCCESS){
            Log.d(ERROR_TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d(ERROR_TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(getActivity(), "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
    }
    // ENDS METHODS FROM GIT HUB FOR LOCATION SERVICES
}
