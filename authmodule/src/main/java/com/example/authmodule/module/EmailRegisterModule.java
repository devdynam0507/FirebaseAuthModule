package com.example.authmodule.module;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.example.authmodule.module.abstraction.AuthenticationModule;
import com.example.authmodule.module.activityutils.TaskCallback;
import com.example.authmodule.module.db.FireBaseDBManager;
import com.example.authmodule.module.db.Session;
import com.example.authmodule.module.util.CryptoUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 남대영 on 2018-05-05.
 */

/**
 * 회원가입 모듈
 * */
public class EmailRegisterModule extends AuthenticationModule {

    private TaskCallback context;

    public EmailRegisterModule(TaskCallback context){
        this.context = context;
    }

    @Override
    public boolean authenticating(String... authData) {
        if(!super.isAuthenticated()) {
            final String email = authData[0];
            final String password = authData[1];
            final String phoneNumber = authData[2];
            final String card = authData[3]; //card 블링키

            this.execute(email, password, phoneNumber, card);
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(this.context.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("회원가입 처리중입니다.");
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        final String email = params[0];
        final String password = params[1];
        final String phoneNumber = params[2];
        final String card = params[3]; //card 블링키

        super.getAuth().createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try {
                                FirebaseFirestore firestore = FireBaseDBManager.manager().getFireStore();
                                CryptoUtil crypto = new CryptoUtil(email);
                                Map<String, Object> map = FireBaseDBManager.manager().getFireStoreUtility("card-" + crypto.aesEncode(card),
                                        "phone-" + phoneNumber)
                                        .addArrayList("reserves", new String[]{}).convertToMap();
                                DocumentReference ref = firestore.collection("profile").document(EmailRegisterModule.super.getAuth().getCurrentUser().getUid());
                                ref.set(map);
                                Session.getSession().setCardKey(card);
                                Session.getSession().setPhoneNumber(phoneNumber);
                                Session.getSession().setReserves(new ArrayList<String>());
                            }catch (Exception e){ e.printStackTrace(); }
                        }
                    });
                    authenticated = true;
                }
                dialog.dismiss();
                context.taskFinish(task.isSuccessful()); //성공 여부에따라 Activity를 조정합니다.
            }
        });
        return null;
    }

}
