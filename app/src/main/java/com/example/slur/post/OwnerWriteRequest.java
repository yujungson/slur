package com.example.slur.post;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class OwnerWriteRequest extends StringRequest {

    final static private String URL = "http://slur.pe.kr/WriteOwnerPost.php";

    private Map<String, String> map;

    public OwnerWriteRequest(int user_id, String title, String place, String pay, String content, String need_position, String play_date, String position_state, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));
        map.put("title", title);
        map.put("play_date", play_date);
        map.put("pay", pay);
        map.put("place", place);
        map.put("need_position", need_position);
        map.put("content", content);
        map.put("position_state", position_state);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}