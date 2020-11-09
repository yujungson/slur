package com.example.slur.post;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.slur.LoginActivity;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.home;
import com.example.slur.postTypeSelect;
import com.example.slur.profile.profile;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ownerPostListActivity extends AppCompatActivity {

    public int user_id = 0;
    private Button btn_write;

    //ListView관련
    ListView PostList;
    List<PostItemModel> PostitemModels = new ArrayList<>();
    List<PostItemModel> PostitemModelsActive = new ArrayList<>();
    PostAdapter postAdapter;
    List<PostItemModel> tempModels = new ArrayList<>();

    //Spinner관련
    Spinner instrumentSpinner, placeSpinner;
    List<String> instrumentList = new ArrayList<>();
    List<String> placeList = new ArrayList<>();
    String key_place, key_instrument;

    //Filter관련
    CheckBox checkBox;

    //Volley
    Response.Listener<String> responseListener;

    //menu
    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_post_list_activity);

        Intent intent = getIntent();
//        if(intent.getExtras() != null){
//            user_id = (int)intent.getIntExtra("user_id",0);
//        }
        user_id = new PreferenceHelper(getApplicationContext()).getUserId(); // EDITED
        Log.d("user_id", user_id+"");

        btn_write = findViewById( R.id.btn_write );
        btn_write.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_id > 0) {
                    Intent intent = new Intent(getApplicationContext(), ownerPostWriteActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "글쓰기를 위해서는 로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        key_place = "전체"; key_instrument = "전체";
        placeList.add("전체");placeList.add("서울경기");placeList.add("강원");placeList.add("충청");placeList.add("전라");placeList.add("경상");placeList.add("제주");placeList.add("기타");

        PostList = (ListView)findViewById(R.id.listView);
        instrumentSpinner = (Spinner)findViewById(R.id.instrument);
        placeSpinner = (Spinner)findViewById(R.id.place);
        checkBox = (CheckBox)findViewById(R.id.checkbox);
        checkBox.setChecked(false);

        responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("냠", "통신");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i<result.length(); i++){
                        Log.d("냠", Integer.toString(i) + "번째 게시글");
                        JSONObject c = result.getJSONObject(i);
                        int post_id = c.getInt("post_id");
                        Log.d("냠", Integer.toString(post_id));
                        String title = c.getString("title");
                        String name = c.getString("writer");
                        int write_user_id = c.getInt("user_id");
                        Log.d("냠", name);
                        String place = c.getString("place");
                        String play_date = c.getString("play_date");
                        String pay = c.getString("pay");
                        String content = c.getString("content");
                        String post_date = c.getString("post_date");

                        int state = c.getInt("state");

                        String need_position = c.getString("need_position");
                        String[] position = need_position.split(" ");
                        Log.d("냠", "포지션 개수 : " + position.length);
                        String need_position_state = c.getString("position_state");
                        String[] position_state = need_position_state.split(" ");

                        for(int j = 0 ; j < position.length ; j++){
                            Log.d("냠", Integer.toString(i) + "번째 게시글의 " + Integer.toString(j) + "번째 포지션");
                            int flag = 0;
                            for(int k = 0 ; k < instrumentList.size() ; k++){
                                if(instrumentList.get(k).equals(position[j])) {
                                    flag = 1;
                                }
                            }
                            if(flag == 0){
                                Log.d("냠", "값추가");
                                instrumentList.add(position[j]);
                                instrumentList = SortPosition.SortBy(instrumentList);
                            }
                        }

                        PostItemModel itemModel = new PostItemModel(post_id, write_user_id, name, title, place, play_date, state, position, pay, content, position_state, post_date);
                        if(itemModel.getState() == 0){
                            PostitemModelsActive.add(0, itemModel);
                        }
                        Log.d("냠", "아이템 리스트" + itemModel.getTitle());
                        PostitemModels.add(0, itemModel);

                    }
                    postAdapter = new PostAdapter(PostitemModels, ownerPostListActivity.this, user_id);
                    PostList.setAdapter(postAdapter);

                    ArrayAdapter spinnerAdapter_place;
                    spinnerAdapter_place = new ArrayAdapter<String>(ownerPostListActivity.this, R.layout.spinner_item, placeList);
                    spinnerAdapter_place.setDropDownViewResource(R.layout.spinner_dropdown);
                    instrumentList.add("직접입력");
                    instrumentList.add(0,"전체");
                    ArrayAdapter spinnerAdapter_instrument;
                    spinnerAdapter_instrument = new ArrayAdapter<String>(ownerPostListActivity.this, R.layout.spinner_item, instrumentList);
                    spinnerAdapter_instrument.setDropDownViewResource(R.layout.spinner_dropdown);

                    placeSpinner.setAdapter(spinnerAdapter_place);
                    instrumentSpinner.setAdapter(spinnerAdapter_instrument);

                    instrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String search = parent.getItemAtPosition(position).toString();
                            key_instrument = search;
                            if(search.equals("직접입력")){
                                //직접입력 구현 필요
                           }else{
                            if(checkBox.isChecked()){
                                postAdapter = new PostAdapter(search_key(key_place, key_instrument,0), ownerPostListActivity.this, user_id);
                            }else{
                                postAdapter = new PostAdapter(search_key(key_place, key_instrument, 1), ownerPostListActivity.this, user_id);
                            }
                            PostList.setAdapter(postAdapter);
                            }
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    placeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            String search = parent.getItemAtPosition(position).toString();
                            key_place = search;
                            if(checkBox.isChecked()){
                                postAdapter = new PostAdapter(search_key(key_place, key_instrument,0), ownerPostListActivity.this, user_id);
                            }else{
                                postAdapter = new PostAdapter(search_key(key_place, key_instrument, 1), ownerPostListActivity.this, user_id);
                            }
                            PostList.setAdapter(postAdapter);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    checkBox.setOnClickListener(new CheckBox.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(checkBox.isChecked()){
                                postAdapter = new PostAdapter(search_key(key_place, key_instrument,0), ownerPostListActivity.this, user_id);
                            }else{
                                postAdapter = new PostAdapter(search_key(key_place, key_instrument, 1), ownerPostListActivity.this, user_id);
                            }
                            PostList.setAdapter(postAdapter);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        OwnerPostRequest ownerPostRequest = new OwnerPostRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(ownerPostListActivity.this);
        queue.add(ownerPostRequest);

        //menu
        menu_home = (ImageView)findViewById(R.id.menu_home);
        menu_post = (ImageView)findViewById(R.id.menu_post);
        menu_rating = (ImageView)findViewById(R.id.menu_rating);
        menu_chat = (ImageView)findViewById(R.id.menu_chat);
        menu_profile = (ImageView)findViewById(R.id.menu_profile);

        menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ownerPostListActivity.this, home.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });
        menu_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ownerPostListActivity.this, postTypeSelect.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });
        menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id > 0){
                    Intent intent = new Intent(ownerPostListActivity.this, com.example.slur.profile.profile.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(ownerPostListActivity.this, LoginActivity.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            }
        });

    }

    public List<PostItemModel> search_key(String place, String instrument, int state){

        Log.d("냠", "함수");

        List<PostItemModel> result = new ArrayList<>();
        List<PostItemModel> temp = new ArrayList<>();

        if(!place.equals("전체") && !instrument.equals("전체")){
            if(state == 0){ //체크되어있음(모집중만 보기)
                temp = PostitemModelsActive;
            }else{
                temp = PostitemModels;
            }

            for(int i = 0 ; i < temp.size() ; i++){
                if(temp.get(i).getPlace().equals(place)){
                    String[] temp_position = temp.get(i).getNeed_position();
                    for(int j = 0 ; j < temp_position.length ; j++){
                        if(temp_position[j].equals(instrument)){
                            result.add(temp.get(i));
                            break;
                        }
                    }
                }
            }

        }else{
            if(place.equals("전체") && instrument.equals("전체")){
                if(state == 0) result = PostitemModelsActive;
                else result = PostitemModels;
            }else if(place.equals("전체")) {
                if (state == 0) {
                    temp = PostitemModelsActive;
                } else {
                    temp = PostitemModels;
                }

                for (int i = 0; i < temp.size(); i++) {
                    String[] temp_position = temp.get(i).getNeed_position();
                    for (int j = 0; j < temp_position.length; j++) {
                        if (temp_position[j].equals(instrument)) {
                            result.add(temp.get(i));
                            break;
                        }
                    }
                }
            }else{
                if (state == 0) {
                    temp = PostitemModelsActive;
                } else {
                    temp = PostitemModels;
                }

                for (int i = 0; i < temp.size(); i++) {
                    if(temp.get(i).getPlace().equals(place)){
                        result.add(temp.get(i));
                    }
                }
            }
        }

        return result;
    };
}

class SortPosition {

    public static List<String> SortBy(List<String> itemsList) {

        String l;

        for (int i = 0; i < itemsList.size(); i++) {
            for (int j = 0; j < itemsList.size() - i - 1; j++) {
                int len1 = itemsList.get(j).length();
                int len2 = itemsList.get(j + 1).length();
                int lim = Math.min(len1, len2);
                String v1 = itemsList.get(j);
                String v2 = itemsList.get(j + 1);

                int check = 0;
                int flag = 0; //두 단어간 우선순위 없음
                int value = 0;
                while (check < lim) {
                    char c1 = v1.charAt(check);
                    char c2 = v2.charAt(check);
                    if (c1 != c2) {
                        flag = 1; //두 단어간 우선순위 있음
                        value = c1 - c2;
                        break;
                    }
                    check++;
                }
                if (flag == 0) value = len1 - len2;

                if ((flag == 1 && value > 0) || (flag == 0 && value > 0)) {

                    l = itemsList.get(j);
                    itemsList.set(j, itemsList.get(j + 1));
                    itemsList.set(j + 1, l);

                }
            }
        }
        return itemsList;
    }
}