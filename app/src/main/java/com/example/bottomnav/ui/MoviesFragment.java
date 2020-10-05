package com.example.bottomnav.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.bottomnav.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MoviesFragment extends Fragment {

    private RequestQueue mQueue;
    private EditText et_movieName;
    private TextView tv_movieInfo;
    private ImageView iv_search;
    private ScrollView sv_overview;
    private LinearLayout linearLayout_post;
    private TextView tv_overview;
    private ImageView imageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_movies, container, false);

        et_movieName = root.findViewById(R.id.et_movieName);
        tv_movieInfo = root.findViewById(R.id.tv_movieInfo);
        sv_overview = root.findViewById(R.id.sv_overview);
        tv_overview = new TextView(this.getContext());
        linearLayout_post = root.findViewById(R.id.linearLayout_post);
        imageView = new ImageView(this.getContext());
        iv_search = root.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mQueue = Volley.newRequestQueue(v.getContext());
                jsonParse();
            }
        });
        return root;
    }

    private void jsonParse() {
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
                            double popularity = results.getDouble("popularity");
                            String releaseDate = results.getString("release_date");
                            String overview = "Overview: " + "\n" + results.getString("overview");
                            String poster_path = results.getString("poster_path");
                            String poster_url = "https://image.tmdb.org/t/p/w500" + poster_path;
                            Picasso.get().load(poster_url).into(imageView);
                            linearLayout_post.removeAllViews();
                            linearLayout_post.addView(imageView);

                            tv_movieInfo.setText("Title: " + title + "\n" +
                                    "Vote Average: " + voteAverage + "\n" +
                                    "Popularity: " + popularity + "\n" +
                                    "Release Date: " + releaseDate + "\n"
                            );
                            tv_overview.setText(overview);
                            tv_overview.setTextSize(18);
                            sv_overview.removeAllViews();
                            sv_overview.addView(tv_overview);
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