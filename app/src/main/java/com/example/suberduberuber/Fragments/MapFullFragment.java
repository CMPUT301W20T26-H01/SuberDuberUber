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
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import com.example.suberduberuber.Models.DroppedPinPlace;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Services.PermissionsService;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOError;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class MapFullFragment extends GoogleMapsFragment {

    private static final float DEFAULT_ZOOM = 15;
    private static final String TAG = "Full Map Fragment";

    ImageButton confirmButton;
    EditText textView;
    Place currentPlace = null;
    Place initPlace = null;
    Marker currentMarker = null;
    PlacesClient placesClient;
    int AUTOCOMPLETE_REQUEST_CODE = 1;


    public MapFullFragment() {
        // Required Empty Constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_map_full, container, false);
        mMapView = view.findViewById(R.id.google_map);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        permissionsService = new PermissionsService(getContext(), getActivity());
        initMap(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirmButton = view.findViewById(R.id.confirmButton);
        textView = view.findViewById(R.id.autocomplete);

        Places.initialize(getActivity().getApplicationContext(), getString(R.string.google_map_api_key));
        placesClient = Places.createClient(getContext());

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
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                currentPlace = Autocomplete.getPlaceFromIntent(data);
                //textView.setText(currentPlace.getName());
                if (currentPlace.getLatLng() != null) {
                    updateMarker();
                }
                Log.i(TAG, "Place: " + currentPlace.getName() + ", " + currentPlace.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void makeCurrentLocationMarker() {
        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Class<?> currentClass = this.getClass();
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        LatLng deviceLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                        if (currentClass == SelectOriginFragment.class) {
                            try {
                                currentPlace = new DroppedPinPlace(deviceLatLng, getContext(), "Current Location").getDroppedPinPlace();
                                updateMarker();
                            } catch (IOError | IOException exception) {
                            }
                        } else {
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLatLng, DEFAULT_ZOOM));
                        }
                    }
                }
            });
        } else {
            permissionsService.getLocationPermission();
        }
    }

    @Override
    public void setupMap() {
        super.setupMap();
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                try {
                    DroppedPinPlace droppedPinPlace = new DroppedPinPlace(latLng, getContext(), null);
                    if (droppedPinPlace.getDroppedPinPlace() != null) {
                        currentPlace = droppedPinPlace.getDroppedPinPlace();
                        updateMarker();
                    } else {
                        Toast.makeText(getContext(), "Invalid Pin Location", Toast.LENGTH_LONG).show();
                    }
                } catch (IOError | java.io.IOException exception) {
                    Log.i(TAG, exception.getLocalizedMessage());
                }
            }
        });
        makeCurrentLocationMarker();
    }

    private void updateMarker() {
        if (currentMarker != null) {
            currentMarker.remove();
        }
        currentMarker = mMap.addMarker(new MarkerOptions().position(currentPlace.getLatLng()).title(currentPlace.getName()).snippet(currentPlace.getAddress()));
        currentMarker.showInfoWindow();
        textView.setText(currentPlace.getName());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPlace.getLatLng(), DEFAULT_ZOOM));
    }
}


