package com.almissbah.barberaclient.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.data.local.SharedPrefManager;
import com.almissbah.barberaclient.firebase.MyFirebaseMessagingService;
import com.almissbah.barberaclient.data.remote.VolleyAcceptOrder;
import com.almissbah.barberaclient.model.Order;
import com.almissbah.barberaclient.model.User;
import com.almissbah.barberaclient.utils.MyUtilities;

public class OrderDetailActivity extends AppCompatActivity {
    public User user;
    public Order order;
    public OrderDetailActivity mCtx;
    Button btn_accept;
    CardView cvOrders;
    TextView tvWaiting;
    TextView tvCostumerPhone, tvBalanceTime;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        mCtx = OrderDetailActivity.this;
        sharedPrefManager = SharedPrefManager.getInstance(mCtx);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btn_accept = findViewById(R.id.btn_accept);
        cvOrders = findViewById(R.id.cv_orders);
        tvWaiting = findViewById(R.id.tv_waiting);
        tvCostumerPhone = findViewById(R.id.tv_costumer_phone);
        tvBalanceTime = findViewById(R.id.tv_balance_time);

        if (getIntent().hasExtra("user")) {
            user = (User) getIntent().getSerializableExtra("user");
        } else {
            user = sharedPrefManager.getUser();
        }
        if (getIntent().hasExtra("order")) {
            order = (Order) getIntent().getSerializableExtra("order");
        } else {
            order = sharedPrefManager.getOrder();
        }

        if (order.isOrderNew()) {
            tvCostumerPhone.setText(order.getCustomerPhone());
            tvBalanceTime.setText(String.valueOf(order.getBalanceTime()));
            cvOrders.setVisibility(View.VISIBLE);
            tvWaiting.setVisibility(View.GONE);
        } else {
            cvOrders.setVisibility(View.GONE);
            tvWaiting.setVisibility(View.VISIBLE);
        }

        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new VolleyAcceptOrder(mCtx, user, order, new VolleyAcceptOrder.CallBack() {
                    @Override
                    public void onSuccess() {
                        mCtx.order.setAccepted(true);
                        mCtx.order.setOrderNew(false);
                        SharedPrefManager.getInstance(mCtx).saveOrder(mCtx.order);
                        MyUtilities.showCustomToast(mCtx, "Accepted successfully !");
                        mCtx.finish();
                    }

                    @Override
                    public void onFail(String msg) {
                        MyUtilities.showErrorDialog(mCtx, msg);

                    }
                });
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

                switch (action) {
                    case "new_order":
                        cvOrders.setVisibility(View.VISIBLE);
                        tvWaiting.setVisibility(View.GONE);
                        order = (Order) intent.getSerializableExtra("order");
                        tvCostumerPhone.setText(order.getCustomerPhone());
                        tvBalanceTime.setText(String.valueOf(order.getBalanceTime()));

                        break;


                }
            }

        }
    };

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
