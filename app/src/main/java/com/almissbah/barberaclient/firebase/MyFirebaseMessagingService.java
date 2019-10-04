package com.almissbah.barberaclient.firebase;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;


import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.model.Order;
import com.almissbah.barberaclient.model.User;
import com.almissbah.barberaclient.ui.OrderDetailActivity;
import com.almissbah.barberaclient.utils.Log;
import com.almissbah.barberaclient.utils.MyNotificationManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mohammed.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    MyFirebaseMessagingService mCtx;

    private static final String TAG = "MyFirebaseMsgService";
    public static final String BarberaBroadCast = "com.almissbah.barberaclient.newbroadcast";
    Intent bi = new Intent(BarberaBroadCast);

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        mCtx=this;
     //   Log.e(TAG, "new message :" + remoteMessage.getData().toString());
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    //this method will display the notification
    //We are passing the JSONObject that is received from
    //firebase cloud messaging
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void sendPushNotification(JSONObject json) {
        //optionally we can display the json into log
        Log.e(TAG, "Notification JSON " + json.toString());
        try {
            //getting the json data
            JSONObject data = json.getJSONObject("data");

            //parsing json data
            String title = data.getString("title");
            String action = data.getString("action");
            String content = data.getString("content");
            MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
//            RideStatusObject status=rideInfo.getRide_Status();
            JSONObject cont_obj;
            Intent intent=new Intent(this, OrderDetailActivity.class);
            cont_obj = new JSONObject(content);
            User user =new MyGsonManager(mCtx).getUserObjectClass();
            switch (action) {
                case "new_order":
                        Order order =new Order();
                        //parsing json data
                        order.setId(cont_obj.getInt("order_id"));
                        order.setAdminId(cont_obj.getInt("admin_id"));
                        order.setBalanceTime(cont_obj.getInt("balance_time"));
                        order.setCustomerPhone(cont_obj.getString("customer_phone"));
                        order.setUserId(cont_obj.getInt("user_id"));
                        order.setOrderNew(true);
                        intent.putExtra("action", "new_order");
                        intent.putExtra("order", order);
                        intent.putExtra("user", user);
                        mNotificationManager.showSmallNotification(getString(R.string.app_name), getString(R.string.new_order), intent);
                        new MyGsonManager(mCtx).saveOrderObjectClass(order);

                    bi.putExtra("action", "new_order");
                    bi.putExtra("order", order);
                    sendBroadcast(bi);


                    break;
                case "notify":
                {   intent.putExtra("action", "notify");
                    mNotificationManager.showSmallNotification(getString(R.string.app_name), cont_obj.getString("notify_data"), intent);
                 }
                break;

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

}