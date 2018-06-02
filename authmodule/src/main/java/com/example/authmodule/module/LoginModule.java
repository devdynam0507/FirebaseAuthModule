package com.example.authmodule.module;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.example.authmodule.module.abstraction.AuthenticationModule;
import com.example.authmodule.module.activityutils.TaskCallback;
import com.example.authmodule.module.db.DataReferenceType;
import com.example.authmodule.module.db.FireBaseDBManager;
import com.example.authmodule.module.db.Session;
import com.example.authmodule.module.db.model.User;
import com.example.authmodule.module.util.CryptoUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

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
                    FireBaseDBManager.manager().getReference(DataReferenceType.PROFILE).child(LoginModule.super.getAuth().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    try {
                                        User user = dataSnapshot.getValue(User.class);
                                        List<String> reserves = dataSnapshot.child("reserve").getValue(new GenericTypeIndicator<List<String>>() {
                                        });
                                        Session.getSession().setReserves(reserves);
                                        Session.getSession().setCardKey(new CryptoUtil(email).aesDecode(user.getCard()));
                                        Session.getSession().setPhoneNumber(user.getPhone());
                                        dialog.dismiss();
                                        context.taskFinish(task.isSuccessful());
                                    }catch (Exception e) { e.printStackTrace(); }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

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
