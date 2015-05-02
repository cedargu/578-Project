package com.tonyg.trojanow;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

public class CreatePostActivity extends Activity implements SensorEventListener {
//    private Post myPost = null;
    private Button post_btn = null;
    private EditText post_txt = null;
    private CheckBox anonymous = null;
    private String isAnonymous = "false";
    private CheckBox shareAcc = null;
    private String acceleration = "null";
    private TextView accValue;
    private SensorManager mSensorManager;
    private Sensor mAcceleration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

//        myPost = new Post();
        post_btn = (Button) findViewById(R.id.post_btn);
        post_txt = (EditText) findViewById(R.id.post_txt);
        anonymous = (CheckBox) findViewById(R.id.anonymous);
        shareAcc = (CheckBox) findViewById(R.id.shareAcc);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accValue = (TextView) findViewById(R.id.acc_value);

        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (anonymous.isChecked()) {
                    isAnonymous = "true";
                }
                createPost();
//                myPost.setText(post_txt.getText().toString());
//                PostFunctions postFunc = new PostFunctions(myPost);
//                String msg = postFunc.postToServer();
//                Toast toast = Toast.makeText(CreatePostActivity.this, msg, Toast.LENGTH_LONG);
//                toast.show();

            }
        });
    }


    @Override
    public final void onSensorChanged(SensorEvent event) {
        acceleration = Float.toString(event.values[0]);
//        if (shareAcc.isChecked())
//            mSensorManager.unregisterListener(this);
//        else
//            mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
        accValue.setText(acceleration);
        System.out.println(acceleration);
        Log.d("debug", acceleration);
    }

    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        // Register a listener for the sensor.
        super.onResume();
        mSensorManager.registerListener(this, mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void createPost() {
        SharedPreferences share = super.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);

        String url = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/addPost";
        url = url + "?owner=" + share.getString("username","null") + "&anonymous=" + isAnonymous + "&content=" + URLEncoder.encode(post_txt.getText().toString())
                + "&acceleration=" + acceleration;
        Log.d("debug",url);
        RequestQueue rq = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // initialize toast message
                        Toast toast_msg = Toast.makeText(CreatePostActivity.this, response, Toast.LENGTH_SHORT);
                        toast_msg.show();

                        if (response.equals("posted")) {

                            //post success, goto PostWall
                            Intent intent = new Intent(CreatePostActivity.this, TabViewActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CreatePostActivity.this, "error: "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(stringRequest);
    }
}
