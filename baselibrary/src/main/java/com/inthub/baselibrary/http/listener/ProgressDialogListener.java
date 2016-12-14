package com.inthub.baselibrary.http.listener;

/**
 * Created by hhl on 2016/9/19.
 * network progress dialog
 */
public interface ProgressDialogListener {
    void showDialog(String message);//show dialog
    void dismissDialog();//cancel
}
