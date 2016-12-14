package com.inthub.baselibrary.http.core;

import android.content.Context;
import android.webkit.URLUtil;

import com.inthub.baselibrary.common.BaseComParams;
import com.inthub.baselibrary.common.BaseUtilty;
import com.inthub.baselibrary.common.CipherTool;
import com.inthub.baselibrary.common.Logger;
import com.inthub.baselibrary.http.Request;
import com.inthub.baselibrary.http.callback.OnProgressUpdatedListener;
import com.inthub.baselibrary.http.err.AppException;
import com.inthub.baselibrary.http.upload.UploadUtil;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.OkUrlFactory;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * okHttp util
 */
public class OKHttpUrlConnectionUtil {
    protected static final String TAG = "netWork";
    private static OkHttpClient mClient;

    private static String getCookie(Context context) {
        String cookie = BaseUtilty.getStringFromMainSP(context, BaseComParams.SP_COOKIE);
        return cookie;
    }

    public synchronized static HttpURLConnection execute(Request request, OnProgressUpdatedListener listener) throws AppException {

        if (!URLUtil.isNetworkUrl(request.getUrl())) {
            throw new AppException(AppException.ErrType.MANUAL, "the url :" + request.getUrl() + " is not valid");
        }
        if (mClient == null) {
            initializeOkHttp();
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

    private static void initializeOkHttp() {
        mClient = new OkHttpClient();
    }


    private static HttpURLConnection get(Request request) throws AppException {
        try {

            request.checkIsCancel();

            HttpURLConnection connection = new OkUrlFactory(mClient).open(new URL(request.getUrl()));
            connection.setRequestMethod(request.getMethod().name());
            connection.setConnectTimeout(15 * 3000);
            connection.setReadTimeout(15 * 3000);

            addHeader(connection, request.getHeader(), request);

            request.checkIsCancel();
            return connection;
        } catch (MalformedURLException e) {
            throw new AppException(AppException.ErrType.SERVER, e.getMessage());
        } catch (ProtocolException e) {
            throw new AppException(AppException.ErrType.SERVER, e.getMessage());
        }
    }


    private static HttpURLConnection post(Request request, OnProgressUpdatedListener listener) throws AppException {
        HttpURLConnection connection = null;
        OutputStream os = null;
        try {
            request.checkIsCancel();

            connection = new OkUrlFactory(mClient).open(new URL(request.getUrl()));
            connection.setRequestMethod(request.getMethod().name());
            connection.setConnectTimeout(15 * 3000);
            connection.setReadTimeout(15 * 3000);
            connection.setDoOutput(true);
            addHeader(connection, request.getHeader(), request);
            request.checkIsCancel();

            os = connection.getOutputStream();
            if (request.getUploadFileName() != null) {
                UploadUtil.upload(os, request.getUploadFileName());
            } else if (request.getFileEntities() != null) {
                UploadUtil.upload(os, request.getContent(), request.getFileEntities(), listener);
            } else if (request.getContent() != null) {
                Logger.I(TAG, "请求的参数：" + request.getUrl() + "        " + request.getContent());
                if (request.isEncrypt()){//加密
                   String content = CipherTool.getCipherString(request.getContent());
                    os.write(content.getBytes());
                }else {
                    os.write(request.getContent().getBytes());
                }
            } else if (request.getParamMap() != null && request.getParamMap().size() > 0) {
                HashMap<String, Object> map = request.getParamMap();
                JSONObject jsonObject = new JSONObject(map);
                Logger.I(TAG, "请求的参数：" + request.getUrl() + "        " + jsonObject.toString());
                if (request.isEncrypt()){//加密
                    String param = CipherTool.getCipherString(jsonObject.toString());
                    os.write(param.getBytes());
                }else {
                    os.write(jsonObject.toString().getBytes());
                }

            } else {
                throw new AppException(AppException.ErrType.MANUAL, "the post request has no post content");
            }

            request.checkIsCancel();
        } catch (InterruptedIOException e) {
            throw new AppException(AppException.ErrType.TIME_OUT, e.getMessage());
        } catch (IOException e) {
            throw new AppException(AppException.ErrType.SERVER, e.getMessage());
        } finally {
            try {
                os.flush();
                os.close();
            } catch (IOException e) {
                throw new AppException(AppException.ErrType.IO, "the post outputstream can't be closed");
            }
        }

        return connection;
    }

    private static void addHeader(HttpURLConnection connection, Map<String, String> header, Request request) {
        if (request.getRequestFormat() == Request.RequestFormat.JSON) {
            connection.addRequestProperty("Content-Type", "application/json");
            connection.addRequestProperty("charset", "UTF-8");
            String cookie = getCookie(request.getContext());
            connection.addRequestProperty("Cookie", BaseUtilty.isNull(cookie) ? "" : cookie);
        }
        if (request.isDoGZip()) {
            connection.addRequestProperty("Accept-Encoding", "gzip");
        }
        if (header == null || header.size() == 0)
            return;

        for (Map.Entry<String, String> entry : header.entrySet()) {
            connection.addRequestProperty(entry.getKey(), entry.getValue());
        }
    }
}
