package com.inthub.baselibrary.http.err;

/**
 * Created by hhl on 2016/9/1.
 */
public interface OnGlobalExpectionListener {
    boolean handleExpection(AppException e);
}
