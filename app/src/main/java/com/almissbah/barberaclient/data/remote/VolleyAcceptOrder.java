package com.almissbah.barberaclient.data.remote;

import android.app.ProgressDialog;
import android.content.Context;


import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.data.local.SharedPrefManager;
import com.almissbah.barberaclient.model.Order;
import com.almissbah.barberaclient.model.User;
import com.almissbah.barberaclient.ui.OrderDetailActivity;
import com.almissbah.barberaclient.utils.MyUtilities;
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

public class VolleyAcceptOrder {
    Context mCtx;
    ProgressDialog mProgress;
    User mUser;
    CallBack mCallBack;

    public VolleyAcceptOrder(Context context, User user, Order order, CallBack callBack) {
        this.mCtx = context;
        this.mCallBack = callBack;
        this.mUser = user;

        mProgress = new ProgressDialog(mCtx);
        mProgress.setMessage(mCtx.getString(R.string.please_wait));
        mProgress.setCancelable(false);
        mProgress.show();

        String query = "?user_id=" + user.getId() +
                "&order_id=" + order.getId() +
                "&admin_id=" + order.getAdminId();
        HttpRequest(ServerAPIs.getAccept_order_url() + query);

    }

    private void HttpRequest(String url) {
        RequestQueue queue = Volley.newRequestQueue(mCtx);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mProgress.dismiss();
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response);
                            if (json.getString("success").equals("1")) {
                                mCallBack.onSuccess();

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
                mProgress.dismiss();
                mCallBack.onFail(mCtx.getString(R.string.networkErr));
            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public interface CallBack {
        void onSuccess();

        void onFail(String msg);
    }
}
