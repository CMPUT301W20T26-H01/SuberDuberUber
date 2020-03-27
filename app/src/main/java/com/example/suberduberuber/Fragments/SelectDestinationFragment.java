package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.ui.AppBarConfiguration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.AuthViewModel;
import com.example.suberduberuber.ViewModels.GetRideViewModel;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectDestinationFragment extends MapFullFragment {

    private GetRideViewModel getRideViewModel;
    private AuthViewModel authViewModel;
    private Request tempRequest;

    private DrawerLayout drawerLayout;

    private AppBarConfiguration appBarConfiguration;

    public SelectDestinationFragment() {
        // Required empty public constructor
    }

    /**
     * Get the viewmodel provider factory to dish out the viewmodel, creating a new instance only
     * if it is the first time that the activity starts
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView.setHint("Where to?");
        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlace != null) {
                    createTempRequest();
                }
                else {
                    Toast.makeText(getContext(), "No Location Chosen", Toast.LENGTH_SHORT).show();
                }
            }
        });

        drawerLayout = view.findViewById(R.id.drawer_layout);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
//                .setDrawerLayout(drawerLayout)
//                .build();
    }

    private void saveRequest() {
        getRideViewModel.saveTempRequest(tempRequest);
    }

    private void createTempRequest() {

        authViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                tempRequest = new Request((Rider) user, new Path(), new Date());
                tempRequest.getPath().setDestination(new CustomLocation(currentPlace));
                saveRequest();
                navController.navigate(R.id.action_selectDestinationFragment_to_selectOriginFragment);
            }
        });
    }
}
