package com.example.authmodule.module.db.request;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.example.authmodule.module.db.FireBaseDBManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

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
            FirebaseFirestore firestore = FireBaseDBManager.manager().getFireStore();
            FireBaseDBManager.FireStoreUtility utility = FireBaseDBManager.manager().getFireStoreUtility(datas);
            Map<String, Object> dataMap = utility.convertToMap();

            dialog.dismiss();
            return true;
        }

    }

}
