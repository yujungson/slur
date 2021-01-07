package com.example.slur.profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.slur.LoginActivity;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.home;
import com.example.slur.post.ProfileRequest;
import com.example.slur.postTypeSelect;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.slur.R.id.tv_thumbnail_title;

//import static com.example.slur.R.id.tv_thumbnail_title;

public class profile extends AppCompatActivity {

    ImageView profileImg;
    TextView nicknameTv, emailTv, universityTv, majorTv, major2Tv, contactTv, introductionTv;
    RecyclerView ClipView;
    ClipAdapter clAdapter;
    Button ModifyBtn;
    Button PostlistBtn, heartBtn;
    int user_id;

    ImageView menu_home, menu_post, menu_rating, menu_chat, menu_profile;

    private static final int REQUEST_EDIT = 100;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        preferenceHelper = new PreferenceHelper(getApplicationContext());
        user_id = preferenceHelper.getUserId();

        initViews();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_EDIT) {
            loadData();
        }
    }

    private void initViews() {
        profileImg = (ImageView) findViewById(R.id.profile_img);
        nicknameTv = (TextView) findViewById(R.id.nick_name);
        emailTv = (TextView) findViewById(R.id.email);
        universityTv = (TextView) findViewById(R.id.tv_university);
        majorTv = (TextView) findViewById(R.id.tv_major);
        major2Tv = (TextView) findViewById(R.id.tv_major_2);
        contactTv = (TextView) findViewById(R.id.tv_contact);
        introductionTv = (TextView) findViewById(R.id.tv_introduction);

        ClipView = findViewById(R.id.recycler_view);
        ClipView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        ArrayList<VideoClip> list = makeDefaultClip(); //테스트용 클립생성
        clAdapter = new ClipAdapter(list);
        ClipView.setAdapter(clAdapter);


        ModifyBtn = findViewById(R.id.btn_modify_info);

        //프로필 수정화면으로 이동
        ModifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileEditActivity.class));
            }
        });

       //작성글보기
        PostlistBtn = findViewById(R.id.btn_show_comment);
        PostlistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPostTypeSelect.class);
                startActivity(intent);
            }
        });


        //찜한글보기
        heartBtn = findViewById(R.id.btn_show_wish);
        heartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HeartPostTypeSelect.class);
                startActivity(intent);
            }
        });


        loadData();
    }

    private ArrayList<VideoClip> makeDefaultClip() {
        ArrayList<VideoClip> list = new ArrayList<>();
        list.add(new VideoClip(null, null, "첫째설명"));
        list.add(new VideoClip(null, null, "둘째설명"));
        list.add(new VideoClip(null, null, "셋째설명"));
        return list;
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;

        public ReviewHolder(View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.img_thumbnail);
            title = itemView.findViewById(tv_thumbnail_title);
        }
    }


    private class ClipAdapter extends RecyclerView.Adapter<ReviewHolder> {
        ArrayList<VideoClip> list;

        public ClipAdapter(ArrayList<VideoClip> list) {
            this.list = list;
        }

        @Override
        public ReviewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.activity_video_clip_list_item, null, false);
            return new ReviewHolder(view);
        }


        @Override
        public void onBindViewHolder(ReviewHolder rowViewHolder, final int pos) {
            VideoClip clip = list.get(pos);
            rowViewHolder.thumbnail.setBackgroundResource(R.drawable.profile);// 화면 테스트 썸네일
            rowViewHolder.title.setText(clip.getTitle());// 화면 테스트 텍스트

        }

        // 아이템 갯수에 따라서 뷰 홀더 보이기
        @Override
        public int getItemCount() {
            return list.size();
        }
    }


    Response.Listener<String> profileResponseListener = new Response.Listener<String>() {

        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                boolean success = jsonObject.getBoolean("success");

                if (success) {
                    JSONObject profile = jsonObject.getJSONObject("profile");

                    String base64String = profile.getString("profile_img");
                    if (!base64String.equals("")) {
                        byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        profileImg.setImageBitmap(bitmap);
                    }
                    nicknameTv.setText(profile.getString("name"));
                    emailTv.setText(profile.getString("email"));
                    universityTv.setText(profile.getString("aff"));
                    if (profile.getString("if_major").equals("")) {
                        majorTv.setText("");
                    } else if (profile.getString("if_major").equals("2")) {
                        majorTv.setText("비전공자");
                    } else {
                        majorTv.setText("전공자");
                    }
                    major2Tv.setText(profile.getString("major"));
                    contactTv.setText(profile.getString("phone"));
                    introductionTv.setText(profile.getString("info"));

                } else {//로그인 실패시
                    Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
                    preferenceHelper.logout();
                    Intent intent = new Intent(profile.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private void loadData() {
        ProfileRequest profileRequest = new ProfileRequest(preferenceHelper.getUserId(), profileResponseListener);
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(profileRequest);
    }
}