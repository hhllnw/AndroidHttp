package com.inthub.baselibrary.http.core;

import android.os.AsyncTask;

import com.inthub.baselibrary.common.BaseComParams;
import com.inthub.baselibrary.common.BaseUtilty;
import com.inthub.baselibrary.common.Logger;
import com.inthub.baselibrary.http.Request;
import com.inthub.baselibrary.http.callback.OnProgressUpdatedListener;
import com.inthub.baselibrary.http.err.AppException;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by hhl on 2016/8/5.
 */
public class RequestTask extends AsyncTask<Void, Integer, Object> {

    private Request request;

    public RequestTask(Request request) {
        this.request = request;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Void... params) {
        return request(1);
    }

    private Object request(int retryCount) {

        try {
            HttpURLConnection connection = null;
            if (request.getTool() == Request.RequestTool.URLCONNECTION) {
                connection = HttpUrlConnectionUtil.execute(request, !request.isEnableProgressUpdate() ? null : new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(request.UPLOAD_FILE, curLen, totalLen);
                    }
                });
                setCookie(connection);
            } else if (request.getTool() == Request.RequestTool.OKHTTP) {
                connection = OKHttpUrlConnectionUtil.execute(request, !request.isEnableProgressUpdate() ? null : new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(request.UPLOAD_FILE, curLen, totalLen);
                    }
                });
                setCookie(connection);
            }

            if (request.isEnableProgressUpdate()) {
                return request.getCallBack().parser(connection, new OnProgressUpdatedListener() {
                    @Override
                    public void onProgressUpdated(int curLen, int totalLen) {
                        publishProgress(request.LOAD_FILE, curLen, totalLen);
                    }
                }, request.isEncrypt());
            } else {
                return request.getCallBack().parser(connection, request.isEncrypt());
            }

        } catch (AppException e) {
            if (e.getType() == AppException.ErrType.TIME_OUT) {
                if (retryCount < request.getmMaxRetryCount()) {
                    retryCount++;
                    return request(retryCount);
                }
            }
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        //dismiss dialog
        if (request.isShowDialog() && request.getDialogListener() != null) {
            request.getDialogListener().dismissDialog();
        }
        if (o instanceof AppException) {
            if (request.getOnGlobalExpectionListener() != null) {
                if (!request.getOnGlobalExpectionListener().handleExpection((AppException) o)) {
                    request.getCallBack().onFailure((AppException) o);
                }
            } else {
                request.getCallBack().onFailure((AppException) o);
            }
        } else {
            request.getCallBack().onSuccess(o);
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        request.getCallBack().onProgressUpdated(values[0], values[1], values[2]);
    }

    private void setCookie(HttpURLConnection connection) {
        Map<String, List<String>> headers = connection.getHeaderFields();
        if (headers != null && headers.size() > 0) {
            if (headers.get("Set-Cookie") != null && headers.get("Set-Cookie").size() > 0) {
                String cookie = headers.get("Set-Cookie").get(0);
                cookie = cookie.split(";")[0] + ";";
                BaseUtilty.saveStringToMainSP(request.getContext(), BaseComParams.SP_COOKIE, cookie);
            }
        }
    }
}
