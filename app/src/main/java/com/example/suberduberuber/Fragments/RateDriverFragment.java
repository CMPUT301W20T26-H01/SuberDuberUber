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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.PaymentViewModel;
import com.example.suberduberuber.ViewModels.RatingViewModel;

import java.util.Observable;

/**
 * A simple {@link Fragment} subclass.
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
