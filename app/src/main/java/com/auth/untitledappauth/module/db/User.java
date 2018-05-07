package com.auth.untitledappauth.module.db;

/**
 * Created by 남대영 on 2018-05-07.
 */

public class User {

    public String card;
    public String phone;

    public User() {
    }

    public User(String card, String phone){
        this.card = card;
        this.phone = phone;
    }

    public String getCard(){ return this.card; }
    public void setCard(String card) { this.card = card; }
    public String getPhone() { return this.phone; }
    public void setPhone(String phone) { this.phone = phone; }

}

