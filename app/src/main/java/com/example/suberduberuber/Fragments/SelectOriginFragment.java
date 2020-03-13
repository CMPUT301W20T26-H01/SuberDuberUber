package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.GetRideViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectOriginFragment extends MapFullFragment {

    private GetRideViewModel getRideViewModel;
    private Request tempRequest;

    public SelectOriginFragment() {
        // Required empty public constructor
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

        textView.setHint("Search Pickup Location");

        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        tempRequest = getRideViewModel.getTempRequest().getValue();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempRequest.getPath().setStartLocation(new CustomLocation(null, currentPlace.getName(), currentPlace.getAddress()));
                saveRequest();
                navController.navigate(R.id.action_selectOriginFragment_to_confirmRideFragment2);
            }
        });

        getRideViewModel.getTempRequest().observe(getViewLifecycleOwner(), new Observer<Request>() {
            @Override
            public void onChanged(Request request) {
                //field.setHint("testing onChanged method of observer model");
            }
        });
    }

    private void saveRequest() {
        getRideViewModel.saveTempRequest(tempRequest);
    }
}
