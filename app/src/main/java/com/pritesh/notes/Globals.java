package com.pritesh.notes;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Globals extends Application{
    private static FirebaseAuth mAuth;
    private static DatabaseReference databaseReference;
    private static Globals global;
    private Globals(){

    }

    public static Globals getGlobal(){
        if (global==null){
            global=new Globals();
        }
        return  global;
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

    //    Network Check Method
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
