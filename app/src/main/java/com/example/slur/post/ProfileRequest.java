package com.example.slur.post;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProfileRequest extends StringRequest {

    final static private String URL = "http://slur.pe.kr/ProfileRequest.php";

    private Map<String, String> map;

    public ProfileRequest(int userId, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}