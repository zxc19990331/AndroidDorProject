package com.stellaris.functions;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class DBUtils {
    private static final String TAG = "DBUtils";
    private static final String IP = "118.25.54.117";
    private static final String DBNAME = "androidapp";
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://" + IP + ":3306/"+DBNAME+"?useSSL=false&serverTimezone=UTC&autoReconnect=true&failOverReadOnly=false",
                    "app", "123456");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return conn;
    }


}
