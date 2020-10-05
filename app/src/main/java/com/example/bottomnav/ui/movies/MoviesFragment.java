package com.example.bottomnav.ui.movies;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottomnav.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MoviesFragment extends Fragment {

    private MoviesViewModel moviesViewModel;
    private RequestQueue mQueue;
    private EditText et_movieName;
    private TextView tv_movieInfo;
    private ImageView iv_search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        moviesViewModel =
                ViewModelProviders.of(this).get(MoviesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_movies, container, false);

        et_movieName = root.findViewById(R.id.et_movieName);
        tv_movieInfo = root.findViewById(R.id.tv_movieInfo);
        iv_search = root.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mQueue = Volley.newRequestQueue(v.getContext());
                jsonParse();
                tv_movieInfo.append("Hi there!");
            }
        });
        return root;
    }

    private void jsonParse() {
        tv_movieInfo.append("Hi from json!");
        String baseUrl = "https://api.themoviedb.org/3/search/movie?api_key=779794aaf1c8685d78e35a03f57b923f&query=";
        String movieName = et_movieName.getText().toString();
        String url = baseUrl + movieName;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("results");
                            JSONObject results = jsonArray.getJSONObject(0);
                            String title = results.getString("title");
                            int voteAverage = results.getInt("vote_average");
                            int popularity = results.getInt("popularity");
                            String releaseDate = results.getString("release_date");
                            String overview = results.getString("overview");
                            tv_movieInfo.setText("Title: " + title + "\n" +
                                    "Vote Average: " + voteAverage + "\n" +
                                    "Popularity: " + popularity + "\n" +
                                    "Release Date: " + releaseDate + "\n" +
                                    "Overview: " + overview + "\n\n"
                            );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }
}