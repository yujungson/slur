package com.example.slur.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.slur.R;
import com.example.slur.post.PostItemModel;

import java.util.List;

public class PostAdapter extends BaseAdapter {

    private List<PostItemModel> itemsModelList;
    private Context context;
    int user_id;

    public PostAdapter(List<PostItemModel> itemsModelList, Context context, int user_id) {
        this.itemsModelList = itemsModelList;
        this.context = context;
        this.user_id = user_id;
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
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ownerpost_view, parent, false);

            int post_id; String post_state;
            TextView title = view.findViewById(R.id.title);
            TextView state = view.findViewById(R.id.state);
            TextView writer = view.findViewById(R.id.writer);
            TextView play_date = view.findViewById(R.id.play_date);
            TextView place = view.findViewById(R.id.place);

            PostItemModel item = itemsModelList.get(position);

            post_id = item.getPost_id();
            title.setText(item.getTitle());
            writer.setText(item.getWriter());
            play_date.setText(item.getPlay_date());
            place.setText(item.getPlace());

            switch (item.getState()){
                case 0 : {
                    post_state = "섭외중";
                    state.setTextColor(Color.parseColor("#088A29"));
                    break;}
                case 1 : {
                    post_state = "연주완료";
                    state.setTextColor(Color.parseColor("#0174DF"));
                    break;
                }
                case 2 : {
                    post_state = "연주취소";
                    break;
                }
                default : post_state = "error"; break;
            }
            state.setText(post_state);

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ownerPostItemView.class);
                intent.putExtra("item", itemsModelList.get(position));
                intent.putExtra("user_id", user_id);
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }



}
