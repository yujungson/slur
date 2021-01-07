package com.example.slur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class guide extends AppCompatActivity {

    int user_id = 0;
    private Button btn_login;

    private PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        preferenceHelper = new PreferenceHelper(getApplicationContext());
        user_id = preferenceHelper.getUserId();

        btn_login = findViewById( R.id.btn_loginAtGuide );
        btn_login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( getApplicationContext(), LoginActivity.class );
                intent.putExtra("user_id", user_id);
                startActivity( intent );
                finish();
            }
        });

        if(user_id > 0){
            btn_login.setText("시작하기");

            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }


    }
}