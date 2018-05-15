package com.auth.untitledappauth.module.db;

import com.auth.untitledappauth.module.abstraction.AuthenticationModule;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    public DatabaseReference getReference(DataReferenceType reference)
    {
        return FirebaseDatabase.getInstance().getReference().child(reference.getName());
    }

}
