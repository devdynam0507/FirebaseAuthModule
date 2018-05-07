package com.auth.untitledappauth;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.auth.untitledappauth.activityutils.TaskCallback;
import com.auth.untitledappauth.module.factory.AuthModuleFactory;
import com.auth.untitledappauth.module.factory.AuthType;

public class EmailRegisterActivity extends AppCompatActivity implements TaskCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailregister);

        ((Button) findViewById(R.id.registerClearButton)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.registerEmailInput)).getText().toString();
                String password = ((EditText) findViewById(R.id.registerPasswordInput)).getText().toString();
                String phone = ((EditText) findViewById(R.id.phoneNumber)).getText().toString();
                String card = ((EditText) findViewById(R.id.cardInput)).getText().toString();
                if(email.equals("") || password.equals("") || card.equals("") || phone.equals("")) return;

                AuthModuleFactory.getFactory().runAuthModule(EmailRegisterActivity.this, AuthType.EMAIL_REGISTER, email, password, phone, card);
            }
        });
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

}
