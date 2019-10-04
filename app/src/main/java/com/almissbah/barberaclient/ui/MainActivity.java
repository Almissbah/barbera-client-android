package com.almissbah.barberaclient.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.data.local.SharedPrefManager;
import com.almissbah.barberaclient.model.Order;
import com.almissbah.barberaclient.model.User;
import com.almissbah.barberaclient.utils.AppConstants;

public class MainActivity extends BaseActivity {
    private MainActivity mCtx;
    private Button btnReady, btnBusy;
    private TextView tvStatusInfo, tvExit, tvLogout;
    private User mUser;
    private Order mOrder;
    private SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCtx = MainActivity.this;

        sharedPrefManager = SharedPrefManager.getInstance(mCtx);

        initUI();
        getIntentData();
        checStatus();
        setListeners();

    }

    private void setListeners() {
        tvExit.setOnClickListener(exitClickListener);
        tvLogout.setOnClickListener(logoutClickListener);
        btnReady.setOnClickListener(readyClickListener);
        btnBusy.setOnClickListener(busyClickListener);
    }


    View.OnClickListener exitClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mCtx.finish();
        }
    };
    View.OnClickListener busyClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setBusy();
        }
    };
    View.OnClickListener readyClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            setReady();
            Intent i = new Intent(mCtx, OrderDetailActivity.class);
            //     i.putExtra("mOrder", mOrder);
            i.putExtra(AppConstants.INTENT_EXSTRA_USER, mUser);
            startActivity(i);
        }
    };
    View.OnClickListener logoutClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            new AlertDialog.Builder(mCtx)
                    .setMessage(getString(R.string.logout_notify))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Whatever...
                            sharedPrefManager.clear();
                            Intent i = new Intent(mCtx, LoginActivity.class);
                            startActivity(i);
                            mCtx.finish();
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();

        }
    };

    private void getIntentData() {
        if (getIntent().hasExtra(AppConstants.INTENT_EXSTRA_USER)) {
            mUser = (User) getIntent().getSerializableExtra(AppConstants.INTENT_EXSTRA_USER);
        } else {
            mUser = sharedPrefManager.getUser();
        }
        if (getIntent().hasExtra(AppConstants.INTENT_EXSTRA_ORDER)) {
            mOrder = (Order) getIntent().getSerializableExtra(AppConstants.INTENT_EXSTRA_ORDER);
        } else {
            mOrder = sharedPrefManager.getOrder();
        }
    }

    void checStatus() {
        if (mUser.isReady()) {
            setReady();
        } else {
            setBusy();
        }
    }

    void initUI() {
        btnReady = findViewById(R.id.btn_ready);
        btnBusy = findViewById(R.id.btn_busy);
        tvExit = findViewById(R.id.tv_exit);
        tvLogout = findViewById(R.id.tv_logout);
        tvStatusInfo = findViewById(R.id.tv_status_info);

    }

    void setReady() {
        btnReady.setBackgroundResource(R.drawable.rounded_button_green);
        btnBusy.setBackgroundResource(R.drawable.rounded_button_blue);
        tvStatusInfo.setText(getString(R.string.status_ready));
    }

    void setBusy() {
        btnBusy.setBackgroundResource(R.drawable.rounded_button_green);
        btnReady.setBackgroundResource(R.drawable.rounded_button_blue);
        tvStatusInfo.setText(getString(R.string.status_busy));
    }
}
