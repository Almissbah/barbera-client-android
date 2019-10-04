package com.almissbah.barberaclient.ui;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.model.User;

public class SplashScreen extends AppCompatActivity {
    SplashScreen   mCtx;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        mCtx=SplashScreen.this;
        user = new MyGsonManager(mCtx).getUserObjectClass();
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

        Thread waiting= new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    mCtx.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent i=null;
                            if(user !=null&& user.isLogged()){
                                i=new Intent(mCtx,MainActivity.class);
                                i.putExtra("user", user);
                            }else {
                                i=new Intent(mCtx,LoginActivity.class);
                            }
                            if(i!=null&&mCtx!=null) {
                                mCtx.startActivity(i);
                                mCtx.finish();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        waiting.start();
    }
}
