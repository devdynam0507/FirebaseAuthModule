package com.auth.untitledappauth.module;

import android.app.AlertDialog;
import android.content.DialogInterface;

import com.auth.untitledappauth.EmailRegisterActivity;
import com.auth.untitledappauth.module.abstraction.AuthenticationModule;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

/**
 * Created by 남대영 on 2018-05-13.
 */

public class PhoneValidModule extends AuthenticationModule {

    private EmailRegisterActivity registerActivity; //TaskCallback Interface

    public PhoneValidModule(EmailRegisterActivity registerActivity){
        this.registerActivity = registerActivity;
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks getCallback()
    {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                registerActivity.setVerifyCode(phoneAuthCredential.getSmsCode());
                registerActivity.setValidPhone(true);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                AlertDialog.Builder alert = new AlertDialog.Builder(registerActivity.getActivity());
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.setMessage("인증에 실패하였습니다.");
                alert.show();
                registerActivity.setValidPhone(false);
            }
        };
        return callbacks;
    }

    @Override
    public boolean authenticating(String... authData) {
        String phoneNumber = authData[0];
        try{
            FirebaseAuth.getInstance().setLanguageCode("kr");
            PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60L, TimeUnit.SECONDS, this.registerActivity.getActivity(), this.getCallback());
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return false;
    }

    @Deprecated
    protected Boolean doInBackground(String... strings) {
        return null;
    }

}
