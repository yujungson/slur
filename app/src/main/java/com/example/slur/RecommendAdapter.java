package com.example.slur;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.slur.post.PlayerPostItemModel;
import com.example.slur.post.PostItemModel;

import java.util.List;

public class RecommendAdapter extends BaseAdapter {

    private List<RecommendItemModel> itemsModelList;
    private Context context;
    private PreferenceHelper preferenceHelper; // 쿠키 관련 객체

    public RecommendAdapter(List<RecommendItemModel> itemsModelList, Context context) {
        this.itemsModelList = itemsModelList;
        this.context = context;
        preferenceHelper = new PreferenceHelper(context); // 쿠키 관련 객체 초기화
    }

    @Override
    public int getCount() {
        return itemsModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup parent) {
        Context c = parent.getContext();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.recommend_view, parent, false);

            // Views
            ImageView profileImg = view.findViewById(R.id.profile_img);
            TextView nickname = view.findViewById(R.id.nick_name);
            TextView affMajor = view.findViewById(R.id.aff_major);
            TextView rating = view.findViewById(R.id.rating);
            TextView title = view.findViewById(R.id.title);
            AppCompatButton detailBtn = view.findViewById(R.id.detail_btn);


            //어댑터에서 아이템 가져 오기
            final RecommendItemModel item = itemsModelList.get(position);

            final int postId = item.getPost_id();
            String base64String = item.getImageStr();
            // 프로필이미지
            if (!TextUtils.isEmpty(base64String)) {
                //base64로 문자열을 디코딩하고 이미지 바이트 데이터 준비
                byte[] decodedString = Base64.decode(base64String, Base64.DEFAULT);
                // 이미지 바이트 데이터를 비트 맵 유형으로 전송
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                // 비트 맵 사용해서 프로필 이미지 설정
                profileImg.setImageBitmap(bitmap);
            } else {
                //디폴트이미지
                profileImg.setImageResource(R.drawable.profile);
            }
            //닉넴
            nickname.setText(item.getNickname());
            // 전공
            if (item.getUniversity().equals("")) {
                affMajor.setText("일반인");
            } else {
                affMajor.setText(item.getUniversity() + " " + (item.getMajor() == null ? "" : item.getMajor()));
            }
            // 평점
            rating.setText(String.format("%.1f", item.getRating()) + "/5.0");
            title.setText("\"" + item.getTitle() + "\"");

            //글보러가기버튼
            detailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // playerPostItemView로부터 전달 받은 경우
                    if (context instanceof com.example.slur.post.playerPostItemView) {
                        Intent intent = new Intent(context, com.example.slur.post.ownerPostItemView.class);
                        // ownerItem
                        PostItemModel temp = new PostItemModel(postId, item.getUser_id(), item.getNickname(), item.getTitle(), item.getPlace(), item.getPlay_date(), 0, item.getNeed_position(), item.getPay(), item.getContent(), item.getPosition_state().split(" "), item.getPost_date());
                        intent.putExtra("item", temp);
                        intent.putExtra("user_id", preferenceHelper.getUserId());
                        v.getContext().startActivity(intent);

                    // playerPostItemView로부터 전달 받은 경우
                    } else {
                        Intent intent = new Intent(context, com.example.slur.post.playerPostItemView.class);
                        // playerItem
                        PlayerPostItemModel temp = new PlayerPostItemModel(postId, item.getNickname(), item.getTitle(), 0, item.getPlace().split(" "), item.getNeed_position(), item.getUser_id(), item.getContent(), item.getPost_date(), item.getPay());
                        intent.putExtra("item", temp);
                        intent.putExtra("user_id", preferenceHelper.getUserId());
                        v.getContext().startActivity(intent);
                    }

                }
            });

            profileImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 프로필 화면가기
                    Intent intent = new Intent(context, com.example.slur.profile.OthersProfileActivity.class);
                    intent.putExtra("user_id", item.getUser_id());
                    v.getContext().startActivity(intent);
                }
            });

        }

        return view;
    }
}
