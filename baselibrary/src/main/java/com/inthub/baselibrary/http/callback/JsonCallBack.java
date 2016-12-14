package com.inthub.baselibrary.http.callback;


import com.google.gson.Gson;
import com.inthub.baselibrary.common.CipherTool;
import com.inthub.baselibrary.common.Logger;
import com.inthub.baselibrary.http.err.AppException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by hhl on 2016/8/6.
 */
public abstract class JsonCallBack<T> extends AbstractCallBack<T> {

    @Override
    protected T bindData(String result,boolean isEncrypt) throws AppException {
        try {
            if (isEncrypt) {
                Logger.I("netWork", "返回的解密的result:" + CipherTool.getOriginString(result));
            } else {
                Logger.I("netWork", "返回的未解密的result:" + result);
            }
            Gson gson = new Gson();
            Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
            return gson.fromJson(result, type);
        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(AppException.ErrType.JSON, e.getMessage());
        }
    }

}
