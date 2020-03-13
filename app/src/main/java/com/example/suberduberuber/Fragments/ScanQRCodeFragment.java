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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanQRCodeFragment extends Fragment {
    private NavController navController;
    private UserRepository firestoreRepository;

    private LinearLayout scanQRLayout;
    private TextView qrCodeText;
    private Button nextButton;

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

        firestoreRepository = new UserRepository();

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        scanQRLayout = view.findViewById(R.id.scan_qr_layout);
        qrCodeText = view.findViewById(R.id.qrCodeString);

        nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_scanQRCode_to_rateDriverFragment);
            }
        });

        scanQRCode();

        // add button then onClick is rescan code

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
            else {
                scanQRLayout.setVisibility(View.VISIBLE);
                qrCodeText.setText(result.getContents());
            }
            scanQRLayout.setVisibility(View.VISIBLE);
            qrCodeText.setText(result.getContents());
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
