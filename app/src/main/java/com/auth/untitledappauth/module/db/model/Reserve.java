package com.auth.untitledappauth.module.db.model;

import java.util.List;

/**
 * Created by 남대영 on 2018-05-13.
 */

public class Reserve {

    public String date;
    public String people;
    public String price;
    public String name;

    public Reserve() {
    }

    public Reserve(String date, String name, String people, String price){
        this.date = date;
        this.name = name;
        this.people = people;
        this.price = price;
    }

    public String getDate(){ return this.date; }
    public void setDate(String date) { this.date = date; }

    public String getPeople() { return this.people; }
    public void setPeople(String people) { this.people = people; }

    public void setPrice(String price) { this.price = price; }
    public String getPrice() { return this.price; }

    public void setName(String name) { this.name = name; }
    public String getName() { return name; }

}
