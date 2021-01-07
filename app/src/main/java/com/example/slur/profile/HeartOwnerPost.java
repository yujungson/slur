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
import com.example.slur.post.PostAdapter;
import com.example.slur.post.PostItemModel;
import com.example.slur.postTypeSelect;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HeartOwnerPost extends AppCompatActivity {

    int user_id;
    TextView back_btn;

    //ListView관련
    ListView PostList;
    List<PostItemModel> PostitemModels = new ArrayList<>();
    List<PostItemModel> PostitemModelsActive = new ArrayList<>();
    PostAdapter postAdapter;

    //Volley
    Response.Listener<String> responseListener;

    //menu
    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_owner_post);

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

        PostList = (ListView)findViewById(R.id.listViewOwner);
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
                        Log.d("냠냠냠", c.toString());
                        int post_id = c.getInt("post_id");
                        Log.d("냠냠냠", "post_id"+ Integer.toString(post_id));


                        String name = c.getString("writer");
                        Log.d("냠냠냠", "게시글작성자" + name);


                        String title = c.getString("title");
                        Log.d("냠냠냠", "title"+title);
                        String place = c.getString("place");
                        String play_date = c.getString("play_date");
                        String pay = c.getString("pay");
                        String content = c.getString("content");
                        String post_date = c.getString("post_date");

                        int state = c.getInt("state");



                        int write_user_id = user_id;


                        String need_position = c.getString("need_position");
                        String[] position = need_position.split(" ");
                        Log.d("냠냠냠", "포지션 개수 : " + position.length);
                        String need_position_state = c.getString("position_state");
                        String[] position_state = need_position_state.split(" ");


                        PostItemModel itemModel = new PostItemModel(post_id, write_user_id, name, title, place, play_date, state, position, pay, content, position_state, post_date);
                        if(itemModel.getState() == 0){
                            PostitemModelsActive.add(0, itemModel);
                        }
                        Log.d("냠냠냠", "아이템 리스트" + itemModel.getTitle());
                        PostitemModels.add(0, itemModel);

                    }
                    postAdapter = new PostAdapter(PostitemModels, HeartOwnerPost.this, user_id);
                    PostList.setAdapter(postAdapter);




                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("냠냠냠", "예외");
                }
            }
        };

        HeartOwnerPostRequest heartPost = new HeartOwnerPostRequest(user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(HeartOwnerPost.this);
        queue.add(heartPost);




        //Volley 이용해서 favorite post 해당 row 세기 setAdapter



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