package com.example.a92083.androidhttp.activity;

import android.os.Bundle;
import android.view.View;

import com.inthub.baselibrary.http.core.HttpUrlConnectionUtil;
import com.inthub.baselibrary.http.core.OKHttpUrlConnectionUtil;
import com.inthub.baselibrary.http.err.AppException;
import com.inthub.baselibrary.view.activity.AppActivity;

/**
 * Created by hhl on 2016/12/19.
 */

public abstract class BaseActivity extends AppActivity implements View.OnClickListener {

    @Override
    protected void onAppCreate(Bundle savedInstanceState) {
        setUpContentView();
        initView();
        initData();
    }

    /**
     * setContentView()
     */
    protected abstract void setUpContentView();

    /**
     * init view
     */
    protected abstract void initView();

    /**
     * init data
     */
    protected abstract void initData();

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void setProgressDialogMessage() {
        mRequestManager.setProgressDialogListener(this, "请稍后...");
    }

    @Override
    public boolean handleExpection(AppException e) {
        if (e.getStatusCode() == 401) {
            //such as token inavlid,need to relogin
            return true;
        }
        return false;
    }

    @Override
    public void showDialog(String message) {

    }

    @Override
    public void dismissDialog() {

    }
}
