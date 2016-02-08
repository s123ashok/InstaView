package com.example.sashok.instaview;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.widget.ListView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

import static android.text.format.DateUtils.MINUTE_IN_MILLIS;

public class InstaView extends AppCompatActivity {

    public static final String CLIENT_ID = "5e4bb8b442144e2cad975512543ecdb8";
    private ArrayList<Photo> photos;
    private InstagramPhotoAdapter photoAdapter;


    @Bind(R.id.lvPhotos)
    ListView lvPhotos;
    @Bind(R.id.swipeContainer)
    SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insta_view);
        photos = new ArrayList<>();
        photoAdapter = new InstagramPhotoAdapter(this, photos);


        //ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);
        ButterKnife.bind(this);
        lvPhotos.setAdapter(photoAdapter);

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                //fetchTimelineAsync(0);
                fetchPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        //fetchPhotos();

    }

    private void fetchPhotos() {
        /*
        https://api.instagram.com/v1/media/popular?client_id=5e4bb8b442144e2cad975512543ecdb8

        data is an array
        Type : data.type : image/video
        URL : data.images.standard_resolution.url
        Caption : data.caption.text

         */
        String url = "https://api.instagram.com/v1/media/popular?client_id=" + CLIENT_ID;
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            //success

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //JSON response is returned
                Log.i("Debug", response.toString());
                JSONArray itemArray = null;
                try {
                    itemArray = response.getJSONArray("data");
                    for (int i = 0; i < itemArray.length(); i++) {
                        JSONObject obj = itemArray.getJSONObject(i);

                        // if(i<10) {
                        Photo p = new Photo();
                        p.userName = obj.getJSONObject("user").getString("username");
                        p.caption = obj.getJSONObject("caption").getString("text");
                        p.imageUrl = obj.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        p.imageHeight = obj.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        p.likesCount = obj.getJSONObject("likes").getInt("count");
                        p.userImageUrl = obj.getJSONObject("user").getString("profile_picture");
                        String strTime = obj.getString("created_time");
                        long time = Long.parseLong(strTime) * 1000;
                        p.timestamp = (String) DateUtils.getRelativeTimeSpanString(time, System.currentTimeMillis(), MINUTE_IN_MILLIS);
                        p.commentsCount = Integer.parseInt(obj.getJSONObject("comments").getString("count"));
                        JSONArray commentArray = null;


                        commentArray = obj.getJSONObject("comments").getJSONArray("data");
                        p.comment1 = commentArray.getJSONObject(commentArray.length() - 1).getString("text");
                        p.comment2 = commentArray.getJSONObject(commentArray.length() - 2).getString("text");

                        photos.add(p);
                        //}
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                photoAdapter.notifyDataSetChanged();
            }


            //Failure

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
