package com.example.slur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class postTypeSelect extends AppCompatActivity {

    int user_id=0;

    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;
    LinearLayout ownerPost, playerPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_type_select);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            user_id = (int)intent.getIntExtra("user_id",0);
        }
        Log.d("user_id", user_id+"");

        ownerPost = (LinearLayout)findViewById(R.id.owner_post);
        playerPost = (LinearLayout)findViewById(R.id.player_post);

        menu_home = (ImageView)findViewById(R.id.menu_home);
        menu_post = (ImageView)findViewById(R.id.menu_post);
        menu_rating = (ImageView)findViewById(R.id.menu_rating);
        menu_chat = (ImageView)findViewById(R.id.menu_chat);
        menu_profile = (ImageView)findViewById(R.id.menu_profile);

        ownerPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(postTypeSelect.this, );
            }
        });
    }
}