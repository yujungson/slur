package com.example.slur.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.example.slur.R;

public class PlayerPopup {
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View inflatedView;

    public PlayerPopup(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
        this.builder.setCancelable(true);
        this.inflatedView = LayoutInflater.from(context).inflate(R.layout.popup_player, null);
    }

    public void show() {
        this.dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setView(inflatedView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        if (context != null && !((Activity) context).isFinishing()) {
            dialog.show();
        }
    }

}
