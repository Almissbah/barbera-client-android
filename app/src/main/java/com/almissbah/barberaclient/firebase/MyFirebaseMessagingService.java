package com.almissbah.barberaclient.firebase;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;


import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.data.local.SharedPrefManager;
import com.almissbah.barberaclient.model.Order;
import com.almissbah.barberaclient.model.User;
import com.almissbah.barberaclient.ui.OrderDetailActivity;
import com.almissbah.barberaclient.utils.AppConstants;
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

    private MyFirebaseMessagingService mCtx;

    private static final String TAG = "MyFirebaseMsgService";
    public static final String BARBERA_BROAD_CAST = "com.almissbah.barberaclient.newbroadcast";
    private Intent broadcastIntent = new Intent(BARBERA_BROAD_CAST);
    private Intent notificationIntent = new Intent(this, OrderDetailActivity.class);

   private MyNotificationManager mNotificationManager = new MyNotificationManager(getApplicationContext());
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        mCtx = this;
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


            JSONObject cont_obj;
            cont_obj = new JSONObject(content);
            User user = SharedPrefManager.getInstance(mCtx).getUser();
            switch (action) {
                case AppConstants.ACTION_NEW_ORDER:
                    Order order=buildOrder(cont_obj);
                    showNewOrderNotification(order,user);
                    SharedPrefManager.getInstance(mCtx).saveOrder(order);
                    sendBroadCast(order);
                    break;
                case AppConstants.ACTION_NOTIFY: {
                    notificationIntent.putExtra(AppConstants.INTENT_EXSTRA_ACTION, AppConstants.ACTION_NOTIFY);
                    mNotificationManager.showSmallNotification(getString(R.string.app_name), cont_obj.getString("notify_data"), notificationIntent);
                }
                break;

            }
        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNewOrderNotification(Order order,User user) {
        notificationIntent.putExtra(AppConstants.INTENT_EXSTRA_ACTION, AppConstants.ACTION_NEW_ORDER);
        notificationIntent.putExtra(AppConstants.INTENT_EXSTRA_ORDER, order);
        notificationIntent.putExtra(AppConstants.INTENT_EXSTRA_USER, user);

        mNotificationManager.showSmallNotification(getString(R.string.app_name), getString(R.string.new_order), notificationIntent);
    }


    private void sendBroadCast(Order order) {
        broadcastIntent.putExtra(AppConstants.INTENT_EXSTRA_ACTION, AppConstants.ACTION_NEW_ORDER);
        broadcastIntent.putExtra(AppConstants.INTENT_EXSTRA_ORDER, order);
        sendBroadcast(broadcastIntent);
    }

    private Order buildOrder(JSONObject cont_obj) throws JSONException {
        Order order = new Order();
        //parsing json data
        order.setId(cont_obj.getInt("order_id"));
        order.setAdminId(cont_obj.getInt("admin_id"));
        order.setBalanceTime(cont_obj.getInt("balance_time"));
        order.setCustomerPhone(cont_obj.getString("customer_phone"));
        order.setUserId(cont_obj.getInt("user_id"));
        order.setOrderNew(true);
        return order;
    }

}