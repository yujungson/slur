package com.example.slur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.slur.post.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    int user_id = 0;
    private EditText et_id, et_pass;
    private Button btn_login, btn_register;

    private PreferenceHelper preferenceHelper; // EDITED

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // EDITED : if logged in
        preferenceHelper = new PreferenceHelper(getApplicationContext());
        preferenceHelper.logout();
        if (preferenceHelper.getUserId() > 0) {
            Intent intent = new Intent(LoginActivity.this, home.class);
            startActivity(intent);
        }

//        Intent intent = getIntent();
//        user_id = intent.getExtras().getInt("user_id");

        et_id = findViewById( R.id.et_id );
        et_pass = findViewById( R.id.et_pass );

        et_id.setText("delay");
        et_pass.setText("1234");

        btn_register = findViewById( R.id.btn_register );
        btn_register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent( LoginActivity.this, SignupActivity.class );
                Intent intent = new Intent( LoginActivity.this, SignupActivity.class );
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity( intent );
                finish();
            }
        });


        btn_login = findViewById( R.id.btn_login);
        btn_login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_id.getText().toString();
                String pwd = et_pass.getText().toString();



                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );

                            if(success) {//로그인 성공시

                                String email = jsonObject.getString( "email" );
                                String pwd = jsonObject.getString( "pwd" );
                                int user_id = Integer.valueOf(jsonObject.getString( "user_id" )).intValue(); // EDITED : data type to int
                                String name = jsonObject.getString( "name" );

                                // EDITED
                                preferenceHelper.setUserId(user_id);
                                preferenceHelper.setName(name);



                                Toast.makeText( getApplicationContext(), String.format(name+"님 환영합니다!"), Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( LoginActivity.this, home.class );
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

//                                intent.putExtra( "email", email );
//                                intent.putExtra( "pwd", pwd );
//
//                                intent.putExtra( "user_id", user_id );
//                                intent.putExtra( "name", name );

                                startActivity( intent );
                                finish();

                            } else {//로그인 실패시
                                Toast.makeText( getApplicationContext(), "로그인에 실패했습니다.", Toast.LENGTH_SHORT ).show();

                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                LoginRequest loginRequest = new LoginRequest( email, pwd, responseListener );
                RequestQueue queue = Volley.newRequestQueue( LoginActivity.this );
                queue.add( loginRequest );

            }
        });
    }
}