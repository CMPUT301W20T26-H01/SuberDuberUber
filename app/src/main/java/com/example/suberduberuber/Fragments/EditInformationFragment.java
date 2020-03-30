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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.Car;
import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.ProfileViewModel;

import java.util.Objects;

public class EditInformationFragment extends Fragment implements View.OnClickListener{
    // Fragment that will display the current information in a given field (ie email) and allow user to edit
    // edit can be done on phone number and email
    private ProfileViewModel profileViewModel;
    private NavController navController;

    private static int id = 0;
    private User user;
    private Driver driver;
    private Car car;
    private boolean isDriver;

    private EditText newInfoField;
    private TextView emailEdit;
    private TextView phoneNumberEdit;
    private TextView yearEdit;
    private TextView makeEdit;
    private TextView modelEdit;
    private TextView colorEdit;

    private LinearLayout carLayout;

    private AlertDialog dialog;

    public EditInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        navController = Navigation.findNavController(view);

        assert getArguments() != null;

        user = (User) getArguments().getSerializable("user");
        isDriver = user.getDriver();

        TextView nameDisplay = view.findViewById(R.id.nameDisplay);

        nameDisplay.setText(user.getUsername());

        emailEdit = view.findViewById(R.id.emailEdit);
        emailEdit.setText(user.getEmail());
        emailEdit.setOnClickListener(this);

        phoneNumberEdit  = view.findViewById(R.id.phoneNumberEdit);
        phoneNumberEdit.setText(user.getPhone());
        phoneNumberEdit.setOnClickListener(this);

        yearEdit = view.findViewById(R.id.yearEdit);
        makeEdit = view.findViewById(R.id.makeEdit);
        modelEdit = view.findViewById(R.id.modelEdit);
        colorEdit = view.findViewById(R.id.colorEdit);

        carLayout = view.findViewById(R.id.car_edit);

        if(isDriver){
            driver = (Driver) user;
            car = driver.getCar();
            displayDriver(car);
            yearEdit.setOnClickListener(this);
            makeEdit.setOnClickListener(this);
            modelEdit.setOnClickListener(this);
            colorEdit.setOnClickListener(this);

        } else{
            carLayout.setVisibility(View.GONE);
        }

        Button confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);

    }

    public void createDialogs(View v) {
        dialog = new AlertDialog.Builder(getActivity()).create();
        newInfoField = new EditText(getActivity());
        dialog.setTitle("Edit User Information");
        dialog.setView(newInfoField);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (id != 0) {
                    TextView temp = v.findViewById(id);
                    temp.setText(newInfoField.getText());
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

        createDialogs(v);
        switch(v.getId()) {
            case R.id.emailEdit:
                newInfoField.setText(emailEdit.getText());
                dialog.setTitle("Edit Email Address");
                dialog.show();
                id = v.getId();
                break;
            case R.id.phoneNumberEdit:
                newInfoField.setText(phoneNumberEdit.getText().toString());
                dialog.setTitle("Edit Phone Number");
                dialog.show();
                id = v.getId();
                break;
            case R.id.yearEdit:
                newInfoField.setText(yearEdit.getText());
                dialog.setTitle("Edit Car Year");
                dialog.show();
                id = v.getId();
                break;
            case R.id.makeEdit:
                newInfoField.setText(makeEdit.getText());
                dialog.setTitle("Edit Car Make");
                dialog.show();
                id = v.getId();
                break;
            case R.id.modelEdit:
                newInfoField.setText(modelEdit.getText());
                dialog.setTitle("Edit Car Model");
                dialog.show();
                id = v.getId();
                break;
            case R.id.colorEdit:
                newInfoField.setText(colorEdit.getText());
                dialog.setTitle("Edit Car Colour");
                dialog.show();
                id = v.getId();
                break;
            case R.id.confirmButton:
                user.setPhone(PhoneNumberUtils.formatNumber(phoneNumberEdit.getText().toString()));
                user.setEmail(emailEdit.getText().toString());
                if(isDriver){
                    car.setYear(Integer.parseInt(yearEdit.getText().toString()));
                    car.setMake(makeEdit.getText().toString());
                    car.setModel(modelEdit.getText().toString());
                    car.setColor(colorEdit.getText().toString());
                }
                profileViewModel.updateCurrentUser(user);
                navController.navigate(R.id.action_editInformationFragment_to_viewProfileFragment);
                break;
            case R.id.cancelButton:
                navController.navigate(R.id.action_editInformationFragment_to_viewProfileFragment);
                break;
        }
    }

    private void displayDriver(Car car){
//        yearEdit.setVisibility(View.VISIBLE);
//        makeEdit.setVisibility(View.VISIBLE);
//        modelEdit.setVisibility(View.VISIBLE);
//        colorEdit.setVisibility(View.VISIBLE);

        yearEdit.setText(Integer.toString(car.getYear()));
        makeEdit.setText(car.getMake());
        modelEdit.setText(car.getModel());
        colorEdit.setText(car.getColor());

    }


}
