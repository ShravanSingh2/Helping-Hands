package com.example.helpinghands;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkConnection {
    Context context;

    public NetworkConnection(Context context){this.context=context;}
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if((activeNetworkInfo!=null) && (activeNetworkInfo.isConnectedOrConnecting())){
            return true;
        }else{
            return false;
        }
    }
}
