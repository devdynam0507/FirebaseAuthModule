package com.example.authmodule.module.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.authmodule.module.db.request.ReserveRequestBuilder;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public void addReserves(String key)
    {
        this.reserves.add(key);
        this.saveReserveDatas();
    }

    public void addReserves(Context context, String... data)
    {
        if(this.reserves == null) this.reserves = new ArrayList<>();
        /*Reserve테이블에 쿼리를 날립니다. 만약 데이터가 존재할시 아무일도 일어나지 않고 존재하지 않을시 키값을 추가합니다.*/
        ReserveRequestBuilder.getBuilder().setContext(context).setModelData(data).build();
    }

    public void saveReserveDatas()
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        if(this.reserves != null && this.reserves.size() > 0) {
            final DocumentReference ref = firestore.collection("profile").document(auth.getCurrentUser().getUid());
            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()) {
                        DocumentSnapshot snapshot = task.getResult();
                        Map<String, Object> data = snapshot.getData();
                        data.put("reserves", reserves);
                        ref.set(data);
                    }
                }
            });
        }
    }

    public void clear()
    {
        if(this.reserves != null) this.reserves.clear();
        this.setReserves(null);
        this.setPhoneNumber("");
        this.setCardKey("");
    }

}