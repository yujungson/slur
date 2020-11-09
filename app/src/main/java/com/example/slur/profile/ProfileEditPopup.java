package com.example.slur.profile;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.slur.R;

public class ProfileEditPopup {
    private Context context;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private View inflatedView;

    private Button CancelBtn, DefaultBtn, GalleryBtn;

    private OnClickListener cancelListener, finishListener;

    public ProfileEditPopup(Context context) {
        this.context = context;
        this.builder = new AlertDialog.Builder(context);
        this.builder.setCancelable(true);

        this.inflatedView = LayoutInflater.from(context).inflate(R.layout.popup_profile_edit, null);
        CancelBtn = inflatedView.findViewById(R.id.btn_cancel);
        DefaultBtn = inflatedView.findViewById(R.id.btn_default);
        GalleryBtn = inflatedView.findViewById(R.id.btn_gallery);
        CancelBtn.setOnClickListener(CancelListener);
        DefaultBtn.setOnClickListener(DefaultListener);
        GalleryBtn.setOnClickListener(GalleryListener);
    }

    //취소 버튼 클릭시
    View.OnClickListener CancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Toast.makeText(context, "취소", Toast.LENGTH_LONG).show();
            dialog.dismiss();
        }
    };

    //기본 이미지로 변경
    View.OnClickListener DefaultListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Toast.makeText(context, "기본 이미지로 변경", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            ((ProfileEditActivity) context).setToDefaultImage();
        }
    };

    //갤러리로 이동시
    View.OnClickListener GalleryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Toast.makeText(context, "앨범에서 사진선택", Toast.LENGTH_LONG).show();
            dialog.dismiss();
            ((ProfileEditActivity) context).getImageFromGallery(); // EDITED
        }
    };

    public void show() {
        this.dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setView(inflatedView);
        dialog.setCancelable(true);
        dialog.show();
    }


    public interface OnClickListener {
        void onClick(View view, AlertDialog dialog);
    }
}
