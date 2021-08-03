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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DonationScreen extends AppCompatActivity implements UserInfoRecycleView.ItemClickListener{

    String userID,donateeID,itemID,formattedDate;
    int donateeItemID;
    InternetRequests internetRequests = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);
    ArrayList<String> itemNameArray = new ArrayList<String>();
    ArrayList<String> quantityArray = new ArrayList<>();
    ArrayList<String> origQuantityArray = new ArrayList<>();
    ArrayList<Integer> donateQuantityArray = new ArrayList<>();
    ArrayList<String> itemNameArray2 = new ArrayList<String>();
    ArrayList<String> quantityArray2 = new ArrayList<>();
    ArrayList<String> itemIDArray = new ArrayList<>();
    RecyclerView recyclerView, recyclerView2;
    UserInfoRecycleView adapter, adapter2;
    TextView numberDisplay,name_display;
    ImageView plusButton,minusButton;
    int index,num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_donation_screen);

        userID = getIntent().getStringExtra("userID");
        donateeID = getIntent().getStringExtra("donateeID");
        setDate();

        String name = getName(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonateeName.php?u=" + donateeID));

        name_display = findViewById(R.id.textView67);
        name_display.setText(name + "'s Requests");

        initializeDonateeRecyler();
        initializeDonaterRecycler();

        for(int i=0;i<quantityArray2.size();i++){
            donateQuantityArray.add(0);
        }

        numberDisplay = findViewById(R.id.textView69);
        plusButton = findViewById(R.id.imageView24);
        minusButton = findViewById(R.id.imageView25);
        plusButton.setVisibility(View.INVISIBLE);
        minusButton.setVisibility(View.INVISIBLE);
    }

    public void initializeDonateeRecyler(){
        //Initialize DonateeRequest RecyclerView
        processDonateeBasket(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonateeBasket.php?BasketID=" + donateeID));
        recyclerView = findViewById(R.id.recycler_3);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        adapter = new UserInfoRecycleView(this, itemNameArray, quantityArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public void initializeDonaterRecycler(){
        //Initialize User Basket RecyclerView
        processUserBasket(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/get_user_basket.php?BasketID=" + userID));
        recyclerView2 = findViewById(R.id.recycler_5);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        adapter2 = new UserInfoRecycleView(this, itemNameArray2, quantityArray2);
        adapter2.setClickListener(this);
        recyclerView2.setAdapter(adapter2);
    }

    public void processDonateeBasket(String json) {
        try {
            JSONArray all = new JSONArray(json);
            itemNameArray.clear();
            quantityArray.clear();
            for (int i = 0; i < all.length(); i++) {
                JSONObject item = all.getJSONObject(i);
                itemNameArray.add(item.getString("ITEM_NAME"));
                quantityArray.add(item.getString("QUANTITY"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void processUserBasket(String json) {
        try {
            JSONArray all = new JSONArray(json);
            itemNameArray2.clear();
            quantityArray2.clear();;
            origQuantityArray.clear();
            itemIDArray.clear();
            for (int i = 0; i < all.length(); i++) {
                JSONObject item = all.getJSONObject(i);
                itemNameArray2.add(item.getString("ITEM_NAME"));
                quantityArray2.add(item.getString("QUANTITY"));
                origQuantityArray.add(item.getString("QUANTITY"));
                itemIDArray.add(item.getString("ITEM_ID"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Plus(View view) {
            num++;
            donateQuantityArray.set(index, num);
            minusButton.setVisibility(View.VISIBLE);
            if (num >= Integer.parseInt(origQuantityArray.get(index))) {
                plusButton.setVisibility(View.INVISIBLE);
            }
            numberDisplay.setText(Integer.toString(num));
            String updatedDonaterQuantity = Integer.toString(Integer.parseInt(quantityArray2.get(index)) - 1);
            quantityArray2.set(index, updatedDonaterQuantity);
            String updatedDonateeQuantity = Integer.toString(Integer.parseInt(quantityArray.get(donateeItemID)) - 1);
            quantityArray.set(donateeItemID, updatedDonateeQuantity);
            if(updatedDonateeQuantity.equals("0")){
                plusButton.setVisibility(View.INVISIBLE);
            }
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            recyclerView2.setAdapter(adapter2);
            adapter2.notifyItemChanged(index);
    }

    public void updateQuantity() {
        boolean flag=false;
        for(int i=0;i<donateQuantityArray.size();i++){
            if(donateQuantityArray.get(i)!=0){
                flag = true;
            }
        }
        if(flag) {
            for (int i = 0; i < quantityArray2.size(); i++) {
                String name = itemNameArray2.get(i);
                int positon = itemNameArray.indexOf(name);
                Log.d("positon", String.valueOf(positon));
                if (positon != -1) {
                    donateeItemID = Integer.parseInt(itemIDArray.get(i));
                    Log.d("donateeITEMID", String.valueOf(donateeItemID));
                    if(donateeItemID!=-1) {
                        int DonateeQuantity = -(donateQuantityArray.get(i));
                        Log.d("DonateeQuantity", String.valueOf(DonateeQuantity));
                        if (DonateeQuantity != 0) {
                            internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/updateDonateeBasket.php?u=" + donateeID + "&i=" + donateeItemID + "&q=" + DonateeQuantity + "&d='" + formattedDate+"'");
                            Log.d("response",internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/sendTransaction.php?u=" + userID + "&d=" + donateeID + "&i=" + donateeItemID + "&q=" + -DonateeQuantity + "&date=" + formattedDate));
                        }
                    }
                }
                String donaterItemID = itemIDArray.get(i);
                String DonaterQuantity = quantityArray2.get(i);
                internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/updateQuantity.php?u=" + userID + "&i=" + donaterItemID + "&q=" + DonaterQuantity);
            }
            donateQuantityArray.clear();
            for (int i = 0; i < quantityArray2.size(); i++) {
                donateQuantityArray.add(0);
            }
            initializeDonateeRecyler();
            initializeDonaterRecycler();
            numberDisplay.setText("");
            Toast.makeText(this, "Items donated succesfully", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "No items have been added", Toast.LENGTH_SHORT).show();
        }
    }

    public void Minus(View view) {
            num--;
            donateQuantityArray.set(index, num);
            if (num == 0) {
                minusButton.setVisibility(View.INVISIBLE);
            }
            plusButton.setVisibility(View.VISIBLE);
            numberDisplay.setText(Integer.toString(num));
            String updatedDonaterQuantity = Integer.toString(Integer.parseInt(quantityArray2.get(index)) + 1);
            quantityArray2.set(index, updatedDonaterQuantity);
            String updatedDonateeQuantity = Integer.toString(Integer.parseInt(quantityArray.get(donateeItemID)) + 1);
            quantityArray.set(donateeItemID, updatedDonateeQuantity);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            recyclerView2.setAdapter(adapter2);
            adapter2.notifyItemChanged(index);
    }

    public void setDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate = df.format(c);
    }

    @Override
    public void onItemClick(View view, int position) {
        try{
            num = donateQuantityArray.get(position);
            if(num>0){
                minusButton.setVisibility(View.VISIBLE);
            }
            if(num==0){
                minusButton.setVisibility(View.INVISIBLE);
            }
            if(num>=Integer.parseInt(origQuantityArray.get(position))){
                plusButton.setVisibility(View.INVISIBLE);
            }else {
                plusButton.setVisibility(View.VISIBLE);
            }
            numberDisplay.setText(Integer.toString(num));
            index = position;
            itemID=itemIDArray.get(position);
            numberDisplay.setVisibility(View.VISIBLE);
            getDonateeItemID();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void getDonateeItemID(){
        String name = itemNameArray2.get(index);
        donateeItemID = itemNameArray.indexOf(name);
        if(donateeItemID==-1){
            Toast.makeText(this,"You cannot donate this item as the donatee does not require it",Toast.LENGTH_SHORT).show();
            minusButton.setVisibility(View.INVISIBLE);
            plusButton.setVisibility(View.INVISIBLE);
            numberDisplay.setVisibility(View.INVISIBLE);
        }
    }

    public void onBackPressed(){
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, ViewDonateeInfo.class);
            intent.putExtra("userID", userID);
            intent.putExtra("DonateeID", donateeID);
            startActivity(intent);
        }else{

            Toast.makeText(this, "Network unavailable, logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
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

    public void Save(View view) {
        if(networkConnection.isNetworkAvailable()) {
            updateQuantity();
        }else{

            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }
}
