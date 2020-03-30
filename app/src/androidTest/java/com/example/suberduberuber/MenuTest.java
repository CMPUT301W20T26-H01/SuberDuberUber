package com.example.suberduberuber;

import androidx.test.rule.ActivityTestRule;

import com.example.suberduberuber.Activities.RiderDashboardActivity;

import org.junit.Rule;

public class MenuTest {
    @Rule
    public ActivityTestRule<RiderDashboardActivity> activityActivityTestRule = new ActivityTestRule<>(RiderDashboardActivity.class);

}
