package com.example.slur.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.profile.OthersProfileActivity;
import com.example.slur.profile.profile;

import java.util.ArrayList;
import java.util.List;

public class messageAdapter extends BaseAdapter {

    private Context context;

    private int user_id;

    ImageView profileImg;
    ArrayList<chatData> messageItems;
    LayoutInflater layoutInflater;
    private List<RoomItemModel> roomItemModelList;

    public messageAdapter(Context context, int user_id, ArrayList<chatData> messageItems, LayoutInflater layoutInflater) {
        this.context = context;
        this.user_id = user_id;
        this.messageItems = messageItems;
        this.layoutInflater = layoutInflater;
    }

    // 새로운 메시지 추가
    public void add(chatData data) {
        messageItems.add(data);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

    @Override
    public int getCount() {
        return messageItems.size();
    }

    @Override
    public Object getItem(int position) {
        return messageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        //현재 보여줄 번째의(position)의 데이터로 뷰를 생성
        final chatData item = messageItems.get(position);

        //재활용할 뷰는 사용하지 않음!!
        View itemView = null;


        //메세지가 내 메세지인지??
        if(item.getUser_id() == user_id){
            itemView = layoutInflater.inflate(R.layout.my_msgbox,viewGroup,false);
        }else{
            itemView = layoutInflater.inflate(R.layout.other_msgbox,viewGroup,false);
        }

        //만들어진 itemView에 값들 설정
        TextView tvName = itemView.findViewById(R.id.tv_name);
        TextView tvMsg = itemView.findViewById(R.id.tv_msg);
        TextView tvTime = itemView.findViewById(R.id.tv_time);

        // kjy
//        tvName.setText(Integer.toString(item.getUser_id()));
        tvMsg.setText(item.getMessage());
        tvTime.setText(item.getTime());

        // 프로필 이미지
        ImageView iv = itemView.findViewById(R.id.iv);

        if (item.getUser_id() == user_id) {  // 메시지 작성자== 사용자
            // 작성자의 프로필 이미지
            if (((chatItemView)context).getUserProfileImgBitmap() != null) {
                iv.setImageBitmap(((chatItemView) context).getUserProfileImgBitmap());
            }
            // 작성자 닉네임
            tvName.setText(((chatItemView) context).getUserNickname());
        }
        else {
            // 상대방의 프로필 이미지
            if (((chatItemView)context).getOtherProfileImgBitmap() != null) {
                iv.setImageBitmap(((chatItemView) context).getOtherProfileImgBitmap());
            }
            // 상대방 닉네임
            tvName.setText(((chatItemView) context).getOtherNickname());
        }

        // 이미지 클릭 시
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 프로필 화면으로 넘어가기
                Intent intent = new Intent(context, OthersProfileActivity.class);
                intent.putExtra("user_id", item.getUser_id());
                context.startActivity(intent);
            }
        });

        return itemView;
    }

}
