package com.almissbah.barberaclient.data.remote;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.model.User;
import com.almissbah.barberaclient.ui.LoginActivity;
import com.almissbah.barberaclient.ui.MainActivity;
import com.almissbah.barberaclient.utils.MyUtilities;
import com.almissbah.barberaclient.data.local.SharedPrefManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by mohamed on 12/4/2017.
 */

public class VolleyLoginUser {
    private Context mCtx;
    private ProgressDialog mProgress;
    private CallBack mCallBack;

    public VolleyLoginUser(Context mCtx, String username, String password, String token, CallBack callBack) {
        this.mCallBack = callBack;
        this.mCtx = mCtx;

        showProgressDialog();
        if (token == null) {
            token = "no token";
        }
        String query = "?username=" + MyUtilities.getEncodedString(username) +
                "&password=" + MyUtilities.getEncodedString(password) +
                "&token=" + MyUtilities.getEncodedString(token);
        HttpRequest(ServerAPIs.getLogin_url() + query);

    }


    void showProgressDialog() {
        mProgress = new ProgressDialog(mCtx);
        mProgress.setMessage(mCtx.getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.show();
    }

    void hideProgressDialog() {
        if (mProgress != null) mProgress.dismiss();
    }

    private void HttpRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(mCtx);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressDialog();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            User user = new User();
                            if (json.getString("success").equals("1")) {

                                user.setId(json.getInt("user_id"));
                                user.setLogged(true);
                                user.setUserName(json.getString("user_name"));

                                mCallBack.onSuccess(user);


                            } else {
                                mCallBack.onFail(json.getString("message"));
                            }
                        } catch (JSONException e) {
                            mCallBack.onFail(mCtx.getString(R.string.networkErr));
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressDialog();
                mCallBack.onFail(mCtx.getString(R.string.networkErr));


            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public interface CallBack {
        void onSuccess(User user);

        void onFail(String msg);
    }
}
