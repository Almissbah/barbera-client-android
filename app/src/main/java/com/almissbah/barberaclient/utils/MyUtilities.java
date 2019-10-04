package com.almissbah.barberaclient.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.almissbah.barberaclient.R;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by mohamed on 9/2/2017.
 */

public class MyUtilities {

Activity activity;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void forceRTLIfSupported()
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            activity.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        }
    }


    public static String getEncodedString(String a){

        try {
            return URLEncoder.encode(a, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return a;
    }

    public static void showCustomToast(Context context, String message){
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.custom_toast_layout, null);
        TextView tv_toast_meassage= view.findViewById(R.id.tv_toast_message);
        tv_toast_meassage.setText(message);
        toast.setView(view);
        toast.show();


    }





    public static void showErrorDialog(Context mCtx, String msg){

        new AlertDialog.Builder(mCtx)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mCtx.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...

                    }
                }).show();
    }

    public static void showDialog(Context mCtx, String msg){

        new AlertDialog.Builder(mCtx)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(mCtx.getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Whatever...

                    }
                }).show();
    }

}


