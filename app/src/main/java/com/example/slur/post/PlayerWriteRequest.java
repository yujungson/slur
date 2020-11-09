package com.example.slur.post;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PlayerWriteRequest extends StringRequest {

    final static private String URL = "http://slur.pe.kr/WritePlayerPost.php";

    private Map<String, String> map;

    public PlayerWriteRequest(int user_id, String title, String place, String pay, String content, String need_position, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));
        map.put("title", title);
        map.put("place", place);
        map.put("pay", pay);
        map.put("content", content);
        map.put("need_position", need_position);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}