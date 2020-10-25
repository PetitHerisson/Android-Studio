package com.example.concertapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private EditText editTextSearch;
    private ImageView imageViewSearch;
    public static final String EXTRA_MESSAGE = "com.example.concertapp.MESSAGE";
    private String coordinates;
    private ImageView imageViewIcon;
    private TextView textViewTemperature;
    private TextView textViewExtra;
    private TextView textViewDescription;
    private TextView textViewLat;
    private TextView textViewLng;
    private static final String TAG = "Main Activity";
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mQueue = Volley.newRequestQueue(this);
        test = new TextView(this);

        imageViewIcon = findViewById(R.id.imageViewIcon);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewExtra = findViewById(R.id.textViewExtra);
        textViewDescription = findViewById(R.id.textViewDescription);
        textViewLat = findViewById(R.id.textViewlat);
        textViewLng = findViewById(R.id.textViewlng);

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
        String baseUrl = "https://api.openweathermap.org/data/2.5/weather?q=";
        String apiKey = "&appid=a79ed0c505017f78d706fd5d799643a6";
        String city = editTextSearch.getText().toString();
        String url = baseUrl + city + apiKey;
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject coord = response.getJSONObject("coord");
                            String lng = coord.getString("lon");
                            String lat = coord.getString("lat");
                            coordinates = lat + "/" + lng;
                            String description = response.getJSONArray("weather")
                                    .getJSONObject(0).getString("description");
                            String icon = response.getJSONArray("weather")
                                    .getJSONObject(0).getString("icon");
                            String iconUrl = "http://openweathermap.org/img/w/" + icon + ".png";
//                            String iconUrl = "https://www.yogajournal.com/.image/t_share/MTUxMDUxNDQ2NDQyMjcyNzA5/fearless.jpg";
                            String wind = "Wind: " + response.getJSONObject("wind").getString("speed");
                            String humidity = "Humidity: " + response.getJSONObject("main").getString("humidity");
                            String pressure = "Pressure: " + response.getJSONObject("main").getString("pressure");
                            String extra = wind + "\n" + humidity + "\n" + pressure;
                            Double temp = Double.parseDouble(response.getJSONObject("main").getString("temp")) - 273.15;
                            Picasso.get().load(iconUrl).into(imageViewIcon);
                            Log.d(TAG, iconUrl);

                            textViewDescription.setText(description);
                            textViewExtra.setText(extra);
                            textViewTemperature.setText(temp.toString());
                            textViewLat.setText(lat);
                            textViewLng.setText(lng);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    public void showLocation(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(EXTRA_MESSAGE, coordinates);
        startActivity(intent);
    }
}