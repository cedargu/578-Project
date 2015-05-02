package com.tonyg.trojanow;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class UserPostActivity extends ListActivity {

    private List<Map<String,String>> listOfMaps;
    private SimpleAdapter adapter = null;
    private String feedUrl = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/getUserPosts?username=";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_post);
        SharedPreferences share = super.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        TextView selectedUser = (TextView) findViewById(R.id.selected_username);
        String selectedUserName = share.getString("selectedUser","null");
        selectedUser.setText(selectedUserName + "'s Post");
        feedUrl += selectedUserName;
        getUserPosts();
    }


    private void getUserPosts() {
        RequestQueue rq = Volley.newRequestQueue(this);
        listOfMaps = new ArrayList<Map<String,String>>();
        adapter = new SimpleAdapter(this,listOfMaps,R.layout.listview_row2,
                new String[]{"content","datetime","acceleration"},
                new int[]{R.id.content2,R.id.datetime2,R.id.acceleration2});

//        listView.setAdapter(adapter);
        setListAdapter(adapter);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, feedUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("posts");
                            for (int i=0; i<jsonArray.length(); i++) {
                                Map<String,String> map = new HashMap<String,String>();

                                if (jsonArray.getJSONObject(i).getString("anonymous").equals("false")) {

                                    map.put("content", jsonArray.getJSONObject(i).getString("content"));
                                    map.put("datetime", jsonArray.getJSONObject(i).getString("datetime"));

                                    if (jsonArray.getJSONObject(i).has("acceleration") && !jsonArray.getJSONObject(i).getString("acceleration").equals("null")) {
                                        map.put("acceleration", "acceleration:  " + jsonArray.getJSONObject(i).getString("acceleration") + "m/s^2");
                                    }
                                }
                                listOfMaps.add(map);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UserPostActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(jsonRequest);
    }
}
