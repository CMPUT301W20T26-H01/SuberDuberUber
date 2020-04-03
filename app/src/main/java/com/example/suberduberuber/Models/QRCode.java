package com.example.suberduberuber.Models;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.suberduberuber.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Class to create a qrCode bitmap given the activity context and string of the qrCode.
 *
 * Reference: https://www.android-examples.com/generate-qr-code-in-android-using-zxing-library-in-android-studio/
 */

public class QRCode {
    private String id;
    private Context context;

    public QRCode(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    public Bitmap genQRCode() throws WriterException {
        int qrWidth = 500;
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
                        context.getResources().getColor(R.color.black): context.getResources().getColor(R.color.colorPrimaryLight);
            }
        }
        Bitmap bp = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bp.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bp;
    }
}
