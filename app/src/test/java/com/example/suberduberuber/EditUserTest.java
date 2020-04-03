package com.example.suberduberuber;


import com.example.suberduberuber.Activities.ProfileActivity;
import com.example.suberduberuber.Models.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

public class EditUserTest {
    User user;


    @Before
    public void setup() {
        user = new User("testUsername", "test@test.ca", "123", false);
    }

    @Test
    public void testOpenEditFragment(){
        //test that edit button on profile activity opens the edit information fragment
    }

    @Test
    public void userConsistent(){
        //test that the user in fragment is consistant with current user on profile activity
    }

    @Test
    public void onItemClickTest(){
        //test that click on email or phone number open dialog with edittext

        //test that after closing dialog, email textview and phone number textview have updated
    }

    @Test
    public void confirmTest(){
        //test after confirm button click that user email and phone number have updated.
    }

    @Test
    public void cancelTest(){
        //test after cancel button click user email and phone number have not changed
    }
}
