package com.almissbha.barberaclient.data.local;

/*
  Created by mohamed on 2/25/2017.
 */

import android.content.Context;
import android.content.SharedPreferences;

import com.almissbha.barberaclient.model.Order;
import com.almissbha.barberaclient.model.User;
import com.google.gson.Gson;

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "APP_PREFS";
    private static final String TAG_TOKEN = "TAG_TOKEN";
    private final static String TAG_USER_INFO="USER_INFO";
    private final static String TAG_REQUEST="REQUEST";
    private SharedPreferences mPrefs;
    private static SharedPrefManager mInstance;

    private SharedPrefManager(Context context) {
        mPrefs = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    //this method will save the device token to shared preferences
    public void saveDeviceToken(String token){
        SharedPreferences sharedPreferences =mPrefs;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TAG_TOKEN, token);
        editor.apply();
    }

    //this method will fetch the device token from shared preferences
    public String getDeviceToken(){
        SharedPreferences sharedPreferences = mPrefs;
        return  sharedPreferences.getString(TAG_TOKEN, null);
    }


    public void saveUser(User object){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString(TAG_USER_INFO, json);
        prefsEditor.apply();
    }

    public User getUser(){
        Gson gson = new Gson();
        String json = mPrefs.getString(TAG_USER_INFO, "");
        User obj = gson.fromJson(json, User.class);
        if(obj==null) obj=new User();
        return obj;
    }
    public void saveOrder(Order object){
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object);
        prefsEditor.putString(TAG_REQUEST, json);
        prefsEditor.apply();
    }

    public Order getOrder(){
        Gson gson = new Gson();
        String json = mPrefs.getString(TAG_REQUEST, "");
        Order obj = gson.fromJson(json, Order.class);
        if(obj==null) obj=new Order();
        return obj;

    }

    public void clear(){
        SharedPreferences.Editor editor = mPrefs.edit();
        editor.clear();
        editor.apply();
    }

}