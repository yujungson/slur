package com.example.slur.post;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.slur.SignupActivity;
import com.example.slur.home;
import com.example.slur.postTypeSelect;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class playerPostListActivity extends AppCompatActivity {


    //ListView관련
    ListView PlayerPostList;
    List<PlayerPostItemModel> PlayerPostitemModels = new ArrayList<>();
    List<PlayerPostItemModel> PlayerPostitemModelsActive = new ArrayList<>();
    PlayerPostAdapter playerPostAdapter;
    List<PlayerPostItemModel> tempModels = new ArrayList<>();

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

    private Button btn_write;
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_post_list);

        user_id = new PreferenceHelper(getApplicationContext()).getUserId(); // EDITED
        Log.d("user_id", user_id+"");

        btn_write = findViewById( R.id.btn_write );
        btn_write.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_id > 0) {
                    Intent intent = new Intent(getApplicationContext(), playerPostWriteActivity.class);
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

        PlayerPostList = (ListView)findViewById(R.id.listView);
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
                        Log.d("냠", name);
                        int state = c.getInt("state");

                        String place = c.getString("place");
                        String[] preferredPlace = place.split(" ");
                        for(int j = 0 ; j < preferredPlace.length ; j++) {
                            Log.d("냠", Integer.toString(i) + "번째 게시글의 " + Integer.toString(j) + "번째 장소 " + preferredPlace[j]);
                        }

                        String need_position = c.getString("need_position");
                        String[] position = need_position.split(" ");
                        Log.d("냠", "포지션 개수 : " + position.length);

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

                        PlayerPostItemModel itemModel = new PlayerPostItemModel(post_id, name, title, state, preferredPlace, position);
                        if(itemModel.getState() == 0){
                            PlayerPostitemModelsActive.add(0, itemModel);
                        }
                        Log.d("냠", "아이템 리스트" + itemModel.getTitle());
                        PlayerPostitemModels.add(0, itemModel);

                    }
                    playerPostAdapter = new PlayerPostAdapter(PlayerPostitemModels, playerPostListActivity.this, user_id);
                    PlayerPostList.setAdapter(playerPostAdapter);

                    ArrayAdapter spinnerAdapter_place;
                    spinnerAdapter_place = new ArrayAdapter<String>(playerPostListActivity.this, R.layout.spinner_item, placeList);
                    spinnerAdapter_place.setDropDownViewResource(R.layout.spinner_dropdown);

                    instrumentList.add("직접입력");
                    instrumentList.add(0,"전체");
                    ArrayAdapter spinnerAdapter_instrument;
                    spinnerAdapter_instrument = new ArrayAdapter<String>(playerPostListActivity.this, R.layout.spinner_item, instrumentList);
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
                                    playerPostAdapter = new PlayerPostAdapter(search_key(key_place, key_instrument,0), playerPostListActivity.this, user_id);
                                }else{
                                    playerPostAdapter = new PlayerPostAdapter(search_key(key_place, key_instrument, 1), playerPostListActivity.this, user_id);
                                }
                                PlayerPostList.setAdapter(playerPostAdapter);
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
                                playerPostAdapter = new PlayerPostAdapter(search_key(key_place, key_instrument,0), playerPostListActivity.this, user_id);
                            }else{
                                playerPostAdapter = new PlayerPostAdapter(search_key(key_place, key_instrument, 1), playerPostListActivity.this, user_id);
                            }
                            PlayerPostList.setAdapter(playerPostAdapter);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    checkBox.setOnClickListener(new CheckBox.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(checkBox.isChecked()){
                                playerPostAdapter = new PlayerPostAdapter(search_key(key_place, key_instrument,0), playerPostListActivity.this, user_id);
                            }else{
                                playerPostAdapter = new PlayerPostAdapter(search_key(key_place, key_instrument, 1), playerPostListActivity.this, user_id);
                            }
                            PlayerPostList.setAdapter(playerPostAdapter);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        PlayerPostRequest playerPostRequest = new PlayerPostRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(playerPostListActivity.this);
        queue.add(playerPostRequest);





        //menu
        menu_home = (ImageView)findViewById(R.id.menu_home);
        menu_post = (ImageView)findViewById(R.id.menu_post);
        menu_rating = (ImageView)findViewById(R.id.menu_rating);
        menu_chat = (ImageView)findViewById(R.id.menu_chat);
        menu_profile = (ImageView)findViewById(R.id.menu_profile);

        menu_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(playerPostListActivity.this, home.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });
        menu_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(playerPostListActivity.this, postTypeSelect.class);
                intent.putExtra("user_id", user_id);
                startActivity(intent);
                finish();
            }
        });
        menu_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user_id > 0){
                    Intent intent = new Intent(playerPostListActivity.this, com.example.slur.profile.profile.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(playerPostListActivity.this, LoginActivity.class);
                    intent.putExtra("user_id", user_id);
                    startActivity(intent);
                }
            }
        });
    }

    public List<PlayerPostItemModel> search_key(String place, String instrument, int state){

        Log.d("냠", "함수");

        List<PlayerPostItemModel> result = new ArrayList<>();
        List<PlayerPostItemModel> temp = new ArrayList<>();

        if(!place.equals("전체") && !instrument.equals("전체")){//둘 다 전체아님
            if(state == 0){ //체크되어있음(연주가능만 보기)
                temp = PlayerPostitemModelsActive;
            }else{
                temp = PlayerPostitemModels;
            }


            for(int i = 0 ; i < temp.size() ; i++){
                for(int k = 0; k < temp.get(i).getPlace().length; k++)
                    if(temp.get(i).getPlace()[k].equals(place)){
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
            if(place.equals("전체") && instrument.equals("전체")){//둘 다 전체
                if(state == 0) result = PlayerPostitemModelsActive;
                else result = PlayerPostitemModels;
            }else if(place.equals("전체")) {//장소만 전체
                if (state == 0) {
                    temp = PlayerPostitemModelsActive;
                } else {
                    temp = PlayerPostitemModels;
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
            }else{//포지션만 전체
                if (state == 0) {
                    temp = PlayerPostitemModelsActive;
                } else {
                    temp = PlayerPostitemModels;
                }

                for(int i = 0 ; i < temp.size() ; i++){
                    for(int k = 0; k < temp.get(i).getPlace().length; k++)
                        if(temp.get(i).getPlace()[k].equals(place)){
                            result.add(temp.get(i));
                            break;
                        }
                }
            }
        }

        return result;
    };
}