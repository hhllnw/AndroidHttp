package com.inthub.baselibrary.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Stay on 4/2/2016.
 */
public class AppStatusTracker implements Application.ActivityLifecycleCallbacks {
    private static AppStatusTracker tracker;
    private Application application;
    private int mAppStatus = BaseComParams.STATUS_FORCE_KILLED;
    private boolean isForground;
    private int activeCount;

    private AppStatusTracker(Application application) {
        this.application = application;
        this.application.registerActivityLifecycleCallbacks(this);
    }

    public static void init(Application application) {
        tracker = new AppStatusTracker(application);
    }

    public static AppStatusTracker getInstance() {
        return tracker;
    }

    public void setAppStatus(int status) {
        this.mAppStatus = status;
    }

    public int getAppStatus() {
        return this.mAppStatus;
    }

    public boolean isForground() {
        return isForground;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        Logger.I("hh", activity.toString() + " onActivityCreated");
    }

    @Override
    public void onActivityStarted(Activity activity) {
//        L.e(activity.toString() + " onActivityStarted");
        activeCount++;
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        L.e(activity.toString() + " onActivityResumed");
        isForground = true;
    }

    @Override
    public void onActivityPaused(Activity activity) {
//        L.e(activity.toString() + " onActivityPaused");
    }

    @Override
    public void onActivityStopped(Activity activity) {
//        L.e(activity.toString() + " onActivityStopped");
        activeCount--;
        if (activeCount == 0) {
            isForground = false;
            Toast.makeText(application.getApplicationContext(), "应用进入后台...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
//        L.e(activity.toString() + " onActivityDestroyed");
    }

}
