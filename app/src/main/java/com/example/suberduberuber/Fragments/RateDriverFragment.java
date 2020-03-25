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
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.PaymentViewModel;
import com.example.suberduberuber.ViewModels.RatingViewModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateDriverFragment extends Fragment {

    private NavController navController;
    private PaymentViewModel paymentViewModel;
    private RatingViewModel ratingViewModel;

    private Button submitButton;
    private TextView driverName;
    private TextView ratingNumber;

    private RatingBar rating;
    private Driver currentDriver;

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

        submitButton = view.findViewById(R.id.submit_button);

        rating = view.findViewById(R.id.rating);
        driverName = view.findViewById(R.id.driver_name);
        ratingNumber = view.findViewById(R.id.rating_number);

        driverName.setText(paymentViewModel.getDriver().getValue().getUsername());

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating_num = (double) rating.getRating();
                ratingNumber.setText(String.valueOf(rating_num));

                ratingViewModel.updateDriverRating(paymentViewModel.getCurrentDriverUID(), paymentViewModel.getDriver().getValue(), rating_num);
                //navController.navigate(R.id.action_rateDriverFragment_to_selectDestinationFragment);
            }
        });
    }
}
