package com.tonyg.trojanow;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private EditText username = null;
    private EditText pwd_txt = null;
    private Button login_btn = null;
    private Button signup_btn = null;
    private TextView forget_pwd = null;
    boolean firstClick = true;
    boolean firstFocus = true;

    private boolean flag = false;
    SharedPreferences share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get widgets by id
        username = (EditText) findViewById(R.id.username);
        pwd_txt = (EditText) findViewById(R.id.pwd_txt);
        login_btn = (Button) findViewById(R.id.login_btn);
        signup_btn = (Button) findViewById(R.id.signup_btn);
//        forget_pwd = (TextView) findViewById(R.id.forget_pwd);
        // create forget password link
//        forget_pwd.setMovementMethod(LinkMovementMethod.getInstance());
        share = super.getSharedPreferences("userInfo",Activity.MODE_PRIVATE);

        autoLogin(share);
        // when first click, empty default prompt message
        username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (firstClick) {
                    username.setText("");
                    username.setTextColor(Color.BLACK);
                    firstClick = false;
                }
            }
        });

        // empty prompt message on first focus
        pwd_txt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && firstFocus) {
                    pwd_txt.setText("");
                    pwd_txt.setTextColor(Color.BLACK);
                    pwd_txt.setTransformationMethod(new PasswordTransformationMethod());
                    firstFocus = false;
                }
            }
        });


        // login button click event handler
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username.getText().toString(), pwd_txt.getText().toString());
            }

        });

        // sign up button click event handler
        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goto register page
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login(final String username, final String password) {
        //            new LoginTask().execute();
        RequestQueue rq = Volley.newRequestQueue(MainActivity.this);

//        final String url = "http://trojanow-env.elasticbeanstalk.com/?email=trojan&password=123";
        String url = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/login";
        url = url + "?username=" + username + "&password=" + password;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("true")) {
                            flag = true;
                        } else {
                            flag = false;
                        }

                        String msg = flag == true ? "Success" : "False";
                        // initialize toast message
                        Toast toast_msg = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
                        toast_msg.show();

                        if (msg.equals("Success")) {
                            // store username
                            SharedPreferences.Editor editor = share.edit();
                            editor.putString("username", username);
                            editor.putString("password", password);
                            editor.commit();
                            // login success, goto PostWall
                            Intent intent = new Intent(MainActivity.this, TabViewActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(stringRequest);
    }
    private void autoLogin(SharedPreferences share) {
        String username = share.getString("username","null");
        String password = share.getString("password","null");
        if (username != "null")
            login(username, password);
    }

//    class LoginTask extends AsyncTask<String,String,String> {
//
//        @Override
//        protected void onPreExecute() {
////            super.onPreExecute();
//            Toast toast_msg = Toast.makeText(MainActivity.this, "Logging in", Toast.LENGTH_SHORT);
//            toast_msg.show();
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
////            http://120.27.97.221:8080/TrojanNowServer/?email=tom@163.com&password=321
//                URL url = new URL("http", "trojanow-env.elasticbeanstalk.com", 80, ""
//                        + "?email=" + email_txt.getText().toString() + "&password=" + pwd_txt.getText().toString());
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                byte data[] = new byte[512];
//                int len = conn.getInputStream().read(data);
//                if (len > 0) {
//                    String temp = new String(data, 0, len).trim();
//                    System.out.println(temp);
//                    flag = Boolean.parseBoolean(temp);
//                }
//
//            } catch(Exception e) {
//                System.out.println(e.toString());
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String str) {
////            super.onPostExecute(str);
//
//            String msg = flag == true ? "Success" : "False";
//            // initialize toast message
//            Toast toast_msg = Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT);
//            toast_msg.show();
//
//            if (msg.equals("Success")) {
//                // login success, goto PostWall
//                Intent intent = new Intent(MainActivity.this, PostWallActivity.class);
//                startActivity(intent);
//            }
//        }
//    }
}
