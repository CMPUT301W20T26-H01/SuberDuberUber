package com.example.suberduberuber.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.suberduberuber.Clients.UserClient;
import com.example.suberduberuber.Fragments.SelectOriginFragment;
import com.example.suberduberuber.Models.DroppedPinPlace;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Models.UserLocation;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.UserLocationRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.Services.LocationService;
import com.example.suberduberuber.Services.PermissionsService;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOError;
import java.io.IOException;

import im.delight.android.location.SimpleLocation;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;




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

    This activity currently holds fragments for a rider's ride request creation.
    It also includes a toolbar with a hamburger menu that contains home (select
    a destination for a ride request), profile (currently a dummy place for the
    rider's profile), request (currently a dummy place to store all rider's request),
    generate wallet (temporary place to generate a user's QR wallet), and sign out
    (redirects back to log in page and logs out user).
 */

abstract class DashboardActivity extends AppCompatActivity {

    private static final String TAG = "Dashboard Activity";
    private FirebaseAuth myAuth;
    private UserRepository userRepository;
    private RequestRepository requestRepository;
    private UserLocationRepository userLocationRepository;
    private GeoPoint userGeoPoint;
    private UserLocation userLocation;

    protected NavController navController;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;

    private PermissionsService permissionsService;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        myAuth = FirebaseAuth.getInstance();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        userRepository = new UserRepository();
        requestRepository = new RequestRepository();
        userLocationRepository = new UserLocationRepository();

        navController = Navigation.findNavController(this, getNavHostId());

        navigationView = findViewById(R.id.nav_view);

        permissionsService = new PermissionsService(this, this);

        drawerLayout = findViewById(getDrawerLayoutId());
        new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawerLayout)
                .build();

        configureToolbar();
        NavigationUI.setupWithNavController(navigationView, navController);
        configureNavigationDrawer();

        goToDestHomeOrRidePending();
    }

    abstract int getNavHostId();
    abstract int getContentViewId();
    abstract int getDrawerLayoutId();
    abstract int getMenuLayoutId();

    // for QR Code Fragment
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_rider);
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, intent);
        }
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ENABLE_GPS: {
                if(permissionsService.locationPermissionGranted){
                    getUserDetails();
                }
                else{
                    permissionsService.getLocationPermission();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(getMenuLayoutId(), navigationView.getMenu());
        return true;
    }

    private void configureToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_menu_white);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureNavigationDrawer() {
        drawerLayout = findViewById(getDrawerLayoutId());
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();

                if (itemId == R.id.menu_home_rider) {
                    goToDestHomeOrRidePending();
                }
                else if (itemId == R.id.menu_home_driver) {
                    goToDestHomeOrRidePending();
                }
                else if (itemId == R.id.profile) {
                    navController.navigate(R.id.action_to_profile_page);
                }
                else if (itemId == R.id.requests) {
                    navController.navigate(R.id.action_to_request_page);
                }
                else if (itemId == R.id.wallet) {
                    navController.navigate(R.id.action_to_wallet);
                }
                else if(itemId == R.id.logout) {
                    logout();
                }
                else {
                    return false;
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch (itemId) {
            case android.R.id.home: // hamburger menu icon
                drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return true;
    }

    private void logout() {
        myAuth.signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity((intent));
        finish();
    }

    private void goToDestHomeOrRidePending() {
        userRepository.getCurrentUser().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                if (user.getDriver()) {
                    requestRepository.getDriverCurrentRequest(user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult().getDocuments().size() > 0) {
                                navController.navigate(R.id.action_to_driver_navigation_page);
                            } else {
                                navController.navigate(R.id.action_to_driver_req_home);
                            }
                        }
                    });
                }
                else {
                    requestRepository.getRidersCurrentRequest(user).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.getResult().getDocuments().size() > 0) {
                                navController.navigate(R.id.action_to_ridePending_page);
                            } else {
                                navController.navigate(R.id.action_to_dest_home);
                            }
                        }
                    });
                }
            }
        });
    }

    private void getUserDetails() {
        if (userLocation == null) {
            userLocation = new UserLocation();
            userRepository.getCurrentUser().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        User user = task.getResult().toObject(User.class);
                        ((UserClient)(getApplicationContext())).setUser(user);
                        userLocation.setUser(user);
                        getDeviceLocation();
                    }
                }
            });
        }
        else {
            getDeviceLocation();
        }
    }

    private void saveUserLocation() {
        if (userLocation != null) {
            userLocationRepository.saveLocation(userLocation);
        }
    }

    private void getDeviceLocation() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    double latitude = task.getResult().getLatitude();
                    double longitude = task.getResult().getLongitude();
                    userGeoPoint = new GeoPoint(latitude, longitude);

                    userLocation.setGeoPoint(userGeoPoint);
                    userLocation.setTimeStamp(null);
                    saveUserLocation();
                    startLocationService();
                }
            });
        }
        else {
            permissionsService.getLocationPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(permissionsService.checkMapServices()) {
            if(!permissionsService.locationPermissionGranted) {
                permissionsService.getLocationPermission();
                getUserDetails();
            }
            else {
                getUserDetails();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsService.locationPermissionGranted = false;
        switch (requestCode) {
            case PermissionsService.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionsService.locationPermissionGranted = true;
                }
            }
        }
    }

    private void startLocationService(){
        if(!isLocationServiceRunning()){
            Intent serviceIntent = new Intent(this, LocationService.class);
            //        this.startService(serviceIntent);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
                DashboardActivity.this.startForegroundService(serviceIntent);
            }else{
                startService(serviceIntent);
            }
        }
    }
    private boolean isLocationServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)){
            if("com.example.suberduberuber.Services.LocationService".equals(service.service.getClassName())) {
                Log.d(TAG, "isLocationServiceRunning: location service is already running.");
                return true;
            }
        }
        Log.d(TAG, "isLocationServiceRunning: location service is not running.");
        return false;
    }
}

