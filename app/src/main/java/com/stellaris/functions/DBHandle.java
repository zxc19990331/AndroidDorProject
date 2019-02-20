package com.stellaris.functions;

import android.util.Log;

import com.stellaris.functions.DBUtils;
import com.stellaris.model.Posting;
import com.stellaris.model.Event;
import com.stellaris.constants.DBKeys;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



/*
* 所有数据库的增删改查在DBHandle中修改
* 因为是目前是JDBC直连
* 日后有可能（没多大可能）换成web中转HTTP连接
* 这样只需要修改DBHandle的方法
* Activity中的调用方法名就不需要改动
* */

public class DBHandle {



    private static HashMap<String,String> executeSqlQuery(String sql){
        HashMap<String, String> map = new HashMap<>();
        Connection conn = DBUtils.getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                int cnt = res.getMetaData().getColumnCount();
                res.next();
                //返回用户所有信息键的值 2019/1/21 id logname password identity
                for (int i = 1; i <= cnt; ++i) {
                    String field = res.getMetaData().getColumnName(i);
                    map.put(field, res.getString(field));
                }
                conn.close();
                st.close();
                res.close();
                return map;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DBHANDLE", " 数据操作异常");
            return null;
        }
    }


    public static HashMap<String, String> getAllInfoByName(String name) {
        String sql = "select * from users where logname = '" + name + "'";
        return executeSqlQuery(sql);
    }

    public static HashMap<String, String> getDetailById(String id){
        String sql = "select name,stu_id,college_id,dor_building_id,dor_room_short_id,major,sex from users where id = '"+ id + "'";
        return executeSqlQuery(sql);
    }

    public static List<Posting> getPostingBySchoolAndBui(String schoolid,String buiid){
        String sql = String.format("select * from postings where school_id = '%s' , dor_building_id = '%s'",schoolid,buiid );
        List<Posting> posts = new ArrayList<Posting>();
        Connection conn = DBUtils.getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                while(res.next()){
                    Posting post = new Posting();
                    post.setComments(res.getString(DBKeys.POST_CONTENT));
                    post.setContent(res.getString(DBKeys.POST_CONTENT));
                    post.setDate(res.getString(DBKeys.POST_DATE));
                    post.setDorbuildingId(res.getString(DBKeys.POST_BUI_ID));
                    post.setId(res.getString(DBKeys.POST_ID));
                    post.setSchoolId(res.getString(DBKeys.POST_SCH_ID));
                    post.setType(res.getString(DBKeys.POST_TYPE));
                    post.setUserID(res.getString(DBKeys.POST_USR_ID));
                    post.setUserName(res.getString(DBKeys.POST_USR_NAME));
                    posts.add(post);
                }

                conn.close();
                st.close();
                res.close();
                return posts;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DBHANDLE", " 获取posting列表失败");
            return null;
        }
    }


}
