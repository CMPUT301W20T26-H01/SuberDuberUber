package com.example.suberduberuber.Activities;

import androidx.navigation.Navigation;

import com.example.suberduberuber.R;

public class DriverDashboardActivity extends DashboardActivity {

    @Override
    int getNavHostId() {
        return R.id.nav_host_driver;
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_driver_dashboard;
    }
}
