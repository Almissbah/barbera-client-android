package com.almissbha.barberaclient.data.remote;

import android.app.ProgressDialog;
import android.content.Intent;

import com.almissbha.barberaclient.R;
import com.almissbha.barberaclient.model.User;
import com.almissbha.barberaclient.ui.LoginActivity;
import com.almissbha.barberaclient.ui.MainActivity;
import com.almissbha.barberaclient.utils.MyGsonManager;
import com.almissbha.barberaclient.utils.MyUtilities;
import com.almissbha.barberaclient.utils.SharedPrefManager;
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

public class VollyLoginUser {
    LoginActivity mCtx;
    String username,password,token;
    ProgressDialog progress;
    public VollyLoginUser(LoginActivity mCtx, String...data) {
        username= data[0];
        password= data[1];
        this.mCtx=mCtx;

        progress= new ProgressDialog(mCtx);
        progress.setMessage(mCtx.getString(R.string.please_wait));
        progress.setCancelable(false);
        progress.show();

        token= SharedPrefManager.getInstance(mCtx).getDeviceToken();
        if(token==null){token="no token";}
        String query="?username="+ MyUtilities.getEncodedString(username)+
                "&password="+ MyUtilities.getEncodedString(password)+
                "&token="+ MyUtilities.getEncodedString(token) ;
        HttpRequest(ServerAPIs.getLogin_url()+query);

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
                            User user =new User();
                        if( json.getString("success").equals("1")){

                            user.setId(json.getInt("user_id"));
                            user.setLogged(true);
                            user.setUserName(json.getString("user_name"));
                            user.setToken(token);
                            Intent i=new Intent(mCtx, MainActivity.class);
                            new MyGsonManager(mCtx).saveUserObjectClass(user);
                            i.putExtra("user", user);
                            mCtx.startActivity(i);
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
                Intent i=new Intent(mCtx, MainActivity.class);
                mCtx.startActivity(i);
                mCtx.finish();

            }
        });
// Add the request to the RequestQueue.
        queue.add(stringRequest);
}
}
