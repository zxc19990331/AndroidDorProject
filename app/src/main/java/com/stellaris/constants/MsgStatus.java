package com.stellaris.constants;

public interface MsgStatus {
    int LOGIN_SUCCESS = 0x0;
    int LOGIN_FAIL = 0x1;

    int DETAIL_GOT = 0x2;

    int POST_SUCCESS = 0x1;
    int POST_FAIL = 0x0;
    int POST_GOT = 0x1;

    int USR_EXIST_LOGNAME = 0x0;
    int USR_AVA = 0x1;          //available
    int REG_EMPTY_INPUT = 0x2;
    int REG_DIFF_INPUT = 0x3;
    int REG_LEN_TOO_SHORT = 0x4;
    int REG_EXIST_STU_ID = 0x5;
    int REG_SUCCESS = 0x6;
    int REG_FAIL = 0x7;
    int REG_NO_STU_ID = 0x8;
}
