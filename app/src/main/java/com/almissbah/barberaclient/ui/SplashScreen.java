package com.almissbah.barberaclient.ui;

import android.content.Intent;
import android.os.Bundle;

import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.data.local.SharedPrefManager;
import com.almissbah.barberaclient.model.User;
import com.almissbah.barberaclient.utils.AppConstants;

public class SplashScreen extends BaseActivity {
    private SplashScreen mCtx;
    private User mUser;
    private int waitTimeOut=2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mCtx = SplashScreen.this;
        mUser = SharedPrefManager.getInstance(mCtx).getUser();

        startActivityThread();
    }

    void startActivityThread() {
        Thread waiting = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(waitTimeOut);
                    mCtx.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = null;
                            if (mUser != null && mUser.isLogged()) {
                                i = new Intent(mCtx, MainActivity.class);
                                i.putExtra(AppConstants.INTENT_EXSTRA_USER, mUser);
                            } else {
                                i = new Intent(mCtx, LoginActivity.class);
                            }
                            if (i != null && mCtx != null) {
                                mCtx.startActivity(i);
                                mCtx.finish();
                            }
                        }
                    });
                } catch (InterruptedException e) {

                }
            }
        });
        waiting.start();
    }
}
