package com.almissbah.barberaclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.almissbah.barberaclient.R;
import com.almissbah.barberaclient.data.local.SharedPrefManager;
import com.almissbah.barberaclient.data.remote.VolleyLoginUser;
import com.almissbah.barberaclient.model.User;
import com.almissbah.barberaclient.utils.AppConstants;
import com.almissbah.barberaclient.utils.MyUtilities;


public class LoginActivity extends BaseActivity {
    Button btnLogin;
    EditText edtUsername, edtPassword;
    LoginActivity mCtx;
    User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mCtx = LoginActivity.this;

        btnLogin = findViewById(R.id.btn_login);
        edtUsername = findViewById(R.id.edt_username);
        ;
        edtPassword = findViewById(R.id.edt_password);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = edtUsername.getText().toString();
                String password = edtPassword.getText().toString();

                final String token = SharedPrefManager.getInstance(mCtx).getDeviceToken();

                if (validateInput(username, password)) {
                    new VolleyLoginUser(mCtx, username, password, token, new VolleyLoginUser.CallBack() {
                        @Override
                        public void onSuccess(User user) {
                            user.setToken(token);
                            mUser = user;
                            SharedPrefManager.getInstance(mCtx).saveUser(mUser);
                            startMainActivity();
                        }

                        @Override
                        public void onFail(String msg) {
                            MyUtilities.showErrorDialog(mCtx, msg);
                        }
                    });
                }
            }
        });
    }


    boolean validateInput(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            MyUtilities.showCustomToast(mCtx, mCtx.getString(R.string.msg_fill_all_fields));
            return false;
        } else {
            return true;
        }
    }

    private void startMainActivity() {
        Intent i = new Intent(mCtx, MainActivity.class);
        i.putExtra(AppConstants.INTENT_EXSTRA_USER, mUser);
        mCtx.startActivity(i);
        mCtx.finish();
    }
}
