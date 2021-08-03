package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DonateScreen extends AppCompatActivity implements ResourceItemsRecycleView.ItemClickListener, AdapterView.OnItemSelectedListener{

    ResourceItemsRecycleView adapter;
    RecyclerView recyclerView;
    String responseData,column,order;
    InternetRequests internetRequests = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);
    ArrayList<String> idArray = new ArrayList<String>();
    ArrayList<String> nameArray = new ArrayList<>();
    ArrayList<String> locationArray = new ArrayList<>();
    ArrayList<String> itemsNeededArray = new ArrayList<>();
    ArrayList<String> photoArray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.donate_screen);
        responseData= internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonatees.php");
        processJSON(responseData);
        order="ASC";
        column = "Name";

        recyclerView = findViewById(R.id.recycler_2);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        adapter = new ResourceItemsRecycleView(this, nameArray,locationArray,itemsNeededArray,idArray,photoArray);
        adapter.setClickListener( this);
        recyclerView.setAdapter(adapter);

        Spinner spinner = findViewById(R.id.spinner3);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Sort,R.layout.donate_spinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    public void processJSON(String json){
        try {
            JSONArray all = new JSONArray(json);
            idArray.clear();
            nameArray.clear();
            locationArray.clear();
            itemsNeededArray.clear();
            photoArray.clear();
            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                nameArray.add(item.getString("NAME"));
                locationArray.add(item.getString("AREA"));
                itemsNeededArray.add(item.getString("ITEMS_NEEDED"));
                idArray.add(item.getString("USER_ID"));
                photoArray.add(item.getString("PHOTO"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, ViewDonateeInfo.class);
            intent.putExtra("userID", getIntent().getStringExtra("userID"));
            intent.putExtra("DonateeID", idArray.get(position));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String sort = parent.getItemAtPosition(position).toString();
        if(sort.equals("Name")){
            column = "NAME";
        }else{
            if(sort.equals("Area")){
                column = "AREA";
            }else{
                column = "ITEMS_NEEDED";
            }
        }
        processJSON(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/sort_request.php?column="+column+"&order="+order));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onBackPressed(){
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonaterMenu.class);
            intent.putExtra("userID", getIntent().getStringExtra("userID"));
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    public void doSortAsc(View view) {
        if(networkConnection.isNetworkAvailable()) {
            order = "ASC";
            processJSON(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/sort_request.php?column=" + column + "&order=" + order));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{

            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void doSortDesc(View view) {
        if(networkConnection.isNetworkAvailable()) {
            order = "DESC";
            processJSON(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/sort_request.php?column=" + column + "&order=" + order));
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }else{

            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }
}
