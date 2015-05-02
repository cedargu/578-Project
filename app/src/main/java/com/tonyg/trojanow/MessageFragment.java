package com.tonyg.trojanow;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tonyg.trojanow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends ListFragment {

    private View rootView;
    private ListView listView = null;
    private List<Map<String,String>> listOfMaps;
    private SimpleAdapter adapter = null;
    private String feedUrl = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/getMessages?username=";
    private SharedPreferences share;
    private String selectedUser;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_message, container, false);

        return rootView;


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        share = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        feedUrl += share.getString("username","null");
        listView = (ListView) rootView.findViewById(android.R.id.list);

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout_frag2);
        swipeView.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                refresh();
                ( new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeView.setRefreshing(false);

                    }
                }, 2000);
            }
        });

        refresh();
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
        Map<String,String> map;


        map =  (Map<String,String>)getListView().getItemAtPosition(position);
        selectedUser = map.get("from");
//        Toast.makeText(getActivity(), selectedUser, Toast.LENGTH_SHORT).show();

        share = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        getUserInfo();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        }, 500);

    }

    private void getUserInfo() {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        String url = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/getUserInfo?username=" + selectedUser;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String email = response.getString("email");
                            String gender = response.getString("gender");

                            SharedPreferences.Editor editor = share.edit();
                            editor.putString("selectedUser", selectedUser);
                            editor.putString("selectedUserEmail", email);
                            editor.putString("selectedUserGender", gender );

                            editor.commit();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(jsonRequest);
    }

    private void refresh() {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        listOfMaps = new ArrayList<Map<String,String>>();
        adapter = new SimpleAdapter(getActivity(),listOfMaps,R.layout.listview_row3,
                new String[]{"from", "message", "datetime"},
                new int[]{R.id.sender, R.id.message, R.id.datetime});

//        listView.setAdapter(adapter);
        setListAdapter(adapter);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, feedUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            JSONArray jsonArray = response.getJSONArray("messages");
                            for (int i=0; i<jsonArray.length(); i++) {
                                Map<String,String> map = new HashMap<String,String>();

                                map.put("from", jsonArray.getJSONObject(i).getString("from"));
                                map.put("message", jsonArray.getJSONObject(i).getString("message"));
                                map.put("datetime", jsonArray.getJSONObject(i).getString("time"));

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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(jsonRequest);
    }
}
