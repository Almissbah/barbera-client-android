package com.almissbha.barberaclient.ui;

import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.almissbha.barberaclient.R;
import com.almissbha.barberaclient.data.remote.VollyLoginUser;
import com.almissbha.barberaclient.utils.MyUtilities;


public class LoginActivity extends AppCompatActivity {
    Button btn_login;
    EditText edt_username,edt_password;
    LoginActivity mCtx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCtx=LoginActivity.this;
        if (Build.VERSION.SDK_INT >= 21) {
            // Set up the user interaction to manually show or hide the system UI.
            Window window = mCtx.getWindow();

// clear FLAG_TRANSLUCENT_STATUS flag:
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
            window.setStatusBarColor(ContextCompat.getColor(mCtx, R.color.colorPrimaryDark));
        }


        btn_login=(Button) findViewById(R.id.btn_login);
        edt_username=(EditText) findViewById(R.id.edt_username);;
        edt_password=(EditText) findViewById(R.id.edt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=edt_username.getText().toString();
                String password=edt_password.getText().toString();
                if(username.equals("")||password.equals("")){
                    MyUtilities.showCustomToast(mCtx,"Please do not leave an empty field !");}
                else new VollyLoginUser(mCtx,username,password);
            }
        });
    }
}
