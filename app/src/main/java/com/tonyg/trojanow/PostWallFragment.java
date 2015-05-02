package com.tonyg.trojanow;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
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
public class PostWallFragment extends ListFragment {
    private View rootView;
    private ListView listView = null;
    private List<Map<String,String>> listOfMaps;
    private SimpleAdapter adapter = null;
    private String feedUrl = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/getPosts";

    public PostWallFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_post_wall, container, false);

        // Inflate the layout for this fragment
        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listView = (ListView) rootView.findViewById(android.R.id.list);

        final SwipeRefreshLayout swipeView = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_layout_frag);
        swipeView.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_blue_light, android.R.color.holo_green_light, android.R.color.holo_green_light);
        swipeView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeView.setRefreshing(true);

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

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
//        System.out.println(position);
//    }

    private void refresh() {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        listOfMaps = new ArrayList<Map<String,String>>();
        adapter = new SimpleAdapter(getActivity(),listOfMaps,R.layout.listview_row,
                new String[]{"owner","content","datetime","acceleration"},
                new int[]{R.id.owner,R.id.content,R.id.datetime,R.id.acceleration});

//        listView.setAdapter(adapter);
        setListAdapter(adapter);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, feedUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
//                        try {
//                            JSONArray videos = response.getJSONObject("data").getJSONArray("items");
//                            for (int i=0; i<videos.length(); i++) {
//                                videoArray.add(videos.getJSONObject(i).getString("title"));
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }

                        try {
                            JSONArray jsonArray = response.getJSONArray("posts");
                            for (int i=0; i<jsonArray.length(); i++) {
                                Map<String,String> map = new HashMap<String,String>();

                                if (jsonArray.getJSONObject(i).getString("anonymous").equals("false"))
                                    map.put("owner", jsonArray.getJSONObject(i).getString("owner"));
                                else
                                    map.put("owner", "anonymous");
                                map.put("content", jsonArray.getJSONObject(i).getString("content"));
                                map.put("datetime", jsonArray.getJSONObject(i).getString("datetime"));

                                if (jsonArray.getJSONObject(i).has("acceleration") && !jsonArray.getJSONObject(i).getString("acceleration").equals("null")) {
                                    map.put("acceleration", "acceleration:  " + jsonArray.getJSONObject(i).getString("acceleration"));
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
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(jsonRequest);
    }

}
