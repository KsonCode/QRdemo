package com.bwie.qrdemo.app;

import android.app.Application;
import android.content.Context;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 *
 */
public class App  extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();


        initZxing(this);


    }


    /**
     * zxing初始化
     * @param context
     */
    private void initZxing(Context context) {

        ZXingLibrary.initDisplayOpinion(this);
    }
}
