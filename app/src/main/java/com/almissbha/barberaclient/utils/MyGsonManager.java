package com.almissbha.barberaclient.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.almissbha.barberaclient.model.Order;
import com.almissbha.barberaclient.model.User;
import com.google.gson.Gson;

/**
 * Created by mohamed on 11/2/2017.
 */

public class MyGsonManager {
    SharedPreferences mPrefs ;
    Context mCtx;
    public static String SHARED_PREF_NAME="MyGsonManager";
    public static String TAG_USER_INFO="USER_INFO";
    public static String TAG_REQUEST="REQUEST";
    public MyGsonManager(Context mCtx) {
        this.mCtx = mCtx;
        mPrefs = mCtx.getSharedPreferences(SHARED_PREF_NAME,mCtx.MODE_PRIVATE);
    }

    public void saveUserObjectClass(User object){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString(TAG_USER_INFO, json);
        prefsEditor.commit();
    }

    public User getUserObjectClass(){
        Gson gson = new Gson();
        String json = mPrefs.getString(TAG_USER_INFO, "");
        User obj = gson.fromJson(json, User.class);
        if(obj==null) obj=new User();
        return obj;
    }
    public void saveOrderObjectClass(Order object){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString(TAG_REQUEST, json);
        prefsEditor.commit();
    }

    public Order getOrderObjectClass(){
        Gson gson = new Gson();
        String json = mPrefs.getString(TAG_REQUEST, "");
        Order obj = gson.fromJson(json, Order.class);
        if(obj==null) obj=new Order();
        return obj;
    }

    public void clear(){
        SharedPreferences.Editor editor = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

}
