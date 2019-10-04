package com.almissbha.barberaclient.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.almissbha.barberaclient.R;
import com.almissbha.barberaclient.model.Order;
import com.almissbha.barberaclient.model.User;
import com.almissbha.barberaclient.utils.MyGsonManager;

public class MainActivity extends AppCompatActivity {
    MainActivity mCtx;
    Button btn_ready, btn_busy;
    TextView tv_status_info, tv_exit, tv_logout;
    User user;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCtx = MainActivity.this;
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

        btn_ready = (Button) findViewById(R.id.btn_ready);
        btn_busy = (Button) findViewById(R.id.btn_busy);
        tv_exit = (TextView) findViewById(R.id.tv_exit);
        tv_logout = (TextView) findViewById(R.id.tv_logout);
        tv_status_info = (TextView) findViewById(R.id.tv_status_info);

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
        if (user.isReady()) {
            setReady();
        } else {
            setBusy();
        }
        tv_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCtx.finish();
            }
        });
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(mCtx)
                        .setMessage(getString(R.string.logout_notify))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Whatever...
                                new MyGsonManager(mCtx).clear();
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

        btn_ready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setReady();
                Intent i = new Intent(mCtx, OrderDetailActivity.class);
           //     i.putExtra("order", order);
                i.putExtra("user", user);
                startActivity(i);
            }
        });
        btn_busy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBusy();
            }
        });
    }

    void setReady() {
        btn_ready.setBackgroundResource(R.drawable.rounded_button_green);
        btn_busy.setBackgroundResource(R.drawable.rounded_button_blue);
        tv_status_info.setText(getString(R.string.status_ready));
    }

    void setBusy() {
        btn_busy.setBackgroundResource(R.drawable.rounded_button_green);
        btn_ready.setBackgroundResource(R.drawable.rounded_button_blue);
        tv_status_info.setText(getString(R.string.status_busy));
    }
}
