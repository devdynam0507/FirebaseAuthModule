package com.auth.untitledappauth.module;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.auth.untitledappauth.activityutils.TaskCallback;
import com.auth.untitledappauth.module.abstraction.AuthenticationModule;
import com.auth.untitledappauth.module.db.FireBaseDBManager;
import com.auth.untitledappauth.module.db.Session;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DatabaseReference;

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
                            DatabaseReference userDB = FireBaseDBManager.manager().getReference("profile", EmailRegisterModule.this);
                            userDB.child("card").setValue(card); //카드 블링키를 user db에 저장합니다.
                            userDB.child("phone").setValue(phoneNumber); //휴대폰 번호를 user db에 저장합니다.
                            Session.cardKey = card;
                            Session.phone = phoneNumber;
                        }
                    });//회원가입 성공시 로그인을 합니다.
                    authenticated = true;
                }
                dialog.dismiss();
                context.taskFinish(task.isSuccessful()); //성공 여부에따라 Activity를 조정합니다.
            }
        });
        return null;
    }

}
