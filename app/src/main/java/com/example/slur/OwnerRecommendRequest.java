package com.example.slur;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

// recommendation for players (모집자 리스트 가져옴)
public class OwnerRecommendRequest extends StringRequest {

    final static private String URL = "http://slur.pe.kr/OwnerPostRecommend.php";

    private Map<String, String> map;

    public OwnerRecommendRequest(String placeList, String pay, String positionList, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("place", placeList);
        map.put("pay", pay);
        map.put("need_position", positionList);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}