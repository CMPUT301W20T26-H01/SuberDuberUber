package com.example.suberduberuber.Fragments;

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

****************************************************************************************************

Fragment to display driver details for a ride. Allows user to call or email driver by tapping on
either the phone or email field.
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Clients.UserClient;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;

public class DriverDetailsFragment extends Fragment {
    // Fragment that will display the current information in a given field (ie email) and allow user to edit
    // edit can be done on phone number and email
    private NavController navController;

    private static int id = 0;
    private User user;

    private TextView email;
    private TextView phone;
    private TextView username;
    private TextView rating;
    private Button backButton;
    private RatingBar ratingBar;



    public DriverDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_driver_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        String driverName = getArguments().getString("driverName");
        String driverEmail = getArguments().getString("driverEmail");
        String driverPhone = getArguments().getString("driverPhone");
        float driverRating = getArguments().getFloat("driverRating");
        if (getArguments().size() < 4) {
            navController.navigate(R.id.action_driverDetailsFragment_to_ridePendingFragment);
        }

        username = view.findViewById(R.id.username);
        username.setText(driverName);

        email = view.findViewById(R.id.email);
        email.setText(driverEmail);

        phone = view.findViewById(R.id.phone_number);
        phone.setText(driverPhone);

        ratingBar = view.findViewById(R.id.ratingBar);
        ratingBar.setRating(driverRating);

        backButton = view.findViewById(R.id.Return);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_driverDetailsFragment_to_ridePendingFragment);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial(driverPhone);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(driverEmail, ((UserClient)(getActivity().getApplicationContext())).getUser().getUsername());
            }
        });

    }
    private void dial(String phone) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:".concat(phone)));
            startActivity(intent);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CALL_PHONE}, 1);
        }
    }

    private void sendEmail(String email, String userName) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[] {email});
        intent.putExtra(Intent.EXTRA_SUBJECT, "SuberDuberUber: Message from ".concat(userName));
        startActivity(intent);
    }
}