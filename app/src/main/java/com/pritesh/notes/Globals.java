package com.pritesh.notes;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;

public class Globals extends Application{
    private static FirebaseAuth mAuth;
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
}
