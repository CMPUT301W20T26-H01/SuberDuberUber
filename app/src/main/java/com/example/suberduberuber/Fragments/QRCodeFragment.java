package com.example.suberduberuber.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.Models.Driver;
import com.example.suberduberuber.Models.QRCode;
import com.example.suberduberuber.Models.Transaction;
import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.UserRepository;
import com.example.suberduberuber.ViewModels.PaymentViewModel;
import com.example.suberduberuber.ViewModels.QRCodeViewModel;
import com.example.suberduberuber.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

/*
    Fragment that generates a QR code based on the user's email.

    Eventually will remove the on click button and have the QR
    code generate automatically and may change the email string
    to a user id.
 */

public class QRCodeFragment extends Fragment {
    private NavController navController;
    private UserRepository userRepository;
    private PaymentViewModel paymentViewModel;

    private ImageView qrImageView;
    private Bitmap bitmap;

    private LinearLayout linearLayout;
    private TextView balanceTV;
    private Button addFundsBtn;
    private Button sendFundsBtn;
    private EditText amountET;
    private EditText amountToSend;

    private AlertDialog dialog;
    private AlertDialog confirmDialog;
    private AlertDialog pickAmount;
    private AlertDialog transactionConfirmDialog;
    private int id;

    private QRCodeViewModel qrCodeViewModel;
    private double balance;

    private String scannedUid;
    private User scannedUser;
    private String userID;
    private User currentUser;
    private double amount;

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
        userRepository = new UserRepository();
        paymentViewModel = new ViewModelProvider(requireActivity()).get(PaymentViewModel.class);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        paymentViewModel.findCurrentUser(userID); // just the current user for a transaction

        amount = 0;

        linearLayout = view.findViewById(R.id.qrCodeFragmentLayout);
        qrImageView = view.findViewById(R.id.qrCodeImage);

        balanceTV = view.findViewById(R.id.balanceTextView);
        addFundsBtn = view.findViewById(R.id.addFundsButton);
        sendFundsBtn = view.findViewById(R.id.sendFundsButton);

        addFundsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFunds();
            }
        });

        sendFundsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFunds();
            }
        });

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
        balanceTV.setText("Balance: ".concat(String.format("%.2f", balance)));
    }

    public void addFunds() {
        dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.setTitle("Enter amount of QRBucks to add");
        amountET = new EditText(getActivity());
        amountET.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        dialog.setView(amountET);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                double amount = Double.parseDouble(amountET.getText().toString().trim());
                confirmDialog = new AlertDialog.Builder(getActivity()).create();
                confirmDialog.setTitle("This will cost you \uD83E\uDDFB".concat(String.format("%.2f", amount / 10)).concat(" toilet paper rolls."));
                confirmDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        qrCodeViewModel.setBalance(balance + amount);
                    }
                });
                confirmDialog.show();
            }
        });
        dialog.show();
    }

    public void sendFunds() {
        pickAmount = new AlertDialog.Builder(getActivity()).create();
        pickAmount.setTitle("Enter amount of QRBucks to send");
        amountToSend = new EditText(getActivity());
        amountToSend.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        pickAmount.setView(amountToSend);
        pickAmount.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                amount = Double.parseDouble(amountToSend.getText().toString().trim());
                scanQR();
            }
        });
        pickAmount.show();
    }

    public void scanQR() {
        linearLayout.setVisibility(View.GONE);
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setPrompt("Scan a QR Code");
        integrator.setOrientationLocked(true);
        integrator.forSupportFragment(QRCodeFragment.this).initiateScan(IntentIntegrator.QR_CODE_TYPES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                linearLayout.setVisibility(View.GONE);
            }
            linearLayout.setVisibility(View.VISIBLE);
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
                    if (scannedUser == null || !scannedUser.getEmail().equals(user.getEmail())) {
                        scannedUser = user;
                        popupTransactionDialog();
                    }
                }
            });
        }
    }

    public void popupTransactionDialog() {
        transactionConfirmDialog = new AlertDialog.Builder(getActivity()).create();

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.dialog_send_funds, null);
        TextView title = v.findViewById(R.id.sendFundsTitle);
        TextView context = v.findViewById(R.id.sendFundsContext);

        if (scannedUser == null) {
            title.setText("Invalid QR Code Scanned");
            context.setText("Please try to send funds again.");
            transactionConfirmDialog.setView(v);
            transactionConfirmDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }
        else {
            title.setText("Confirm Payment");
            String msg = "Send $" + String.format("%.2f", amount) + " to " + scannedUser.getUsername() + "?";
            context.setText(msg);
            transactionConfirmDialog.setView(v);
            transactionConfirmDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    paymentViewModel.sendFundsCompleteTransaction(paymentViewModel.getCurrentUser(), scannedUser, scannedUid, amount);
                    transactionConfirmDialog.dismiss();
                }
            });
            transactionConfirmDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    transactionConfirmDialog.dismiss();
                }
            });
        }
        transactionConfirmDialog.show();
    }
}
