package com.example.slur.post;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;
import com.example.slur.SignupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class playerPostWriteActivity extends AppCompatActivity {
    private EditText et_title, et_instrument, et_pay;
    private TextView tv_content;
    private Button btn_cancel, btn_save, btn_place;
    String place="";
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_post_write);

        user_id = new PreferenceHelper(getApplicationContext()).getUserId(); // EDITED
        Log.d("user_id", user_id+"");



        btn_cancel = findViewById( R.id.btn_cancel );//취소버튼
        btn_cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), playerPostListActivity.class );
                startActivity( intent );
            }
        });





        btn_place = findViewById( R.id.btn_place );//장소선택
        btn_place.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] placeList = {"서울경기", "강원", "충청", "전라", "경상", "제주"};
                final boolean[] selectedList =  {false, false, false, false, false, false, false};

                AlertDialog.Builder builder = new AlertDialog.Builder(playerPostWriteActivity.this);
                builder.setTitle("선호 연주 지역을 선택해주세요");
                builder.setMultiChoiceItems(placeList, selectedList, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                selectedList[which] = isChecked;
                            }
                        });

                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == DialogInterface.BUTTON_POSITIVE){
                            place = "";
                            for(int i=0; i<placeList.length; i++){
                                if(selectedList[i])
                                    place += placeList[i] + " ";
                            }
                        }
                        place = place.substring(0, place.length()-1);
                        Toast.makeText(playerPostWriteActivity.this, "선택 지역: " + place, Toast.LENGTH_LONG).show();
                    }
                };

                builder.setPositiveButton("선택", listener);
                builder.setNegativeButton("취소", listener);
                builder.setCancelable(true);
                builder.create().show();
            }
        });






        et_title = findViewById( R.id.et_title );
        et_instrument = findViewById( R.id.et_instrument );
        et_pay = findViewById(R.id.et_pay);
        tv_content = findViewById( R.id.tv_content );


        btn_save = findViewById( R.id.btn_save );//저장버튼
        btn_save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_title.getText().toString();
                String instrument = et_instrument.getText().toString();
                String pay = et_pay.getText().toString();
                String content = tv_content.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Log.d("냠냠", "response");
                            boolean success = jsonObject.getBoolean("success");
                            Log.d("냠냠", "success");
                            //성공시
                            if (success) {
                                Log.d("냠냠", "if문들어옴");
                                Toast.makeText(getApplicationContext(), "연주희망 게시판에 글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), playerPostListActivity.class);
                                startActivity(intent);

                                //실패시
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(playerPostWriteActivity.this);
                                builder.setMessage("빈 칸 없이 입력해주세요");
                                builder.setPositiveButton("확인", null);
                                builder.create().show();
                            }

                        } catch (JSONException e) {
                            Log.d("냠냠", "예외");
                            e.printStackTrace();
                        }

                    }
                };
                PlayerWriteRequest playerWriteRequest = new PlayerWriteRequest(user_id, title, place, pay, content, instrument, responseListener);
                RequestQueue queue = Volley.newRequestQueue(playerPostWriteActivity.this);
                queue.add(playerWriteRequest);
                Log.d("냠냠", "db에 추가됨");
            }
        });
    }
}