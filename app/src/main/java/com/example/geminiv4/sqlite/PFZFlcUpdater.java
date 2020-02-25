package com.example.geminiv4.sqlite;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PFZFlcUpdater {



    public void getFLcPfzdata(){
        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        DatabaseReference mGetReference = mDatabase.getReference().child("flcpfzdata");
        mGetReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : dataMap.keySet()){
                        Object data = dataMap.get(key);
                        try{
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            Log.d("FirebaseTest",userData.toString());
                        }catch (ClassCastException cce){
                            // If the object can’t be casted into HashMap, it means that it is of type String.
                            try{
                                String mString = String.valueOf(dataMap.get(key));
                                Log.d("FirebaseTest mString",mString);
                            }catch (ClassCastException cce2){
                                cce2.printStackTrace();
                            }
                        }

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
