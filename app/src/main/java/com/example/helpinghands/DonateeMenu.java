package com.example.helpinghands;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.UploadProgressListener;
import com.androidnetworking.widget.ANImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DonateeMenu extends AppCompatActivity {

    int SELECT_PHOTO = 1;
    Uri uri;
    String userID;
    TextView name_display;
    InternetRequests webRequest = new InternetRequests();
    NetworkConnection networkConnection = new NetworkConnection(this);
    ANImageView imageView;
    String URL_UPLOAD = "https://lamp.ms.wits.ac.za/home/s2173638/upload.php";
    String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.donatee_menu);

        userID = getIntent().getStringExtra("userID");
        String name = getName(webRequest.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/getDonateeName.php?u=" + userID));
        name_display = findViewById(R.id.textView47);
        name_display.setText("Welcome " + name);

        imageView = findViewById(R.id.imageView14);
        if(imgUrl.equals("default")) {
            imageView.setImageUrl("https://lamp.ms.wits.ac.za/home/s2173638/upload_images/default.png");
        }else{
            imageView.setImageUrl("https://lamp.ms.wits.ac.za/home/s2173638/upload_images/" + userID + ".jpeg");
        }
    }

    public String getName(String json){
        String name="";
        try {
            JSONArray all = new JSONArray(json);

            for (int i=0; i<all.length(); i++){
                JSONObject item=all.getJSONObject(i);
                name = item.getString("NAME");
                imgUrl = item.getString("PHOTO");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    public void onBackPressed(){
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, Login.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, logging out...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
    }

    public void doAddImage(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Bitmap bitmap = null;
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(bitmap!=null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            byte[] bArray = bos.toByteArray();
            final String encodedImage = Base64.encodeToString(bArray, Base64.DEFAULT);
            Toast.makeText(this, "Uploading Profile Picture...", Toast.LENGTH_SHORT).show();

            StringRequest request = new StringRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(DonateeMenu.this, "Profile picture updated!", Toast.LENGTH_SHORT).show();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(DonateeMenu.this, "Network error, please try again later", Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("id", userID);
                    params.put("photo", encodedImage);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(request);
        }
    }

    public void doEditRequests(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonateeBasket.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void ViewDonations(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, ViewDonations.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public void Settings(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, DonateeSettings.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }

    }

    public void doEditBiography(View view) {
        if(networkConnection.isNetworkAvailable()) {
            Intent intent = new Intent(this, EditDonateeBio.class);
            intent.putExtra("userID", userID);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }
}


