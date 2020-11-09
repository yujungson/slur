package com.example.slur.post;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.slur.R;

import java.util.ArrayList;

public class OwnerPositionAdapter extends RecyclerView.Adapter<OwnerPositionAdapter.CustomViewHolder> {
    private ArrayList<OwnerPosition> mList;
    Context context;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView position;


        public CustomViewHolder(View view) {
            super(view);
            this.position = (TextView) view.findViewById(R.id.position_name);
        }
    }


    public OwnerPositionAdapter(ArrayList<OwnerPosition> list,  Context context) {
        this.mList = list;
        this.context = context;
    }



    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.position_item_match, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }




    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

        viewholder.position.setText(mList.get(position).getPosition());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}
