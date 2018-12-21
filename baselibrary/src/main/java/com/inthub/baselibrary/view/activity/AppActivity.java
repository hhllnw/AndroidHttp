package com.inthub.baselibrary.view.activity;

import android.app.Activity;

import com.inthub.baselibrary.http.listener.ProgressDialogListener;
import com.inthub.baselibrary.http.manager.RequestManager;

/**
 * Created by hhl on 2016/12/19.
 */

public abstract class AppActivity extends Activity implements /*OnGlobalExpectionListener,*/ ProgressDialogListener {
    protected RequestManager mRequestManager;

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRequestManager = RequestManager.newInstance();
        setProgressDialogMessage();
        onAppCreate(savedInstanceState);
    }

    protected abstract void onAppCreate(Bundle savedInstanceState);

    *//**
     * findViewById()
     *
     * @param resId
     * @param <T>
     * @return
     *//*
    protected <T> T $(int resId) {
        return (T) findViewById(resId);
    }

    *//**
     * set dialog message
     *//*
    protected abstract void setProgressDialogMessage();

    @Override
    protected void onStop() {
        super.onStop();
        //kill request
        mRequestManager.cancleRequest(toString(), true);
    }*/
}
