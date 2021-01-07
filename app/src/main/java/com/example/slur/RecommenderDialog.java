package com.example.slur;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.widget.AppCompatButton;

import com.example.slur.post.ownerPostItemView;
import com.example.slur.post.playerPostItemView;

import java.util.List;

public class RecommenderDialog {
    private Context context;
    private Dialog dialog;

    private AppCompatButton closeBtn;
    private ListView recommendRecyclerList;

    private RecommendAdapter recommendAdapter;

    private List<RecommendItemModel> recommendItemModels, filteredItems;

    public RecommenderDialog(final Context context, String[] positionList) {
        this.context = context;

        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.95);
        int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.95);

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.popup_recom);
        dialog.getWindow().setLayout(width, height);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        recommendRecyclerList = dialog.findViewById(R.id.scroll);
        closeBtn = dialog.findViewById(R.id.close_btn);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });


        // 추천 데이터 검색하기 위해 getRecommendList 메소드를 호출
        if (context instanceof ownerPostItemView) {
            ((ownerPostItemView) context).getRecommendList();
        } else {
            ((playerPostItemView) context).getRecommendList();
        }

    }

    public void show() {
        if (context != null && !((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

    // 어댑터
    public void setAdapter(List<RecommendItemModel> recommendItemModels) {
        this.recommendItemModels = recommendItemModels;
        recommendAdapter = new RecommendAdapter(this.recommendItemModels, context);
        recommendRecyclerList.setAdapter(recommendAdapter);
    }


}
