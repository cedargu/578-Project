package com.tonyg.trojanow;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class UserProfileActivity extends Activity {
    private TextView usernameView;
    private TextView userEmail;
    private TextView userGender;
    private Button userPost;
    private Button messageBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        usernameView = (TextView) findViewById(R.id.username);
        userEmail = (TextView) findViewById(R.id.userEmail);
        userGender = (TextView) findViewById(R.id.userGender);
        userPost = (Button) findViewById(R.id.userPost);
        messageBtn = (Button) findViewById(R.id.message_btn);

        SharedPreferences share = super.getSharedPreferences("userInfo", Activity.MODE_PRIVATE);


        usernameView.setText(share.getString("selectedUser","null"));
        userEmail.setText(share.getString("selectedUserEmail","null"));
        userGender.setText(share.getString("selectedUserGender","null"));

        userPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UserPostActivity.class);
                startActivity(intent);
            }
        });

        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, SendMessageActivity.class);
                startActivity(intent);
            }
        });
    }
}
