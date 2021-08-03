package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopDonaters extends AppCompatActivity implements UserInfoRecycleView.ItemClickListener{

    InternetRequests internetRequests = new InternetRequests();
    ArrayList<String> NameArray = new ArrayList<String>();
    ArrayList<String> quantityArray = new ArrayList<>();
    RecyclerView recyclerView;
    UserInfoRecycleView adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_top_donaters);

        //Initialize User Basket RecyclerView
        processDonaters(internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonaters.php"));
        recyclerView = findViewById(R.id.recycler_7);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(mDividerItemDecoration);
        adapter = new UserInfoRecycleView(this, NameArray, quantityArray);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    public void processDonaters(String json) {
        try {
            JSONArray all = new JSONArray(json);
            NameArray.clear();
            quantityArray.clear();
            for (int i = 0; i < all.length(); i++) {
                JSONObject item = all.getJSONObject(i);
                NameArray.add(item.getString("NAME"));
                quantityArray.add(item.getString("ITEMS_DONATED"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}
