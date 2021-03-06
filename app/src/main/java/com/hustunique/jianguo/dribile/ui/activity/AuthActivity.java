/*
 * Copyright (c) 2016.  TedaLIEz <aliezted@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hustunique.jianguo.dribile.ui.activity;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatCallback;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.hustunique.jianguo.dribile.R;
import com.hustunique.jianguo.dribile.am.MyAccountManager;
import com.hustunique.jianguo.dribile.app.AppData;
import com.hustunique.jianguo.dribile.app.PresenterManager;
import com.hustunique.jianguo.dribile.models.OAuthUser;
import com.hustunique.jianguo.dribile.presenters.AuthPresenter;
import com.hustunique.jianguo.dribile.ui.widget.OAuthWebView;
import com.hustunique.jianguo.dribile.views.AuthView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Activity for auth including a webView
 */
public class AuthActivity extends AccountAuthenticatorActivity implements AppCompatCallback, AuthView {
    @BindView(R.id.view_auth)
    OAuthWebView webView;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;


    private ProgressDialog mProgressDialog;

    private AuthPresenter mAuthPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String scope = getIntent().getStringExtra(AuthPresenter.ARG_AUTH_TYPE);
        String accountType = getIntent().getStringExtra(AuthPresenter.ARG_ACCOUNT_TYPE);
        if (savedInstanceState == null) {
            mAuthPresenter = new AuthPresenter(accountType, scope);
        } else {
            mAuthPresenter = PresenterManager.getInstance().restorePresenter(savedInstanceState);
        }
        AppCompatDelegate delegate = AppCompatDelegate.create(this, this);
        delegate.onCreate(savedInstanceState);
        delegate.setContentView(R.layout.activity_auth);
        ButterKnife.bind(this);
        delegate.setSupportActionBar(mToolbar);
        delegate.getSupportActionBar().setTitle(AppData.getString(R.string.log_in_to_dribbble));
        delegate.getSupportActionBar().setDisplayShowTitleEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        initView();
    }

    private void initView() {
        initWebView();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(AppData.getString(R.string.log_in_to_dribbble));

    }

    private void initWebView() {
        // Clear cookie to clear the login details!
        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();

        webView.allowCookies(false);

        webView.setAuthListener(new OAuthWebView.IAuth() {
            @Override
            public void onAuth(Uri uri) {
                mProgressDialog.show();
                mAuthPresenter.parseToken(uri);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAuthPresenter.bindView(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAuthPresenter.unbindView();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        PresenterManager.getInstance().savePresenter(mAuthPresenter, outState);
    }


    @Override
    public void onSupportActionModeStarted(ActionMode mode) {

    }

    @Override
    public void onSupportActionModeFinished(ActionMode mode) {

    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void onError(Throwable e) {
        mProgressDialog.dismiss();
        Intent intent = new Intent();
        intent.putExtra(AuthPresenter.ERR_AUTH_MSG, e.getMessage());
        setResult(AuthPresenter.AUTH_FAILED, intent);
        finish();
    }

    @Override
    public void onLoginFailed(int resultCode) {
        setResult(resultCode);
        finish();
    }

    @Override
    public void onSuccess(Intent intent, int resultCode) {
        mProgressDialog.dismiss();
        setAccountAuthenticatorResult(intent.getExtras());
        setResult(resultCode);
        finish();
    }

    @Override
    public void loadUrl(String url, String redirect_url, String client_id, String scope) {
        webView.loadUrl(url, redirect_url, client_id, scope);
    }

    @Override
    public void addAccount(Account account, OAuthUser user) {
        MyAccountManager.addAccount(account, user);
    }

}
