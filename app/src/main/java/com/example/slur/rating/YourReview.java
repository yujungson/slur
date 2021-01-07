package com.example.slur.rating;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.slur.LoginActivity;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.home;
import com.example.slur.postTypeSelect;



public class YourReview extends AppCompatActivity {

    int user_id;
    String user_name;
    int user_rate;


    private PreferenceHelper preferenceHelper;

    ImageView profile_image;
    LinearLayout my_review_list, your_review_list, make_review_list;
    TextView profile_name, writer_rate;
    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;
    RatingBar rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_yourreview);

        Intent intent = getIntent();
        // EDITED : set user id by using PreferenceHelper
        preferenceHelper = new PreferenceHelper(getApplicationContext());
        user_id = preferenceHelper.getUserId();
        user_name = preferenceHelper.getName();
        //user_rate= preferenceHelper.getUser_rate();

        profile_image = (ImageView) findViewById(R.id.profile_img);

        writer_rate =  (TextView) findViewById(R.id.writer_rate);
        rating = findViewById(R.id.ratingbarSmall);











        //menu
        menu_home = (ImageView) findViewById(R.id.menu_home);
        menu_post = (ImageView) findViewById(R.id.menu_post);
        menu_rating = (ImageView) findViewById(R.id.menu_rating);
        menu_chat = (ImageView) findViewById(R.id.menu_chat);
        menu_profile = (ImageView) findViewById(R.id.menu_profile);

        menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.slur.home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        menu_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), com.example.slur.postTypeSelect.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id > 0) {
                    Intent intent = new Intent(getApplicationContext(), com.example.slur.profile.profile.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), com.example.slur.LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        });
        menu_rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id > 0) {
                    Intent intent = new Intent(getApplicationContext(), com.example.slur.rating.RatingHome.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), com.example.slur.LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        });
        menu_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id > 0) {
                    Intent intent = new Intent(getApplicationContext(), com.example.slur.chat.chatList.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getApplicationContext(), com.example.slur.LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}