package com.example.slur.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.slur.PreferenceHelper;
import com.example.slur.R;

import org.w3c.dom.Text;

public class HeartPostList extends AppCompatActivity {

    int user_id;
    TextView back_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heart_post_list);

        Intent intent = getIntent();
        user_id = new PreferenceHelper(getApplicationContext()).getUserId(); // EDITED
        Log.d("user_id", user_id+"");

        back_btn = (TextView)findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), profile.class);
                startActivity(intent);
                finish();
            }
        });

        //Volley 이용해서 favorite post 해당 row 세기 setAdapter
    }
}