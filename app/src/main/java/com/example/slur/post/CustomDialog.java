package com.example.slur.post;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.slur.R;

public class CustomDialog extends Dialog implements View.OnClickListener{

    private Button btn_save;
    private Button btn_cancel;
    private EditText et_instrument;
    private EditText et_pay;
    private Context context;

    private CustomDialogListener customDialogListener;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }


    //인터페이스 설정
    interface CustomDialogListener{
        void onPositiveClicked(String position, String pay);
        void onNegativeClicked();
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.custom_dialog);

        //init
        btn_save = (Button)findViewById(R.id.btn_save);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        et_instrument = (EditText)findViewById(R.id.et_instrument);
        et_pay = (EditText)findViewById(R.id.et_pay);


        //버튼 클릭 리스너 등록
        btn_save.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_save: //확인 버튼을 눌렀을 때
                //각각의 변수에 EditText에서 가져온 값을 저장
                String instrument = et_instrument.getText().toString();
                String pay = et_pay.getText().toString();
                Log.d("냠냠", instrument + pay);
                //인터페이스의 함수를 호출하여 변수에 저장된 값들을 Activity로 전달
                customDialogListener.onPositiveClicked(instrument, pay);
                dismiss();
                break;
            case R.id.btn_cancel: //취소 버튼을 눌렀을 때
                cancel();
                break;
        }
    }
}
