package com.auth.untitledappauth;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.authmodule.module.activityutils.InitializeBase;
import com.example.authmodule.module.activityutils.TaskCallback;
import com.example.authmodule.module.db.Session;
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
            if(Session.getSession().getPhoneNumber() == null || Session.getSession().getCardKey() == null){ /* Session값이 null일시 db에서 정보를 받아옵니다. */
                new InitializeBase(this).execute();
            }else {
                ((TextView) findViewById(R.id.mainCardView)).setText("블링키: " + Session.getSession().getCardKey());
                ((TextView) findViewById(R.id.mainPhonView)).setText("전화번호: " + Session.getSession().getPhoneNumber());
            }

            findViewById(R.id.logoutButton).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    FirebaseAuth.getInstance().signOut();
                    Session.getSession().clear();
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            findViewById(R.id.exampleReserveButton).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String date = "2018-5-7-12:10";
                    String name = "가게A";
                    String price = "1000";
                    String numOfPeople = "4";

                    Session.getSession().addReserves("key1");
                    Log.d("[Fire-Base]", "Input reserve example data");
                }
            });

            findViewById(R.id.move_test).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, TestActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void taskFinish(boolean success) {
        if(success){
            ((TextView) findViewById(R.id.mainCardView)).setText(Session.getSession().getCardKey());
            ((TextView) findViewById(R.id.mainPhonView)).setText(Session.getSession().getPhoneNumber());
        }
    }

    @Override
    public Context getContext() {
        return MainActivity.this;
    }

}
