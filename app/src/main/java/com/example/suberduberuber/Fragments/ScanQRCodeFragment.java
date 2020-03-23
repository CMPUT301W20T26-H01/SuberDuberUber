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
import com.example.suberduberuber.Models.Rider;
import com.example.suberduberuber.Models.Transaction;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.TransactionRepository;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.ViewModels.PaymentViewModel;
import com.example.suberduberuber.ViewModels.ProfileViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Date;

/*
    Fragment for scanning a QR code. Launches a zxing activity to the camera and
    auto detection of a QR code. Once a QR code is scanned, returns to this fragment
    to display the QR code string and next button to rate my driver fragment.
 */
public class ScanQRCodeFragment extends Fragment {
    private NavController navController;
    private TransactionRepository transactionRepository;

    private LinearLayout scanQRLayout;
    private TextView qrCodeId;
    private Button nextButton;

    private PaymentViewModel viewModel;
    private String scannedUid;

    private Rider currentRider;
    private Driver driverPaid;

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

        transactionRepository = new TransactionRepository();

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        viewModel = new ViewModelProvider(requireActivity()).get(PaymentViewModel.class);

        scanQRLayout = view.findViewById(R.id.scan_qr_layout);
        qrCodeId = view.findViewById(R.id.scannedId);

        nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (driverPaid != null && currentRider != null) {

                    double amount = 5; // TODO: change hard coded value to actual amount
                    double newPayingBalance = currentRider.getBalance() - amount;
                    double newPaidBalance = driverPaid.getBalance() + amount;
                    transactionRepository.processTransaction(scannedUid, newPayingBalance, newPaidBalance);
                    Transaction transaction = new Transaction(currentRider, driverPaid, amount, new Date());
                    transactionRepository.saveTransaction(transaction);

                    navController.navigate(R.id.action_scanQRCode_to_rateDriverFragment);
                }
            }
        });

        findCurrentUser();
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
        viewModel.getDriver(scannedUid).observe(getViewLifecycleOwner(), new Observer<Driver>() {
            @Override
            public void onChanged(Driver user) {
                if (user != null) {
                    qrCodeId.setText(user.getEmail());
                    driverPaid = user;
                }
            }
        });
    }

    public void findCurrentUser() {
        viewModel.getCurrentRider().observe(getViewLifecycleOwner(), new Observer<Rider>() {
            @Override
            public void onChanged(Rider user) {
                currentRider = user;
            }
        });
    }

}
