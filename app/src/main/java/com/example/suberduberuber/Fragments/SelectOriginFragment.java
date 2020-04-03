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

Fragment class built implement specific functionality for destination not common to MapFullFragment.
Saved selected origin to current request and initializes location to users current location.
 */

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.suberduberuber.Models.CustomLocation;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.GetRideViewModel;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectOriginFragment extends MapFullFragment {

    private GetRideViewModel getRideViewModel;
    private NavController navController;
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

        textView.setHint("Pickup here?");

        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);
        navController = findNavController(view);
        tempRequest = getRideViewModel.getTempRequest().getValue();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPlace == null && initPlace != null) {
                    tempRequest.getPath().setStartLocation(new CustomLocation(initPlace));
                    saveRequest();
                    navController.navigate(R.id.action_selectOriginFragment_to_confirmRideFragment2);
                }
                else if (currentPlace != null) {
                    tempRequest.getPath().setStartLocation(new CustomLocation(currentPlace));
                    saveRequest();
                    navController.navigate(R.id.action_selectOriginFragment_to_confirmRideFragment2);
                }
                else {
                    Toast.makeText(getContext(), "No Location Chosen", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveRequest() {
        getRideViewModel.saveTempRequest(tempRequest);
    }
}
