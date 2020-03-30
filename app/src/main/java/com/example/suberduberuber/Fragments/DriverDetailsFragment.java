package com.example.suberduberuber.Fragments;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.ViewModels.ProfileViewModel;

import java.util.Objects;

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
        double driverRating = getArguments().getInt("driverRating");
        if (getArguments().size() < 4) {
            navController.navigate(R.id.action_driverDetailsFragment_to_ridePendingFragment);
        }

        username = view.findViewById(R.id.username);
        username.setText(driverName);

        email = view.findViewById(R.id.email);
        email.setText(driverEmail);

        phone = view.findViewById(R.id.phone_number);
        phone.setText(driverPhone);

        rating = view.findViewById(R.id.rating);
        rating.setText(String.valueOf(driverRating));

        backButton = view.findViewById(R.id.Return);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_driverDetailsFragment_to_ridePendingFragment);
            }
        });

    }
}