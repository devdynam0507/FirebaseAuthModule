package com.example.authmodule.module;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.authmodule.module.abstraction.AuthenticationModule;
import com.example.authmodule.module.activityutils.TaskCallback;
import com.example.authmodule.module.db.FireBaseDBManager;
import com.example.authmodule.module.db.Session;
import com.example.authmodule.module.util.ArrayUtility;
import com.example.authmodule.module.util.CryptoUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 남대영 on 2018-05-07.
 */

public class LoginModule extends AuthenticationModule {

    private TaskCallback context;

    public LoginModule(TaskCallback context){
        this.context = context;
    }

    @Override
    public boolean authenticating(String... authData)
    {
        if(!super.isAuthenticated()){
            String email = authData[0];
            String password = authData[1];
            this.execute(email, password);
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(this.context.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("로그인 처리중입니다.");
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... params) {
        final String email = params[0];
        final String password = params[1];

        super.getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull final Task<AuthResult> task) {
                if(task.isSuccessful()){
                    /** DB에서 유저 데이터를 가져와서 Session에 저장합니다. */
                    FirebaseFirestore firestore = FireBaseDBManager.manager().getFireStore();
                    DocumentReference ref = firestore.collection("profile").document(LoginModule.super.getAuth().getCurrentUser().getUid());
                    ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                try {
                                    Session session = Session.getSession();
                                    DocumentSnapshot snapshot = task.getResult();
                                    Map<String, Object> data = snapshot.getData();
                                    List<String> reserves = ArrayUtility.typeChange((ArrayList<Object>) data.get("reserves"));
                                    CryptoUtil cryptoUtil = new CryptoUtil(email);
                                    session.setCardKey(cryptoUtil.aesDecode((String) data.get("card")));
                                    session.setPhoneNumber((String) data.get("phone"));
                                    session.setReserves(reserves);
                                    Log.d("[RES]", "" + reserves.size());
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            dialog.dismiss();
                            context.taskFinish(task.isSuccessful());
                        }
                    });
                }else{
                    dialog.dismiss();
                    context.taskFinish(task.isSuccessful());
                }
            }
        });
        return null;
    }

}
