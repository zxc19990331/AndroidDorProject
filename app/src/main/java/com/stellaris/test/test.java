package com.stellaris.test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

import com.stellaris.functions.TimeUtil;
import com.stellaris.functions.DBUtils;
import com.stellaris.functions.DBHandle;
import com.stellaris.manager.UsrManager;

public class test {
    private static String ip = "118.25.54.117";
    private static String dbname = "androidapp";
    private static String url = "jdbc:mysql://" + ip + "/"+ dbname +"?useSSL=false&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false";//数据库服务地址
    private static String driver = "com.mysql.jdbc.Driver";//驱动路径
    private static String username = "app";
    private static String passworld = "123456";

    public static void main(String[] args) throws Exception {
        String str = "[\"1\",2\",3,4]]]";
        System.out.print(str.replaceAll("[\\[\\]\"]",""));
        //System.out.print(DBHandle.getEventsByBuiAndSch("05", "1", 2));
    }

    public static Connection getconnection()
    {
        return DBUtils.getConnection();
    }
}