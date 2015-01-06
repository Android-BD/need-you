package com.example.nipun.aid_test_2;

import android.app.Application;
import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by nipun on 31/12/14.
 */
public class SingletonApp extends Application {

    private static Context mContext;

    public void onCreate(){
        super.onCreate();
        this.mContext = this;
    }

    public static Context getContext() {
        return mContext;
    }

}
