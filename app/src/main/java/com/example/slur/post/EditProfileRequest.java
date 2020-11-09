package com.example.slur.post;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileRequest extends StringRequest {

    final static private String URL = "http://slur.pe.kr/EditProfileRequest.php";

    private Map<String, String> map;

    public EditProfileRequest(int userId, String name, String university, int isMajor, String major, String contact, String introduction, Bitmap profileImg, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);


        map = new HashMap<>();
        map.put("user_id", String.valueOf(userId));
        map.put("name", name);
        map.put("university", university);
        map.put("is_major", String.valueOf(isMajor));
        map.put("major", isMajor == 1 ? major : "");
        map.put("contract", contact);
        map.put("introduction", introduction);
        if (profileImg == null) {
            map.put("is_profile_img", "0");
            map.put("profile_img", "");
        } else {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            profileImg.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            final String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Log.d("Test", "image length: " + imageString.length());
            map.put("is_profile_img", "1");
            map.put("profile_img", imageString);
        }
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}