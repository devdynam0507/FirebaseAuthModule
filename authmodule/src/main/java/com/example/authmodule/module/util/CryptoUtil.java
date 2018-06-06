package com.example.authmodule.module.util;

import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by 남대영 on 2018-05-15.
 */

public class CryptoUtil {

    /** Key --> user email*/

    private String crypto;
    private Key spec;

    public CryptoUtil(String email) throws Exception {
        if(email.length() < 16) {
            for(int i = 0; i < 16-email.length()+1; i++) {
                email += "-";
            }
        }
        this.crypto = email.substring(0, 16);
        byte[] keyBytes = new byte[16];
        byte[] b = email.getBytes("UTF-8");
        int len = b.length;
        if(len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");

        this.spec = keySpec;
    }

    // 암호화
    public String aesEncode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException{
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.ENCRYPT_MODE, this.spec, new IvParameterSpec(this.crypto.getBytes()));

        byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
        String enStr = Base64.encodeToString(encrypted, 0);

        return enStr;
    }

    //복호화
    public String aesDecode(String str) throws java.io.UnsupportedEncodingException, NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE, this.spec, new IvParameterSpec(this.crypto.getBytes("UTF-8")));

        byte[] byteStr = Base64.decode(str, 0);

        return new String(c.doFinal(byteStr),"UTF-8");
    }


}
