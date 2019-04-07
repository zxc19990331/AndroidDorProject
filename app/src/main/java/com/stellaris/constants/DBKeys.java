package com.stellaris.constants;

public interface DBKeys {
    String USR_ID = "id";
    String USR_NAME = "name";
    String USR_STU_ID = "stu_id";
    String USR_COL_ID = "college_id";
    String USR_DOR_BUI_ID = "dor_building_id";
    String USR_DOR_ROOM_ID = "dor_room_id";
    String USR_DOR_ROOM_SHORT = "dor_room_short_id";
    String USR_MAJ = "major";
    String USR_SEX = "sex";
    String USR_DESR = "descr";
    String USR_PASWRD = "password";
    String USR_LOG_NAME = "logname";
    String USR_IDENT = "identity";

    String USR_IDENT_AYI = "ayi";
    String USR_IDENT_STU = "stu";

    String POST_ID = "id";
    String POST_DATE = "date";
    String POST_USR_ID = "user_id";
    String POST_CONTENT = "content";
    String POST_COM = "comments";
    String POST_BUI_ID = "dor_building_id";
    String POST_SCH_ID = "school_id";
    String POST_TYPE = "type";
    String POST_USR_NAME = "user_name";
    String POST_FROM = "from_id";

    String POST_TYPE_STU = "stu";
    String POST_TYPE_AYI = "ayi";

    String ROOM_ID = "id";
    String ROOM_ID_SHORT = "short_id";
    String ROOM_BUI_ID = "building_id";
    String ROOM_SCH_ID = "school_id";
    String ROOM_STUS = "students";
    String ROOM_STU_COUNT = "student_count";
    String ROOM_POWER = "power_rate";
    String ROOM_WATER = "water_rate";
    String ROOM_LAST_REQ_DATE = "last_requry_date";
    String ROOM_EVENTS = "events";

    String EVENT_ID = "id";
    String EVENT_TITLE = "title";
    String EVENT_TYPE = "type";
    String EVENT_SCH = "school_id";
    String EVENT_BUI = "building_id";
    String EVENT_ROOM = "room_id";
    String EVENT_DATE = "date";
    String EVENT_NOTE = "note";
    String EVENT_USR_ID = "user_id";

    String EVENT_TYPE_GOOD = "表扬";
    String EVENT_TYPE_BAD = "批评";
    String EVENT_TYPE_COMMON = "普通";

}
