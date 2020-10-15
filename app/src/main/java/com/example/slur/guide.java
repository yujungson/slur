package com.example.slur;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class guide extends AppCompatActivity {

    int user_id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
    }

    public void loginBtnClick(View view){
        Intent intent = new Intent(getApplicationContext(), home.class);
        startActivity(intent);
    }
}