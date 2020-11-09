package com.example.slur.post;

import androidx.annotation.Nullable;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class OwnerPostRequest extends StringRequest {
    final static private String url = "http://s29dongss.dothome.co.kr/LoadOwnerPost.php";

    public OwnerPostRequest(Response.Listener<String> listener) {
        super(Method.POST, url, listener, null);
    }
}
