package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DonateeSettings extends AppCompatActivity {

    InternetRequests internetRequests = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);
    String userName,userID,contactNum,name,password,email,responseData,area,address;
    EditText username_display, password_display, email_address_display, contact_num_display, name_display,area_display,address_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_donatee_settings);

        userID = getIntent().getStringExtra("userID");
        responseData =internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonateeInfo.php?u="+userID);
        processJSON(responseData);

        name_display = findViewById(R.id.editText19);
        username_display = findViewById(R.id.editText17);
        contact_num_display = findViewById(R.id.editText15);
        email_address_display = findViewById(R.id.editText18);
        password_display = findViewById(R.id.editText16);
        area_display = findViewById(R.id.editText20);
        address_display = findViewById(R.id.editText21);

        password_display.setHint("(unchanged)");
        password_display.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                password_display.setHint("");
            }
        });

        name_display.setText(name);
        username_display.setText(userName);
        password_display.setText(password);
        email_address_display.setText(email);
        contact_num_display.setText(contactNum);
        area_display.setText(area);
        address_display.setText(address);
    }

    public void processJSON(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item =all.getJSONObject(i);
                contactNum = item.getString("CONTACT_NUM");
                name = item.getString("NAME");
                userName=item.getString("USERNAME");
                email = item.getString("EMAIL_ADDRESS");
                area = item.getString("AREA");
                address=item.getString("ADDRESS");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void doSave(View view) {
        if(networkConnection.isNetworkAvailable()) {
            userName = username_display.getText().toString();
            name = name_display.getText().toString();
            password = password_display.getText().toString();
            if(password.equals("")){
                password="unchanged";
            }
            email = email_address_display.getText().toString();
            contactNum = contact_num_display.getText().toString();
            area = area_display.getText().toString();
            address = address_display.getText().toString();
            responseData = internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/updateDonateeInfo.php?id=" + userID + "&un=" + userName + "&n=" + name
                    + "&num=" + contactNum + "&e=" + email + "&p=" + password + "&a=" + area + "&ad=" + address);
            Log.d("RESPONSE dATA ", responseData);
            if (responseData.equals("Records updated successfully.")) {
                Toast.makeText(this, "Details saved successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Username already exists", Toast.LENGTH_SHORT).show();
            }
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
