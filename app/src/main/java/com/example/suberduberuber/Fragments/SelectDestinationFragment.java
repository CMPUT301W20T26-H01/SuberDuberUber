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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView;

import com.example.suberduberuber.Models.Location;
import com.example.suberduberuber.Models.Path;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.GetRideViewModel;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

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

        paymentButton = view.findViewById(R.id.qr_button);
        scanButton = view.findViewById(R.id.scan_qr_button);
        layoutDest = view.findViewById(R.id.dest_layout);
        qrCodeText = view.findViewById(R.id.qrcodetext);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempRequest.getPath().setDestination(new Location(null, field.getText().toString(), null));
                saveRequest();
                navController.navigate(R.id.action_selectDestinationFragment_to_selectOriginFragment);
            }
        });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_selectDestinationFragment_to_qrcodeFragment);
            }
        });

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_selectDestinationFragment_to_scanqrcodeFragment);
//                layoutDest.setVisibility(View.GONE);
//                IntentIntegrator integrator = new IntentIntegrator(getActivity());
//                integrator.setPrompt("Scan a QR Code");
//                integrator.setOrientationLocked(true);
//                integrator.forSupportFragment(SelectDestinationFragment.this).initiateScan(IntentIntegrator.QR_CODE_TYPES);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                layoutDest.setVisibility(View.GONE);
            }
            else {
                layoutDest.setVisibility(View.VISIBLE);
                qrCodeText.setText(result.getContents());
            }
            layoutDest.setVisibility(View.VISIBLE);
            qrCodeText.setText(result.getContents());
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        drawerLayout = view.findViewById(R.id.drawer_layout);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph())
//                .setDrawerLayout(drawerLayout)
//                .build();
    }

    private void saveRequest() {
        getRideViewModel.saveTempRequest(tempRequest);
    }
}
