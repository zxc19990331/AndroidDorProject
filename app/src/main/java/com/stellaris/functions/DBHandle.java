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
            Log.d("DBHANDLE", " 查询语句有误");
            return null;
        }
    }

    private static boolean executeInsertSql(String sql){
        Connection conn = DBUtils.getConnection();
        try {
            Statement st = conn.createStatement();
            //执行update sql
            st.executeUpdate(sql);
            conn.close();
            st.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DBHANDLE", " insert语句有误");
            return false;
        }
    }


    public static HashMap<String, String> getAllInfoByName(String name) {
        String sql = "SELECT * FROM users WHERE logname = '" + name + "'";
        return executeSqlQuery(sql);
    }

    public static HashMap<String, String> getDetailById(String id){
        String sql = "SELECT name,stu_id,college_id,dor_building_id,dor_room_short_id,major,sex FROM users WHERE id = '"+ id + "'";
        return executeSqlQuery(sql);
    }

    public static List<Posting> getPostingBySchoolAndBui(String schoolid,String buiid,boolean isDesc,String type){
        String sql = "SELECT id,DATE_FORMAT(date,'%Y-%m-%d %H:%m:%s')date,user_id,user_name,content,comments,dor_building_id,school_id,type FROM postings WHERE school_id = '"
                +schoolid+"' AND dor_building_id = '"+ buiid + "' AND type = '" + type + "'";
        sql += isDesc?" ORDER BY date DESC":" ORDER BY date ASC";
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
                    post.setComments(res.getString(DBKeys.POST_COM));
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

    public static boolean sendPosting(Posting post){
        String sql = String.format("INSERT INTO postings (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUE('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                DBKeys.POST_ID,DBKeys.POST_DATE,DBKeys.POST_USR_ID,DBKeys.POST_CONTENT,DBKeys.POST_COM,DBKeys.POST_BUI_ID,DBKeys.POST_SCH_ID,DBKeys.POST_TYPE,DBKeys.POST_USR_NAME,
                post.getId(),post.getDate(),post.getUserID(),post.getContent(),post.getComments(),post.getDorbuildingId(),post.getSchoolId(),post.getType(),post.getUserName());

        return executeInsertSql(sql);
    }


}
