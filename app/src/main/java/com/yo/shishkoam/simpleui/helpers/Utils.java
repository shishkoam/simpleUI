package com.yo.shishkoam.simpleui.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 08.04.2017
 */

public class Utils {
    // convert from bitmap to byte array
    public static byte[] getBytes(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        Bitmap.createScaledBitmap(bitmap, 160, 160, true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Drawable getImage(byte[] image) {
        return new BitmapDrawable(BitmapFactory.decodeByteArray(image, 0, image.length));
    }

    public static String formatDate(long time) {
        return new SimpleDateFormat("dd-MM-yyyy").format(time);
    }

    public static String formatDate(Date time) {
        return new SimpleDateFormat("dd-MM-yyyy").format(time);
    }
    public static boolean validatePhoneNumber(String phoneNo) {
        return phoneNo.matches("\\d{12}");
    }

    public static boolean isPasswordValid(String password) {
        return password.length() > 4;
    }
}
