package com.example.suberduberuber.Fragments;

import android.content.Intent;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.suberduberuber.R;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/**
 * A simple {@link Fragment} subclass.
 */
public class RidePendingFragment extends Fragment {

    private NavController navController;

    private Button submitButton;

    private LinearLayout layout;
    private TextView qrCodeText;

    public RidePendingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ride_pending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        submitButton = view.findViewById(R.id.submit_button);

        layout = view.findViewById(R.id.dest_layout);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_ridePendingFragment_to_scanqrcodeFragment);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                layout.setVisibility(View.GONE);
            }
            else {
                layout.setVisibility(View.VISIBLE);
                qrCodeText.setText(result.getContents());
            }
            layout.setVisibility(View.VISIBLE);
            qrCodeText.setText(result.getContents());
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
