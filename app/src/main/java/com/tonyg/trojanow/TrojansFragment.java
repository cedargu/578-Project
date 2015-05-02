package com.tonyg.trojanow;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tonyg.trojanow.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrojansFragment extends ListFragment {

    private View rootView;
    private ListView trojans_list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> nameList;
    private String selectedUser;
    private String url = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/getAllUsers";
    SharedPreferences share;

    public TrojansFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trojans, container, false);
        // Inflate the layout for this fragment

        share = getActivity().getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

        getAllUsers();

        return rootView;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        trojans_list = getListView();
//        trojans_list = (ListView) rootView.findViewById(R.id.frag_list);

        // moved code
//        share = getActivity().getSharedPreferences("userIfo", Activity.MODE_PRIVATE);
//
//        getAllUsers();


//        trojans_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                System.out.println(position);
//                Toast.makeText(getActivity(), "Stop Clicking me", Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//                startActivity(intent);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        trojans_list.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
//        super.onListItemClick(l, v, position, id);
        selectedUser = (String) getListView().getItemAtPosition(position);
//        Toast.makeText(getActivity(), selectedUser, Toast.LENGTH_SHORT).show();

        getUserInfo();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                startActivity(intent);
            }
        }, 500);

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//
//        // Make sure that we are currently visible
//        if (this.isVisible()) {
//            // Do your stuff here
//            Log.d("MyFragment","Visible");
//            if (!isVisibleToUser) {
//                Log.d("MyFragment", "Not visible");
//            }
//        }
//    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        System.out.println(position);
//        String chosenUser = nameList.get(position);
//        System.out.println(chosenUser);
//
//        SharedPreferences.Editor editor = share.edit();
//        editor.putString("chosenUser", chosenUser);
//        editor.commit();
//
//        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//        startActivity(intent);
//    }


    private void getAllUsers() {
        RequestQueue rq = Volley.newRequestQueue(getActivity());
//        adapter = new ArrayAdapter<String>(getActivity(), R.layout.fragment_trojans, nameList);
//        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameList);
//        setListAdapter(adapter);




        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            nameList = new ArrayList<String>();
                            for (int i=0; i<response.length(); i++) {
                                nameList.add(response.getString(i));
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, R.id.frag_list, nameList);
                        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, nameList);
                        setListAdapter(adapter);
//                        trojans_list.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
//                        System.out.println("success");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                        System.out.println(error.toString());
                    }
                });

        rq.add(jsonArrayRequest);
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
}
