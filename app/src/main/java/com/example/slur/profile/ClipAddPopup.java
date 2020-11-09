package com.example.slur.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.example.slur.R;

public class ClipAddPopup {
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View inflatedView;

    private Button caCancelBtn, caUploadBtn;
    public ClipAddPopup(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
        this.builder.setCancelable(true);
        this.inflatedView = LayoutInflater.from(context).inflate(R.layout.popup_clip_add, null);
        caCancelBtn = inflatedView.findViewById(R.id.btn_cancel);
        caUploadBtn = inflatedView.findViewById(R.id.btn_upload);


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

    public void setOnCancelListener(View.OnClickListener listener) {
        caCancelBtn.setOnClickListener(listener);
    }

    public void setOnUploadListener(View.OnClickListener listener) {
        caUploadBtn.setOnClickListener(listener);
    }

    public void dismiss() {
        dialog.dismiss();
    }
}
