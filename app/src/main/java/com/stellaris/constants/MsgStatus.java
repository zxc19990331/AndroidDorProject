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

    int DOR_MATES_GOT = 0x0;
    int DOR_PAYMENT_GOT = 0x1;

    int EVENT_SEND_SUCCESS = 0x1;
    int EVENT_SEND_FAIL = 0x0;

    int EVENT_GOT = 0x3;

    int STU_FIND_GOT = 0x9;
    int POST_DEL_SUCCESS = 0xa;
    int POST_DEL_FAIL = 0xb;
    int POST_GET_USR_INFO = 0xc;

    int PAY_UPDATE_SUCCESS = 0xc;
    int PAY_UPDATE_FAIL = 0xd;
    int MSG_FINISH = 0xf;

    int INTENT_SEND = 0x0;
    int INTENT_NEW_CONTENT = 0x1;
    int INTENT_NO_NEW_CONTENT = 0x2;

    int INFO_DESCR_CHANGE = 0x10;
}
