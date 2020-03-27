package com.example.suberduberuber.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
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

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.RequestRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

/*
    This activity currently holds fragments for a rider's ride request creation.
    It also includes a toolbar with a hamburger menu that contains home (select
    a destination for a ride request), profile (currently a dummy place for the
    rider's profile), request (currently a dummy place to store all rider's request),
    generate wallet (temporary place to generate a user's QR wallet), and sign out
    (redirects back to log in page and logs out user).
 */

abstract class DashboardActivity extends AppCompatActivity {

    private FirebaseAuth myAuth;
    private UserRepository userRepository;
    private RequestRepository requestRepository;

    protected NavController navController;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        myAuth = FirebaseAuth.getInstance();

        userRepository = new UserRepository();
        requestRepository = new RequestRepository();

        navController = Navigation.findNavController(this, getNavHostId());

        navigationView = findViewById(R.id.nav_view);

        drawerLayout = findViewById(getDrawerLayoutId());
        new AppBarConfiguration.Builder(navController.getGraph())
                .setDrawerLayout(drawerLayout)
                .build();

        configureToolbar();
        NavigationUI.setupWithNavController(navigationView, navController);
        configureNavigationDrawer();

        // only rider's page does this
        if (getNavHostId() == R.id.nav_host_rider) {
            goToDestHomeOrRidePending();
        }
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
                    navController.navigate(R.id.action_to_driver_req_home);
                }
                else if (itemId == R.id.profile) {
                    navController.navigate(R.id.action_to_profile_page);
                }
                else if (itemId == R.id.requests) {
                    navController.navigate(R.id.action_to_request_page);
                }
                else if (itemId == R.id.genQRCode) {
                    navController.navigate(R.id.action_to_gen_qr_code);
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
        });
    }

}

