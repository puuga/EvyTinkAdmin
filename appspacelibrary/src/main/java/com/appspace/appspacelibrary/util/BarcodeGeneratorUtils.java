package com.appspace.appspacelibrary.util;

import android.graphics.Bitmap;

import com.appspace.appspacelibrary.util.barcode.BarcodeGenerator;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;

/**
 * Created by siwaweswongcharoen on 1/28/2016 AD.
 */
public class BarcodeGeneratorUtils {
    public static Bitmap generate(String sBarcode) {
        Bitmap bBitmap = null;
        try {
            bBitmap = BarcodeGenerator
                    .getInstance().encodeAsBitmap(sBarcode, BarcodeFormat.CODE_128, 520, 328);
        } catch (WriterException ignored) {
        }
        return bBitmap;
    }
}
