package com.example.suberduberuber;

import android.content.ComponentName;

import androidx.test.espresso.intent.Intents;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import com.example.suberduberuber.Activities.LoginActivity;
import com.example.suberduberuber.Activities.MainActivity;
import com.example.suberduberuber.Activities.RegisterActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> activityActivityTestRule
            = new ActivityTestRule<>(LoginActivity.class);

    /*
     * Test register button
     */
    @Test
    public void registerButtonTest(){
        Intents.init();
        onView(withId(R.id.register_button)).perform(click());
        intended(hasComponent(RegisterActivity.class.getName()));
    }

    /**
     * Test that registering a new rider successfully adds a new user to database
     * and redirects to dashboard
     */
    @Test
    public void registerRiderTest(){
        onView(withId(R.id.register_button)).perform(click());
        onView(withId(R.id.username_field)).perform(clearText(),typeText("loginSuccess"));
        onView(withId(R.id.email_field)).perform(clearText(),typeText("loginSuccess@mail.com"));
        onView(withId(R.id.phone_field)).perform(clearText(),typeText("780-123-4567"));
        onView(withId(R.id.password_field)).perform(clearText(),typeText("loginSuccessPassword"));
        onView(withId(R.id.password_field_confirm)).perform(clearText(),typeText("loginSuccessPassword"));

        onView(withId(R.id.register_button)).perform(click());

    }

    @Test
    public void loginSuccessTest(){
        onView(withId(R.id.email_field)).check(matches(isDisplayed()));
        onView(withId(R.id.email_field)).perform(clearText(),typeText("loginSuccess"));

    }

}
