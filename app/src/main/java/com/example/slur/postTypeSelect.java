package com.example.slur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.slur.profile.profile;

public class postTypeSelect extends AppCompatActivity {

    int user_id=0;

    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;
    LinearLayout ownerPost, playerPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_type_select);

        Intent intent = getIntent();
//        if(intent.getExtras() != null){
//            user_id = (int)intent.getIntExtra("user_id",0);
//        }
        user_id = new PreferenceHelper(getApplicationContext()).getUserId(); // EDITED
        Log.d("user_id", user_id+"");

        ownerPost = (LinearLayout)findViewById(R.id.owner_post);
        playerPost = (LinearLayout)findViewById(R.id.player_post);

        ownerPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(postTypeSelect.this, com.example.slur.post.ownerPostListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });

        playerPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(postTypeSelect.this, com.example.slur.post.playerPostListActivity.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });

        //menu
        menu_home = (ImageView)findViewById(R.id.menu_home);
        menu_post = (ImageView)findViewById(R.id.menu_post);
        menu_rating = (ImageView)findViewById(R.id.menu_rating);
        menu_chat = (ImageView)findViewById(R.id.menu_chat);
        menu_profile = (ImageView)findViewById(R.id.menu_profile);

        menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(postTypeSelect.this, home.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });
        menu_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(postTypeSelect.this, postTypeSelect.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });
        menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id > 0){
                    Intent intent = new Intent(postTypeSelect.this, com.example.slur.profile.profile.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(postTypeSelect.this, LoginActivity.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            }
        });
    }
}