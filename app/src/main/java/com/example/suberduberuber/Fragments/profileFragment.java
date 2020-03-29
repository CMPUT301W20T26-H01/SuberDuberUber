package com.example.suberduberuber.Fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.ProfileViewModel;

public class profileFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private NavController navController;
    private TextView usernamePro;
    private TextView emailPro;
    private TextView ratingPro;
    private TextView phoneNumberPro;
    private Button editButton;


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

        profileViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                displayUserDetails(user);
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserDetails();
            }
        });

        emailPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(emailPro.getText().toString(), usernamePro.getText().toString());
            }
        });

        phoneNumberPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dial(phoneNumberPro.getText().toString());
            }
        });
    }

    private void displayUserDetails(User user) {
        usernamePro.setText(user.getUsername());
        emailPro.setText(user.getEmail());
        phoneNumberPro.setText(PhoneNumberUtils.formatNumber(user.getPhone(), "CA"));
        ratingPro.setText(String.valueOf(user.getRating()));
    }
    private void editUserDetails(){
        User user = profileViewModel.getCurrentUser().getValue();
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        navController.navigate(R.id.action_viewProfileFragment_to_editInformationFragment,bundle);
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
