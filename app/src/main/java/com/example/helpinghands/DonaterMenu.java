package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DonaterMenu extends AppCompatActivity {

    TextView name_display;
    String userID;
    InternetRequests webRequest = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.donater_menu);
        userID = getIntent().getStringExtra("userID");

        String name = getName(webRequest.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonaterName.php?u=" + userID));

        name_display = findViewById(R.id.textView8);
        name_display.setText("Welcome " + name);
    }

    public String getName(String json){
        String name="";
        try {
            JSONArray all = new JSONArray(json);

            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                name = item.getString("NAME");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void doBasket(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonatorBasket.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void doDonate(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonateScreen.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void doSettings(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonaterSettings.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();

        }
    }

    public void onBackPressed(){
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable,Logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }
}
