package com.example.suberduberuber.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
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

import com.example.suberduberuber.Models.QRCode;
import com.example.suberduberuber.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.WriterException;

public class DriverQRCodeFragment extends Fragment {
    private ImageView qrImageView;
    private Bitmap bitmap;

    private TextView balanceTV;
    private Button addFundsBtn;
    private Button sendFundsBtn;
    private EditText amountET;

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

        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        qrImageView = view.findViewById(R.id.qrCodeImage);

        view.findViewById(R.id.balanceTextView).setVisibility(View.GONE);
        view.findViewById(R.id.addFundsButton).setVisibility(View.GONE);
        view.findViewById(R.id.sendPaymentButton).setVisibility(View.GONE);

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
}
