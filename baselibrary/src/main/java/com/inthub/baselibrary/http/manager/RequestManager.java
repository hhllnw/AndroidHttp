package com.inthub.baselibrary.http.manager;

import android.util.Log;

import com.inthub.baselibrary.common.BaseUtilty;
import com.inthub.baselibrary.http.Request;
import com.inthub.baselibrary.http.listener.ProgressDialogListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by hhl on 2016/9/1.
 * manager network request
 */
public class RequestManager {
    private static RequestManager mInstance;
    private static HashMap<String, List<Request>> mCahceRequestMap;
    private ExecutorService executors;
    private ProgressDialogListener dialogListener;
    private String dialogTip;
    private String hostUrl;

    public static RequestManager newInstance() {
        if (mInstance == null) {
            mInstance = new RequestManager();
        }
        return mInstance;
    }

    private RequestManager() {
        mCahceRequestMap = new HashMap<>();
        executors = Executors.newFixedThreadPool(5);
    }

    public void setProgressDialogListener(ProgressDialogListener dialogListener, String hostUrl, String dialogTip) {
        this.dialogListener = dialogListener;
        this.dialogTip = dialogTip;
        this.hostUrl = hostUrl;
    }

    /**
     * @param request
     */
    public void requestAction(Request request) {
        if (BaseUtilty.isNull(request.getTag())) {
            Log.e("warn", "please set request tag");
            return;
        }

        request.setHostUrl(hostUrl);

        if (request.isShowDialog() && dialogListener != null) {
            dialogListener.showDialog(dialogTip);
            request.setDialogListener(dialogListener);
        }


        request.execute(executors);
        if (!mCahceRequestMap.containsKey(request.getTag())) {
            List<Request> requests = new ArrayList<>();
            mCahceRequestMap.put(request.getTag(), requests);
        }
        mCahceRequestMap.get(request.getTag()).add(request);
    }


    public void cancleRequest(String tag) {
        cancleRequest(tag, false);
    }

    public void cancleRequest(String tag, boolean force) {

        if (BaseUtilty.isNull(tag)) {
            Log.e("warn", "please set request tag");
            return;
        }
        if (mCahceRequestMap.containsKey(tag)) {
            List<Request> requests = mCahceRequestMap.remove(tag);
            for (Request request : requests) {
                if (!request.isCanceled()) {
                    request.cancel(force);
                }
            }
        }
    }
}
