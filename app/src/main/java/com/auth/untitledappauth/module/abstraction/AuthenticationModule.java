package com.auth.untitledappauth.module.abstraction;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by 남대영 on 2018-05-05.
 */

public abstract class AuthenticationModule extends AsyncTask<String, Void, Boolean> implements IAuthentication {

    protected boolean authenticated = false;
    protected ProgressDialog dialog;

    public FirebaseAuth getAuth(){
        return FirebaseAuth.getInstance();
    }

    @Override
    public boolean isAuthenticated() {
        return this.authenticated;
    }

}
