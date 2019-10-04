package com.almissbah.barberaclient.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.data.remote.VolleyLoginUser;
import com.almissbah.barberaclient.utils.MyUtilities;


public class LoginActivity extends BaseActivity {
    Button btn_login;
    EditText edt_username,edt_password;
    LoginActivity mCtx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCtx=LoginActivity.this;

        btn_login= findViewById(R.id.btn_login);
        edt_username= findViewById(R.id.edt_username);;
        edt_password= findViewById(R.id.edt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username=edt_username.getText().toString();
                String password=edt_password.getText().toString();
                if(username.equals("")||password.equals("")){
                    MyUtilities.showCustomToast(mCtx,"Please do not leave an empty field !");}
                else new VolleyLoginUser(mCtx,username,password);
            }
        });
    }
}
