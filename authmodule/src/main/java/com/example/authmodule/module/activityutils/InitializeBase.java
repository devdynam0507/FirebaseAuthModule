package com.example.authmodule.module.activityutils;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.example.authmodule.module.db.DataReferenceType;
import com.example.authmodule.module.db.FireBaseDBManager;
import com.example.authmodule.module.db.Session;
import com.example.authmodule.module.db.model.User;
import com.example.authmodule.module.util.CryptoUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by 남대영 on 2018-05-07.
 */

public class InitializeBase extends AsyncTask<Void, Void, Void> {

    private TaskCallback context;
    private ProgressDialog dialog;

    public InitializeBase(TaskCallback context){
        this.context = context;
    }

    /** 유저 카드키, 번호, 예약정보가 담겨있는 유저 db에서 데이터를 로드후 Session에 저장함 */
    private void loadProfileData(final FirebaseAuth auth, final boolean dismiss)
    {
        FireBaseDBManager.manager().getReference(DataReferenceType.PROFILE).child(auth.getCurrentUser().getUid())
            .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        User user = dataSnapshot.getValue(User.class);
                        List<String> reserves = dataSnapshot.child("reserve").getValue(new GenericTypeIndicator<List<String>>() {
                        });
                        Session.getSession().setCardKey(new CryptoUtil(auth.getCurrentUser().getEmail()).aesDecode(user.card));
                        Session.getSession().setPhoneNumber(user.phone);
                        Session.getSession().setReserves(reserves);
                    }catch (Exception e) { e.printStackTrace(); }

                    if(dismiss) {
                        dialog.dismiss();
                        context.taskFinish(true);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
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
        this.loadProfileData(auth, true);
        return null;
    }
}
