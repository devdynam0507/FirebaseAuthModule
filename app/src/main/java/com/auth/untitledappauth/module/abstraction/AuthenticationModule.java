package com.auth.untitledappauth.module.abstraction;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.google.firebase.auth.FirebaseAuth;

/**
 * 모든 인증모듈의 상위 클래스
 * @author 남대영
 */
public abstract class AuthenticationModule extends AsyncTask<String, Void, Boolean> implements IAuthentication {

    public static boolean authenticated = false;
    protected ProgressDialog dialog;

    public FirebaseAuth getAuth(){
        return FirebaseAuth.getInstance();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

}
