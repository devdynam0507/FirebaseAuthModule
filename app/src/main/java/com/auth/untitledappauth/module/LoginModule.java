package com.auth.untitledappauth.module;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;

import com.auth.untitledappauth.activityutils.TaskCallback;
import com.auth.untitledappauth.module.abstraction.AuthenticationModule;
import com.auth.untitledappauth.module.db.DataReference;
import com.auth.untitledappauth.module.db.FireBaseDBManager;
import com.auth.untitledappauth.module.db.Session;
import com.auth.untitledappauth.module.db.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

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
                    FireBaseDBManager.manager().getReference(DataReference.PROFILE).child(LoginModule.super.getAuth().getCurrentUser().getUid())
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User user = dataSnapshot.getValue(User.class);
                                    Session.cardKey = user.card;
                                    Session.phone = user.phone;
                                    dialog.dismiss();
                                    context.taskFinish(task.isSuccessful());
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
