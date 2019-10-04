package com.almissbah.barberaclient.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import com.almissbah.barberaclient.utils.AppConstants;
import com.almissbah.barberaclient.utils.MyUtilities;

public class OrderDetailActivity extends BaseActivity {
    private User user;
    private Order mOrder;
    private OrderDetailActivity mCtx;
    private Button btnAccept;
    private CardView cvOrders;
    private TextView tvWaiting;
    private TextView tvCostumerPhone, tvBalanceTime;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        mCtx = OrderDetailActivity.this;
        sharedPrefManager = SharedPrefManager.getInstance(mCtx);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initUI();
        getIntentData();
        checkOrderStatus();

       registerReceiver(br, new IntentFilter(MyFirebaseMessagingService.BARBERA_BROAD_CAST));
    }

    View.OnClickListener acceptClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new VolleyAcceptOrder(mCtx, user, mOrder, new VolleyAcceptOrder.CallBack() {
                @Override
                public void onSuccess() {
                    mCtx.mOrder.setAccepted(true);
                    mCtx.mOrder.setOrderNew(false);
                    SharedPrefManager.getInstance(mCtx).saveOrder(mCtx.mOrder);
                    MyUtilities.showCustomToast(mCtx, mCtx.getString(R.string.msg_request_accepted));
                    mCtx.finish();
                }

                @Override
                public void onFail(String msg) {
                    MyUtilities.showErrorDialog(mCtx, msg);

                }
            });
        }
    };

    private void checkOrderStatus() {
        if (mOrder.isOrderNew()) {
            tvCostumerPhone.setText(mOrder.getCustomerPhone());
            tvBalanceTime.setText(String.valueOf(mOrder.getBalanceTime()));
            cvOrders.setVisibility(View.VISIBLE);
            tvWaiting.setVisibility(View.GONE);
        } else {
            cvOrders.setVisibility(View.GONE);
            tvWaiting.setVisibility(View.VISIBLE);
        }
    }

    private void getIntentData() {
        if (getIntent().hasExtra(AppConstants.INTENT_EXSTRA_USER)) {
            user = (User) getIntent().getSerializableExtra(AppConstants.INTENT_EXSTRA_USER);
        } else {
            user = sharedPrefManager.getUser();
        }
        if (getIntent().hasExtra(AppConstants.INTENT_EXSTRA_ORDER)) {
            mOrder = (Order) getIntent().getSerializableExtra(AppConstants.INTENT_EXSTRA_ORDER);
        } else {
            mOrder = sharedPrefManager.getOrder();
        }
    }

    private void initUI() {
        btnAccept = findViewById(R.id.btn_accept);
        cvOrders = findViewById(R.id.cv_orders);
        tvWaiting = findViewById(R.id.tv_waiting);
        tvCostumerPhone = findViewById(R.id.tv_costumer_phone);
        tvBalanceTime = findViewById(R.id.tv_balance_time);
        btnAccept.setOnClickListener(acceptClickListener);
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
                String action = intent.getStringExtra(AppConstants.INTENT_EXSTRA_ACTION);

                switch (action) {
                    case AppConstants.ACTION_NEW_ORDER:
                        cvOrders.setVisibility(View.VISIBLE);
                        tvWaiting.setVisibility(View.GONE);
                        mOrder = (Order) intent.getSerializableExtra(AppConstants.INTENT_EXSTRA_ORDER);
                        tvCostumerPhone.setText(mOrder.getCustomerPhone());
                        tvBalanceTime.setText(String.valueOf(mOrder.getBalanceTime()));

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
