package com.example.authmodule.module;

import android.app.Activity;

import com.example.authmodule.module.abstraction.IAuthModuleListener;
import com.example.authmodule.module.abstraction.customer.PhoneValidListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * Created by 남대영 on 2018-05-13.
 */

public class PhoneValidModule implements IAuthModuleListener {


    private PhoneValidListener listener;
    private String phoneNumber;

    public void registerPhoneValidListener(PhoneValidListener listener) {
        this.listener = listener;
    }

    public PhoneAuthProvider.OnVerificationStateChangedCallbacks getCallback()
    {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                if(listener != null) {
                    listener.authPhoneListener(phoneAuthCredential.getSmsCode(), true);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                listener.authPhoneListener(phoneNumber, false);
            }
        };
        return callbacks;
    }

    @Override
    public IAuthModuleListener authModuleCallback(Object... objects) {
        this.phoneNumber = (String) objects[0];
        Activity activity = (Activity) objects[1];

        try{
            FirebaseAuth.getInstance().setLanguageCode("kr");
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60L, TimeUnit.SECONDS, activity, this.getCallback());
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return null;
    }
}
