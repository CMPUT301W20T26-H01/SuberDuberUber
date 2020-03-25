package com.example.suberduberuber.Activities;

import androidx.navigation.Navigation;

import com.example.suberduberuber.R;

/**
 *  This is a concrete extension of the Dashboard activity that imlements the methods that enable
 *  it to properly setup its Navigation Controller. The main goal with this design was to avoid remaking
 *  the basic functionality involved in Hosting navitation, etc.
 */
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
