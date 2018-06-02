package com.auth.untitledappauth;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.example.authmodule.module.PhoneValidModule;
import com.example.authmodule.module.abstraction.customer.PhoneValidListener;
import com.example.authmodule.module.abstraction.handler.AuthListenerHandler;
import com.example.authmodule.module.activityutils.TaskCallback;
import com.example.authmodule.module.factory.AuthModuleFactory;
import com.example.authmodule.module.factory.AuthType;

public class EmailRegisterActivity extends AppCompatActivity implements TaskCallback, PhoneValidListener {

    private boolean validPhone = false;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailregister);

        findViewById(R.id.phoneValid).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();
                if(phoneNumber == null || phoneNumber.equals("")) return;
                PhoneValidModule module = new PhoneValidModule();
                module.registerPhoneValidListener(getListener());
                AuthListenerHandler.getHandler().registerListener(module).run(phoneNumber,(Activity) getContext());
            }
        });

        findViewById(R.id.registerClearButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validPhone) {
                    String email = ((EditText) findViewById(R.id.registerEmailInput)).getText().toString();
                    String password = ((EditText) findViewById(R.id.registerPasswordInput)).getText().toString();
                    String phone = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();
                    String card = ((EditText) findViewById(R.id.cardInput)).getText().toString();
                    if (email.equals("") || password.equals("") || card.equals("") || phone.equals(""))
                        return;

                    AuthModuleFactory.getFactory().runAuthModule(EmailRegisterActivity.this, AuthType.EMAIL_REGISTER, email, password, phone, card);
                }else {
                    android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(EmailRegisterActivity.this);
                    alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.setMessage("전화번호 인증을 해주세요.");
                    alert.show();
                }
            }
        });
    }

    public Activity getActivity(){ return this; }

    public void setVerifyCode(String smsCode)
    {
        EditText verifyEdit = findViewById(R.id.inputValid);
        verifyEdit.setText(smsCode);
    }

    @Override
    public Context getContext() {
        return EmailRegisterActivity.this;
    }

    @Override
    public void taskFinish(boolean success) {
        if(success) {
            Intent intent = new Intent(EmailRegisterActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            this.finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(EmailRegisterActivity.this);
            builder.setMessage("로그인 실패").setNegativeButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public PhoneValidListener getListener() { return this; }

    @Override
    public void authPhoneListener(String phoneNumber, boolean valid) {
        this.validPhone = valid;
        this.phoneNumber = phoneNumber;
        if(!this.validPhone) {
            android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);
            alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.setMessage("인증에 실패하였습니다.");
            alert.show();
            return;
        }else this.setVerifyCode(phoneNumber);
    }
}
