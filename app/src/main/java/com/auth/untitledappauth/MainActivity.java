package com.auth.untitledappauth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.auth.untitledappauth.activityutils.InitializeBase;
import com.auth.untitledappauth.activityutils.TaskCallback;
import com.auth.untitledappauth.module.db.Session;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements TaskCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* 로그인이 되어있지 않다면 */
        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            //로그인 액티비티로 변경합니다. (로그인 액티비티에 회원가입 버튼 있음)
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            ((TextView) findViewById(R.id.mainTextView)).setText(FirebaseAuth.getInstance().getCurrentUser().getEmail() + " 님 환영합니다");
            if(Session.phone == null || Session.cardKey == null){ /* Session값이 null일시 db에서 정보를 받아옵니다. */
                new InitializeBase(this).execute();
            }else {
                ((TextView) findViewById(R.id.mainCardView)).setText("블링키: " + Session.cardKey);
                ((TextView) findViewById(R.id.mainPhonView)).setText("전화번호: " + Session.phone);
            }

            ((Button) findViewById(R.id.logoutButton)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Session.phone = "";
                    Session.cardKey = "";
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void taskFinish(boolean success) {
        if(success){
            ((TextView) findViewById(R.id.mainCardView)).setText(Session.cardKey);
            ((TextView) findViewById(R.id.mainPhonView)).setText(Session.phone);
        }
    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

}
