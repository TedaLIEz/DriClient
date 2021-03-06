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

package com.hustunique.jianguo.dribile.am;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.hustunique.jianguo.dribile.presenters.AuthPresenter;
import com.hustunique.jianguo.dribile.ui.activity.AuthActivity;

/**
 * Created by JianGuo on 4/1/16.
 * AccountAuthenticator for dribbble oauth
 */
public class DribbbleAuthenticator extends AbstractAccountAuthenticator {
    private String TAG = DribbbleAuthenticator.class.getSimpleName();
    private Context mContext;

    public DribbbleAuthenticator(Context context) {
        super(context);
        mContext = context;
    }


    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType, String authTokenType, String[] requiredFeatures, Bundle options) throws NetworkErrorException {
        Intent intent = new Intent(mContext, AuthActivity.class);
        intent.putExtra(AuthPresenter.ARG_ACCOUNT_TYPE, accountType);
        intent.putExtra(AuthPresenter.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AuthPresenter.ARG_IS_ADDING_NEW_ACCOUNT, true);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);
        return bundle;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        AccountManager am = AccountManager.get(mContext);
        String authToken = am.peekAuthToken(account, authTokenType);
        if (!TextUtils.isEmpty(authToken)) {
            Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);
            return result;
        }

        //If there is no token, then you should re-auth the account
        Intent intent = new Intent(mContext, AuthActivity.class);
        intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
        intent.putExtra(AuthPresenter.ARG_AUTH_TYPE, authTokenType);
        intent.putExtra(AuthPresenter.ARG_ACCOUNT_TYPE, account.type);
        Bundle bundle = new Bundle();
        bundle.putParcelable(AccountManager.KEY_INTENT, intent);

        return bundle;
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account, Bundle options) throws NetworkErrorException {
        return null;
    }




    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS)) {
            return AccountGeneral.AUTHTOKEN_TYPE_FULL_ACCESS_LABEL;
        } else if (authTokenType.equals(AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY)) {
            return AccountGeneral.AUTHTOKEN_TYPE_READ_ONLY_LABEL;
        } else {
            return authTokenType + " (Label)";
        }
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account, String authTokenType, Bundle options) throws NetworkErrorException {
        return null;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account, String[] features) throws NetworkErrorException {
        return null;
    }

}
