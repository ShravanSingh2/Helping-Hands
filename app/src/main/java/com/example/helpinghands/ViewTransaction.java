package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ViewTransaction extends AppCompatActivity {

    String userID ;
    NetworkConnection networkConnection = new NetworkConnection(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_transaction);
        String name,quantity,email,contact_num,itemName,date;

        userID = getIntent().getStringExtra("userID");

        TextView title = findViewById(R.id.textView76);
        TextView donation_display = findViewById(R.id.textView80);
        TextView number_display = findViewById(R.id.textView81);
        TextView email_display = findViewById(R.id.textView83);
        TextView date_display = findViewById(R.id.textView85);

        name = getIntent().getStringExtra("donaterName");
        quantity = getIntent().getStringExtra("quantity");
        email = getIntent().getStringExtra("email");
        contact_num = getIntent().getStringExtra("contactNum");
        itemName = getIntent().getStringExtra("itemName");
        date = getIntent().getStringExtra("date");

        title.setText(name + "'s donation to you");
        donation_display.setText(name + " is willing to donate " + quantity + " " + itemName + "(s)");
        number_display.setText("Phone number :" + '\n' + contact_num);
        email_display.setText("Email Address :" + '\n' + email);
        date_display.setText("Dated :" + '\n' + date);
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
