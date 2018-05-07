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

public class LoginActivity extends AppCompatActivity implements TaskCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email = ((EditText) findViewById(R.id.inputEmail)).getText().toString();
                String password = ((EditText) findViewById(R.id.inputPassword)).getText().toString();
                if(email.equals("") || password.equals("")) return;

                AuthModuleFactory.getFactory().runAuthModule(LoginActivity.this, AuthType.LOGIN, email, password);
            }
        });

        ((Button) findViewById(R.id.loginPageRegisterButton)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, EmailRegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public Context getContext() {
        return LoginActivity.this;
    }

    @Override
    public void taskFinish(boolean success) {
        if(success) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
