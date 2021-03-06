package com.example.slur.post;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.slur.LoginActivity;
import com.example.slur.PlayerRecommendRequest;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.RecommendItemModel;
import com.example.slur.RecommenderDialog;
import com.example.slur.home;
import com.example.slur.postTypeSelect;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class playerPostItemView extends AppCompatActivity {

    int user_id; int heart_p;
    PlayerPostItemModel itemModel;

    TextView goto_list_btn, post_title, post_writer, post_place, post_content;
    TextView post_write_date, cnt_view, cnt_heart;
    TextView writer_name, writer_major, writer_rate;
    Spinner post_state;
    RecyclerView position;
    RecyclerView.LayoutManager pLayoutManager;
    LinearLayout heart_btn_container, contact_btn_container, recommend_container;
    RelativeLayout writer_cardBox;

    //menu
    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;

    //kjy
    List<RecommendItemModel> recommendItemModels;
    AppCompatButton recommend_btn;
    Response.Listener<String> responseListener;
    RecommenderDialog recommenderDialogDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_post_item_view);

        Intent intent = getIntent();
        if(intent.getExtras() != null){
            itemModel = (PlayerPostItemModel) intent.getSerializableExtra("item");
            user_id = new PreferenceHelper(getApplicationContext()).getUserId();
        }
        Log.d("냠냠냠", "View's writer_id : " + itemModel.getUser_id());
        Log.d("냠냠냠", "present user_id : " + user_id);
        Log.d("냠냠냠", "post_id : " + itemModel.getPost_id());


        //init
        goto_list_btn = (TextView)findViewById(R.id.goto_list_btn);
        post_title = (TextView)findViewById(R.id.post_title); post_writer = (TextView)findViewById(R.id.post_writer); post_place = (TextView)findViewById(R.id.post_place);
        post_content = (TextView)findViewById(R.id.post_content);
        post_write_date = (TextView)findViewById(R.id.post_write_date);
        cnt_view = (TextView)findViewById(R.id.count_view); cnt_heart = (TextView)findViewById(R.id.count_heart);
        writer_name = (TextView)findViewById(R.id.writer_name);
        //writer_major = (TextView)findViewById(R.id.writer_major); writer_rate = (TextView)findViewById(R.id.writer_rate);
        post_state = (Spinner)findViewById(R.id.post_state);
        writer_cardBox = (RelativeLayout)findViewById(R.id.writer_card);
        position = (RecyclerView)findViewById(R.id.scroll);
        pLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        position.setLayoutManager(pLayoutManager);

        //kjy
        recommend_btn = (AppCompatButton) findViewById(R.id.recommend_btn);
        recommenderDialogDialog = new RecommenderDialog(this, itemModel.getNeed_position());


        //set position view
        List<positionItem> positionList = new ArrayList<>();
        String[] need_position = itemModel.getNeed_position();
        for(int i = 0 ; i < need_position.length ; i++){
            positionItem temp = new positionItem(need_position[i], "0");
            positionList.add(temp);
        }
        positionAdapter adapter = new positionAdapter(positionList, this);
        position.setAdapter(adapter);


        //spinner init setting
        switch (itemModel.getState()){
            case 1 : {
                post_state.setSelection(1);
                break;
            }
            default :{
                post_state.setSelection(0);
                break;
            }
        }

        //edit post state
        post_state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //db에 게시글 상태 변경
                Response.Listener<String> responseListener1 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                postStateRequest poststateRequest = new postStateRequest(itemModel.getPost_id(), position, responseListener1);
                RequestQueue queue = Volley.newRequestQueue(playerPostItemView.this);
                queue.add(poststateRequest);

                Intent intent = new Intent(playerPostItemView.this, playerPostItemView.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // kjy
        if (itemModel.getUser_id() != user_id) {
            recommend_btn.setVisibility(View.GONE);
        }
        recommend_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recommenderDialogDialog.show();
            }
        });



        //getCNTvalue
        Response.Listener<String> responseCNT = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("냠냠냠", "get RESPONSE from getCNT");

                    JSONObject jsonObject = new JSONObject(response);
                    int cntHeart = jsonObject.getInt("cntHeart");
                    int cntView = jsonObject.getInt("cntView");
                    Log.d("냠냠냠", "cntView : " + cntView);
                    int possible = jsonObject.getInt("possible");
                    Log.d("냠냠냠", "possible : " + possible);
                    if(itemModel.getUser_id() != user_id) cntView+=1;
                    cnt_view.setText(cntView+"");
                    cnt_heart.setText(cntHeart+"");

                    if(possible>0) {
                        heart_p = 0; //찜하기 이미했으니 불가능함을 의미
                        Log.d("냠냠냠", "heart_p in response1 : " + heart_p);
                    }else{
                        heart_p = 1;
                        Log.d("냠냠냠", "heart_p in response2 : " + heart_p);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        GetcntRequest getcntrequest = new GetcntRequest(user_id, itemModel.getPost_id(), responseCNT, itemModel.getUser_id());
        RequestQueue queue = Volley.newRequestQueue(playerPostItemView.this);
        queue.add(getcntrequest);


        //back btn
        goto_list_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //go to writer's profile
        writer_cardBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //클릭하면 해당 회원의 프로필로 이동
            }
        });
        post_title.setText(itemModel.getTitle()); post_writer.setText(itemModel.getWriter());
        post_content.setText(itemModel.getContent()); post_write_date.setText(itemModel.getPost_date());
        writer_name.setText(itemModel.getWriter());

        String placeText="";
        for(int i = 0 ; i<itemModel.getPlace().length ; i++){
            placeText = placeText + itemModel.getPlace()[i] + " ";
        }
        post_place.setText(placeText);


        //Inflator container
        heart_btn_container = (LinearLayout)findViewById(R.id.heart_btn_container);
        contact_btn_container = (LinearLayout)findViewById(R.id.contact_btn_container);
        recommend_container = (LinearLayout)findViewById(R.id.recommend_btn_container);

        if(itemModel.getUser_id() != user_id){ //not my post

            post_state.setEnabled(false);

            //contact containter
            LayoutInflater inflater_contact = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater_contact.inflate(R.layout.post_container_contact, contact_btn_container, true);

            contact_btn_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(user_id <= 0){
                        Toast.makeText(getApplicationContext(), "로그인 해주세요!", Toast.LENGTH_SHORT).show();
                    } else if(itemModel.getState() == 1){
                        Toast.makeText(getApplicationContext(), "연주 중단 게시물입니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        final AlertDialog.Builder removeDialog = new AlertDialog.Builder(playerPostItemView.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                        removeDialog.setMessage(itemModel.getWriter()+"님에게 연락하시겠습니까?\n(채팅방이 개설됩니다.)")
                                .setTitle("Slur")
                                .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .setNeutralButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Response.Listener<String> responseListenerCHAT = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    Log.d("냠냠냠", "통신");
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");

                                                    if(!success){
                                                        Toast.makeText(getApplicationContext(), "이미 연락 중입니다.", Toast.LENGTH_SHORT).show();
                                                    }else if(success){
                                                        Toast.makeText( getApplicationContext(), "채팅이 생성되었습니다.", Toast.LENGTH_SHORT ).show();

                                                        Intent intent = new Intent(playerPostItemView.this, com.example.slur.chat.chatList.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                        startActivity(intent);
                                                        finish();
                                                    }

                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        };
                                        newChat newchat = new newChat(itemModel.getPost_id(), user_id, itemModel.getUser_id(), responseListenerCHAT);
                                        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                                        queue.add(newchat);

                                    }
                                })
                                .setCancelable(false)
                                .show();
                    }
                }
            });

            //heart container
            LayoutInflater inflater_heart = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater_heart.inflate(R.layout.post_container_heart, heart_btn_container, true);

            heart_btn_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("냠냠냠", "heart_p in ClickListener : " + heart_p);
                    if(heart_p == 1 && user_id > 0){
                        //DB update
                        Response.Listener<String> heartresponse = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("냠냠냠", "get RESPONSE from CNT_HEART");

                            }
                        };
                        cntHeartRequest cntheartRequest = new cntHeartRequest(user_id, itemModel.getPost_id(), heartresponse);
                        RequestQueue queue = Volley.newRequestQueue(playerPostItemView.this);
                        queue.add(cntheartRequest);

                        Toast.makeText( getApplicationContext(), "게시글을 찜해두었습니다!", Toast.LENGTH_SHORT ).show();
                        String now = (String) cnt_heart.getText();
                        int nowcnt = Integer.parseInt(now)+1;
                        cnt_heart.setText(nowcnt+"");

                        heart_p = 0;
                    }else if(user_id > 0){
                        Toast.makeText( getApplicationContext(), "이미 찜한 게시글입니다.", Toast.LENGTH_SHORT ).show();
                    }else{
                        Toast.makeText( getApplicationContext(), "로그인해주세요!", Toast.LENGTH_SHORT ).show();
                    }

                }
            });
        }

        else{
          /*  //recommend container
            LayoutInflater inflater_recommend = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater_recommend.inflate(R.layout.post_container_recommend, recommend_container, true);

            TextView textView = recommend_container.findViewById(R.id.text);
            textView.setText("추천 연주자 보러가기");

            recommend_container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(playerPostItemView.this, com.example.slur.home.class);
                    intent.putExtra("item", itemModel);
                    startActivity(intent);
                    finish();
                }
            });*/ //kjy 주석처리

            //찜하기 container에 수정/삭제
            LayoutInflater inflater_edit = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater_edit.inflate(R.layout.post_container_edit, heart_btn_container, true);

            TextView edit = heart_btn_container.findViewById(R.id.edit);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //post edit 화면으로 intent
                }
            });

            TextView remove = heart_btn_container.findViewById(R.id.remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog.Builder removeDialog = new AlertDialog.Builder(playerPostItemView.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    removeDialog.setMessage("게시글을 삭제하시겠습니까?")
                            .setTitle("Slur")
                            .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setNeutralButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            //모든 activity 종료하고 list화면으로 넘어가기
                                            try {
                                                Log.d("냠", "통신");
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    };
                                    removeRequest removerequest = new removeRequest(itemModel.getPost_id(), responseListener2);
                                    RequestQueue queue = Volley.newRequestQueue(playerPostItemView.this);
                                    queue.add(removerequest);

                                    Toast.makeText( getApplicationContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_SHORT ).show();

                                    Intent intent = new Intent(playerPostItemView.this, playerPostListActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(intent);
                                }
                            })
                            .setCancelable(false)
                            .show();
                }
            });
        }



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

    //request func
    public class removeRequest extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/PostRemove.php";
        private Map<String,String> map;

        public removeRequest(int post_id, Response.Listener<String>listener){
            super(Method.POST,url,listener,null);

            map=new HashMap<>();
            map.put("post_type","1"); //ownerPostType_num = 0, playerPostType_num = 1
            map.put("post_id", post_id+"");
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class newChat extends StringRequest{
        final static private String url = "http://s29dongss.dothome.co.kr/NewChat.php";
        private Map<String, String> map;

        public newChat(int post_id, int owner_id, int player_id, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            map = new HashMap<>();
            map.put("root_post_type", "1");
            map.put("root_post_id", post_id+"");
            map.put("owner_id", owner_id+"");
            map.put("player_id", player_id+"");

            Log.d("냠냠냠", "owner_id : "+owner_id+"  |  player_id : "+player_id);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class postStateRequest extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/PostStateEdit.php";
        private Map<String,String> map;

        public postStateRequest(int post_id, int state, Response.Listener<String>listener){
            super(Method.POST,url,listener,null);

            map=new HashMap<>();
            map.put("post_type","1"); //ownerPostType_num = 0, playerPostType_num = 1
            map.put("post_id", post_id+"");
            map.put("state", state+"");
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class cntHeartRequest extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/cntHeart.php";
        private Map<String,String> map;

        public cntHeartRequest(int user_id, int post_id, Response.Listener<String>listener){
            super(Method.POST,url,listener,null);

            map=new HashMap<>();
            map.put("user_id", user_id+"");
            map.put("post_type","1"); //ownerPostType_num = 0, playerPostType_num = 1
            map.put("post_id", post_id+"");
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class GetcntRequest extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/cnt_get.php";
        private Map<String,String> map;

        public GetcntRequest(int user_id, int post_id, Response.Listener<String>listener, int writer_id){
            super(Method.POST,url,listener,null);

            map=new HashMap<>();
            map.put("writer_id", writer_id+"");
            map.put("user_id", user_id+"");
            map.put("post_type","1"); //ownerPostType_num = 0, playerPostType_num = 1
            map.put("post_id", post_id+"");
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    // kjy
    public void getRecommendList() {
        recommendItemModels = new ArrayList<>();
        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        double rating = c.isNull("rating") ? 0 : c.getDouble("rating");
                        String imageStr = c.getString("prof_image");
                        int post_id = c.getInt("post_id");
                        String title = c.getString("title");
                        String name = c.getString("name");
                        String major;
                        if (c.getInt("if_major") == 0) {
                            major = "";
                        } else {
                            major = c.getString("major");
                        }
                        String university = c.getString("aff");
                        String place = c.getString("place");
                        String pay = String.valueOf(c.getInt("pay"));
                        String need_position = c.getString("need_position");
                        String[] position = need_position.split(" ");
                        int userId = c.getInt("user_id");
                        String content = c.getString("content");
                        String postDate = c.getString("post_date");
                        String position_state = c.getString("position_state");

                        RecommendItemModel recommend = new RecommendItemModel(post_id, title, place, "", position, pay, imageStr, university, major, rating, name, userId, content, postDate, position_state);

                        recommendItemModels.add(recommend);
                    }
                    recommenderDialogDialog.setAdapter(recommendItemModels);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        String needPosition = "";

        for (int i = 0; i < itemModel.getNeed_position().length; i++) {
            needPosition += " O.need_position LIKE '%" + itemModel.getNeed_position()[i] + "%' ";
            if (i < itemModel.getNeed_position().length - 1) {
                needPosition += "OR";
            }
        }


        RequestQueue queue = Volley.newRequestQueue(this);
        PlayerRecommendRequest playerRecommendRequest;
        String places = "";

        for (int i = 0; i < itemModel.getPlace().length; i++) {
            places += itemModel.getPlace()[i] + " ";
        }
        places = places.substring(0, places.length() - 1);


        playerRecommendRequest = new PlayerRecommendRequest(places, itemModel.getPay(), needPosition, responseListener);
        queue.add(playerRecommendRequest);
    }
}