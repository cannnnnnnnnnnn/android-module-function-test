package com.example.http;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.StringRequestListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AndroidNetworking.initialize(getApplicationContext());

        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                JSONObject ssid = new JSONObject();
                JSONObject pass = new JSONObject();
                try {
                    ssid.put("ssid","123");
                    pass.put("pass","123");
                    ssid.put("123","123");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                array.put(ssid);
                array.put(pass);
                AndroidNetworking.post("http://192.168.206.108/rfid_data")
//                        .addBodyParameter("info","123456")
//                        .addJSONArrayBody(array)
                        .addJSONObjectBody(ssid)
//                        .addStringBody("This is my data")
                        .build()
                        .getAsString(new StringRequestListener() {
                            @Override
                            public void onResponse(String response) {

                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });

            }
        });
    }
}