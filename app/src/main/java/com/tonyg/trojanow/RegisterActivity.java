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
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends Activity {
    private EditText email = null;
    private EditText password1 = null;
    private EditText password2 = null;
    private EditText username = null;
    private Spinner gender = null;
    private Button reg_btn = null;
    private TextView email_checker;
    private TextView pwd_checker;
    private boolean flag = false;
    private String chosenGender;
    private SharedPreferences share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        reg_btn = (Button) findViewById(R.id.reg_btn);
        email = (EditText) findViewById(R.id.regEmail);
        email_checker = (TextView) findViewById(R.id.email_checker);
        password1 = (EditText) findViewById(R.id.pwd1);
        password2 = (EditText) findViewById(R.id.pwd2);
        pwd_checker = (TextView) findViewById(R.id.pwd_checker);
        username = (EditText) findViewById(R.id.username);
        gender = (Spinner) findViewById(R.id.gender);



        email.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (!isEmailValid(email.getText().toString())) {
                        email_checker.setText("invalid");
                        email_checker.setTextColor(Color.RED);
                    } else {
                        email_checker.setText("valid");
                        email_checker.setTextColor(Color.GREEN);
                    }
                }
                return false;
            }
        });
        password2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP) {
                    if (!password1.getText().toString().equals(password2.getText().toString())) {

                        pwd_checker.setText("inconsistent!");
                        pwd_checker.setTextColor(Color.RED);
                    } else {
                        pwd_checker.setText("consistent");
                        pwd_checker.setTextColor(Color.GREEN);
                    }
                }
                return false;
            }
        });

        chosenGender = gender.getSelectedItem().toString();
        Log.d("debug", chosenGender);
        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!isEmailValid(email.getText().toString()) || !password1.getText().toString().equals(password2.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "illegal input", Toast.LENGTH_LONG).show();
                    return;
                }
                register();
//                if (password1.equals(password2))
                // create a Student instance
//                Student stud = new Student();
//                stud.setEmail(email.getText().toString());
//                stud.setPassword(password1.getText().toString());
//                stud.setFullName(username.getText().toString());
//                stud.setGender(gender.toString());
//
//                // create a StudentFunctions object
//                StudentFunctions studFunc = new StudentFunctions(stud);
//                // success or error message
//                String msg = studFunc.register();
//                Toast toast_msg = Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_LONG);
//                toast_msg.show();
            }
        });


    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    private void register() {
        RequestQueue rq = Volley.newRequestQueue(RegisterActivity.this);

        String url = "http://default-environment-mx5yjbdnks.elasticbeanstalk.com/register";
        url = url + "?username=" + username.getText().toString() + "&password=" + password1.getText().toString()
                + "&email=" + email.getText().toString() + "&gender=" + chosenGender;
        Log.d("debug", url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // initialize toast message
                        Toast toast_msg = Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT);
                        toast_msg.show();

                        if (response.equals("register success")) {
                            //update sharedPreference
                            share = RegisterActivity.super.getSharedPreferences("userInfo",Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = share.edit();
                            editor.putString("username",username.getText().toString());
                            editor.putString("password",password1.getText().toString());
//                            editor.putString("email",email.getText().toString());
//                            editor.putString("gender",chosenGender);
                            editor.commit();
                            // register success, go to log in
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RegisterActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        rq.add(stringRequest);
    }
}
