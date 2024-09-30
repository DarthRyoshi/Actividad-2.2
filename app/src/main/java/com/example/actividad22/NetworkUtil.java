package com.example.actividad22;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class NetworkUtil {

    public static  Bitmap fetchImage(String url){
        Bitmap bitmapImage = null;
        try{
            InputStream inputStream = new URL(url).openStream();
            bitmapImage = BitmapFactory.decodeStream(inputStream);
        } catch (Exception e){
            e.printStackTrace();
        }
        return bitmapImage;
    }
}
