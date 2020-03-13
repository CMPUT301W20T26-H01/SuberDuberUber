package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.GetRideViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmRideFragment extends Fragment {

    private NavController navController;
    private GetRideViewModel getRideViewModel;
    private Request tempRequest;

    private Button submitButton;

    public ConfirmRideFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_confirm_ride, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        tempRequest = getRideViewModel.getTempRequest().getValue();

        TextView pickupTextView = view.findViewById(R.id.pickupLocation);
        pickupTextView.setText(tempRequest.getPath().getStartLocation().getLocationName());
        TextView destinationTextView = view.findViewById(R.id.destination);
        destinationTextView.setText(tempRequest.getPath().getDestination().getLocationName());
        TextView pickupTimeTextView = view.findViewById(R.id.pickupTime);
        pickupTimeTextView.setText(tempRequest.getTime());

        submitButton = view.findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Submit ride to database
                saveRequest();
                getRideViewModel.commitTempRequest();
                navController.navigate(R.id.action_confirmRideFragment2_to_ridePendingFragment);
            }
        });
    }

    private void saveRequest() {
        getRideViewModel.saveTempRequest(tempRequest);
    }
}
