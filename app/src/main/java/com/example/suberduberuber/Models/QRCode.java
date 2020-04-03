package com.example.suberduberuber.Models;

/*
Copyright [2020] [SuberDuberUber]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

import android.content.Context;
import android.graphics.Bitmap;

import com.example.suberduberuber.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

public class QRCode {
    private String id;
    private Context context;
    private Bitmap qrCode;


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
