package com.auth.untitledappauth.module.db;

/**
 * Created by 남대영 on 2018-05-07.
 */

public enum DataReference {

    PROFILE("profile");

    private String ref;

    private DataReference(String ref){
        this.ref = ref;
    }

    public String getName(){ return this.ref; }

}
