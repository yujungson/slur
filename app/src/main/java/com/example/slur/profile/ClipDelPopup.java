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


public class ClipDelPopup {
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View inflatedView;

    private Button cdCancelBtn, cdUploadBtn;

    public ClipDelPopup(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
        this.builder.setCancelable(true);
        this.inflatedView = LayoutInflater.from(context).inflate(R.layout.popup_clip_add, null);
        cdCancelBtn = inflatedView.findViewById(R.id.btn_cancel);
        cdUploadBtn = inflatedView.findViewById(R.id.btn_upload);
        cdCancelBtn.setOnClickListener(cdCancelListener);
        cdUploadBtn.setOnClickListener(cdUploadListener);
    }



    View.OnClickListener cdCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    };
    View.OnClickListener cdUploadListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();
        }
    };

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
