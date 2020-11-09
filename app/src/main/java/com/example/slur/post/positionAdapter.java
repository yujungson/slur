package com.example.slur.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.slur.R;

import java.util.List;

public class positionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<positionItem> itemList;
    Context context;

    public positionAdapter(List<positionItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    public static class myViewHolder extends RecyclerView.ViewHolder{
        LinearLayout container;
        TextView position_name;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.container);
            position_name = itemView.findViewById(R.id.position_name);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.position_item, parent, false);

        return new myViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        myViewHolder myviewHolder = (myViewHolder) holder;

        if(itemList.get(position).getState() == 1){
            myviewHolder.container.setBackgroundColor(context.getResources().getColor(R.color.active));
            myviewHolder.position_name.setTextColor(context.getResources().getColor(R.color.white));
        }
        myviewHolder.position_name.setText(itemList.get(position).getPosition());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
