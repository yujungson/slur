package com.example.slur.post;

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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.slur.PreferenceHelper;
import com.example.slur.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.security.acl.Owner;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;

public class ownerPostWriteActivity extends AppCompatActivity implements View.OnClickListener{
    RecyclerView position;
    RecyclerView.LayoutManager pLayoutManager;
    ArrayList<OwnerPosition> positionList;
    OwnerPositionAdapter adapter;

    private EditText et_title, et_play_date;
    private TextView tv_content;
    private Button btn_cancel, btn_save, btn_place, btn_add;
    private String need_pay="";
    private String need_position="";
    private String position_state="";
    String place="";
    int user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_post_write);

        user_id = new PreferenceHelper(getApplicationContext()).getUserId(); // EDITED
        Log.d("user_id", user_id+"");



        btn_cancel = findViewById( R.id.btn_cancel );//취소버튼
        btn_cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), ownerPostListActivity.class );
                startActivity( intent );
            }
        });





        btn_place = findViewById( R.id.btn_place );//장소선택
        btn_place.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] placeList = {"서울경기", "강원", "충청", "전라", "경상", "제주"};
                final int[] selectedItem = {0};
                AlertDialog.Builder builder = new AlertDialog.Builder(ownerPostWriteActivity.this);
                builder.setTitle("연주 지역을 선택해주세요");
                builder.setSingleChoiceItems(placeList, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedItem[0] = which;
                            }
                        });
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                place = placeList[selectedItem[0]];
                                Toast.makeText(ownerPostWriteActivity.this, "선택 지역: " + place, Toast.LENGTH_LONG).show();
                            }
                        }

                );
                builder.setNeutralButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }
                );
                builder.setCancelable(true);
                builder.create().show();
            }
        });






        et_title = findViewById( R.id.et_title );//글제목
        tv_content = findViewById( R.id.tv_content );//글내용
        et_play_date = findViewById(R.id.et_play_date);//연주날짜

        btn_add = findViewById(R.id.btn_add);//포지션추가버튼
        btn_add.setOnClickListener(this);


        position = (RecyclerView)findViewById(R.id.scroll);
        pLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        position.setLayoutManager(pLayoutManager);

        positionList = new ArrayList<>();

        adapter = new OwnerPositionAdapter(positionList, this);
        position.setAdapter(adapter);


        btn_save = findViewById( R.id.btn_save );//저장버튼
        btn_save.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = et_title.getText().toString();
                String play_date = et_play_date.getText().toString();
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
                                Toast.makeText(getApplicationContext(), "구인 게시판에 글이 작성되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), ownerPostListActivity.class);
                                startActivity(intent);

                                //실패시
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(ownerPostWriteActivity.this);
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
                OwnerWriteRequest ownerWriteRequest = new OwnerWriteRequest(user_id, title, place, need_pay, content, need_position, play_date, position_state, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ownerPostWriteActivity.this);
                queue.add(ownerWriteRequest);
                Log.d("냠냠", "포지션:" + need_position);
                Log.d("냠냠", "db에 추가됨");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_add:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setDialogListener(new CustomDialog.CustomDialogListener() {
                    @Override
                    public void onPositiveClicked(String instrument, String pay) {
                        OwnerPosition data = new OwnerPosition(instrument);
                        positionList.add(data);
                        adapter.notifyDataSetChanged();

                        need_position += instrument + " ";
                        need_pay += pay+ " ";
                        position_state += "0 ";
                        Log.d("냠냠", "포지션: " + need_position);
                        Log.d("냠냠", "페이: "+ need_pay);
                        Log.d("냠냠", "상태: "+ position_state);


                    }

                    @Override
                    public void onNegativeClicked() {

                    }
                });
                dialog.show();
                break;
        }
    }


}
