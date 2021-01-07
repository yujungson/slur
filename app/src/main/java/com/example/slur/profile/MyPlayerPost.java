package com.example.slur.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.slur.LoginActivity;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.home;
import com.example.slur.post.PlayerPostAdapter;
import com.example.slur.post.PlayerPostItemModel;
import com.example.slur.post.PostItemModel;
import com.example.slur.postTypeSelect;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyPlayerPost extends AppCompatActivity {

    int user_id;
    TextView back_btn;

    //ListView관련
    ListView PostList;
    List<PlayerPostItemModel> PlayerPostitemModels = new ArrayList<>();
    List<PlayerPostItemModel> PlayerPostitemModelsActive = new ArrayList<>();
    PlayerPostAdapter postAdapter;
    List<PostItemModel> tempModels = new ArrayList<>();

    //Volley
    Response.Listener<String> responseListener;

    //menu
    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_player_post);

        Intent intent = getIntent();
        user_id = new PreferenceHelper(getApplicationContext()).getUserId(); // EDITED
        Log.d("user_id", user_id+"");

        back_btn = (TextView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        PostList = (ListView)findViewById(R.id.listViewPlayer);
        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("냠냠냠", "통신");

                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("냠냠냠", jsonObject.toString());
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i<result.length(); i++){
                        Log.d("냠냠냠", Integer.toString(i) + "번째 게시글");
                        JSONObject c = result.getJSONObject(i);
                        int post_id = c.getInt("post_id");
                        Log.d("냠냠냠", "post_id"+ Integer.toString(post_id));

                        int write_user_id = user_id;
                        String name = c.getString("writer");
                        Log.d("냠냠냠", "게시글작성자" + name);


                        String title = c.getString("title");
                        Log.d("냠냠냠", "title"+title);
                        String place = c.getString("place");
                        Log.d("냠냠냠", "place" + place);
                        String[] preferredPlace = place.split(" ");
                        String pay = c.getString("pay");
                        Log.d("냠냠냠", "pay" + pay);
                        String content = c.getString("content");
                        Log.d("냠냠냠", "content" + content);
                        String post_date = c.getString("post_date");
                        Log.d("냠냠냠", "post_date" + post_date);

                        int state = c.getInt("state");
                        Log.d("냠냠냠", "state" + state);


                        String need_position = c.getString("need_position");
                        String[] position = need_position.split(" ");
                        Log.d("냠냠냠", "포지션 개수 : " + position.length);



                        PlayerPostItemModel itemModel = new PlayerPostItemModel(post_id, name, title, state, preferredPlace, position, user_id, content, post_date, pay);
                        if(itemModel.getState() == 0){
                            PlayerPostitemModelsActive.add(0, itemModel);
                        }
                        Log.d("냠냠냠", "아이템 리스트" + itemModel.getTitle());
                        PlayerPostitemModels.add(0, itemModel);
                        Log.d("냠냠냠", "포스트아이템모델추가");

                    }
                    postAdapter = new PlayerPostAdapter(PlayerPostitemModels, MyPlayerPost.this, user_id);
                    Log.d("냠냠냠", "어댑터");
                    PostList.setAdapter(postAdapter);

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("냠냠냠", "예외");
                }


            }


        };

        MyPlayerPostRequest myPost = new MyPlayerPostRequest(user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(MyPlayerPost.this);
        queue.add(myPost);



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