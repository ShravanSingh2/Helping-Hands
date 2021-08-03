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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DonateeBasket extends AppCompatActivity implements  BasketItemsRecycleView2.ItemClickListener{

    String userID,itemID;
    int index;
    BasketItemsRecycleView2 adapter;
    ArrayList<String> itemIDArray = new ArrayList<>();
    ArrayList <String> itemNameArray = new ArrayList<>();
    ArrayList <String> OrigQuantityArray = new ArrayList<>();
    ArrayList <String> quantityArray = new ArrayList<>();
    ArrayList <String> dateArray = new ArrayList<>();
    RecyclerView recyclerView;
    InternetRequests webRequest = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);
    TextView numberDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.donatee_basket);

        userID = getIntent().getStringExtra("userID");
        getUserBasket();
        recyclerView = findViewById(R.id.recycler_4);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        adapter = new BasketItemsRecycleView2(this, itemNameArray,quantityArray,dateArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        numberDisplay = findViewById(R.id.textView52);
    }

    public void processJSON(String json){
        try {
            JSONArray all = new JSONArray(json);
            itemNameArray.clear();
            itemIDArray.clear();
            quantityArray.clear();
            dateArray.clear();
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                itemIDArray.add(item.getString("ITEM_ID"));
                itemNameArray.add(item.getString("ITEM_NAME"));
                quantityArray.add(item.getString("QUANTITY"));
                OrigQuantityArray.add(item.getString("QUANTITY"));
                dateArray.add(item.getString("DATE_ADDED"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getUserBasket(){
        processJSON(webRequest.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonateeBasket.php?BasketID="+ userID ));
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



    public void updateQuantity(){
        for(int i=0;i<quantityArray.size();i++){
            int sum = Integer.parseInt(OrigQuantityArray.get(i))-Integer.parseInt(quantityArray.get(i));
            itemID = itemIDArray.get(i);
            webRequest.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/updateDonateeQuantity.php?u=" + userID + "&i=" + itemID + "&q=" + -sum);
            OrigQuantityArray.set(i,quantityArray.get(i));
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        itemID=itemIDArray.get(position);
        index = position;
        numberDisplay.setText(quantityArray.get(position));
    }

    public void Plus(View view) {
        String temp = numberDisplay.getText().toString();
        if(temp.equals("")){

        }else {
            int num = Integer.parseInt(temp);
            num++;
            numberDisplay.setText(Integer.toString(num));
            quantityArray.set(index, Integer.toString(num));
            recyclerView.setAdapter(adapter);
            adapter.notifyItemChanged(index);
        }
    }


    public void Minus(View view) {
        String temp = numberDisplay.getText().toString();
        if(temp.equals("")){

        }else {
            int num = Integer.parseInt(temp);
            num--;
            if (num >= 0) {
                numberDisplay.setText(Integer.toString(num));
                quantityArray.set(index, Integer.toString(num));
                recyclerView.setAdapter(adapter);
                adapter.notifyItemChanged(index);
            } else {
                Toast.makeText(this, "Number of items has to be greater than 0", Toast.LENGTH_SHORT);
            }
        }
    }

    public void doSave(View view) {
        if(networkConnection.isNetworkAvailable()) {
            updateQuantity();
            Toast.makeText(this, "Items updated successfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void AddItem(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, AddDonateeItem.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }
}
