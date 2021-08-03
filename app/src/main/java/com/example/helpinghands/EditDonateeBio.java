package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EditDonateeBio extends AppCompatActivity {

    InternetRequests internetRequests;
    NetworkConnection networkConnection = new NetworkConnection(this);
    EditText biography_display;
    TextView name_display;
    String userID,name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_edit_donatee_bio);

        userID = getIntent().getStringExtra("userID");
        internetRequests = new InternetRequests();
        String biography = processJSON(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonateeBio.php?u="+userID));

        name_display = findViewById(R.id.textView87);
        name_display.setText("Tell us a little bit more about yourself " + name);
        biography_display = findViewById(R.id.editText22);
        biography_display.setText(biography);
    }

    public String processJSON(String json){
        String bio="";
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                bio = item.getString("BIOGRAPHY");
                name = item.getString("NAME");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bio;
    }

    public void doSave(View view) {
        if(networkConnection.isNetworkAvailable()) {
            String bio = biography_display.getText().toString();
            internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/updateDonateeBio.php?u=" + userID + "&b=" + bio);
            Toast.makeText(this, "Biography updated successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonateeMenu.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }
}
