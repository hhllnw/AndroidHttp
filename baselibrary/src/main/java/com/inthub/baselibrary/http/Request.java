package com.inthub.baselibrary.http;

import android.content.Context;
import android.os.Build;

import com.inthub.baselibrary.http.core.RequestTask;
import com.inthub.baselibrary.http.entities.FileEntity;
import com.inthub.baselibrary.http.err.AppException;
import com.inthub.baselibrary.http.err.OnGlobalExpectionListener;
import com.inthub.baselibrary.http.listener.ICallBack;
import com.inthub.baselibrary.http.listener.ProgressDialogListener;
import com.inthub.baselibrary.http.manager.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created by hhl on 2016/7/16.
 * 网络请求的配置参数实体
 */
public class Request {

    public final int UPLOAD_FILE = 1000;
    public final int LOAD_FILE = 1001;

    public enum RequestMethod {GET, POST, DELETE, PUT}

    public enum RequestTool {OKHTTP, URLCONNECTION}

    public enum RequestFormat {JSON}

    private Context context;
    private RequestMethod method;
    private RequestTool tool;//default OKHTTP
    private RequestFormat requestFormat;//default json

    private static String hostUrl;
    private String url;
    private String content;
    private HashMap<String, String> header;
    private ICallBack callBack;
    private boolean isEnableProgressUpdate = false;
    private volatile boolean isCanceled = false;
    private boolean isDoGZip = false;
    private boolean isEncrypt = false;//is encrypt
    private boolean isShowDialog = true; //when network request,is show dialog.default true
    private ProgressDialogListener dialogListener;
    private OnGlobalExpectionListener onGlobalExpectionListener;
    private int mMaxRetryCount = 2;
    private String tag;
    private RequestTask task;
    private String uploadFileName;
    private ArrayList<FileEntity> fileEntities;
    private HashMap<String, Object> paramMap;


    public void execute(ExecutorService executors) {
        task = new RequestTask(this);
        if (Build.VERSION.SDK_INT > 11) {
            task.executeOnExecutor(executors);
        } else {
            task.execute();
        }
    }

    /**
     * cancel networking request
     *
     * @param force
     */
    public void cancel(boolean force) {
        isCanceled = true;
        callBack.cancel();
        if (force && task != null) {
            task.cancel(force);
        }
    }


    /**
     * check if need to cancel networking request
     *
     * @throws AppException
     */
    public void checkIsCancel() throws AppException {
        if (isCanceled) {
            throw new AppException(AppException.ErrType.CANCEL, "the request has canceled");
        }
    }

    public Context getContext() {
        return context;
    }


    public ICallBack getCallBack() {
        return callBack;
    }


    public String getUrl() {
        return url;
    }


    public String getContent() {
        return content;
    }


    public RequestMethod getMethod() {
        return method;
    }


    public HashMap<String, String> getHeader() {
        return header;
    }


    public boolean isEnableProgressUpdate() {
        return isEnableProgressUpdate;
    }


    public OnGlobalExpectionListener getOnGlobalExpectionListener() {
        return onGlobalExpectionListener;
    }

    public int getmMaxRetryCount() {
        return mMaxRetryCount;
    }


    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public String getTag() {
        return tag;
    }


    public String getUploadFileName() {
        return uploadFileName;
    }


    public ArrayList<FileEntity> getFileEntities() {
        return fileEntities;
    }


    public RequestTool getTool() {
        return tool;
    }


    public RequestFormat getRequestFormat() {
        return requestFormat;
    }


    public boolean isDoGZip() {
        return isDoGZip;
    }


    public HashMap<String, Object> getParamMap() {
        return paramMap;
    }

    public boolean isEncrypt() {
        return isEncrypt;
    }

    public boolean isShowDialog() {
        return isShowDialog;
    }

    public ProgressDialogListener getDialogListener() {
        return dialogListener;
    }

    public void setDialogListener(ProgressDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }

    public static String getHostUrl() {
        return hostUrl;
    }

    public void setHostUrl(String hostUrl) {
        this.hostUrl = hostUrl;
    }

    private Request(Builder builder) {

        this.context = builder.context;
        this.tool = builder.tool;
        this.method = builder.method;
        this.requestFormat = builder.requestFormat;
        this.url = builder.url;
        this.content = builder.content;
        this.paramMap = builder.paramMap;
        this.header = builder.header;
        this.callBack = builder.callBack;
        this.isEnableProgressUpdate = builder.isEnableProgressUpdate;
        this.isDoGZip = builder.isDoGZip;
        this.mMaxRetryCount = builder.mMaxRetryCount;
        this.tag = builder.tag;
        this.uploadFileName = builder.uploadFileName;
        this.fileEntities = builder.fileEntities;
        this.onGlobalExpectionListener = builder.onGlobalExpectionListener;
        this.isEncrypt = builder.isEncrypt;
        this.isShowDialog = builder.isShowDialog;
    }

    public static class Builder {
        private Context context;
        private RequestTool tool = RequestTool.OKHTTP;//default OKHTTP
        private RequestMethod method = RequestMethod.GET;//default get method
        private RequestFormat requestFormat = RequestFormat.JSON;//default json
        private String url;
        private String content;
        private HashMap<String, Object> paramMap;
        private HashMap<String, String> header;
        private ICallBack callBack;
        private boolean isEnableProgressUpdate = false;
        private boolean isDoGZip = false;
        private boolean isEncrypt = false;
        private boolean isShowDialog = true;
        private int mMaxRetryCount = 2;
        private String tag;
        private String uploadFileName;
        private ArrayList<FileEntity> fileEntities;
        private OnGlobalExpectionListener onGlobalExpectionListener;

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public Builder requestTool(RequestTool tool) {
            this.tool = tool;
            return this;
        }

        public Builder requestMethod(RequestMethod method) {
            this.method = method;
            return this;
        }

        public Builder requestFormat(RequestFormat format) {
            this.requestFormat = format;
            return this;
        }

        public Builder url(String url) {
            this.url = getHostUrl() + url;
            return this;
        }

        public Builder requestContent(String content) {
            this.content = content;
            return this;
        }

        public Builder paramMap(HashMap<String, Object> map) {
            this.paramMap = map;
            return this;
        }

        public Builder requestHeader(HashMap<String, String> map) {
            this.header = map;
            return this;
        }

        public Builder callBack(ICallBack callBack) {
            this.callBack = callBack;
            return this;
        }

        public Builder isEnableProgressUpdate(boolean isEnableProgressUpdate) {
            this.isEnableProgressUpdate = isEnableProgressUpdate;
            return this;
        }

        public Builder isDoGZip(boolean isDoGZip) {
            this.isDoGZip = isDoGZip;
            return this;
        }

        public Builder maxRetryCount(int max) {
            this.mMaxRetryCount = max;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder isEncrypt(boolean isEncrypt) {
            this.isEncrypt = isEncrypt;
            return this;
        }

        public Builder uploadFileName(String name) {
            this.uploadFileName = name;
            return this;
        }

        public Builder fileEntities(ArrayList<FileEntity> fileEntities) {
            this.fileEntities = fileEntities;
            return this;
        }

        public Builder OnGlobalExpectionListener(OnGlobalExpectionListener listener) {
            this.onGlobalExpectionListener = listener;
            return this;
        }

        public Request build() {
            Request request = new Request(this);
            RequestManager.newInstance().requestAction(request);
            return request;
        }
    }

}
