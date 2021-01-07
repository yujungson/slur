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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.slur.LoginActivity;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.post.ProfileRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.example.slur.R.id.tv_thumbnail_title;

// 다른 프로필 화면
public class OthersProfileActivity extends AppCompatActivity {

    ImageView profileImg;
    TextView nicknameTv, emailTv, universityTv, majorTv, major2Tv, contactTv, introductionTv;
    Button showPostBtn;
    RecyclerView ClipView;
    ClipAdapter CLipAdapter;

    int userId, view_userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);

        view_userID = getIntent().getIntExtra("user_id", 0);
       // userId = new PreferenceHelper(getApplicationContext()).getUserId();

        initViews();

        loadData();
    }

    // View 초기화
    private void initViews() {
        profileImg = (ImageView) findViewById(R.id.profile_img);
        nicknameTv = (TextView) findViewById(R.id.nick_name);
        emailTv = (TextView) findViewById(R.id.email);
        universityTv = (TextView) findViewById(R.id.tv_university);
        majorTv = (TextView) findViewById(R.id.tv_major);
        major2Tv = (TextView) findViewById(R.id.tv_major_2);
        contactTv = (TextView) findViewById(R.id.tv_contact);
        introductionTv = (TextView) findViewById(R.id.tv_introduction);
        showPostBtn = (Button) findViewById(R.id.btn_show_post);

        ClipView = findViewById(R.id.recycler_view);
        ClipView.setHasFixedSize(true);
        ClipView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        CLipAdapter = new ClipAdapter();
        ClipView.setAdapter(CLipAdapter);

        makeDefaultClip();
    }

    //디폴트 생성
    private void makeDefaultClip() {
        ArrayList<VideoClip> list = new ArrayList<>();
        list.add(new VideoClip(null, null, "첫번째 영상"));
        list.add(new VideoClip(null, null, "두번째 영상"));
        list.add(new VideoClip(null, null, "세번째 영상"));
        CLipAdapter.onRefresh(list);
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


        // avoid null
        ArrayList<VideoClip> list = new ArrayList<>();

        @Override
        public ReviewHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_video_clip_list_item, null, false);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(lp);
            return new ReviewHolder(view);
        }


        @Override
        public void onBindViewHolder(ReviewHolder rowViewHolder, final int pos) {
            VideoClip clip = list.get(pos);
            rowViewHolder.thumbnail.setBackgroundResource(R.drawable.profile);
            rowViewHolder.title.setHint(clip.getTitle());
            rowViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 타인 프로필의 클립 선택시 플레이어 팝업 호출
                    new PlayerPopup(OthersProfileActivity.this).show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void onRefresh(ArrayList<VideoClip> list) {
            if (list == null || list.size() == 0) {
                list.clear();
            } else {
                this.list = list;
            }
            this.notifyDataSetChanged();
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
                    showPostBtn.setText(profile.getString("name") + "  님의 작성한 글 보기");
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
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private void loadData() {
        if (view_userID > 0) {
            ProfileRequest profileRequest = new ProfileRequest(view_userID, profileResponseListener);
            RequestQueue queue = Volley.newRequestQueue(this);
            queue.add(profileRequest);
        } else {
            Toast.makeText(getApplicationContext(), "정보가 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}