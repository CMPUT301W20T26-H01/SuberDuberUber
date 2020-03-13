package com.example.suberduberuber.Activities;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.R;

public class RiderDashboardActivity extends DashboardActivity {

    @Override
    int getNavHostId() {
        return R.id.nav_host_rider;
    }
    @Override
    int getContentViewId() {
        return R.layout.activity_rider_dashboard;
    }
}
