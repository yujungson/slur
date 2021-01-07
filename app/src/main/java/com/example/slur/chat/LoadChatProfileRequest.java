package com.example.slur.chat;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

//채팅 프로필 정보 불러오는 클래스
public class LoadChatProfileRequest extends StringRequest {

    final static private String URL = "http://slur.pe.kr/LoadChatProfileRequest.php";

    private Map<String, String> map;

    public LoadChatProfileRequest(int user_id, int other_id, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("user_id", String.valueOf(user_id));
        map.put("other_id", String.valueOf(other_id));
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}