package com.example.slur.profile;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class HeartPlayerPostRequest extends StringRequest {

    final static private String URL = "http://slur.pe.kr/HeartPlayerPost.php";

    private Map<String, String> map;

    public HeartPlayerPostRequest(int user_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}