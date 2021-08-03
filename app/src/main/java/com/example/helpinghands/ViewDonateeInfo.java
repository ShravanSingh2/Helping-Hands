package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.widget.ANImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewDonateeInfo extends AppCompatActivity implements UserInfoRecycleView.ItemClickListener,BasketItemsRecycleView.ItemClickListener {

    InternetRequests internetRequests = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);
    String id,name,contactNum,address,area,biography,itemsNeeded,userID,donateeID,photo;
    ANImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.view_donatee_info);

        TextView title = findViewById(R.id.textView28);
        TextView nameDisplay = findViewById(R.id.textView36);
        TextView contactNumDisplay = findViewById(R.id.textView37);
        TextView addressDisplay = findViewById(R.id.textView38);
        TextView areaDisplay = findViewById(R.id.textView39);
        TextView biographyDisplay = findViewById(R.id.textView40);
        TextView helpDisplay = findViewById(R.id.textView30);
        userID = getIntent().getStringExtra("userID");
        donateeID = getIntent().getStringExtra("DonateeID");

        // Set user info
        processJSON(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonateeInfo.php?u="+donateeID));
        title.setText(name + "'s Information");
        nameDisplay.setText(name);
        contactNumDisplay.setText(contactNum);
        addressDisplay.setText(address);
        areaDisplay.setText(area);
        biographyDisplay.setText(biography);
        helpDisplay.setText("Help " + name + " out");

        imageView = findViewById(R.id.imageView14);
        if(photo.equals("default")) {
            imageView.setImageUrl("https://lamp.ms.wits.ac.za/home/s2173638/upload_images/default.png");
        }else{
            imageView.setImageUrl("https://lamp.ms.wits.ac.za/home/s2173638/upload_images/" + userID + ".jpeg");
        }
    }

    public void processJSON(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                name=item.getString("NAME");
                contactNum=item.getString("CONTACT_NUM");
                address=item.getString("ADDRESS");
                area=item.getString("AREA");
                biography=item.getString("BIOGRAPHY");
                itemsNeeded=item.getString("ITEMS_NEEDED");
                photo = item.getString("PHOTO");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }

    public void Donate(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonationScreen.class);
            intent.putExtra("userID", userID);
            intent.putExtra("donateeID", donateeID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBackPressed(){
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonateScreen.class);
            intent.putExtra("userID", getIntent().getStringExtra("userID"));
            intent.putExtra("donateeID", donateeID);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "Network unavailable, logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            }
    }
}
