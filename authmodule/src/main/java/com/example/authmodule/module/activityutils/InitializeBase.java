package com.example.authmodule.module.activityutils;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.authmodule.module.db.FireBaseDBManager;
import com.example.authmodule.module.db.Session;
import com.example.authmodule.module.util.ArrayUtility;
import com.example.authmodule.module.util.CryptoUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 남대영 on 2018-05-07.
 */

public class   InitializeBase extends AsyncTask<Void, Void, Void> {

    private TaskCallback context;
    private ProgressDialog dialog;

    public InitializeBase(TaskCallback context){
        this.context = context;
    }

    /** 유저 카드키, 번호, 예약정보가 담겨있는 유저 db에서 데이터를 로드후 Session에 저장함 */
    private void loadProfileData(final FirebaseAuth auth, final boolean dismiss)
    {
        FirebaseFirestore firestore = FireBaseDBManager.manager().getFireStore();
        DocumentReference ref = firestore.collection("profile").document(auth.getCurrentUser().getUid());
        ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    try {
                        Session session = Session.getSession();
                        DocumentSnapshot snapshot = task.getResult();
                        Map<String, Object> data = snapshot.getData();
                        List<String> reserves = ArrayUtility.typeChange((ArrayList<Object>) data.get("reserves"));
                        CryptoUtil cryptoUtil = new CryptoUtil(auth.getCurrentUser().getEmail());
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
