package com.tonyg.trojanow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileActivity extends Activity {
    ImageButton photo_btn = null;
    Button mypost_btn = null;
    Button logout_btn = null;
    TextView myUserName;
    TextView myEmail;
    TextView myGender;
    String my_username;
    SharedPreferences share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        share = super.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

        myUserName = (TextView) findViewById(R.id.my_username);
        myEmail = (TextView) findViewById(R.id.my_email);
        myGender = (TextView) findViewById(R.id.my_gender);
        photo_btn = (ImageButton) findViewById(R.id.photo_btn);
        mypost_btn = (Button) findViewById(R.id.mypost);
        logout_btn = (Button) findViewById(R.id.logout);


        my_username = share.getString("username","null");

        getMyInfo();



//        photo_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changePhoto();
//            }
//        });

        mypost_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MyPostsActivity.class);
                startActivity(intent);
            }
        });

        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = share.edit();
                editor.remove("username");
                editor.remove("password");
                editor.commit();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void getMyInfo() {
        RequestQueue rq = Volley.newRequestQueue(this);
        String url = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/getUserInfo?username=" + my_username;
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String my_email = response.getString("email");
                            String my_gender = response.getString("gender");

                            myUserName.setText(my_username);
                            myEmail.setText(my_email);
                            myGender.setText(my_gender);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(jsonRequest);
    }
}
