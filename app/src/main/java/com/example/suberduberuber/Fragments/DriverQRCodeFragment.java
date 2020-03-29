package com.example.suberduberuber.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.QRCode;
import com.example.suberduberuber.Models.User;
import com.example.suberduberuber.R;
import com.example.suberduberuber.ViewModels.DriverPaidRateViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.zxing.WriterException;

public class DriverQRCodeFragment extends Fragment {
    private NavController navController;
    private DriverPaidRateViewModel driverPaidRateViewModel;
    private ImageView qrImageView;
    private Bitmap bitmap;

    private double previousBalance;
    private double updatedBalance;

    private int setupFlag;
    private AlertDialog dialog;

    public DriverQRCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qrcode_page, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        driverPaidRateViewModel = new ViewModelProvider(requireActivity()).get(DriverPaidRateViewModel.class);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        qrImageView = view.findViewById(R.id.qrCodeImage);

        view.findViewById(R.id.balanceTextView).setVisibility(View.GONE);
        view.findViewById(R.id.addFundsButton).setVisibility(View.GONE);
        view.findViewById(R.id.sendPaymentButton).setVisibility(View.GONE);

        previousBalance = 0;
        updatedBalance = 0;
        setupFlag = -1;
        setupUser();

        // generating QR Code
        try {
            QRCode qrCode = new QRCode(this.getContext(), userID);
            bitmap = qrCode.genQRCode();
            qrImageView.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            e.printStackTrace();
        }

        driverPaidRateViewModel.getDriverLive().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                updatedBalance = user.getBalance();
                if (setupFlag != -1 && updatedBalance != previousBalance) {
                    dialog = new AlertDialog.Builder(getActivity()).create();

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View v = inflater.inflate(R.layout.dialog_driver_payment, null);
                    dialog.setView(v);

                    TextView amount = v.findViewById(R.id.payment_amount);
                    String amountStr = "$" + String.format("%.2f", updatedBalance-previousBalance);
                    amount.setText(amountStr);

                    dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface d, int which) {
                            dialog.dismiss();
                            driverPaidRateViewModel.finishRequest();
                            navController.navigate(R.id.action_driverQRCode_to_rateUser);
                        }
                    });

                    dialog.show();
                    setupFlag = -1; // ensures that it only enters this if statement once
                }
            }
        });

    }

    public void setupUser() {
        driverPaidRateViewModel.userRepoGetUser().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = task.getResult().toObject(User.class);
                previousBalance = user.getBalance();
                setupFlag = 0;
            }
        });
    }
}
