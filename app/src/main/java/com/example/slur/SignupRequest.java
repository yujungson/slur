package com.example.slur;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class SignupRequest extends StringRequest {
    //서버 URL 설정(php 파일 연동)
    final static private String URL = "http://slur.pe.kr/Signup.php";
    private Map<String, String> map;
    //private Map<String, String>parameters;

    public SignupRequest(String email, String pwd, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("email", email);
        map.put("pwd", pwd);
    }

    @Override
    protected Map<String, String>getParams() throws AuthFailureError {
        return map;
    }

}
