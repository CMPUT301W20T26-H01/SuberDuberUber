package com.example.suberduberuber.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
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
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.QRCode;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.ViewModels.QRCodeViewModel;
import com.example.suberduberuber.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/*
    Fragment that generates a QR code based on the user's email.

    Eventually will remove the on click button and have the QR
    code generate automatically and may change the email string
    to a user id.
 */

public class QRCodeFragment extends Fragment implements View.OnClickListener {
    private NavController navController;

    private ImageView qrImageView;
    private Bitmap bitmap;

    private TextView balanceTV;
    private Button addFundsBtn;
    private Button sendFundsBtn;
    private EditText amountET;

    private AlertDialog dialog;
    private AlertDialog confirmDialog;
    private int id;

    private QRCodeViewModel qrCodeViewModel;
    private double balance;


    public QRCodeFragment() {
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
        qrCodeViewModel = ViewModelProviders.of(this).get(QRCodeViewModel.class);
        navController = Navigation.findNavController(view);

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        qrImageView = view.findViewById(R.id.qrCodeImage);

        balanceTV = view.findViewById(R.id.balanceTextView);
        addFundsBtn = view.findViewById(R.id.addFundsButton);
        addFundsBtn.setOnClickListener(this);
        sendFundsBtn = view.findViewById(R.id.sendPaymentButton);
        sendFundsBtn.setOnClickListener(this);

        qrCodeViewModel.getCurrentUser().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                displayBalance(user);
            }
        });

        // generating QR Code
        try {
            QRCode qrCode = new QRCode(this.getContext(), userID);
            bitmap = qrCode.genQRCode();
            qrImageView.setImageBitmap(bitmap);
        }
        catch (WriterException e) {
            e.printStackTrace();
        }

    }

    private void displayBalance(User user) {
        balance = user.getBalance();
        balanceTV.setText("Balance: ".concat(Double.toString(balance)));
    }

    @Override
    public void onClick(View v) {
        dialog = new AlertDialog.Builder(getActivity()).create();
        amountET = new EditText(getActivity());
        amountET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.setView(amountET);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (id == R.id.addFundsButton) {
                    double amount = Double.parseDouble(amountET.getText().toString().trim());
                    confirmDialog = new AlertDialog.Builder(getActivity()).create();
                    confirmDialog.setTitle("This will cost you ".concat(Double.toString(amount / 10)).concat(" toilet paper rolls."));
                    confirmDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            qrCodeViewModel.setBalance(balance + amount);
                        }
                    });
                    confirmDialog.show();
                }
            }
        });

        switch (v.getId()) {
            case R.id.addFundsButton:
                dialog.setTitle("Enter amount of QRBucks to add");
                id = v.getId();
                dialog.show();
                break;
            case R.id.sendPaymentButton:
                dialog.setTitle("Enter amount of QRBucks to send");
                id = v.getId();
                dialog.show();
                break;
        }

    }
}
