package com.example.helpinghands;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddDonaterItem extends AppCompatActivity implements BasketItemsRecycleView.ItemClickListener, AdapterView.OnItemSelectedListener {

    BasketItemsRecycleView adapter;
    ArrayList<String> item_ID = new ArrayList<String>();
    ArrayList<String> item_Name = new ArrayList<>();
    ArrayList<String> users_Needed = new ArrayList<>();
    EditText amount;
    String quantity,itemID,userID,formattedDate;
    int index;
    InternetRequests internetRequests = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.add_donater_item);

        amount = findViewById(R.id.editText6);
        processJSON(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/get_resources.php"));
        userID = getIntent().getStringExtra("userID");
        EditText amount = findViewById(R.id.editText6);
        amount.setVisibility(View.INVISIBLE);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        adapter = new BasketItemsRecycleView(this, item_ID,item_Name,users_Needed);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //set up spinner
        Spinner spinner = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.NumberArray,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemClick(View view, int position) {
        index = position;
        itemID = item_ID.get(position);
    }

    public void processJSON(String json){
        try {
            JSONArray all = new JSONArray(json);
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                item_ID.add(item.getString("ITEM_ID"));
                item_Name.add(item.getString("ITEM_NAME"));
                users_Needed.add(item.getString("USERS_NEEDED"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ImageView customAmount = findViewById(R.id.imageView5);

        quantity = parent.getItemAtPosition(position).toString();
        if(quantity.equals("Custom Amount")){
            customAmount.setY(1400);
            amount.setVisibility(View.VISIBLE);
        }else{
            amount.setVisibility(View.INVISIBLE);
            customAmount.setY(1300);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addItem(){
        if(quantity.equals("Custom Amount")){
            quantity = amount.getText().toString();
        }
        if(quantity.equals("")){
            Toast.makeText(this,"Please enter a valid quantity",Toast.LENGTH_SHORT).show();
        }else {
            internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/updateUserBasket.php?u=" + userID + "&i=" + itemID + "&q=" + quantity + "&d='" + formattedDate + "'");
            int updatedValue = Integer.parseInt(users_Needed.get(index)) - Integer.parseInt(quantity);
            users_Needed.set(index, Integer.toString(updatedValue));
        }
    }

    public void onBackPressed(){
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonatorBasket.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    public void AddItem(View view) {
        if(networkConnection.isNetworkAvailable()) {
            if (!item_ID.equals(null)) {
                if (!quantity.equals(null)) {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    formattedDate = df.format(c);
                    addItem();
                    Toast.makeText(this, "Item Added Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please select a valid quantity", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please select an item from the list", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }
}
