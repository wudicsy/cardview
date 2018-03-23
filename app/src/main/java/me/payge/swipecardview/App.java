package me.payge.swipecardview;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by chengsiyuan on 2017/9/3.
 */

public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
