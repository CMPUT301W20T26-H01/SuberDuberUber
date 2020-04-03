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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.PaymentViewModel;
import com.example.suberduberuber.ViewModels.RatingViewModel;

/**
 * Rider fragment to rate the driver at the end of a trip. Takes in the rating and updates the driver's
 * score through the viewmodel.
 */

public class RateDriverFragment extends Fragment {

    private NavController navController;
    private PaymentViewModel paymentViewModel;
    private RatingViewModel ratingViewModel;

    private Button submitButton;
    private TextView driverName;

    private RatingBar rating;

    public RateDriverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rate_driver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        paymentViewModel = new ViewModelProvider(requireActivity()).get(PaymentViewModel.class);
        ratingViewModel = new ViewModelProvider(requireActivity()).get(RatingViewModel.class);

        submitButton = view.findViewById(R.id.submit_button);

        rating = view.findViewById(R.id.rating);
        driverName = view.findViewById(R.id.driver_name_str);

        String driverStr = "Please rate " + paymentViewModel.getCurrentDriver().getUsername();

        driverName.setText(driverStr);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating_num = (double) rating.getRating();

                ratingViewModel.updateDriverRating(paymentViewModel.getCurrentDriverUID(), paymentViewModel.getCurrentDriver(), rating_num);
                navController.navigate(R.id.action_rateDriverFragment_to_selectDestinationFragment);
            }
        });
    }
}
