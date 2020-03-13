package com.example.suberduberuber.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.suberduberuber.Models.Location;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.GetRideViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectDestinationFragment extends Fragment {

    private NavController navController;
    private GetRideViewModel getRideViewModel;
    private Request tempRequest;

    private EditText field;
    private Button submitButton;

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
        return inflater.inflate(R.layout.fragment_select_destination, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        field = view.findViewById(R.id.destination_field);
        submitButton = view.findViewById(R.id.submit_button);

        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        tempRequest = new Request(-1, null, new Path(), "12:00 AM", "initiated");
        saveRequest();

        TextView pickupTimeTextView = view.findViewById(R.id.pickupTime);
        pickupTimeTextView.setText(tempRequest.getTime());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempRequest.getPath().setDestination(new Location(null, field.getText().toString(), null));
                saveRequest();
                navController.navigate(R.id.action_selectDestinationFragment_to_selectOriginFragment);
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
}
