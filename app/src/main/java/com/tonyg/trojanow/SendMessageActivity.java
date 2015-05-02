package com.tonyg.trojanow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URLEncoder;


public class SendMessageActivity extends Activity {
    private TextView toUser;
    private String toUserName;
    private EditText messageTxt;
    private Button sendBtn;
    private String url = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/addMessage?to=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_message);

        toUser = (TextView) findViewById(R.id.to_username);
        messageTxt = (EditText) findViewById(R.id.message_txt);
        sendBtn = (Button) findViewById(R.id.send_btn);

        final SharedPreferences share = super.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);
        toUserName = share.getString("selectedUser","null");
        toUser.setText("Send Message to " + toUserName);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = url + toUserName + "&from=" + share.getString("username","null") + "&message=" + URLEncoder.encode(messageTxt.getText().toString());
                sendMessage();
            }
        });
    }


    private void sendMessage() {

        RequestQueue rq = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // initialize toast message
                        Toast toast_msg = Toast.makeText(SendMessageActivity.this, response, Toast.LENGTH_SHORT);
                        toast_msg.show();

                        if (response.equals("send")) {

                            //post success, goto PostWall
                            Intent intent = new Intent(SendMessageActivity.this, TabViewActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SendMessageActivity.this, "error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(stringRequest);
    }


}
