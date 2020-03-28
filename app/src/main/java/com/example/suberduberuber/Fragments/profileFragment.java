package com.example.suberduberuber.Fragments;

import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.Car;
import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.ProfileViewModel;

public class profileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private NavController navController;
    private TextView usernamePro;
    private TextView year;
    private TextView make;
    private TextView model;
    private TextView color;
    private TextView plateNumber;
    private TextView emailPro;
    private TextView ratingPro;
    private TextView phoneNumberPro;
    private Button editButton;

    private User user = profileViewModel.getCurrentUser().getValue();
    private boolean isDriver = user.getDriver();
    public profileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        navController = Navigation.findNavController(view);

        usernamePro = view.findViewById(R.id.usernameProfile);
        emailPro = view.findViewById(R.id.emailProfile);
        ratingPro = view.findViewById(R.id.ratingProfile);
        phoneNumberPro = view.findViewById(R.id.phoneNumberProfile);
        editButton = view.findViewById(R.id.editInfoProfile);
        year = view.findViewById(R.id.carYear);
        make = view.findViewById(R.id.carMake);
        model = view.findViewById(R.id.carModel);
        color = view.findViewById(R.id.carColor);
        plateNumber = view.findViewById(R.id.carPlate);

        profileViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                displayUserDetails(user);
            }

        });editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserDetails();
            }
        });
    }

    private void displayUserDetails(User user) {
        if (isDriver){
            Driver driver = (Driver) user;
            Car car = driver.getCar();
            year.setVisibility(View.VISIBLE);
            make.setVisibility(View.VISIBLE);
            model.setVisibility(View.VISIBLE);
            color.setVisibility(View.VISIBLE);
            plateNumber.setVisibility(View.VISIBLE);

            year.setText(car.getYear());
            make.setText(car.getMake());
            model.setText(car.getModel());
            color.setText(car.getColor());
            plateNumber.setText(car.getLicensePlate());

        } else {year.setVisibility(View.GONE);
            make.setVisibility(View.GONE);
            model.setVisibility(View.GONE);
            color.setVisibility(View.GONE);
            plateNumber.setVisibility(View.GONE);}

        usernamePro.setText(user.getUsername());
        emailPro.setText(user.getEmail());
        phoneNumberPro.setText(PhoneNumberUtils.formatNumber(user.getPhone(), "CA"));
        ratingPro.setText(String.valueOf(user.getRating()));
    }
    private void editUserDetails(){
        //User user = profileViewModel.getCurrentUser().getValue();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        navController.navigate(R.id.action_viewProfileFragment_to_editInformationFragment,bundle);
    }

}
