package com.example.suberduberuber.Activities;
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
 */

import com.example.suberduberuber.R;

/**
 *  This is a concrete extension of the Dashboard activity that imlements the methods that enable
 *  it to properly setup its Navigation Controller. The main goal with this design was to avoid remaking
 *  the basic functionality involved in Hosting navitation, etc.
 */
public class RiderDashboardActivity extends DashboardActivity {

    @Override
    int getNavHostId() {
        return R.id.nav_host_rider;
    }
    @Override
    int getContentViewId() {
        return R.layout.activity_rider_dashboard;
    }
    @Override
    int getDrawerLayoutId() {
        return R.id.rider_drawer_layout;
    }
    @Override
    int getMenuLayoutId() {
        return R.menu.rider_drawer_menu;
    }
}
