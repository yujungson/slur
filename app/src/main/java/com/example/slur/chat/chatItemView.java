package com.example.slur.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.slur.LoginActivity;
import com.example.slur.MainActivity;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.home;
import com.example.slur.post.OwnerPosition;
import com.example.slur.post.PlayerPostItemModel;
import com.example.slur.post.PostAdapter;
import com.example.slur.post.PostItemModel;
import com.example.slur.post.SortPosition;
import com.example.slur.post.ownerPostItemView;
import com.example.slur.post.ownerPostListActivity;
import com.example.slur.post.playerPostItemView;
import com.example.slur.post.playerPostWriteActivity;
import com.example.slur.postTypeSelect;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class chatItemView extends AppCompatActivity {

    int user_id;
    int chat_id, state, state_demand, demand_who, position_index, owner_id, position;

    int changed_post_id;
    String cancel_reason;

    int root_post_type, root_post_id;
    PostItemModel rootOwnerPostitem;
    PlayerPostItemModel rootPlayerPostitem;

    String need_position;
    String position_state;
    String[] stateList;
    List<String> instrumentList = new ArrayList<>();
    List<Integer> instrumentStateList = new ArrayList<>();

    LinearLayout rootPostContainer;
    ListView chat;
    EditText inputMessage;
    Button sendBtn;
    TextView state_view;


    List<PostItemModel> PostitemModels = new ArrayList<>();
    List<PostItemModel> PostitemModelsActive = new ArrayList<>();
    chatChoosePostAdapter postAdapter;

    // kjy 상대방
    int otherId;
    Bitmap userProfileImgBitmap, otherProfileImgBitmap;
    String userNickname, otherNickname;

    ArrayList<chatData> messageItems = new ArrayList<>();
    messageAdapter adapter;

    //menu
    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;

    //Firebase DB관리 객체와 'chat'노드 참조객체 얻어오기
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("chat");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_item_view);


        Intent intent = getIntent();
        if (intent.getExtras() != null) {
            chat_id = intent.getIntExtra("chat_id", -1);
            state = intent.getIntExtra("state", -1);
            state_demand = intent.getIntExtra("stateDemand", -1);
            demand_who = intent.getIntExtra("demand_who", -1);
            root_post_type = intent.getIntExtra("root_post_type", -1);
            root_post_id = intent.getIntExtra("root_post_id", -1);
            position = intent.getIntExtra("position", -1);
            changed_post_id = intent.getIntExtra("changed_post_id", -1);
            cancel_reason = intent.getStringExtra("cancel_reason");
            owner_id = intent.getIntExtra("owner_id", -1);

            otherId = intent.getIntExtra("other_id", -1);// kjy

            user_id = new PreferenceHelper(getApplicationContext()).getUserId();
            Log.d("냠", "root_id : " + root_post_id + " | item user_id : " + user_id);
        }

        rootPostContainer = (LinearLayout) findViewById(R.id.root_post);
        chat = (ListView) findViewById(R.id.chat_message);
        inputMessage = (EditText) findViewById(R.id.input_message);
        sendBtn = (Button) findViewById(R.id.sendbtn);
        state_view = (TextView) findViewById(R.id.state);

        // kjy  프로필 이미지, 닉네임 가져오는 부분
        Response.Listener<String> loadChatProfileResponse = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    // 작성자 프로필 데이터
                    JSONObject userData = jsonObject.getJSONObject("user_data");
                    // 상대방 프로필 데이터
                    JSONObject otherData = jsonObject.getJSONObject("other_data");


                    userNickname = userData.getString("name");
                    // 작성자 프로필 이미지 연동
                    String userBase64String = userData.getString("prof_image");
                    if (!userBase64String.equals("")) {
                        byte[] decodedString = Base64.decode(userBase64String, Base64.DEFAULT);   // 바이트배열 형태로 변환 후
                        userProfileImgBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);  // 비트맵 객체로 변환하여 저장
                    }
                    // 상대방 이름 저장
                    otherNickname = otherData.getString("name");
                    // 상대방 프로필 이미지 연동
                    String otherBase64String = otherData.getString("prof_image");
                    if (!otherBase64String.equals("")) {
                        byte[] decodedString = Base64.decode(otherBase64String, Base64.DEFAULT);
                        otherProfileImgBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    }

                    init();
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        LoadChatProfileRequest loadChatProfile = new LoadChatProfileRequest(user_id, otherId, loadChatProfileResponse);
        RequestQueue loadChatProfileQueue = Volley.newRequestQueue(chatItemView.this);
        loadChatProfileQueue.add(loadChatProfile);


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

    public void clickSend(View view) {
        String message = inputMessage.getText().toString();

        //메세지 작성 시간 문자열로..
        Calendar calendar = Calendar.getInstance(); //현재 시간을 가지고 있는 객체
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE); //14:16

        //firebase DB에 저장할 값(MessageItem객체) 설정
        chatData messageItem = new chatData(chat_id, user_id, message, time);
        //'char'노드에 MessageItem객체를 통해
        databaseReference.push().setValue(messageItem);

        //EditText에 있는 글씨 지우기
        inputMessage.setText("");

        //소프트키패드를 안보이도록..
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

        Log.d("냠냠", "clickSend완료");
    }

    public class chatRoomRequest extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/LoadChatRoom.php";
        private Map<String, String> map;

        public chatRoomRequest(int chat_id, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            map = new HashMap<>();
            map.put("chat_id", String.valueOf(chat_id));
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class GetRootPost_Owner extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/LoadOwnerPost.php";
        private Map<String, String> map;

        public GetRootPost_Owner(int root_post_type, int root_post_id, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            Log.d("냠냠냠", "GetRootPost");

            map = new HashMap<>();
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class GetRootPost_Player extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/LoadPlayerPost.php";
        private Map<String, String> map;

        public GetRootPost_Player(int root_post_type, int root_post_id, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            Log.d("냠냠냠", "GetRootPost");

            map = new HashMap<>();
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class ChatChoosePost extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/ChatChoosePost.php";
        private Map<String, String> map;

        public ChatChoosePost(int chat_id, int owner_id, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            map = new HashMap<>();
            map.put("chat_id", String.valueOf(chat_id));
            map.put("owner_id", String.valueOf(owner_id));
            map.put("changed_post_id", String.valueOf(changed_post_id));
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class MatchRequest extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/MatchRequest.php";
        private Map<String, String> map;

        public MatchRequest(int chat_id, int state, int state_demand, int demand_who, int position, int changed_post_id, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            map = new HashMap<>();
            map.put("chat_id", String.valueOf(chat_id));
            map.put("state", String.valueOf(state));
            map.put("state_demand", String.valueOf(state_demand));
            map.put("position", String.valueOf(position));
            map.put("demand_who", demand_who+"");
            map.put("changed_post_id", String.valueOf(changed_post_id));
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class MatchRootRequest extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/MatchRootRequest.php";
        private Map<String, String> map;

        public MatchRootRequest(int chat_id, int state, int changed_post_id, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            map = new HashMap<>();
            map.put("chat_id", String.valueOf(chat_id));
            map.put("state", String.valueOf(state));
            map.put("changed_post_id", String.valueOf(changed_post_id));
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class MatchSuccess extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/MatchSuccess.php";
        private Map<String, String> map;

        public MatchSuccess(int chat_id, int root_post_id, int state, int state_demand, String positionState, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            map = new HashMap<>();
            map.put("chat_id", String.valueOf(chat_id));
            map.put("root_post_id", root_post_id + "");
            map.put("state", String.valueOf(state));
            map.put("state_demand", String.valueOf(state_demand));
            map.put("positionState", positionState);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    public class CancelRequest extends StringRequest {
        final static private String url = "http://s29dongss.dothome.co.kr/CancelMatching.php";
        private Map<String, String> map;

        public CancelRequest(int chat_id, int state, int state_demand, int demand_who, int position, String cancel_reason, Response.Listener<String> listener) {
            super(Method.POST, url, listener, null);

            map = new HashMap<>();
            map.put("chat_id", String.valueOf(chat_id));
            map.put("state", String.valueOf(state));
            map.put("state_demand", String.valueOf(state_demand));
            map.put("demand_who", String.valueOf(demand_who));
            map.put("position", String.valueOf(position));
            map.put("cancel_reason", cancel_reason);
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return map;
        }
    }

    // kjy
    public Bitmap getOtherProfileImgBitmap() {
        return otherProfileImgBitmap;
    }
    public Bitmap getUserProfileImgBitmap() {
        return userProfileImgBitmap;
    }
    public String getOtherNickname() {
        return otherNickname;
    }
    public String getUserNickname() {
        return userNickname;
    }

    private void init() throws ParseException {

        //getRootPostINFO
        Response.Listener<String> PostresponseOwner = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        int post_id = c.getInt("post_id");

                        if (post_id == root_post_id) {
                            int write_user_id = user_id;
                            String name = c.getString("writer");

                            String title = c.getString("title");
                            String place = c.getString("place");
                            String play_date = c.getString("play_date");
                            String pay = c.getString("pay");
                            String content = c.getString("content");
                            String post_date = c.getString("post_date");

                            int state = c.getInt("state");

                            String need_position = c.getString("need_position");
                            String[] position = need_position.split(" ");
                            String need_position_state = c.getString("position_state");
                            String[] position_state = need_position_state.split(" ");

                            for (int j = 0; j < position.length; j++) {
                                int flag = 0;
                                for (int k = 0; k < instrumentList.size(); k++) {
                                    if (instrumentList.get(k).equals(position[j])) {
                                        flag = 1;
                                    }
                                }
                                if (flag == 0) {
                                    instrumentList.add(position[j]);
                                    instrumentStateList.add(Integer.parseInt(position_state[j]));
                                    instrumentList = SortPosition.SortBy(instrumentList);
                                }
                            }

                            rootOwnerPostitem = new PostItemModel(post_id, write_user_id, name, title, place, play_date, state, position, pay, content, position_state, post_date);

                            LayoutInflater inflater_root = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            inflater_root.inflate(R.layout.ownerpost_view, rootPostContainer, true);

                            TextView rootTitle = rootPostContainer.findViewById(R.id.title);
                            TextView rootState = rootPostContainer.findViewById(R.id.state);
                            TextView rootWriter = rootPostContainer.findViewById(R.id.writer);
                            TextView rootPlay_date = rootPostContainer.findViewById(R.id.play_date);
                            TextView rootPlace = rootPostContainer.findViewById(R.id.place);

                            rootTitle.setText(rootOwnerPostitem.getTitle());
                            rootWriter.setText(rootOwnerPostitem.getWriter());
                            rootPlay_date.setText(rootOwnerPostitem.getPlay_date());
                            rootPlace.setText(rootOwnerPostitem.getPlace());

                            String post_state;
                            switch (rootOwnerPostitem.getState()) {
                                case 0: {
                                    post_state = "섭외중";
                                    rootState.setTextColor(Color.parseColor("#088A29"));
                                    break;
                                }
                                case 1: {
                                    post_state = "연주완료";
                                    rootState.setTextColor(Color.parseColor("#0174DF"));
                                    break;
                                }
                                case 2: {
                                    post_state = "연주취소";
                                    break;
                                }
                                default:
                                    post_state = "error";
                                    break;
                            }
                            rootState.setText(post_state);

                            rootPostContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(v.getContext(), ownerPostItemView.class);
                                    intent.putExtra("item", rootOwnerPostitem);
                                    intent.putExtra("user_id", user_id);
                                    v.getContext().startActivity(intent);
                                }
                            });
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("냠", "exception");
                }
            }
        };

        Response.Listener<String> PostresponsePlayer = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("냠", "통신");

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject c = result.getJSONObject(i);
                        int post_id = c.getInt("post_id");
                        if (post_id == root_post_id) {
                            String title = c.getString("title");
                            String name = c.getString("writer");
                            int state = c.getInt("state");
                            int writer_id = c.getInt("writer_id");
                            String content = c.getString("content");
                            String post_date = c.getString("post_date");
                            String pay = c.getString("pay");

                            String place = c.getString("place");
                            String[] preferredPlace = place.split(" ");

                            String need_position = c.getString("need_position");
                            String[] position = need_position.split(" ");

                            for (int j = 0; j < position.length; j++) {
                                int flag = 0;
                                for (int k = 0; k < instrumentList.size(); k++) {
                                    if (instrumentList.get(k).equals(position[j])) {
                                        flag = 1;
                                    }
                                }
                                if (flag == 0) {
                                    instrumentList.add(position[j]);
                                    instrumentList = SortPosition.SortBy(instrumentList);
                                }
                            }

                            rootPlayerPostitem = new PlayerPostItemModel(post_id, name, title, state, preferredPlace, position, writer_id, content, post_date, pay);

                            LayoutInflater inflater_root = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            inflater_root.inflate(R.layout.playerpost_view, rootPostContainer, true);

                            TextView rootTitle = rootPostContainer.findViewById(R.id.title);
                            TextView rootState = rootPostContainer.findViewById(R.id.state);
                            TextView rootWriter = rootPostContainer.findViewById(R.id.writer);
                            TextView rootPlace = rootPostContainer.findViewById(R.id.place);

                            rootTitle.setText(rootPlayerPostitem.getTitle());
                            rootWriter.setText(rootPlayerPostitem.getWriter());
                            if (rootPlayerPostitem.getPlace().length > 1)
                                rootPlace.setText(rootPlayerPostitem.getPlace()[0] + " 외 " + (rootPlayerPostitem.getPlace().length - 1) + "곳");
                            else
                                rootPlace.setText(rootPlayerPostitem.getPlace()[0]);

                            String post_state;
                            switch (rootPlayerPostitem.getState()) {
                                case 0: {
                                    post_state = "연주가능";
                                    rootState.setTextColor(Color.parseColor("#088A29"));
                                    break;
                                }
                                case 1: {
                                    post_state = "연주중단";
                                    rootState.setTextColor(Color.parseColor("#A8A7A7"));
                                    break;
                                }
                                default:
                                    post_state = "error";
                                    break;
                            }
                            rootState.setText(post_state);

                            rootPostContainer.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(v.getContext(), playerPostItemView.class);
                                    intent.putExtra("item", rootPlayerPostitem);
                                    intent.putExtra("user_id", user_id);
                                    v.getContext().startActivity(intent);
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("냠", "exception");
                }
            }
        };

        if (root_post_type == 0) {
            GetRootPost_Owner getRootPost = new GetRootPost_Owner(root_post_type, root_post_id, PostresponseOwner);
            RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
            queue.add(getRootPost);
        } else {
            GetRootPost_Player getRootPost = new GetRootPost_Player(root_post_type, root_post_id, PostresponsePlayer);
            RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
            queue.add(getRootPost);
        }


//top TextView init
        String state_txt;
        int flag = 0;

        if(state != state_demand && state == 1){ //매칭요구가 있다면

            if( (owner_id == user_id && demand_who == 0) || (owner_id != user_id && demand_who == 1)){
                flag = 1;
            }

            if(flag == 1){ //요구한 사람이 나라면

                if(state_demand == 2){
                    state_txt = "확정 대기중";
                }
                else{
                    state_txt = "error";
                }
                state_view.setTextColor(Color.parseColor("#1E8BEB"));
                state_view.setText(state_txt);


            }else{
                state_txt = "매칭중";
                state_view.setTextColor(Color.parseColor("#1E8BEB"));
                state_view.setText(state_txt);
            }

        }
        else if(state != state_demand && state == 2) {//취소요구가 있다면
            if(state_demand == 3){
                state_txt = "취소 대기중";
            }
            else{
                state_txt = "error";
            }
            state_view.setTextColor(Color.parseColor("#1E8BEB"));
            state_view.setText(state_txt);
        }

        else if(state == state_demand){ //매칭요구가 없음
            switch (state){
                case 0 : {
                    state_txt = "채팅종료";
                    state_view.setBackgroundColor(Color.rgb(255, 0, 0));
                    state_view.setTextColor(Color.rgb(255, 255, 255));
                    break;
                }
                case 2 : {
                    state_txt = "매칭확정";
                    state_view.setTextColor(Color.parseColor("#3CC56B"));
                    break;
                }
                case 3: {
                    state_txt = "매칭취소";
                    state_view.setTextColor(Color.parseColor("#CD3A32"));
                    break;
                }
                default : state_txt = "error";
            }
            state_view.setText(state_txt);
        }
        else{
            state_txt = "error";
            state_view.setText(state_txt);
        }





        if (state == 0 && state == state_demand) {//매칭 미확정일 때
            state_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final LinearLayout linear = (LinearLayout) View.inflate(chatItemView.this, R.layout.popup_end_chat, null);
                    AlertDialog.Builder builder = new AlertDialog.Builder(chatItemView.this);
                    builder.setView(linear);

                    final ListView list = (ListView)linear.findViewById(R.id.listView);

                    TextView title = (TextView)linear.findViewById(R.id.title);

                    if(root_post_type == 0){
                        list.setVisibility(View.GONE);

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray("result");

                                    instrumentList.clear();

                                    for (int i = 0; i < result.length(); i++) {
                                        JSONObject c = result.getJSONObject(i);
                                        need_position = c.getString("need_position");
                                        position_state = c.getString("position_state");
                                        String[] positionList = need_position.split(" ");
                                        String[] positionStateList = position_state.split(" ");


                                        for (int j = 0; j < positionList.length; j++){
                                            Log.d("냠", positionList[j]);
                                            if(positionStateList[j].equals("0")){
                                                instrumentList.add(positionList[j]);
                                                instrumentStateList.add(Integer.parseInt(positionStateList[j]));
                                                Log.d("냠", "악기 : " + positionList[j] + " 포지션 : " + positionStateList[j]);
                                            }
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("냠", "exception");
                                }

                            }
                        };
                        chatRoomRequest chatRoomRequest = new chatRoomRequest(chat_id, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                        queue.add(chatRoomRequest);

                        final Spinner instrumentSpinner = (Spinner) linear.findViewById(R.id.spinner);
                        ArrayAdapter spinnerAdapter_instrument;
                        spinnerAdapter_instrument = new ArrayAdapter<String>(chatItemView.this, R.layout.spinner_item, instrumentList);
                        spinnerAdapter_instrument.setDropDownViewResource(R.layout.spinner_dropdown);
                        instrumentSpinner.setAdapter(spinnerAdapter_instrument);
                        instrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                position_index = i;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                            }
                        });
                    }else{ //매칭 미확정이면서 연주게시판으로부터 파생된 채팅
                        title.setText("어떤 연주에 매칭할지 선택해주세요.");

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("냠", "통신");

                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray result = jsonObject.getJSONArray("result");
                                    instrumentList.clear();

                                    for (int i = 0; i<result.length(); i++){
                                        JSONObject c = result.getJSONObject(i);
                                        int post_id = c.getInt("post_id");
                                        String title = c.getString("title");
                                        String name = c.getString("writer");
                                        String place = c.getString("place");
                                        String play_date = c.getString("play_date");
                                        String pay = c.getString("pay");
                                        String content = c.getString("content");
                                        String post_date = c.getString("post_date");

                                        int state = c.getInt("state");

                                        String need_position = c.getString("need_position");
                                        String[] position = need_position.split(" ");
                                        String need_position_state = c.getString("position_state");
                                        String[] position_state = need_position_state.split(" ");

                                        PostItemModel itemModel = new PostItemModel(post_id, owner_id, name, title, place, play_date, state, position, pay, content, position_state, post_date);
                                        if(itemModel.getState() == 0){
                                            PostitemModelsActive.add(0, itemModel);
                                        }
                                        PostitemModels.add(0, itemModel);

                                    }

                                    final Spinner instrumentSpinner = (Spinner) linear.findViewById(R.id.spinner);
                                    final ArrayAdapter[] spinnerAdapter_instrument = new ArrayAdapter[1];
                                    spinnerAdapter_instrument[0] = new ArrayAdapter<String>(chatItemView.this, R.layout.spinner_item, instrumentList);
                                    spinnerAdapter_instrument[0].setDropDownViewResource(R.layout.spinner_dropdown);
                                    instrumentSpinner.setAdapter(spinnerAdapter_instrument[0]);
                                    instrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                            position_index = i;
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {
                                        }
                                    });
                                    instrumentSpinner.setSelection(0);

                                    postAdapter = new chatChoosePostAdapter(PostitemModels, chatItemView.this, user_id);
                                    list.setAdapter(postAdapter);

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            list.setBackgroundColor(Color.parseColor("#ffeadb"));
                                            instrumentList.clear();
                                            Toast.makeText(getApplicationContext(),PostitemModels.get(i).getTitle(),Toast.LENGTH_SHORT).show();

                                            String[] positionList = PostitemModels.get(i).getNeed_position();
                                            changed_post_id = PostitemModels.get(i).getPost_id();
                                            String[] positionStateList = PostitemModels.get(i).getPosition_state();
                                            for(int j=0; j<positionList.length; j++){
                                                if(positionStateList[j].equals("0")){
                                                    instrumentList.add(positionList[j]);
                                                }
                                            }
                                            spinnerAdapter_instrument[0] = new ArrayAdapter<String>(chatItemView.this, R.layout.spinner_item, instrumentList);
                                            spinnerAdapter_instrument[0].setDropDownViewResource(R.layout.spinner_dropdown);
                                            instrumentSpinner.setAdapter(spinnerAdapter_instrument[0]);
                                            instrumentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                @Override
                                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                                    position_index = i;
                                                }

                                                @Override
                                                public void onNothingSelected(AdapterView<?> adapterView) {
                                                }
                                            });
                                            instrumentSpinner.setSelection(0);
                                        }
                                    });



                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("냠", "exception");
                                }

                            }
                        };
                        ChatChoosePost chatChoosePost = new ChatChoosePost(chat_id, owner_id, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                        queue.add(chatChoosePost);


                    }


                    final int demander;
                    if(owner_id == user_id){
                        demander = 0;
                    }else{
                        demander = 1;
                    }
                    Log.d("냠", "demander:"+demander);

                    builder.setPositiveButton("매칭확정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if((root_post_type == 0 && rootOwnerPostitem.getState() != 0) || (root_post_type == 1 && rootPlayerPostitem.getState() != 0)){
                                if(root_post_type == 0){
                                    switch (rootOwnerPostitem.getState()){
                                        case 1 : {
                                            Toast.makeText(chatItemView.this, "이미 연주완료된 연주이므로 매칭을 확정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                        case 2 : {
                                            Toast.makeText(chatItemView.this, "연주가 취소된 연주이므로 매칭을 확정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                            break;
                                        }
                                    }
                                }else{
                                    Toast.makeText(chatItemView.this, "연주자가 연주를 중단했으므로 매칭을 확정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                state = 1;
                                state_demand = 2;
                            /*stateList[position_index] = "1";
                            for (int i = 0; i < stateList.length; i++)
                                position_state += stateList[i] + " ";*/
                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if (success)
                                                Toast.makeText(getApplicationContext(), "매칭확정 대기중", Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("냠", "exception");
                                        }

                                    }
                                };

                                Log.d("냠", "demander2:"+demander);
                                MatchRequest matchRequest = new MatchRequest(chat_id, state, state_demand, demander, position_index, changed_post_id, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                                queue.add(matchRequest);

                                ((chatList)chatList.CONTEXT).onResume();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 2000);
                            }

                        }
                    });
                    builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    builder.setNegativeButton("매칭취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            state = 1;
                            state_demand = 3;
                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("냠", "취소매칭통신");
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) {
                                            Toast.makeText(getApplicationContext(), "매칭취소", Toast.LENGTH_SHORT).show();
                                            state = 3;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("냠", "exception");
                                    }

                                }
                            };
                            MatchRequest matchRequest2 = new MatchRequest(chat_id, state, state_demand, demander,-1, changed_post_id, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                            queue.add(matchRequest2);

                            ((chatList)chatList.CONTEXT).onResume();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);

                        }

                    });
                    builder.show();

                }
            });
        } else if (state == 1) {//매칭중 상태일 때

            if( (owner_id == user_id && demand_who == 0) || (owner_id != user_id && demand_who == 1) ){ //demander = user
                LinearLayout blackPage = (LinearLayout)findViewById(R.id.blackpage);
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater.inflate(R.layout.page_waiting, blackPage, true);

                TextView stateDemand = (TextView)blackPage.findViewById(R.id.state_demand);
                Button cancleDemand = (Button)blackPage.findViewById(R.id.cancle_demand);

                switch (state_demand){
                    case 2 : {
                        stateDemand.setText("매칭확정");
                        stateDemand.setTextColor(Color.parseColor("#0174DF"));
                        break;
                    }
                    case 3 : {
                        stateDemand.setText("매칭취소");
                        stateDemand.setTextColor(Color.parseColor("#CD3A32"));
                        break;
                    }
                }

                cancleDemand.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        state = 0;
                        state_demand = 0;

                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    Log.d("냠", "매칭요구 취소통신");
                                    JSONObject jsonObject = new JSONObject(response);
                                    boolean success = jsonObject.getBoolean("success");
                                    if (success) {
                                        Toast.makeText(getApplicationContext(), "요청이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                        state = 0;
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("냠", "exception");
                                }
                            }
                        };

                        MatchRequest matchRequest3 = new MatchRequest(chat_id, state, state_demand, -1, -1, 0, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                        queue.add(matchRequest3);

                        ((chatList)chatList.CONTEXT).onResume();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                finish();
                            }
                        }, 2000);

                    }
                });

            }else{  //demander != sender

                LinearLayout blackPage = (LinearLayout)findViewById(R.id.blackpage);
                LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater.inflate(R.layout.page_waiting_not_demander, blackPage, true);

                TextView stateDemand = (TextView)blackPage.findViewById(R.id.state_demand);
                TextView info = (TextView)blackPage.findViewById(R.id.info);

                String demand_state = "";
                switch (state_demand){
                    case 2 : {
                        demand_state = "매칭확정";
                        stateDemand.setText(demand_state);
                        stateDemand.setTextColor(Color.parseColor("#0174DF"));
                        info.setText("수락시 해당 연주자와 매칭됩니다.");
                        break;
                    }
                    case 3 : {
                        demand_state = "매칭취소";
                        stateDemand.setText(demand_state);
                        stateDemand.setTextColor(Color.parseColor("#CD3A32"));
                        info.setText("수락시 채팅이 종료됩니다.");
                        break;
                    }
                }
//여기
                if(state_demand == 3){

                    final androidx.appcompat.app.AlertDialog.Builder removeDialog = new androidx.appcompat.app.AlertDialog.Builder(chatItemView.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    removeDialog.setMessage("요청을 수락하시겠습니까? \n수락시 채팅이 종료됩니다.")
                            .setTitle("매칭취소")
                            .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    state = 0; state_demand = 0;

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(getApplicationContext(), "요청을 거부하였습니다.", Toast.LENGTH_SHORT).show();
                                                    state = 0;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("냠", "exception");
                                            }
                                        }
                                    };

                                    MatchRequest matchRequest3 = new MatchRequest(chat_id, state, state_demand, -1, -1, changed_post_id, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                                    queue.add(matchRequest3);

                                    ((chatList)chatList.CONTEXT).onResume();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);

                                }
                            })
                            .setNeutralButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    state = 3; state_demand = 3;

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(getApplicationContext(), "채팅이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                                                    state = 3;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("냠", "exception");
                                            }
                                        }
                                    };
                                    MatchRequest matchRequest3 = new MatchRequest(chat_id, state, state_demand, -1, -1, changed_post_id, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                                    queue.add(matchRequest3);

                                    ((chatList)chatList.CONTEXT).onResume();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);
                                }
                            })
                            .setCancelable(false)
                            .show();

                }else{

                    final androidx.appcompat.app.AlertDialog.Builder removeDialog = new androidx.appcompat.app.AlertDialog.Builder(chatItemView.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    removeDialog.setMessage("요청을 수락하시겠습니까? \n수락시 채팅이 종료됩니다.")
                            .setTitle("매칭확정")
                            .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    state = 0; state_demand = 0;

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(getApplicationContext(), "요청을 거부하였습니다.", Toast.LENGTH_SHORT).show();
                                                    state = 0;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("냠", "exception");
                                            }
                                        }
                                    };

                                    MatchRequest matchRequest3 = new MatchRequest(chat_id, state, state_demand, -1, -1, changed_post_id, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                                    queue.add(matchRequest3);

                                    ((chatList)chatList.CONTEXT).onResume();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);

                                }
                            })
                            .setNeutralButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    state = 2; state_demand = 2;

                                    if(root_post_type == 0){

                                        String positionState = "";

                                        Log.d("냠", "ListSize : 악기 = " + instrumentList.size() + " 상태 = " + instrumentStateList.size());
                                        String temp = "";
                                        for(int i = 0 ; i<instrumentStateList.size() ; i++){
                                            temp += instrumentStateList.get(i).toString() + " ";
                                        }
                                        Log.d("냠","StateList : " + temp);

                                        for(int i = 0 ; i<instrumentList.size() ; i++){
                                            if(i != position){
                                                Log.d("냠", "plus : List");
                                                positionState += instrumentStateList.get(i).toString() + " ";
                                                Log.d("냠", "state : " + instrumentStateList.get(i));
                                            }else{
                                                positionState += "1" + " ";
                                                Log.d("냠", "state : 1");
                                            }
                                            Log.d("냠", "positionState : " + positionState);
                                        }

                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if (success) {
                                                        Toast.makeText(getApplicationContext(), "채팅이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                                                        state = 2;
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Log.d("냠", "exception");
                                                }
                                            }
                                        };
                                        MatchSuccess matchSuccess = new MatchSuccess(chat_id, root_post_id, state, state_demand, positionState, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                                        queue.add(matchSuccess);

                                        ((chatList)chatList.CONTEXT).onResume();

                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        }, 2000);

                                    }else{ //매칭확정 예 누르고 연주게시판에서 파생된 채팅인 경우

                                        //root_post가 owner가 아니므로 owner_post 하나를 골라주는 작업이 필요->changed_post_id
                                        // dialog에 listView 넣어서 선택하게 하자 > 711 line
                                        //선택한 이후 작업은 root가 owner인 경우와 동일
                                        // + chat table에 root_post에 관련한 정보를 새로고른 것으로 바꿔준다.
                                        // (이후 rating 작업을 편리하게 하기 위함)
                                        Response.Listener<String> responseListener = new Response.Listener<String>() {
                                            @Override
                                            public void onResponse(String response) {
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response);
                                                    boolean success = jsonObject.getBoolean("success");
                                                    if (success) {
                                                        Toast.makeText(getApplicationContext(), "매칭이 확정되었습니다.", Toast.LENGTH_SHORT).show();
                                                        state = 2;
                                                        Log.d("냠","changed_post_id:" + changed_post_id);
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    Log.d("냠", "exception");
                                                }
                                            }
                                        };
                                        MatchRootRequest matchRootRequest = new MatchRootRequest(chat_id, state, changed_post_id, responseListener);
                                        RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                                        queue.add(matchRootRequest);



                                    }


                                }
                            })
                            .setCancelable(false)
                            .show();

                }

            }


        } else if (state == 2) {//매칭확정상태 > cancelable

            if(state == state_demand) {//state = state_demand = 2

                LinearLayout blackPage = (LinearLayout) findViewById(R.id.blackpage);
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                inflater.inflate(R.layout.page_after_matching, blackPage, true);

                TextView Dday = (TextView) blackPage.findViewById(R.id.d_day);
                Button cancel = (Button) blackPage.findViewById(R.id.btn_cancel);

                /*Date now = new Date();
                String playdate = rootOwnerPostitem.getPlay_date();
                SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
                Date playDate = fm.parse(playdate);

                long calDate  = now.getTime() - playDate.getTime();
                long calDateDays = calDate / (24*60*60*1000);

                calDateDays = Math.abs(calDateDays);

                Dday.setText(calDateDays+"");*/
                //rootOwnerPostItem.getPlay_date 값이 널이라고 함 > 확인 필요

                //cancle Click Event : Use custom Dialog, Get reason for cancel using Edit Text
                //Chat State에 state = 2, state_demand = 3인 경우도 넣어야됨...
                //확정 했다가 취소하는 경우

                cancel.setOnClickListener(new View.OnClickListener() {//매칭확정 취소 버튼 눌렀을 때
                    @Override
                    public void onClick(View view) {

                        final LinearLayout linear = (LinearLayout) View.inflate(chatItemView.this, R.layout.popup_end_match_reason, null);
                        AlertDialog.Builder builder = new AlertDialog.Builder(chatItemView.this);
                        builder.setView(linear);

                        final EditText et_reason = (EditText) linear.findViewById(R.id.et_reason);


                        builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        builder.setPositiveButton("매칭취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                state_demand = 3;

                                final int demander;
                                if (owner_id == user_id) {
                                    demander = 0;
                                } else {
                                    demander = 1;
                                }
                                Log.d("냠", "demander:" + demander);

                                cancel_reason = et_reason.getText().toString();
                                Log.d("냠", "cancel_reason:" + cancel_reason);

                                Response.Listener<String> responseListener = new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        try {
                                            JSONObject jsonObject = new JSONObject(response);
                                            boolean success = jsonObject.getBoolean("success");
                                            if (success)
                                                Toast.makeText(getApplicationContext(), "매칭취소 대기중", Toast.LENGTH_SHORT).show();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            Log.d("냠", "exception");
                                        }

                                    }
                                };


                                CancelRequest cancelRequest = new CancelRequest(chat_id, state, state_demand, demander, position, cancel_reason, responseListener);
                                RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                                queue.add(cancelRequest);

                                ((chatList) chatList.CONTEXT).onResume();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        finish();
                                    }
                                }, 2000);
                            }
                        });
                        builder.show();


                    }
                });
            }

            else if(state != state_demand && state_demand == 3) { //취소요청 했을 때

                if( (owner_id == user_id && demand_who == 0) || (owner_id != user_id && demand_who == 1) ){ //demander = user
                    LinearLayout blackPage2 = (LinearLayout)findViewById(R.id.blackpage);
                    LayoutInflater inflater2 = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    inflater2.inflate(R.layout.page_waiting, blackPage2, true);

                    TextView stateDemand = (TextView)blackPage2.findViewById(R.id.state_demand);
                    Button cancleDemand = (Button)blackPage2.findViewById(R.id.cancle_demand);

                    stateDemand.setText("매칭취소");
                    stateDemand.setTextColor(Color.parseColor("#CD3A32"));

                    cancleDemand.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {//매칭취소의 취소버튼을 눌렀을 때
                            state_demand = 2;

                            Response.Listener<String> responseListener = new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        Log.d("냠", "매칭취소");
                                        JSONObject jsonObject = new JSONObject(response);
                                        boolean success = jsonObject.getBoolean("success");
                                        if (success) {
                                            Toast.makeText(getApplicationContext(), "요청이 취소되었습니다.", Toast.LENGTH_SHORT).show();
                                            state = 0;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Log.d("냠", "exception");
                                    }
                                }
                            };

                            CancelRequest cancelRequest = new CancelRequest(chat_id, state, state_demand, demand_who, position, cancel_reason, responseListener);
                            RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                            queue.add(cancelRequest);

                            ((chatList)chatList.CONTEXT).onResume();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 2000);

                        }
                    });

                }
                else{//매칭취소 요구자가 상대방일 때
                    LinearLayout blackPage = (LinearLayout)findViewById(R.id.blackpage);
                    LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    inflater.inflate(R.layout.page_waiting_not_demander, blackPage, true);

                    TextView stateDemand = (TextView)blackPage.findViewById(R.id.state_demand);
                    TextView info = (TextView)blackPage.findViewById(R.id.info);

                    String demand_state = "매칭취소";
                    stateDemand.setText(demand_state);
                    stateDemand.setTextColor(Color.parseColor("#CD3A32"));
                    info.setText("수락시 채팅이 종료됩니다.");

                    final androidx.appcompat.app.AlertDialog.Builder removeDialog = new androidx.appcompat.app.AlertDialog.Builder(chatItemView.this, android.R.style.Theme_DeviceDefault_Light_Dialog);
                    removeDialog.setMessage("취소 사유는 ["+cancel_reason+"]입니다.\n요청을 수락하시겠습니까? \n수락시 채팅이 종료됩니다.")
                            .setTitle("매칭취소")
                            .setPositiveButton("아니오", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    state = 2; state_demand = 2;

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(getApplicationContext(), "요청을 거부하였습니다.", Toast.LENGTH_SHORT).show();
                                                    state = 0;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("냠", "exception");
                                            }
                                        }
                                    };

                                    CancelRequest cancelRequest = new CancelRequest(chat_id, state, state_demand, demand_who, position, "-", responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                                    queue.add(cancelRequest);

                                    ((chatList)chatList.CONTEXT).onResume();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);

                                }
                            })
                            .setNeutralButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    state = 3; state_demand = 3;

                                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            try {
                                                JSONObject jsonObject = new JSONObject(response);
                                                boolean success = jsonObject.getBoolean("success");
                                                if (success) {
                                                    Toast.makeText(getApplicationContext(), "채팅이 종료되었습니다.", Toast.LENGTH_SHORT).show();
                                                    state = 3;
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Log.d("냠", "exception");
                                            }
                                        }
                                    };
                                    MatchRequest matchRequest3 = new MatchRequest(chat_id, state, state_demand, -1, -1, changed_post_id, responseListener);
                                    RequestQueue queue = Volley.newRequestQueue(chatItemView.this);
                                    queue.add(matchRequest3);

                                    ((chatList)chatList.CONTEXT).onResume();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finish();
                                        }
                                    }, 2000);
                                }
                            })
                            .setCancelable(false)
                            .show();


                }
            }




        } else if (state == 3) {//매칭취소상태 > 채팅 완전 종료이므로 재개 불가
            inputMessage.setClickable(false);
            inputMessage.setEnabled(false);
            inputMessage.setFocusable(false);
            inputMessage.setFocusableInTouchMode(false);
            inputMessage.setText("종료된 채팅입니다.");

            sendBtn.setClickable(false);
            sendBtn.setEnabled(false);
            sendBtn.setFocusable(false);
            sendBtn.setFocusableInTouchMode(false);

        }


        //제목줄 제목글씨를 닉네임으로(또는 채팅방)
        //getSupportActionBar().setTitle(상대방아이디);

        // kjy context 부분 추가
        adapter = new messageAdapter(this, user_id, messageItems, getLayoutInflater());
        chat.setAdapter(adapter);

        //firebaseDB에서 채팅 메세지들 실시간 읽어오기..
        //'chat'노드에 저장되어 있는 데이터들을 읽어오기
        //databaseReference에 데이터가 변경되는 것을 듣는 리스너 추가
        databaseReference.addChildEventListener(new ChildEventListener() {
            //새로 추가된 것만 줌 ValueListener는 하나의 값만 바뀌어도 처음부터 다시 값을 줌
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                //새로 추가된 데이터(값 : chatData객체) 가져오기
                chatData messageItem = dataSnapshot.getValue(chatData.class);
                if (chat_id == messageItem.getChat_id()) {
                    Log.d("냠냠", "chat_id:" + messageItem.getChat_id());
                    Log.d("냠냠", "user_id:" + messageItem.getUser_id());

                    //새로운 메세지를 리스트뷰에 추가하기 위해 ArrayList에 추가
                    messageItems.add(messageItem);

                    //리스트뷰를 갱신
                    adapter.notifyDataSetChanged();
                    chat.setSelection(messageItems.size() - 1); //리스트뷰의 마지막 위치로 스크롤 위치 이동
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}