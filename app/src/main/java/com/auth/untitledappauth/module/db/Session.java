package com.auth.untitledappauth.module.db;

import android.content.Context;

import com.auth.untitledappauth.module.db.request.ReserveRequestBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 남대영 on 2018-05-07.
 */

public class Session {

    private String cardKey; //카드 블링키
    private String phone; //전화번호
    private List<String> reserves; //사용자가 예약한 스터디룸 정보들(키 값)

    private static Session sessionInstance;

    private Session() {}

    public  static Session getSession()
    {
        if(sessionInstance == null) sessionInstance = new Session();
        return sessionInstance;
    }

    public void setCardKey(String cardKey) { this.cardKey = cardKey; }
    public void setPhoneNumber(String phoneNumber) { this.phone = phoneNumber; }
    public void setReserves(List<String> reserves) { this.reserves = reserves; }

    public String getCardKey() { return this.cardKey; }
    public String getPhoneNumber() { return this.phone; }
    public List<String> getReserves() { return this.reserves; }

    public void addReserves(String key) { this.reserves.add(key); }

    public void addReserves(Context context, String... data)
    {
        if(this.reserves == null) this.reserves = new ArrayList<>();
        /*Reserve테이블에 쿼리를 날립니다. 만약 데이터가 존재할시 아무일도 일어나지 않고 존재하지 않을시 키값을 추가합니다.*/
        ReserveRequestBuilder.getBuilder().setContext(context).setModelData(data).build();
    }

    public void saveReserveDatas()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        DatabaseReference reference = FireBaseDBManager.manager().getReference(DataReferenceType.PROFILE).child(auth.getCurrentUser().getUid());
        if(this.reserves != null && this.reserves.size() > 0) reference.child("reserve").setValue(this.reserves);
    }

    public void clear()
    {
        if(this.reserves != null) this.reserves.clear();
        this.setReserves(null);
        this.setPhoneNumber("");
        this.setCardKey("");
    }

}