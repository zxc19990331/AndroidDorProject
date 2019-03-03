package com.stellaris.model;

import com.stellaris.constants.DBKeys;

import java.util.HashMap;

public class Event {
    private String id;
    private String type;
    private String schoolId;
    private String buiId;
    private String roomId;
    private String date;
    private String note;
    private String userId;

    public void init(HashMap<String,String> map){
        setId(map.get(DBKeys.EVENT_ID));
        setTitle(map.get(DBKeys.EVENT_TITLE));
        setType(map.get(DBKeys.EVENT_TYPE));
        setSchoolId(map.get(DBKeys.EVENT_SCH));
        setBuiId(map.get(DBKeys.EVENT_BUI));
        setRoomId(map.get(DBKeys.EVENT_ROOM));
        setDate(map.get(DBKeys.EVENT_DATE));
        setNote(map.get(DBKeys.EVENT_NOTE));
        setUserId(map.get(DBKeys.EVENT_USR_ID));
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getBuiId() {
        return buiId;
    }

    public void setBuiId(String buiId) {
        this.buiId = buiId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }}
