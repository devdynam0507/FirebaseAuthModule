package com.auth.untitledappauth.module.db.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.auth.untitledappauth.module.db.DataReferenceType;
import com.auth.untitledappauth.module.db.FireBaseDBManager;
import com.auth.untitledappauth.module.db.Session;
import com.auth.untitledappauth.module.db.model.Reserve;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by 남대영 on 2018-05-13.
 */

public class ReserveRequestBuilder {

    private Context context;
    private String[] datas;
    private String inputKey;

    private ReserveRequestBuilder(){}

    public static ReserveRequestBuilder getBuilder()
    {
        return new ReserveRequestBuilder();
    }

    public ReserveRequestBuilder setContext(Context context)
    {
        this.context = context;
        return this;
    }

    public ReserveRequestBuilder setModelData(String[] modelData)
    {
        this.datas = modelData;
        return this;
    }

    public ReserveRequestBuilder setNewInputKey(String key)
    {
        this.inputKey = key;
        return this;
    }

    public void build()
    {
        ReserveDataRequest request = new ReserveDataRequest();
        request.execute(this.datas);
    }

    public class ReserveDataRequest extends AsyncTask<String, Void, Boolean>  {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage("데이터 처리중입니다..");
            dialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            FireBaseDBManager.manager().getReference(DataReferenceType.RESERVE).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Reserve reserve = snapshot.getValue(Reserve.class);
                        System.out.println("[Fire-Base] db-date->" + reserve.getDate() + ",request-date->" + datas[0]);
                        System.out.println("[Fire-Base] db-name->" + reserve.getName() + ",request-name->" + datas[1]);
                        System.out.println("[Fire-Base] db-people->" + reserve.getPeople() + ",request-people->" + datas[2]);
                        System.out.println("[Fire-Base] db-price->" + reserve.getPrice() + ",request-price->" + datas[3]);
                        if(reserve.getDate().equals(datas[0]) && reserve.getName().equals(datas[1]) &&
                                reserve.getPeople().equals(datas[2]) && reserve.getPrice().equals(datas[3])){
                            dialog.dismiss();
                            return;
                        }
                    }
                    DatabaseReference reference = FireBaseDBManager.manager().getReference(DataReferenceType.RESERVE);
                    String key = reference.push().getKey();
                    reference.child(key).child("date").setValue(datas[0]);
                    reference.child(key).child("name").setValue(datas[1]);
                    reference.child(key).child("people").setValue(datas[2]);
                    reference.child(key).child("price").setValue(datas[3]);
                    Session.getSession().addReserves(key);
                    Session.getSession().saveReserveDatas();
                    dialog.dismiss();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return true;
        }

    }

}
