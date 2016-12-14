package com.example.a92083.androidhttp.common;

import android.app.Application;

import com.inthub.baselibrary.common.Logger;

/**
 * Created by hhl on 2016/12/14.
 */

public class HHLApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Logger.islog = true;// 打开日志，正式版关闭
    }
}
