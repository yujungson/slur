package com.example.slur.post;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    //서버 url 설정(php파일 연동)
    //final static  private String URL="http://s29dongss.dothome.co.kr/Login.php";
    final static  private String URL="http://slur.pe.kr/Login.php";

    private Map<String,String> map;

    public LoginRequest(String email, String pwd, Response.Listener<String>listener){
        super(Method.POST,URL,listener,null);

        map=new HashMap<>();
        map.put("email",email);
        map.put("pwd",pwd);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}