package com.pritesh.notes;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Globals extends Application{
    private static FirebaseAuth mAuth;
    private static DatabaseReference databaseReference;
    public Globals(){
    }

    public static FirebaseAuth getAuth(){
        if(mAuth!=null){
            return mAuth;
        }
        else {
            mAuth = FirebaseAuth.getInstance();
            return mAuth;
        }
    }

    public static DatabaseReference getDatabaseReference(){
        if(databaseReference!=null){
            return databaseReference;
        }
        else {
            databaseReference = FirebaseDatabase.getInstance().getReference();
            return databaseReference;
        }
    }
}
