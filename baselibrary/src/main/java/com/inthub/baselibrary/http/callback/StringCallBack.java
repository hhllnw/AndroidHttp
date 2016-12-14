package com.inthub.baselibrary.http.callback;

import com.inthub.baselibrary.common.CipherTool;
import com.inthub.baselibrary.common.Logger;

import java.io.UnsupportedEncodingException;


/**
 * Created by hhl on 2016/8/6.
 */
public abstract class StringCallBack extends AbstractCallBack<String> {

    @Override
    protected String bindData(String result, boolean isEncrypt) {
        try {
            if (isEncrypt) {
                Logger.I("netWork", "返回的解密的result:" + CipherTool.getOriginString(result));
            } else {
                Logger.I("netWork", "返回的未解密的result:" + result);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

}
