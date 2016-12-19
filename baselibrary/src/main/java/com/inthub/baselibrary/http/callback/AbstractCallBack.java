package com.inthub.baselibrary.http.callback;

import com.inthub.baselibrary.common.BaseUtilty;
import com.inthub.baselibrary.common.Logger;
import com.inthub.baselibrary.http.err.AppException;
import com.inthub.baselibrary.http.listener.ICallBack;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

/**
 * Created by hhl on 2016/8/6.
 */
public abstract class AbstractCallBack<T> implements ICallBack<T> {
    protected final String TAG = "netWork";
    private String path = "";
    private volatile boolean isCanceled = false;

    @Override
    public void cancel() {
        isCanceled = true;
    }

    private void checkIsCanceled() throws AppException {
        if (isCanceled) {
            throw new AppException(AppException.ErrType.CANCEL, "the quest has cancel");
        }
    }

    @Override
    public T parser(HttpURLConnection connection, boolean isEncrypt) throws AppException {
        return parser(connection, null, isEncrypt);
    }


    @Override
    public T parser(HttpURLConnection connection, OnProgressUpdatedListener listener, boolean isEncrypt) throws AppException {
        // TODO normally 200 represent success, but you should know 200-299 all means success,
        // TODO please ensure with your backend colleagues
        try {
            checkIsCanceled();
            InputStream in = null;
            int status = connection.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {

                Logger.I(TAG, "statusCode:" + status);
                //                support gzip || deflate
                String encoding = connection.getContentEncoding();
                if (encoding != null && "gzip".equalsIgnoreCase(encoding))
                    in = new GZIPInputStream(connection.getInputStream());
                else if (encoding != null
                        && "deflate".equalsIgnoreCase(encoding))
                    in = new InflaterInputStream(connection.getInputStream());
                else {
                    in = connection.getInputStream();
                }
                if (BaseUtilty.isNull(path)) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    byte[] bytes = new byte[2048];
                    int len;
                    int totalLength = connection.getContentLength();
                    int currentlength = 0;
                    int percent = 0;
                    while ((len = in.read(bytes)) != -1) {
                        checkIsCanceled();
                        out.write(bytes, 0, len);
                        if (currentlength * 100l / totalLength >= percent) {
                            if (listener != null) {
                                currentlength += len;
                                listener.onProgressUpdated(currentlength, totalLength);
                                percent = (int) (currentlength * 100l / totalLength);
                            }
                        }
                    }

                    in.close();
                    out.flush();
                    out.close();
                    String result = new String(out.toByteArray());
                    T t = bindData(result, isEncrypt);
                    return postRequest(t);
                } else {
                    FileOutputStream out = new FileOutputStream(path);
                    byte[] bytes = new byte[2048];
                    int len;
                    int totalLength = connection.getContentLength();
                    int currentlength = 0;
                    int percent = 0;
                    while ((len = in.read(bytes)) != -1) {
                        checkIsCanceled();
                        out.write(bytes, 0, len);
                        if (currentlength * 100l / totalLength >= percent) {
                            currentlength += len;
                            listener.onProgressUpdated(currentlength, totalLength);
                            percent = (int) (currentlength * 100l / totalLength);
                        }
                    }
                    in.close();
                    out.flush();
                    out.close();
                    T t = bindData(path, isEncrypt);
                    return postRequest(t);
                }
            } else {
                Logger.I(TAG, "statusCode:" + status);
                Logger.I(TAG, "errMessage:" + connection.getResponseMessage());
                throw new AppException(connection.getResponseCode(), connection.getResponseMessage());
            }
        } catch (IOException e) {
            throw new AppException(AppException.ErrType.SERVER, e.getMessage());
        }
    }

    protected abstract T bindData(String result, boolean isEncrypt) throws AppException;

    @Override
    public T postRequest(T t) {
        return t;
    }

    public AbstractCallBack setPath(String path) {
        this.path = path;
        return this;
    }

    @Override
    public void onProgressUpdated(int type, int curLen, int totalLen) {

    }
}
