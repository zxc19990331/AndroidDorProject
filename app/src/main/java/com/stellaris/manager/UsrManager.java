package com.stellaris.manager;

public class UsrManager {
    static private String id = "";
    static private String logname = "";
    static private String password = "";
    static private String identity = "";
    static private String name = "";
    static private String studentId = "";
    static private String collegeId = "";
    static private String major = "";
    static private String dorBuildingId = "";
    static private String dorRoomShortId = "";
    static private String dorRoomId = "";
    static private String sex = "";
    static private String desc = "";

    public static int getSetDest() {
        return setDest;
    }

    public static void setSetDest(int setDest) {
        UsrManager.setDest = setDest;
    }

    static private int setDest = 1;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        UsrManager.name = name;
    }
    public static String getId() {
        return id;
    }

    public static void setId(String Id) {
        id = Id;
    }

    public static String getLogname() {
        return logname;
    }

    public static void setLogname(String logname) {
        UsrManager.logname = logname;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        UsrManager.password = password;
    }

    public static String getIdentity() {
        return identity;
    }

    public static void setIdentity(String Identity) {
        identity = Identity;
    }

    public static String getStudentId() {
        return studentId;
    }

    public static void setStudentId(String StudentId) {
        studentId = StudentId;
    }

    public static String getCollegeId() {
        return collegeId;
    }

    public static void setCollegeId(String CollegeId) {
        collegeId = CollegeId;
    }

    public static String getMajor() {
        return major;
    }

    public static void setMajor(String Major) {
        major = Major;
    }

    public static String getDorBuildingId() {
        return dorBuildingId;
    }

    public static void setDorBuildingId(String DorBuildingId) {
        dorBuildingId = DorBuildingId;
    }

    public static String getDorRoomShortId() {
        return dorRoomShortId;
    }

    public static void setDorRoomShortId(String dorRoomShortId) {
        UsrManager.dorRoomShortId = dorRoomShortId;
    }

    public static String getDorRoomId() {
        return dorRoomId;
    }

    public static void setDorRoomId(String DorRoomId) {
        dorRoomId = DorRoomId;
    }

    public static String getSex() {
        return sex;
    }

    public static void setSex(String Sex) {
        sex = Sex;
    }

    public static String getDesc() {
        return desc;
    }

    public static void setDesc(String Desc) {
        desc = Desc;
    }
}
