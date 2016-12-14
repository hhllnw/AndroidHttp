package com.inthub.baselibrary.http.listener;

import com.inthub.baselibrary.http.callback.OnProgressUpdatedListener;
import com.inthub.baselibrary.http.err.AppException;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * Created by hhl on 2016/8/5.
 */
public interface ICallBack<T> {

    /**
     * 网络请求成功
     *
     * @param result
     */
    void onSuccess(T result);

    void onFailure(AppException e);

    /**
     * 解析返回数据
     *
     * @param connection
     * @return
     * @throws JSONException
     * @throws IOException
     */
    T parser(HttpURLConnection connection,boolean isEncrypt) throws AppException;

    //T parser(HttpURLConnection connection, OnProgressUpdatedListener listener) throws AppException;

    T parser(HttpURLConnection connection, OnProgressUpdatedListener listener,boolean isEncrypt) throws AppException;

    void onProgressUpdated(int type,int curLen, int totalLen);

    void cancel();

    /**
     * invoked on sub thread
     * @param t serialized by SubCallbacks
     * @return final result by calling onSuccess(t)
     */
    T postRequest(T t);
}
