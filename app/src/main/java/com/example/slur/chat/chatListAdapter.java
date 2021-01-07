package com.example.slur.chat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.example.slur.R;
import com.example.slur.post.PostItemModel;
import com.example.slur.post.ownerPostItemView;

import java.util.List;

public class chatListAdapter extends BaseAdapter {

    private List<RoomItemModel> roomItemModelList;
    private Context context;
    private int user_id;

    public chatListAdapter(List<RoomItemModel> roomItemModelList, Context context, int user_id) {
        this.roomItemModelList = roomItemModelList;
        this.context = context;
        this.user_id = user_id;
    }

    @Override
    public int getCount() {
        return roomItemModelList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        Context c = parent.getContext();
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.chat_list_item_view, parent, false);

            TextView UsersPosition = view.findViewById(R.id.userposition);

            String sender;
            String sender_img = null;
            RoomItemModel item = roomItemModelList.get(position);

            if(user_id == item.getOwner_id()){
                sender = item.getPlayer_name();
                sender_img = item.getBase64String_player();
                UsersPosition.setText("player");
            }else{
                sender = item.getOwner_name();
                sender_img = item.getBase64String_owner();
                UsersPosition.setText("owner");
            }

            TextView senderName = (TextView)view.findViewById(R.id.nick_name);
            TextView LastMessage = (TextView)view.findViewById(R.id.message);
            TextView LastTime = (TextView)view.findViewById(R.id.time);
            TextView state = (TextView)view.findViewById(R.id.state);
            ImageView prof_image = (ImageView)view.findViewById(R.id.profile_img);

            senderName.setText(sender);
            if(item.getLastMessage().length() >= 15){
                LastMessage.setText(item.getLastMessage().substring(0,15));
            }else{
                LastMessage.setText(item.getLastMessage());
            }
            LastTime.setText(item.getLastTime());

            if (!sender_img.equals("")) {
                byte[] decodedString = Base64.decode(sender_img, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                prof_image.setImageBitmap(bitmap);
            }else{
                prof_image.setImageResource(R.drawable.account);
            }

            String state_txt;
            switch (item.getState()){
                case 0 : {
                    state_txt = "매칭 미완료";
                    break;
                }
                case 1 : {
                    state_txt = "매칭중";
                    state.setTextColor(Color.parseColor("#1E8BEB"));
                    break;
                }
                case 2 : {
                    if(item.getState_demand()==3){
                        state_txt = "취소 신청중";
                        state.setTextColor(Color.parseColor("#1E8BEB"));
                    }
                    else{
                        state_txt = "매칭확정";
                        state.setTextColor(Color.parseColor("#3CC56B"));
                    }
                    break;
                }
                case 3: {
                    state_txt = "매칭취소";
                    state.setTextColor(Color.parseColor("#CD3A32"));
                    break;
                }
                default : state_txt = "error";
            }
            state.setText(state_txt);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), chatItemView.class);
                intent.putExtra("chat_id", roomItemModelList.get(position).getChat_id());
                intent.putExtra("state", roomItemModelList.get(position).getState());
                intent.putExtra("stateDemand", roomItemModelList.get(position).getState_demand());
                intent.putExtra("demand_who", roomItemModelList.get(position).getDemand_who());
                intent.putExtra("root_post_type", roomItemModelList.get(position).getRoot_post_type());
                intent.putExtra("root_post_id", roomItemModelList.get(position). getRoot_post_id());
                intent.putExtra("owner_id", roomItemModelList.get(position).getOwner_id());
                intent.putExtra("position", roomItemModelList.get(position).getPosition());
                intent.putExtra("changed_post_id", roomItemModelList.get(position).getChanged_post_id());
                intent.putExtra("cancel_reason", roomItemModelList.get(position).getCancel_reason());

                // kjy  상대방 인덱스 전송
                if (roomItemModelList.get(position). getOwner_id() == user_id) {
                    intent.putExtra("other_id", roomItemModelList.get(position).getPlayer_id());
                } else {
                    intent.putExtra("other_id", roomItemModelList.get(position).getOwner_id());
                }
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }
}
