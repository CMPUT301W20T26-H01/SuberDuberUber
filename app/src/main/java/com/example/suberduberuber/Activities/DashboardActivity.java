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
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.suberduberuber.Clients.UserClient;
import com.example.suberduberuber.Models.DroppedPinPlace;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.Models.UserLocation;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.UserLocationRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.Services.LocationService;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
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

    public boolean locationPermissionGranted = false;
    public static final int PERMISSIONS_REQUEST_ENABLE_GPS = 9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    public static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        myAuth = FirebaseAuth.getInstance();

        userRepository = new UserRepository();
        requestRepository = new RequestRepository();
        userLocationRepository = new UserLocationRepository();

        navController = Navigation.findNavController(this, getNavHostId());

        navigationView = findViewById(R.id.nav_view);

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
                if(locationPermissionGranted){
                    getUserDetails();
                }
                else{
                    getLocationPermission();
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
            SimpleLocation location = new SimpleLocation(this);
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            LatLng deviceLatLng = new LatLng(latitude, longitude);
            userGeoPoint = new GeoPoint(latitude, longitude);

            userLocation.setGeoPoint(userGeoPoint);
            userLocation.setTimeStamp(null);
            saveUserLocation();
            startLocationService();
        }
        else {
            getLocationPermission();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(checkMapServices()) {
            if(!locationPermissionGranted) {
                getLocationPermission();
            }
            else {
                getUserDetails();
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
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );

        if ( !manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
            getUserDetails();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(DashboardActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(DashboardActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
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
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
    }

    // ENDS METHODS FROM GIT HUB FOR LOCATION SERVICES

}

