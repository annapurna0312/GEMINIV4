package com.example.geminiv4.sqlite;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FireBaseUtils {

    static DatabaseReference databaseReference;

    public static DatabaseReference getDatabaseReference(){
        if(databaseReference == null){
            FirebaseDatabase instance = FirebaseDatabase.getInstance();
            instance.setPersistenceEnabled(true);
            databaseReference = instance.getReference();
            return  databaseReference;
        }else{
            return  databaseReference;
        }
    }
}
