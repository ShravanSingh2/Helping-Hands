package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewDonations extends AppCompatActivity implements TransactionRecyclerView.ItemClickListener{

    InternetRequests internetRequests = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);
    String userID;
    ArrayList<String> DonaterName = new ArrayList<>();
    ArrayList<String> DateArray = new ArrayList<String>();
    ArrayList<String> ItemArray = new ArrayList<String>();
    ArrayList<String> QuantityArray = new ArrayList<String>();
    ArrayList<String> IDArray = new ArrayList<String>();
    ArrayList<String> ContactNumArray = new ArrayList<String>();
    ArrayList<String> EmailArray = new ArrayList<String>();
    RecyclerView recyclerView;
    TransactionRecyclerView adapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_donations);
        userID = getIntent().getStringExtra("userID");
        getTransactions(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getTransactions.php?id=" + userID));

        //Initialize DonateeRequest RecyclerView
        recyclerView = findViewById(R.id.recycler_6);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        adapter = new TransactionRecyclerView(this, DonaterName,ItemArray,QuantityArray,DateArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public void getTransactions(String json) {
        try {
            JSONArray all = new JSONArray(json);
            Log.d("JSON","HERE " + json);
            for (int i = 0; i < all.length(); i++) {
                JSONObject item = all.getJSONObject(i);
                DonaterName.add(item.getString("NAME"));
                ContactNumArray.add(item.getString("CONTACT_NUM"));
                EmailArray.add(item.getString("EMAIL"));
                ItemArray.add(item.getString("ITEM_NAME"));
                DateArray.add(item.getString("DATE"));
                QuantityArray.add(item.getString("QUANTITY"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
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

    @Override
    public void onItemClick(View view, int position) {
            Intent intent = new Intent(this, ViewTransaction.class);
            intent.putExtra("userID",userID);
            intent.putExtra("donaterName", DonaterName.get(position));
            intent.putExtra("itemName", ItemArray.get(position));
            intent.putExtra("date", DateArray.get(position));
            intent.putExtra("quantity", QuantityArray.get(position));
            intent.putExtra("email", EmailArray.get(position));
            intent.putExtra("contactNum", ContactNumArray.get(position));
            startActivity(intent);
    }
}
