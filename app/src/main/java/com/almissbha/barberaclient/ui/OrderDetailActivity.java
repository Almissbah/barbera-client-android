package com.almissbha.barberaclient.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.almissbha.barberaclient.R;
import com.almissbha.barberaclient.firebase.MyFirebaseMessagingService;
import com.almissbha.barberaclient.data.remote.VollyAcceptOrder;
import com.almissbha.barberaclient.model.Order;
import com.almissbha.barberaclient.model.User;
import com.almissbha.barberaclient.utils.MyGsonManager;

public class OrderDetailActivity extends AppCompatActivity {
    public User user;
    public Order order;
    public OrderDetailActivity mCtx;
    Button btn_accept;
    CardView cv_orders;
    TextView tv_waiting;
    TextView tv_costumer_phone, tv_balance_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        mCtx = OrderDetailActivity.this;
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            // Set up the user interaction to manually show or hide the system UI.
            Window window = mCtx.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(ContextCompat.getColor(mCtx, R.color.colorPrimaryDark));
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_accept = (Button) findViewById(R.id.btn_accept);
        cv_orders = (CardView) findViewById(R.id.cv_orders);
        tv_waiting = (TextView) findViewById(R.id.tv_waiting);
        tv_costumer_phone = (TextView) findViewById(R.id.tv_costumer_phone);
        tv_balance_time = (TextView) findViewById(R.id.tv_balance_time);
        if (getIntent().hasExtra("user")) {
            user = (User) getIntent().getSerializableExtra("user");
        } else {
            user = new MyGsonManager(mCtx).getUserObjectClass();
        }
        if (getIntent().hasExtra("order")) {
            order = (Order) getIntent().getSerializableExtra("order");
        } else {
            order = new MyGsonManager(mCtx).getOrderObjectClass();
        }
if(order.isOrderNew()){
        tv_costumer_phone.setText(order.getCustomerPhone());
        tv_balance_time.setText(String.valueOf(order.getBalanceTime()));
         cv_orders.setVisibility(View.VISIBLE);
        tv_waiting.setVisibility(View.GONE);}
        else{
    cv_orders.setVisibility(View.GONE);
    tv_waiting.setVisibility(View.VISIBLE);
}
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new VollyAcceptOrder(mCtx);
            }
        });
        registerReceiver(br, new IntentFilter(MyFirebaseMessagingService.BarberaBroadCast));
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(br);
        super.onDestroy();
    }

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                String action = intent.getStringExtra("action");

                switch (action){
                    case "new_order":
                        cv_orders.setVisibility(View.VISIBLE);
                        tv_waiting.setVisibility(View.GONE);
                        order =(Order) intent.getSerializableExtra("order");
                        tv_costumer_phone.setText(order.getCustomerPhone());
                        tv_balance_time.setText(String.valueOf(order.getBalanceTime()));

                        break;


                }}

        }
    };
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
