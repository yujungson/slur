package com.example.slur;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.slur.chat.chatItemView;
import com.example.slur.post.ownerPostItemView;
import com.example.slur.post.ownerPostListActivity;
import com.example.slur.profile.profile;

import org.json.JSONException;
import org.json.JSONObject;

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

        more_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LinearLayout popup = (LinearLayout) View.inflate(home.this, R.layout.popup_home_more, null);
                android.app.AlertDialog.Builder builder = new AlertDialog.Builder(home.this);
                builder.setView(popup);
                TextView question = popup.findViewById(R.id.guide);
                TextView logout = popup.findViewById(R.id.logout);

                question.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(home.this, guide.class);
                        startActivity(intent);
                    }
                });

                logout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final androidx.appcompat.app.AlertDialog.Builder removeDialog = new androidx.appcompat.app.AlertDialog.Builder(home.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                        removeDialog.setMessage("로그아웃 하시겠습니까?")
                                .setTitle("Slur")
                                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setNeutralButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        preferenceHelper.logout();
                                        Toast.makeText( getApplicationContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT ).show();

                                        Intent intent = new Intent(home.this, home.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                });

                builder.show();
            }
        });

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
                Intent intent = new Intent(getApplicationContext(), home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        menu_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), postTypeSelect.class);
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
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
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
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }
            }
        });
    }
}