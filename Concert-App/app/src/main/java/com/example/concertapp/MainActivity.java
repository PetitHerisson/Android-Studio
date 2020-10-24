package com.example.concertapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private EditText editTextSearch;
    private ImageView imageViewSearch;
    public static final String EXTRA_MESSAGE = "com.example.concertapp.MESSAGE";
    String coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(this);

        editTextSearch = findViewById(R.id.editTextSearch);
        imageViewSearch = findViewById(R.id.imageViewSearch);
        imageViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jsonParse();
            }
        });
    }

    private void jsonParse() {
        String baseUrl = "https://api.openweathermap.org/data/2.5/weather?q=London,uk&appid=a79ed0c505017f78d706fd5d799643a6";
        String searchKeyword = editTextSearch.getText().toString();
        String url = baseUrl + searchKeyword;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
    }

    public void showLocation(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(EXTRA_MESSAGE, coordinates);
        startActivity(intent);
    }
}