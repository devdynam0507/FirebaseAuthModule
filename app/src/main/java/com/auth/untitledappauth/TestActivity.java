package com.auth.untitledappauth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.authmodule.module.util.CryptoUtil;

public class TestActivity extends AppCompatActivity {

    private static final String key = "wsnam0507@naver.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        findViewById(R.id.test_encrypt_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String encryptMessage = ((EditText) findViewById(R.id.test_encrypt_edit)).getText().toString();
                if(encryptMessage.equals("")) return;

                try {
                    CryptoUtil cryptoUtil = new CryptoUtil(key);

                    ((TextView) findViewById(R.id.test_encrypt_view)).setText(cryptoUtil.aesEncode(encryptMessage));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.test_decrypt_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String decryptMessage = ((EditText) findViewById(R.id.test_decrypt_edit)).getText().toString();
                String encryptMessage = ((TextView) findViewById(R.id.test_encrypt_view)).getText().toString();
                if(decryptMessage.equals("") && encryptMessage.equals("")) return;

                try{
                    CryptoUtil cryptoUtil = new CryptoUtil(key);

                    ((TextView) findViewById(R.id.test_encrypt_view)).setText(cryptoUtil.aesDecode(encryptMessage));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }
}
