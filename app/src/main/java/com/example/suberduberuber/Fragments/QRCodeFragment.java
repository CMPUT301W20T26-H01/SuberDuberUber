package com.example.suberduberuber.Fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.suberduberuber.R;
import com.example.suberduberuber.Repositories.UserRepository;
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

public class QRCodeFragment extends Fragment {
    private NavController navController;
    private UserRepository firestoreRepository;

    private Button genQRCodeButton;
    private ImageView qrImageView;
    private Bitmap bitmap;
    public final static int qrWidth = 1000;


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
        navController = Navigation.findNavController(view);

        firestoreRepository = new UserRepository();

        String userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        genQRCodeButton = view.findViewById(R.id.qrGen_button);
        qrImageView = view.findViewById(R.id.qrCodeImage);
        genQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = userEmail;
                try {
                    bitmap = generateQRCode(id);
                    qrImageView.setImageBitmap(bitmap);
                }
                catch (WriterException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Bitmap generateQRCode(String id) throws WriterException {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(id, BarcodeFormat.DATA_MATRIX.QR_CODE, qrWidth, qrWidth, null);
        }
        catch (IllegalArgumentException e){
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int [] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {
                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.black): getResources().getColor(R.color.white);
            }
        }
        Bitmap bp = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bp.setPixels(pixels, 0, 1000, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bp;
    }
}
