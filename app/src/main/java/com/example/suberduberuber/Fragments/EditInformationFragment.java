package com.example.suberduberuber.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.suberduberuber.ViewModels.ProfileViewModel;

import java.util.Objects;

public class EditInformationFragment extends Fragment implements View.OnClickListener{
    // Fragment that will display the current information in a given field (ie email) and allow user to edit
    // edit can be done on phone number and email
    private ProfileViewModel profileViewModel;
    private NavController navController;

    private static int id = 0;
    private User user;

    private EditText newInfoField;
    private TextView emailEdit;
    private TextView phoneNumberEdit;

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

        TextView nameDisplay = view.findViewById(R.id.nameDisplay);

        nameDisplay.setText(user.getUsername());

        emailEdit = view.findViewById(R.id.emailEdit);
        emailEdit.setText(user.getEmail());
        emailEdit.setOnClickListener(this);

        phoneNumberEdit  = view.findViewById(R.id.phoneNumberEdit);
        phoneNumberEdit.setText(user.getPhone());
        phoneNumberEdit.setOnClickListener(this);

        Button confirmButton = view.findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(this);

        Button cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
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
        switch(v.getId()) {
            case R.id.emailEdit:
                newInfoField.setText(emailEdit.getText());
                dialog.show();
                id = v.getId();
                break;
            case R.id.phoneNumberEdit:
                newInfoField.setText(phoneNumberEdit.getText());
                dialog.show();
                id = v.getId();
                break;
            case R.id.confirmButton:
                user.setPhone(phoneNumberEdit.getText().toString());
                user.setEmail(emailEdit.getText().toString());
                profileViewModel.updateCurrentUser(user);
                navController.navigate(R.id.action_editInformationFragment_to_viewProfileFragment);
                break;
            case R.id.cancelButton:
                navController.navigate(R.id.action_editInformationFragment_to_viewProfileFragment);
                break;
        }
    }


}
