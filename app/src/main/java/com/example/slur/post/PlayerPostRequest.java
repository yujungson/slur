package com.example.slur.post;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class PlayerPostRequest extends StringRequest {
    final static private String url = "http://s29dongss.dothome.co.kr/LoadPlayerPost.php";

    public PlayerPostRequest(Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
    }
}
