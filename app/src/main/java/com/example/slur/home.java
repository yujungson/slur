package com.example.slur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slur.profile.profile;

public class home extends AppCompatActivity {

    int user_id;
    String user_name;
//     int user_id = 100; //프로필테스트

    private PreferenceHelper preferenceHelper;

    ImageView profile_image, more_btn;
    LinearLayout profile_btn, go_ownerPost, go_playerPost;
    TextView profile_name, lastpost_owner, lastpost_player;
    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        // EDITED : set user id by using PreferenceHelper
        preferenceHelper = new PreferenceHelper(getApplicationContext());
        user_id = preferenceHelper.getUserId();
        user_name = preferenceHelper.getName();

        profile_image = (ImageView) findViewById(R.id.profile_image);
        profile_name = (TextView) findViewById(R.id.profile_name);
        if(user_id != 0){
            profile_name.setText(user_name);
        }
        profile_btn = (LinearLayout) findViewById(R.id.profile_btn);

        more_btn = (ImageView) findViewById(R.id.more_btn);

        lastpost_owner = (TextView) findViewById(R.id.owner_last_post);
        lastpost_player = (TextView) findViewById(R.id.player_last_post);
        go_ownerPost = (LinearLayout) findViewById(R.id.go_ownerpost);
        go_playerPost = (LinearLayout) findViewById(R.id.go_playerpost);

        //responseListener 이용하여 최근 게시물 제목 가져오기

        profile_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id > 0) {
                    Intent intent = new Intent(home.this, com.example.slur.profile.profile.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(home.this, LoginActivity.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            }
        });

        go_ownerPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, com.example.slur.post.ownerPostListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
            }
        });

        //menu 버튼 작동 작성 필요

        menu_home = (ImageView) findViewById(R.id.menu_home);
        menu_post = (ImageView) findViewById(R.id.menu_post);
        menu_rating = (ImageView) findViewById(R.id.menu_rating);
        menu_chat = (ImageView) findViewById(R.id.menu_chat);
        menu_profile = (ImageView) findViewById(R.id.menu_profile);

        menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, home.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });
        menu_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home.this, postTypeSelect.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });
        menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id > 0) {
                    Intent intent = new Intent(home.this, com.example.slur.profile.profile.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(home.this, LoginActivity.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            }
        });
    }
}