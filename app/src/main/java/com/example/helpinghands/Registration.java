package com.example.helpinghands;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String userType, responseData;
    NetworkConnection networkConnection = new NetworkConnection(this);
    String errorMessage;
    EditText username_display, password_display, email_address_display, contact_num_display, name_display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        Spinner spinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.userType, R.layout.myspinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        username_display = findViewById(R.id.editText3);
        password_display = findViewById(R.id.editText4);
        email_address_display = findViewById(R.id.editText5);
        contact_num_display = findViewById(R.id.editText12);
        name_display = findViewById(R.id.editText11);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        userType = parent.getItemAtPosition(position).toString();
        Log.d("user type",userType);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void registerUser(View view) {
        if(networkConnection.isNetworkAvailable()) {
            boolean validData = validateData();
            if (validData) {
                String resData;
                if (userType.equals("Donator")) {
                    resData = insertDonater(name_display.getText().toString(),
                            username_display.getText().toString(),
                            password_display.getText().toString(),
                            email_address_display.getText().toString(),
                            contact_num_display.getText().toString());
                    Log.d("REACHED", "XX");
                } else {
                    resData = insertDonatee(name_display.getText().toString(),
                            username_display.getText().toString(),
                            password_display.getText().toString(),
                            email_address_display.getText().toString(),
                            contact_num_display.getText().toString());
                    Log.d("REACHED", "DDD");
                }
                Log.d("responseData", resData);
                Log.d("heavies", String.valueOf(resData.equals("Y")));
                if (resData.equals("Y")) {
                    Toast.makeText(Registration.this, "User Created Successfully", Toast.LENGTH_SHORT).show();
                    name_display.setText("");
                    username_display.setText("");
                    password_display.setText("");
                    contact_num_display.setText("");
                    email_address_display.setText("");
                } else {
                    Toast.makeText(Registration.this, "Username already taken, please try again.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Registration.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Network unavailable, please try again later", Toast.LENGTH_SHORT).show();
        }
    }

    public String insertDonater(final String name, final String username, final String password, final String emailAddress, final String number) {
        InternetRequests internetRequests = new InternetRequests();
        String Data = internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/insertDonater.php?n=" + name + "&u=" + username + "&p=" + password
                + "&nu=" + number +"&e=" + emailAddress );
        return Data;
    }

    public String insertDonatee(final String name, final String username, final String password, final String emailAddress, final String number) {
        InternetRequests internetRequests = new InternetRequests();
        String Data = internetRequests.sendRequest("https://lamp.ms.wits.ac.za/home/s2173638/insertDonatee.php?n=" + name + "&u=" + username + "&p=" + password
                +  "&nu=" + number + "&e=" + emailAddress);
        return Data;
    }

    public boolean validateData() {
        if (!contact_num_display.getText().toString().equals("")) {
            if (!name_display.getText().toString().equals("")) {
                if (!username_display.getText().toString().equals("")) {
                    if (!password_display.getText().toString().equals("")) {
                        if (!email_address_display.getText().toString().equals("")) {
                            return true;
                        } else {
                            errorMessage = "Please enter in a valid email address";
                            return false;
                        }
                    } else {
                        errorMessage = "Please enter in a valid password";
                        return false;
                    }
                } else {
                    errorMessage = "Please enter in a valid username";
                    return false;
                }
            } else {
                errorMessage = "Please enter in a valid name";
                return false;
            }
        }else{
            errorMessage = "Please enter in a valid cellphone number";
            return false;
        }
    }

}
