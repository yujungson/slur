package com.example.slur;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

// EDITED : SharedPreference to keep data in app
public class PreferenceHelper {
    private static final String USER_ID = "com.example.slur.user_id";
    private static final String NAME = "com.example.slur.name";
    private static final String USER_PREFERENCES = "com.example.slur";
    private Context mContext;

    public PreferenceHelper(Context context) {
        this.mContext = context;
    }

    public int getUserId() {
        return getSharedPreferences(this.mContext).getInt(USER_ID, 0);
    }

    public void setUserId(int userId) {
        Editor editor = getEditor(this.mContext);
        editor.putInt(USER_ID, userId);
        editor.apply();
    }

    public String getName() {
        return getSharedPreferences(this.mContext).getString(NAME, "");
    }

    public void setName(String name) {
        Editor editor = getEditor(this.mContext);
        editor.putString(NAME, name);
        editor.apply();
    }

    public void logout() {
        Editor editor = getEditor(this.mContext);
        editor.clear();
        editor.apply();
    }

    public static Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(USER_PREFERENCES, Context.MODE_PRIVATE);
    }
}
