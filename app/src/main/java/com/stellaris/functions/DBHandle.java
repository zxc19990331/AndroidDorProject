package com.stellaris.functions;

import android.util.Log;

import com.stellaris.model.Event;
import com.stellaris.model.Posting;
import com.stellaris.constants.DBKeys;
import com.stellaris.model.User;

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
*
* 过了二三十天发现裸连数据库真的是太睿智了……
* */

public class DBHandle {



    private static HashMap<String,String> executeSqlQuery(String sql){
        HashMap<String, String> map = new HashMap<>();
        Connection conn = DBUtils.getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);
            while(res.next()){
                int cnt = res.getMetaData().getColumnCount();
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
            return null;
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

    //有很多方法非常冗余，但这是寒假刚设计数据库时的我的锅，不是现在正在写bug的我的锅
    //开学学了一节课的SQL就发现当初数据库设计的太sb了
    //对不起，是我太弱了
    //通过宿舍长ID获得宿舍信息
    public static HashMap<String,String> getDorInfo(String dor_id){
        String sql = "SELECT * FROM dor_room WHERE id = '" + dor_id + "'";
        return executeSqlQuery(sql);
    }

    //通过用户名获得user信息
    public static HashMap<String, String> getAllInfoByLogname(String name) {
        String sql = "SELECT * FROM users WHERE logname = '" + name + "'";
        return executeSqlQuery(sql);
    }
    //通过学号和学校获得stu信息
    public static HashMap<String, String> getStuInfoByIdAndSch(String id,String sch) {
        String sql = "SELECT * FROM stus WHERE stu_id = '" + id + "' AND college_id = '"+sch+"'";
        Log.d("DBHANDLE",sql);
        return executeSqlQuery(sql);
    }
    //通过学号和学校获得user信息
    public static HashMap<String,String> getUserInfoByStuIdAndSch(String stu_id,String sch){
        String sql = "SELECT * FROM users WHERE stu_id = '" + stu_id + "' AND college_id = '"+sch+"'";
        return executeSqlQuery(sql);
    }
    //通过id获得user信息
    public static HashMap<String, String> getDetailById(String id){
        String sql = "SELECT name,stu_id,college_id,dor_building_id,dor_room_short_id,major,sex,descr FROM users WHERE id = '"+ id + "'";
        return executeSqlQuery(sql);
    }

    //宿舍相关

    //通过宿舍楼号获得宿舍号列表
    public static List<String> getDorIdsByBuiIdAndSchoolId(String bui_id,String sch_id){
        String sql = String.format("SELECT DISTINCT short_id FROM dor_room WHERE school_id = '%s' AND building_id = '%s'",sch_id,bui_id);
        List<String> dors = new ArrayList<String>();
        Connection conn = DBUtils.getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                String dor = res.getString(DBKeys.ROOM_ID_SHORT);
                dors.add(dor);
            }
            res.close();
            conn.close();
            return dors;
        }catch (Exception e) {
            e.printStackTrace();
            Log.d("DBHANDLE", " 获取宿舍id列表失败");
            return null;
        }
    }



    //通过dor_id获得用户列表
    public static List<User> getUsersByDor(String dor_id){
        String sql = String.format("SELECT * FROM users WHERE %s = '%s'",DBKeys.USR_DOR_ROOM_ID,dor_id);
        return getUserList(sql);
    }

    public static List<User> getStusByBui(String bui_id){
        String sql = String.format("SELECT * FROM stus WHERE %s = '%s' ORDER BY %s ASC",DBKeys.USR_DOR_BUI_ID,bui_id,DBKeys.USR_DOR_ROOM_SHORT);
        return getUserList(sql);
    }

    //这里可以重构成Event Post什么都继承DBObject类 getList就可以只写一个基类了
    //但再重构我就要暴毙了
    public static List<Event> getEventsByBuiAndSch(String bui_id,String sch_id,int limit){
        String sql = String.format("SELECT * FROM events WHERE %s = '%s' AND %s = '%s' ORDER BY date DESC LIMIT %s",DBKeys.EVENT_BUI,bui_id,DBKeys.EVENT_SCH,sch_id,String.valueOf(limit));
        return getEventList(sql);
    }

    //用like来查找包含字符串，我真是个小天才
    //复杂度应该迟早会爆炸吧！
    public static List<Event> getEventsByBuiAndSchAndRoom(String bui_id,String sch_id,String room_id,int limit){
        String sql = String.format("SELECT * FROM events WHERE %s = '%s' AND %s = '%s' AND %s LIKE '%s' ORDER BY date DESC LIMIT %s",
                DBKeys.EVENT_BUI,bui_id,DBKeys.EVENT_SCH,sch_id,DBKeys.EVENT_ROOM,"%"+room_id+"%",String.valueOf(limit));
        return getEventList(sql);
    }

    private static List<Event> getEventList(String sql){
        List<Event> events = new ArrayList<>();
        Connection conn = DBUtils.getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                Event event = new Event();
                HashMap<String,String> map = new HashMap<>();
                int cnt = res.getMetaData().getColumnCount();
                for (int i = 1; i <= cnt; ++i) {
                    String field = res.getMetaData().getColumnName(i);
                    map.put(field, res.getString(field));
                }
                event.init(map);
                events.add(event);
            }
            res.close();
            conn.close();
            return events;
        }catch (Exception e) {
            e.printStackTrace();
            Log.d("DBHANDLE", " 获取Event列表失败");
            return null;
        }
    }

    public static List<User> getUsersByDor(String sch_id,String bui_id,String dor_id){
        String sql = String.format("SELECT * FROM users WHERE %s = '%s' AND %s = '%s' AND %s = '%s'",DBKeys.USR_COL_ID,sch_id,DBKeys.USR_DOR_BUI_ID,bui_id,DBKeys.USR_DOR_ROOM_SHORT,dor_id);
        return getUserList(sql);
    }

    private static List<User> getUserList(String sql){
        List<User> users = new ArrayList<User>();
        Connection conn = DBUtils.getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                User user = new User();
                HashMap<String,String> map = new HashMap<>();
                int cnt = res.getMetaData().getColumnCount();
                for (int i = 1; i <= cnt; ++i) {
                    String field = res.getMetaData().getColumnName(i);
                    map.put(field, res.getString(field));
                }
                user.init(map);
                users.add(user);
            }
            res.close();
            conn.close();
            return users;
        }catch (Exception e) {
            e.printStackTrace();
            Log.d("DBHANDLE", " 获取User列表失败");
            return null;
        }
    }

    public static List<Posting> getPostingBySchoolAndBui(String schoolid,String buiid,boolean isDesc,String type){
        String sql = "SELECT id,DATE_FORMAT(date,'%Y-%m-%d %T')date,user_id,user_name,content,comments,dor_building_id,school_id,type FROM postings WHERE school_id = '"
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
                    //可以给Posting增加init方法
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

    public static List<Posting> getCommentsByPostId(String postId){
        String sql = "SELECT id,DATE_FORMAT(date,'%Y-%m-%d %T')date,user_id,user_name,content,dor_building_id,school_id,type FROM comments WHERE from_id = '"+postId+"'";
        List<Posting> posts = new ArrayList<Posting>();
        Connection conn = DBUtils.getConnection();
        try {
            Statement st = conn.createStatement();
            ResultSet res = st.executeQuery(sql);
            if (res == null) {
                return null;
            } else {
                while(res.next()){
                    //可以给Posting增加init方法
                    Posting post = new Posting();
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
            Log.d("DBHANDLE", " 获取comment列表失败");
            return null;
        }
    }

    public static boolean sendComments(Posting post,String toPost){
        String sql = String.format("INSERT INTO comments (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUE('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                DBKeys.POST_ID,DBKeys.POST_DATE,DBKeys.POST_USR_ID,DBKeys.POST_CONTENT,DBKeys.POST_BUI_ID,DBKeys.POST_SCH_ID,DBKeys.POST_TYPE,DBKeys.POST_USR_NAME,DBKeys.POST_FROM,
                post.getId(),post.getDate(),post.getUserID(),post.getContent(),post.getDorbuildingId(),post.getSchoolId(),post.getType(),post.getUserName(),toPost);
        Log.d("DBHANDLE",sql);
        return executeInsertSql(sql);
    }
    public static boolean sendEvent(Event event){
        event.setId(ShortUUID.generateShortUuid());
        String sql = String.format("INSERT INTO events (%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUE ('%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                DBKeys.EVENT_ID,DBKeys.EVENT_TITLE,DBKeys.EVENT_TYPE,DBKeys.EVENT_SCH,DBKeys.EVENT_BUI,DBKeys.EVENT_ROOM,DBKeys.EVENT_DATE,DBKeys.EVENT_NOTE,DBKeys.EVENT_USR_ID,
                event.getId(),event.getTitle(),event.getType(),event.getSchoolId(),event.getBuiId(),event.getRoomId(),event.getDate(),event.getNote(),event.getUserId());
        return executeInsertSql(sql);
    }

    public static boolean insertUser(User user){
        //设置随机uuid
        user.setId(ShortUUID.generateShortUuid());
        //宿舍全ID = 学校id+宿舍楼id+宿舍号id 虽然我觉得很可能是没必要的数据库设计但我就是这么写了
        user.setDorRoomId(user.getCollegeId()+user.getDorBuildingId()+user.getDorRoomShortId());
        //第二天发现这里的bug是有个字段写重复了……果然神志模糊
        //不要把desc这种关键字当做字段名……debug的血泪经验
        String sql = String.format("INSERT INTO users (%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s) VALUE('%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s','%s')",
                DBKeys.USR_ID,DBKeys.USR_LOG_NAME,DBKeys.USR_PASWRD,DBKeys.USR_IDENT,DBKeys.USR_SEX,DBKeys.USR_COL_ID,
                DBKeys.USR_DOR_BUI_ID,DBKeys.USR_DOR_ROOM_ID,DBKeys.USR_DOR_ROOM_SHORT,DBKeys.USR_NAME,DBKeys.USR_STU_ID,DBKeys.USR_MAJ,DBKeys.USR_DESR,
                user.getId(),user.getLogname(),user.getPassword(),user.getIdentity(),user.getSex(),user.getCollegeId(),
                user.getDorBuildingId(),user.getDorRoomId(),user.getDorRoomShortId(),user.getName(),user.getStudentId(),user.getMajor(),user.getDesc());
       Log.d("INSERT-SQL",sql);
        return executeInsertSql(sql);
    }

    public static boolean delEvent(String eventId){
        String sql = String.format("DELETE FROM events WHERE %s = '%s'",DBKeys.EVENT_ID,eventId);
        Log.d("DEL",sql);
        return executeInsertSql(sql);
    }

    public static boolean delPosting(String postId){
        String sql = String.format("DELETE FROM postings WHERE %s = '%s'",DBKeys.POST_ID,postId);
        Log.d("DEL",sql);
        return executeInsertSql(sql);
    }
    public static boolean UpdatePay(String dorId,double num,int type){
        String sql = String.format("UPDATE dor_room SET %s = %s + %s ,%s = NOW() WHERE %s = '%s'",type==0x0?DBKeys.ROOM_POWER:DBKeys.ROOM_WATER,type==0x0?DBKeys.ROOM_POWER:DBKeys.ROOM_WATER,String.valueOf(num),DBKeys.ROOM_LAST_REQ_DATE,DBKeys.ROOM_ID,dorId);
        Log.d("PAY",sql);
        return executeInsertSql(sql);
    }

    public static boolean UpdateDescr(String userId,String descr){
        String sql = String.format("UPDATE users SET %s = '%s' WHERE %s = '%s'",DBKeys.USR_DESR,descr,DBKeys.USR_ID,userId);
        return executeInsertSql(sql);
    }

}
