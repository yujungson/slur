package com.example.slur.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.slur.LoginActivity;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.home;
import com.example.slur.post.OwnerPostRequest;
import com.example.slur.post.ownerPostListActivity;
import com.example.slur.postTypeSelect;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chatList extends AppCompatActivity {

    public static Context CONTEXT;

    int user_id;
    ListView chatList;
    List<RoomItemModel> roomItemModelList = new ArrayList<>();;
    chatListAdapter chatlistAdapter;

    //menu
    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_list);

        CONTEXT = this;

        Intent intent = getIntent();
        user_id = new PreferenceHelper(getApplicationContext()).getUserId(); // EDITED

        chatList = (ListView)findViewById(R.id.listView);



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    roomItemModelList.clear();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i<result.length(); i++){
                        JSONObject c = result.getJSONObject(i);
                        int chat_id = c.getInt("chat_id");
                        int root_post_type = c.getInt("root_post_type");
                        int root_post_id = c.getInt("root_post_id");
                        int owner_id = c.getInt("owner_id");
                        int player_id = c.getInt("player_id");
                        int state = c.getInt("state");
                        int state_demand = c.getInt("state_demand");
                        int demand_who = c.getInt("demand_who");
                        int position = c.getInt("position");
                        int changed_post_id = c.getInt("changed_post_id");
                        String cancel_reason = c.getString("cancel_reason");
                        String owner_name = c.getString("owner_name");
                        String owner_bit = c.getString("owner_bit");
                        String player_name = c.getString("player_name");
                        String player_bit = c.getString("player_bit");

                        if(owner_id == user_id || player_id == user_id){
                            RoomItemModel itemModel = new RoomItemModel(chat_id, root_post_type, root_post_id, owner_id, player_id, state, state_demand, demand_who, position, changed_post_id, cancel_reason, owner_name, player_name, "", "", owner_bit, player_bit);

                            roomItemModelList.add(itemModel);
                        }

                    }

                    chatlistAdapter = new chatListAdapter(roomItemModelList, chatList.this, user_id);
                    chatList.setAdapter(chatlistAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("냠", "exception");
                }

            }
        };
        LoadChatRequest loadChatRequest = new LoadChatRequest(user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(chatList.this);
        queue.add(loadChatRequest);



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

    @Override
    public void onResume() {
        super.onResume();

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("채팅을 불러오고 있습니다.");
        dialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    roomItemModelList.clear();

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i<result.length(); i++){
                        JSONObject c = result.getJSONObject(i);
                        int chat_id = c.getInt("chat_id");
                        int root_post_type = c.getInt("root_post_type");
                        int root_post_id = c.getInt("root_post_id");
                        int owner_id = c.getInt("owner_id");
                        int player_id = c.getInt("player_id");
                        int state = c.getInt("state");
                        int state_demand = c.getInt("state_demand");
                        int demand_who = c.getInt("demand_who");
                        int position = c.getInt("position");
                        int changed_post_id = c.getInt("changed_post_id");
                        String cancel_reason = c.getString("cancel_reason");
                        String owner_name = c.getString("owner_name");
                        String owner_bit = c.getString("owner_bit");
                        String player_name = c.getString("player_name");
                        String player_bit = c.getString("player_bit");

                        if(owner_id == user_id || player_id == user_id){
                            RoomItemModel itemModel = new RoomItemModel(chat_id, root_post_type, root_post_id, owner_id, player_id, state, state_demand, demand_who, position, changed_post_id, cancel_reason, owner_name, player_name, "", "", owner_bit, player_bit);

                            roomItemModelList.add(itemModel);
                        }

                    }

                    chatlistAdapter = new chatListAdapter(roomItemModelList, chatList.this, user_id);
                    chatList.setAdapter(chatlistAdapter);

                    dialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("냠", "exception");
                }

            }
        };
        LoadChatRequest loadChatRequest = new LoadChatRequest(user_id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(chatList.this);
        queue.add(loadChatRequest);
    }



    public class LoadChatRequest extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/LoadChatList.php";
        private Map<String,String> map;

        public LoadChatRequest(int user_id, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            map=new HashMap<>();
            map.put("user_id",user_id+"");
        }
    }
}