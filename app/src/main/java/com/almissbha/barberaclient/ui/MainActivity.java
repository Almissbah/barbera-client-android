package com.almissbha.barberaclient.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.almissbha.barberaclient.R;
import com.almissbha.barberaclient.data.local.SharedPrefManager;
import com.almissbha.barberaclient.model.Order;
import com.almissbha.barberaclient.model.User;

public class MainActivity extends BaseActivity {
    MainActivity mCtx;
    Button btnReady, btnBusy;
    TextView tvStatusInfo, tvExit, tvLogout;
    User user;
    Order order;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCtx = MainActivity.this;

        sharedPrefManager = SharedPrefManager.getInstance(mCtx);
        btnReady = (Button) findViewById(R.id.btn_ready);
        btnBusy = (Button) findViewById(R.id.btn_busy);
        tvExit = (TextView) findViewById(R.id.tv_exit);
        tvLogout = (TextView) findViewById(R.id.tv_logout);
        tvStatusInfo = (TextView) findViewById(R.id.tv_status_info);

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
        if (user.isReady()) {
            setReady();
        } else {
            setBusy();
        }
        tvExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCtx.finish();
            }
        });
        tvLogout.setOnClickListener(new View.OnClickListener() {
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
        });

        btnReady.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReady();
                Intent i = new Intent(mCtx, OrderDetailActivity.class);
                //     i.putExtra("order", order);
                i.putExtra("user", user);
                startActivity(i);
            }
        });
        btnBusy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBusy();
            }
        });
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
