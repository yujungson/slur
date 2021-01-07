package com.example.slur.chat;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class RoomItemModel implements Serializable {

    private int chat_id, root_post_type, root_post_id, owner_id, player_id, state, state_demand, demand_who, position, changed_post_id;
    private String cancel_reason, owner_name, player_name;
    private String lastMessage, lastTime;
    private String base64String_owner, base64String_player;

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference = firebaseDatabase.getReference("chat");
    String lateMessage = "";
    String time = "";

    public RoomItemModel(int chat_id, int root_post_type, int root_post_id, int owner_id, int player_id, int state, int state_demand, int demand_who, int position, int changed_post_id, String cancel_reason, String owner_name, String player_name, String lastMessage, String lastTime, String base64String_owner, String base64String_player) {
        this.chat_id = chat_id;
        this.root_post_type = root_post_type;
        this.root_post_id = root_post_id;
        this.owner_id = owner_id;
        this.player_id = player_id;
        this.state = state;
        this.state_demand = state_demand;
        this.demand_who = demand_who;
        this.position = position;
        this.changed_post_id = changed_post_id;
        this.cancel_reason = cancel_reason;
        this.owner_name = owner_name;
        this.player_name = player_name;
        this.lastMessage = lastMessage; //from fireBase
        this.lastTime = lastTime; //from fireBase
        this.base64String_owner = base64String_owner;
        this.base64String_player = base64String_player;

    }

    public int getChat_id() {
        return chat_id;
    }

    public void setChat_id(int chat_id) {
        this.chat_id = chat_id;
    }

    public int getRoot_post_type() {
        return root_post_type;
    }

    public void setRoot_post_type(int root_post_type) {
        this.root_post_type = root_post_type;
    }

    public int getRoot_post_id() {
        return root_post_id;
    }

    public void setRoot_post_id(int root_post_id) {
        this.root_post_id = root_post_id;
    }

    public int getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(int owner_id) {
        this.owner_id = owner_id;
    }

    public int getPlayer_id() {
        return player_id;
    }

    public void setPlayer_id(int player_id) {
        this.player_id = player_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState_demand() {
        return state_demand;
    }

    public void setState_demand(int state_demand) {
        this.state_demand = state_demand;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getChanged_post_id() {
        return changed_post_id;
    }

    public void setChanged_post_id(int changed_post_id) {
        this.changed_post_id = changed_post_id;
    }

    public String getCancel_reason() {
        return cancel_reason;
    }

    public void setCancel_reason(String cancel_reason) {
        this.cancel_reason = cancel_reason;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    public String getPlayer_name() {
        return player_name;
    }

    public void setPlayer_name(String player_name) {
        this.player_name = player_name;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastTime() {
        return lastTime;
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getBase64String_owner() {
        return base64String_owner;
    }

    public void setBase64String_owner(String base64String_owner) {
        this.base64String_owner = base64String_owner;
    }

    public String getBase64String_player() {
        return base64String_player;
    }

    public void setBase64String_player(String base64String_player) {
        this.base64String_player = base64String_player;
    }

    public int getDemand_who() {
        return demand_who;
    }

    public void setDemand_who(int demand_who) {
        this.demand_who = demand_who;
    }
}
