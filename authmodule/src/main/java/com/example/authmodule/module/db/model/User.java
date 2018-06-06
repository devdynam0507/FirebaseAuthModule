package com.example.authmodule.module.db.model;

import java.util.List;

/**
 * Created by 남대영 on 2018-05-07.
 */

public class User {

    public String card;
    public String phone;
    public List<String> reserves;

    public User() {
    }

    public User(String card, String phone, List<String> reserves){
        this.card = card;
        this.phone = phone;
        this.reserves = reserves;
    }

    public String getCard(){ return this.card; }
    public void setCard(String card) { this.card = card; }

    public String getPhone() { return this.phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public void setReserves(List<String> reserves) { this.reserves = reserves; }
    public List<String> getReserves() { return this.reserves; }
    public void addReserve(String reserve) { this.reserves.add(reserve); }

}

