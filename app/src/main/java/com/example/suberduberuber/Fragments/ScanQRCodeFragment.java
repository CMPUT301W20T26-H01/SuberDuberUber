package com.example.suberduberuber.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.Request;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.GetRideViewModel;
import com.example.suberduberuber.ViewModels.PaymentViewModel;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/*
    Fragment for scanning a QR code. Launches a zxing activity to the camera and
    auto detection of a QR code. Once a QR code is scanned, returns to this fragment
    to display the QR code string and next button to rate my driver fragment.

    Reference: https://www.android-examples.com/generate-qr-code-in-android-using-zxing-library-in-android-studio/
 */

public class ScanQRCodeFragment extends Fragment {
    private NavController navController;
    private PaymentViewModel paymentViewModel;
    private GetRideViewModel getRideViewModel;

    private LinearLayout scanQRLayout;
    private TextView amountText;
    private TextView qrCodeId;
    private Button nextButton;
    private Button rescanQRButton;

    private String scannedUid;

    private Request currentRequest;

    public ScanQRCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scan_qrcode, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        paymentViewModel = new ViewModelProvider(requireActivity()).get(PaymentViewModel.class);
        getRideViewModel = new ViewModelProvider(requireActivity()).get(GetRideViewModel.class);

        currentRequest = getRideViewModel.getCurrentRequest();
        double amount = currentRequest.getPrice();

        scanQRLayout = view.findViewById(R.id.scan_qr_layout);
        amountText = view.findViewById(R.id.amount);
        qrCodeId = view.findViewById(R.id.scannedId);

        String amountStr = "$" + String.format("%.2f", amount) + " to";
        amountText.setText(amountStr);

        nextButton = view.findViewById(R.id.next_button);
        rescanQRButton = view.findViewById(R.id.rescan_button);

        qrCodeId.setText("The QR Code does not match with the driver. Please rescan the driver's QR Code.");
        amountText.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentViewModel.getCurrentDriver() != null) {
                    double amount = currentRequest.getPrice();

                    paymentViewModel.completeTransaction(amount);

                    navController.navigate(R.id.action_scanQRCode_to_rateDriverFragment);
                }
            }
        });

        rescanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanQRCode();
            }
        });

        scanQRCode();

    }

    public void scanQRCode() {
        scanQRLayout.setVisibility(View.GONE);
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setPrompt("Scan a QR Code");
        integrator.setOrientationLocked(true);
        integrator.forSupportFragment(ScanQRCodeFragment.this).initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                scanQRLayout.setVisibility(View.GONE);
            }
            scanQRLayout.setVisibility(View.VISIBLE);
            scannedUid = result.getContents();
            findUser();
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void findUser() {
        if (scannedUid != null) {
            paymentViewModel.getDriverByUID(scannedUid).observe(getViewLifecycleOwner(), new Observer<Driver>() {
                @Override
                public void onChanged(Driver user) {
                    if (user == null || !user.getEmail().equals(currentRequest.getDriver().getEmail())) {
                        qrCodeId.setText("The QR Code does not match with the driver. Please rescan the driver's QR Code.");
                        amountText.setVisibility(View.GONE);
                        nextButton.setVisibility(View.GONE);
                    }
                    else {
                        qrCodeId.setText(user.getUsername());
                        paymentViewModel.setDriver(user); // sets the user being paid
                        paymentViewModel.setCurrentDriverUID(scannedUid); // sets the user being paid UID
                        amountText.setVisibility(View.VISIBLE);
                        nextButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

}
