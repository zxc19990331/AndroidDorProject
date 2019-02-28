package com.stellaris.model;

import com.stellaris.constants.DBKeys;

import java.util.HashMap;

public class User {
    private String id = "";
    private String logname = "";
    private String password = "";
    private String identity = "";
    private String name = "";
    private String studentId = "";
    private String collegeId = "";
    private String major = "";
    private String dorBuildingId = "";
    private String dorRoomShortId = "";
    private String dorRoomId = "";
    private String sex = "";
    private String desc = "";

    public void init(HashMap<String,String> mp){
        //为什么不用getOrDefault是因为这个API好像至少要API24
        //虽然感觉很蠢应该有更好的写法
        //因为不知道String赋值为null会不会出错？？
        String _id = mp.get(DBKeys.USR_ID);
        String _logname = mp.get(DBKeys.USR_LOG_NAME);
        String _password = mp.get(DBKeys.USR_PASWRD);
        String _ident = mp.get(DBKeys.USR_IDENT);
        String _name = mp.get(DBKeys.USR_NAME);
        String _stuId = mp.get(DBKeys.USR_STU_ID);
        String _col = mp.get(DBKeys.USR_COL_ID);
        String _maj = mp.get(DBKeys.USR_MAJ);
        String _dorBui = mp.get(DBKeys.USR_DOR_BUI_ID);
        String _dorRoomShort = mp.get(DBKeys.USR_DOR_ROOM_SHORT);
        String _dorRoom = mp.get(DBKeys.USR_DOR_ROOM_ID);
        String _sex = mp.get(DBKeys.USR_SEX);
        String _desc = mp.get(DBKeys.USR_DESR);
        if(_id!=null){
            setId(_id);
        }
        if(_logname!=null){
            setLogname(_logname);
        }
        if(_password!=null){
            setPassword(_password);
        }
        if(_ident!=null){
            setIdentity(_ident);
        }
        if(_name!=null){
            setName(_name);
        }
        if(_stuId!=null){
            setStudentId(_stuId);
        }
        if(_col!=null){
            setCollegeId(_col);
        }
        if(_maj!=null){
            setMajor(_maj);
        }
        if(_dorBui!=null){
            setDorBuildingId(_dorBui);
        }
        if(_dorRoom!=null){
            setDorRoomId(_dorRoom);
        }
        if(_dorRoomShort!=null){
            setDorRoomShortId(_dorRoomShort);
        }
        if(_sex!=null){
            setSex(_sex);
        }
        if(_desc!=null){
            setDesc(_desc);
        }
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogname() {
        return logname;
    }

    public void setLogname(String logname) {
        this.logname = logname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCollegeId() {
        return collegeId;
    }

    public void setCollegeId(String collegeId) {
        this.collegeId = collegeId;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getDorBuildingId() {
        return dorBuildingId;
    }

    public void setDorBuildingId(String dorBuildingId) {
        this.dorBuildingId = dorBuildingId;
    }

    public String getDorRoomShortId() {
        return dorRoomShortId;
    }

    public void setDorRoomShortId(String dorRoomShortId) {
        this.dorRoomShortId = dorRoomShortId;
    }

    public String getDorRoomId() {
        return dorRoomId;
    }

    public void setDorRoomId(String dorRoomId) {
        this.dorRoomId = dorRoomId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }



}
