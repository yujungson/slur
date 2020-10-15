package com.example.slur;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class SignupActivity extends AppCompatActivity {

    private EditText et_id, et_pass;
    private Button btn_validate, btn_signup;
    private AlertDialog dialog;
    private boolean validate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //아이디값 찾아주기
        et_id = findViewById( R.id.et_email );
        et_pass = findViewById( R.id.et_pwd );

        btn_validate=findViewById(R.id.btn_validate);
        btn_validate.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {
                String email = et_id.getText().toString();
                if(validate)
                {
                    return;
                }
                if(email.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( SignupActivity.this );
                    dialog=builder.setMessage("아이디는 빈 칸일 수 없습니다")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            System.out.println(jsonResponse);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( SignupActivity.this, R.style.AlertDialogTheme );
                                dialog=builder.setMessage("사용할 수 있는 이메일입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                et_id.setEnabled(false);
                                validate=true;
                                btn_validate.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( SignupActivity.this, R.style.AlertDialogTheme );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest=new ValidateRequest(email,responseListener);
                RequestQueue queue= Volley.newRequestQueue(SignupActivity.this);
                queue.add(validateRequest);

            }
        });

        //회원가입 버튼 클릭 시 수행
        btn_signup = findViewById( R.id.btn_signup );
        btn_signup.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = et_id.getText().toString();
                String pwd = et_id.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject( response );
                            boolean success = jsonObject.getBoolean( "success" );

                            //회원가입 성공시
                            if(success) {
                                Toast.makeText( getApplicationContext(), "Slur의 회원이 되신 걸 환영합니다", Toast.LENGTH_SHORT ).show();
                                Intent intent = new Intent( getApplicationContext(), LoginActivity.class );
                                startActivity( intent );

                                //회원가입 실패시
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this, R.style.AlertDialogTheme);
                                builder.setMessage("회원가입에 실패했습니다");
                                builder.setPositiveButton("확인", null);
                                builder.create().show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };

                //서버로 Volley를 이용해서 요청
                SignupRequest signupRequest = new SignupRequest( email, pwd, responseListener);
                RequestQueue queue = Volley.newRequestQueue( SignupActivity.this );
                queue.add( signupRequest );
            }
        });
    }
}