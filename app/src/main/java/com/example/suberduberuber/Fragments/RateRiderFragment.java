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
 */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.RatingRepository;
import com.example.suberduberuber.ViewModels.DriverPaidRateViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

/**
 * Driver fragment to rate the rider at the end of a trip. Takes in the rating and updates the
 * rider's rating score through the viewmodel.
 */

public class RateRiderFragment extends Fragment {
    private NavController navController;
    private RatingRepository ratingRepository;

    private DriverPaidRateViewModel driverPaidRateViewModel;
    private String riderID;

    private Button submitButton;
    private TextView riderName;

    private RatingBar rating;

    public RateRiderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate_rider, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        ratingRepository = new RatingRepository();

        driverPaidRateViewModel = new ViewModelProvider(requireActivity()).get(DriverPaidRateViewModel.class);

        submitButton = view.findViewById(R.id.submit_button);

        rating = view.findViewById(R.id.rating);

        riderName = view.findViewById(R.id.rider_name_str);

        String riderStr = "Please rate " + driverPaidRateViewModel.getRider().getUsername();
        riderName.setText(riderStr);

        ratingRepository.findId(driverPaidRateViewModel.getRider().getEmail()).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                riderID = task.getResult().getDocuments().get(0).getId();
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating_num = (double) rating.getRating();

                driverPaidRateViewModel.updateUserRating(riderID, rating_num);
                driverPaidRateViewModel.updateUserNumOfRatings(riderID);
                navController.navigate(R.id.action_rateDriverFragment_to_viewRequestFragment);
            }
        });
    }
}
