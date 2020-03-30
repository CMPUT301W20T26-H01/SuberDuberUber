package com.example.suberduberuber;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;

import com.example.suberduberuber.Activities.RiderDashboardActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class RiderDashboardFragmentTest {
// Cannot test map visibility with espresso
    @Rule
    public ActivityTestRule<RiderDashboardActivity> activityActivityTestRule = new ActivityTestRule<>(RiderDashboardActivity.class);

    /**
     * Runs before all tests and initializes intents
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        Intents.init();
    }

    /**
     * test the dashboard has visible map
     */
    @Test
    public void mapVisibilityTest(){

    }
    /**
     * test that entering a location and hitting enter redirects to pick up location
     */
    @Test
    public void whereToTest(){


    }
    /**
     * test that entering current location and hitting okay redirects to confirmation screen
     */
    @Test
    public void pickUpLocationTest(){

    }
    /**
     * test that confirming ride creates a request in db and displays waiting for driver page
     *
     */
    @Test
    public void confirmRideTest(){

    }
    /**
     * test that open request updates home page to open current request
     */
    @Test
    public void homeIsRequestTest(){

    }
    /**
     * test toolbar click opens menu
     */
    @Test
    public void toolBarTest(){

    }
}
