package com.example.suberduberuber.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.example.suberduberuber.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RateDriverFragment extends Fragment {

    private NavController navController;

    private EditText field;
    private Button submitButton;

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
        field = view.findViewById(R.id.destination_field);
        submitButton = view.findViewById(R.id.submit_button);

        rating = view.findViewById(R.id.rating);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double rating_num = (double) rating.getRating();
                navController.navigate(R.id.action_rateDriverFragment_to_selectDestinationFragment);
            }
        });
    }
}
