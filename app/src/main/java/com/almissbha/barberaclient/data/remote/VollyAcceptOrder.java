package com.almissbha.barberaclient.data.remote;

import android.app.ProgressDialog;


import com.almissbha.barberaclient.R;
import com.almissbha.barberaclient.model.User;
import com.almissbha.barberaclient.ui.OrderDetailActivity;
import com.almissbha.barberaclient.utils.MyGsonManager;
import com.almissbha.barberaclient.utils.MyUtilities;
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

public class VollyAcceptOrder {
    OrderDetailActivity mCtx;
    String costumerPhone,balanceTime;
    ProgressDialog progress;
    User user;
    public VollyAcceptOrder(OrderDetailActivity mCtx, String...data) {
        this.mCtx=mCtx;
        user =mCtx.user;
        progress= new ProgressDialog(mCtx);
        progress.setMessage(mCtx.getString(R.string.please_wait));
        progress.setCancelable(false);
        progress.show();

        String query="?user_id="+ user.getId()+
                "&order_id="+  mCtx.order.getId()+
                "&admin_id="+  mCtx.order.getAdminId();
        HttpRequest(ServerAPIs.getAccept_order_url()+query);

    }

     private void HttpRequest(String url){
        RequestQueue queue = Volley.newRequestQueue(mCtx);

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progress.dismiss();
                        JSONObject json= null;
                        try {
                            json = new JSONObject(response);
                        if( json.getString("success").equals("1")){
                            mCtx.order.setAccepted(true);
                            mCtx.order.setOrderNew(false);
                            new MyGsonManager(mCtx).saveOrderObjectClass( mCtx.order);
                            MyUtilities.showCustomToast(mCtx,"Accepted successfully !");
                            mCtx.finish();

                        }else
                        {
                            MyUtilities.showErrorDialog(mCtx,json.getString("message"));
                        }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();
                MyUtilities.showCustomToast(mCtx,mCtx.getString(R.string.networkErr));

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
}
}
