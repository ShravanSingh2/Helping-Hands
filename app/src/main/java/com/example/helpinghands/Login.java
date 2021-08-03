package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class Login extends AppCompatActivity {

    InternetRequests internetRequests = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);
    String ID;
    EditText username_display,password_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        username_display = findViewById(R.id.editText);
        password_display = findViewById(R.id.editText2);

        password_display.setHint("Enter Password");
        password_display.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                password_display.setHint("");
            }
        });

        username_display.setHint("Enter Username");
        username_display.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                username_display.setHint("");
            }
        });
    }

    public void doLogin(View view) {
        if(networkConnection.isNetworkAvailable()) {
            EditText usernameField = findViewById(R.id.editText);
            EditText passwordField = findViewById(R.id.editText2);
            String username = usernameField.getText().toString();
            String password = passwordField.getText().toString();
            String userType = GetUserType(username, password);
            if (userType.equals("D")) {
                String url = "https://lamp.ms.wits.ac.za/home/s2173638/getDonaterID.php?u=" + username + "&p=" + password;
                getUserID(url);
                Intent intent = new Intent(this, DonaterMenu.class);
                intent.putExtra("userID", ID);
                Log.d("userid", ID);
                startActivity(intent);
            } else {
                if (userType.equals("R")) {
                    String url = "https://lamp.ms.wits.ac.za/home/s2173638/getDonateeID.php?u=" + username + "&p=" + password;
                    getUserID(url);
                    Intent intent = new Intent(this, DonateeMenu.class);
                    intent.putExtra("userID", ID);
                    Log.d("userid", ID);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Invalid Username/Password combination", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void getUserID(String url){
        String json = internetRequests.sendRequest(url);
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                ID = item.getString("USER_ID");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public String GetUserType(final String username, final String password) {
        return (internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/login.php?u=" + username + "&p=" + password));
    }

    public void doRegister(View view) {
        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }

    public void ViewTopDonaters(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, TopDonaters.class);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }
}
