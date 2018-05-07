package com.auth.untitledappauth.activityutils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.auth.untitledappauth.module.LoginModule;
import com.auth.untitledappauth.module.db.DataReference;
import com.auth.untitledappauth.module.db.FireBaseDBManager;
import com.auth.untitledappauth.module.db.Session;
import com.auth.untitledappauth.module.db.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by 남대영 on 2018-05-07.
 */

public class InitializeBase extends AsyncTask<Void, Void, Void> {

    private TaskCallback context;
    private ProgressDialog dialog;

    public InitializeBase(TaskCallback context){
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        dialog = new ProgressDialog(this.context.getContext());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("정보를 얻어오고 있습니다.");
        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FireBaseDBManager.manager().getReference(DataReference.PROFILE).child(auth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        Session.cardKey = user.card;
                        Session.phone = user.phone;
                        dialog.dismiss();
                        context.taskFinish(true);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return null;
    }
}
