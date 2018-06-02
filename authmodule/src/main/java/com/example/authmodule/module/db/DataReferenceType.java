package com.example.authmodule.module.db;

/**
 * Created by 남대영 on 2018-05-07.
 */

public enum DataReferenceType {

    PROFILE("profile"),
    RESERVE("reserve");

    private String ref;

    private DataReferenceType(String ref){
        this.ref = ref;
    }

    public String getName(){ return this.ref; }

}
