package com.inthub.baselibrary.http.callback;

/**
 * Created by hhl on 2016/8/8.
 */
public abstract class FileCallBack extends AbstractCallBack<String> {
    @Override
    protected String bindData(String path, boolean isEncrypt) {
        return path;
    }

}
