package com.hustunique.jianguo.driclient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.hustunique.jianguo.driclient.R;
import com.hustunique.jianguo.driclient.app.UserManager;
import com.hustunique.jianguo.driclient.bean.AccessToken;
import com.hustunique.jianguo.driclient.bean.OAuthUser;
import com.hustunique.jianguo.driclient.bean.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Login Activity for application
 */
public class LoginActivity extends BaseActivity {
    private static final int LOGIN = 0x00000000;

    @Bind(R.id.btn_login)
    Button login;

    //TODO: Login UI
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_login)
    void login() {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivityForResult(intent, LOGIN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case LOGIN:
                if (resultCode == AuthActivity.AUTH_DENIED) {
                    Log.e("driclient", getTag() + " user deny the authentications");
                    return;
                }
                if (resultCode == AuthActivity.AUTH_FAILED) {
                    String msg = data.getStringExtra(AuthActivity.ERR_AUTH_MSG);
                    Log.e("driclient", getTag() + " login failed " + msg);
                    return;
                }
                if (resultCode == RESULT_CANCELED) {
                    return;
                }
                User authUser = (User) data.getSerializableExtra(AuthActivity.AUTH_USER);
                AccessToken token = (AccessToken) data.getSerializableExtra(AuthActivity.TOKEN);
                OAuthUser user = new OAuthUser();
                user.setAccessToken(token);
                user.setUser(authUser);
                Log.i("driclient", getTag() + " get user " + authUser.getJson() + "successfully");
                UserManager.setCurrentUser(user);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }



}
