package com.inthub.baselibrary.http.core;

import android.content.Context;
import android.webkit.URLUtil;

import com.inthub.baselibrary.common.BaseComParams;
import com.inthub.baselibrary.common.BaseUtilty;
import com.inthub.baselibrary.common.Logger;
import com.inthub.baselibrary.http.Request;
import com.inthub.baselibrary.http.callback.OnProgressUpdatedListener;
import com.inthub.baselibrary.http.err.AppException;
import com.inthub.baselibrary.http.upload.UploadUtil;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by hhl on 2016/7/16.
 */
public class HttpUrlConnectionUtil {
    private static final String TAG = "netWork";
    private static int READ_TIME = 45 * 1000;
    private static int TIME_OUT = 45 * 1000;
    private static String getCookie(Context context) {
        String cookie = BaseUtilty.getStringFromMainSP(context, BaseComParams.SP_COOKIE);
        return cookie;
    }

    public static HttpURLConnection execute(Request request, OnProgressUpdatedListener listener) throws AppException {

        if (!URLUtil.isNetworkUrl(request.getUrl())) {//judge the url is network url
            throw new AppException(AppException.ErrType.MANUAL, "this url " + request.getUrl() + " is not valid.");
        }
        switch (request.getMethod()) {
            case GET:
            case DELETE:
                return get(request);
            case POST:
            case PUT:
                return post(request, listener);
        }
        return null;
    }

    private static HttpURLConnection get(Request request) throws AppException {

        try {
            request.checkIsCancel();
            //declare visit path
            URL url = new URL(request.getUrl());
            //by path to get a connect
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //request method
            connection.setRequestMethod(request.getMethod().name());
            //set connect time is timeout
            connection.setConnectTimeout(TIME_OUT);
            //set read time
            connection.setReadTimeout(READ_TIME);
            addHeader(connection, request.getHeader(), request);

            request.checkIsCancel();

            return connection;
        } catch (InterruptedIOException e) {
            throw new AppException(AppException.ErrType.TIME_OUT, e.getMessage());
        } catch (IOException e) {
            throw new AppException(AppException.ErrType.SERVER, e.getMessage());
        }
    }

    private static HttpURLConnection post(Request request, OnProgressUpdatedListener listener) throws AppException {
        OutputStream outputStream = null;
        HttpURLConnection connection = null;
        try {
            request.checkIsCancel();

            URL url = new URL(request.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(request.getMethod().name());
            connection.setReadTimeout(READ_TIME);
            connection.setConnectTimeout(TIME_OUT);
            connection.setDoOutput(true);
            addHeader(connection, request.getHeader(), request);

            request.checkIsCancel();

            outputStream = connection.getOutputStream();

            if (BaseUtilty.isNotNull(request.getUploadFileName())) {
                UploadUtil.upload(outputStream, request.getUploadFileName());
            } else if (request.getFileEntities() != null) {
                UploadUtil.upload(outputStream, request.getContent(), request.getFileEntities(), listener);
            } else if (BaseUtilty.isNotNull(request.getContent())) {
                Logger.I(TAG, "请求的参数："+ request.getUrl() + " ------>" + request.getContent());
                outputStream.write(request.getContent().getBytes());
            } else if (request.getParamMap() != null && request.getParamMap().size() > 0) {
                HashMap<String, Object> map = request.getParamMap();
                JSONObject jsonObject = new JSONObject(map);
                Logger.I(TAG, "请求的参数："+ request.getUrl() + " ------>" + jsonObject.toString());
                outputStream.write(jsonObject.toString().getBytes());
            } else {
                throw new AppException(AppException.ErrType.MANUAL, "please set request content");
            }

            request.checkIsCancel();

        } catch (InterruptedIOException e) {
            throw new AppException(AppException.ErrType.TIME_OUT, e.getMessage());
        } catch (IOException e) {
            throw new AppException(AppException.ErrType.SERVER, e.getMessage());
        } finally {
            try {
                if (outputStream != null){
                    outputStream.flush();
                    outputStream.close();
                }else {
                    throw new AppException(AppException.ErrType.SERVER, "outputstream is null");
                }
            } catch (IOException e) {
                throw new AppException(AppException.ErrType.IO, "the post outputstream can't be closed");
            }
        }
        return connection;
    }

    /**
     * 添加头部
     *
     * @param connection
     * @param headers
     */
    private static void addHeader(HttpURLConnection connection, HashMap<String, String> headers, Request request) {
        if (request.getRequestFormat() == Request.RequestFormat.JSON) {
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("charset", "UTF-8");
            String cookie = getCookie(request.getContext());
            connection.addRequestProperty("Cookie", BaseUtilty.isNull(cookie) ? "" : cookie);
        }
        if (request.isDoGZip()) {
            connection.addRequestProperty("Accept-Encoding", "gzip");
        }
        if (headers == null || headers.size() > 0) {
            return;
        }
        for (HashMap.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }
}
