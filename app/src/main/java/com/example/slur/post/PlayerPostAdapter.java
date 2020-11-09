package com.example.slur.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.slur.R;

import java.util.List;

public class PlayerPostAdapter extends BaseAdapter {

    private List<PlayerPostItemModel> itemsModelList;
    private Context context;
    int user_id;

    public PlayerPostAdapter(List<PlayerPostItemModel> itemsModelList, Context context, int user_id) {
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
            view = inflater.inflate(R.layout.playerpost_view, parent, false);

            int post_id; String post_state;
            TextView title = view.findViewById(R.id.title);
            TextView state = view.findViewById(R.id.state);
            TextView writer = view.findViewById(R.id.writer);
            TextView place = view.findViewById(R.id.place);

            PlayerPostItemModel item = itemsModelList.get(position);

            post_id = item.getPost_id();
            title.setText(item.getTitle());
            writer.setText(item.getWriter());
            if(item.getPlace().length > 1)
                place.setText(item.getPlace()[0]+" 외 " + (item.getPlace().length-1) + "곳");
            else
                place.setText(item.getPlace()[0]);

            switch (item.getState()){
                case 0 : {
                    post_state = "연주가능";
                    state.setTextColor(Color.parseColor("#088A29"));
                    break;}
                case 1 : {
                    post_state = "연주중단";
                    state.setTextColor(Color.parseColor("#A8A7A7"));
                    break;
                }
                default : post_state = "error"; break;
            }
            state.setText(post_state);

            //should setonClickListener

        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), playerPostItemView.class);
                intent.putExtra("item", itemsModelList.get(position));
                intent.putExtra("user_id", user_id);
                v.getContext().startActivity(intent);
            }
        });

        return view;
    }



}
