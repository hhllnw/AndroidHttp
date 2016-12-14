package com.inthub.baselibrary.common;

/**
 * Created by hhl on 2016/9/5.
 */
public class BaseComParams {
    public static final String SP_NAME = "SP_NAME";
    public static final String SP_KEY = "SP_KEY";
    public static final String SP_COOKIE = "SP_COOKIE";

    public static final int STATUS_FORCE_KILLED = -1;//程序被强杀
    public static final int STATUS_LOGOUT = 0;//注销
    public static final int STATUS_OFFLINE = 1;//未登录
    public static final int STATUS_ONLINE = 2;//已登录
    public static final int STATUS_KICK_OUT = 3;//账号已登出

    public static final String KEY_HOME_ACTION = "key_home_action";
    public static final int ACTION_BACK_TO_HOME = 0;
    public static final int ACTION_RESTART_APP = 1;
    public static final int ACTION_LOGOUT = 2;
    public static final int ACTION_KICK_OUT = 3;

}
