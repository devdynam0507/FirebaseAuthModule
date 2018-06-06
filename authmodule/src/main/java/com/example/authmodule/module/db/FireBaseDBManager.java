package com.example.authmodule.module.db;

import com.example.authmodule.module.abstraction.AuthenticationModule;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 남대영 on 2018-05-05.
 */

public class FireBaseDBManager {

    private static FireBaseDBManager manager;

    private FireBaseDBManager(){}

    public static FireBaseDBManager manager()
    {
        if(manager == null) manager = new FireBaseDBManager();
        return manager;
    }

    /** 파이어베이스 인증 db레퍼런스를 가져옵니다. */
    public DatabaseReference getReference(String child, AuthenticationModule aModule)
    {
        if(aModule.isAuthenticated()) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(child);
            return reference.child(aModule.getAuth().getCurrentUser().getUid());
        }
        return null;
    }

    public DatabaseReference getReference(DataReferenceType reference) {return FirebaseDatabase.getInstance().getReference().child(reference.getName());}
    public FirebaseFirestore getFireStore() { return FirebaseFirestore.getInstance(); }
    public FireStoreUtility getFireStoreUtility(String... data) { return new FireStoreUtility(data); }

    public class FireStoreUtility {

        private String[] datas;
        Map<String, Object> map = new HashMap<>();

        public FireStoreUtility(String... data)
        {
            this.datas = data;
        }

        public FireStoreUtility addArrayList(String key, String[] arrays)
        {
            this.map.put(key, Arrays.asList(arrays));
            return this;
        }

        public Map<String, Object> convertToMap()
        {
            for(String serial : this.datas) {
                String[] serials = serial.split("-");
                map.put(serials[0], serials[1]);
            }
            return map;
        }
    }
}
