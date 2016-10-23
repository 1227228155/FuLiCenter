package cn.ucai.fulicenter;

import android.app.Application;

/**
 * Created by Administrator on 2016/10/17 0017.
 */

public class FuLiCenterApplication extends Application {
    private  static  FuLiCenterApplication instance;
    public   static  FuLiCenterApplication application;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        FuLiCenterApplication.username = username;
    }

    public  static  String username;


    @Override
    public void onCreate() {
        super.onCreate();
        application=this;
        instance=this;

    }

    public  static  FuLiCenterApplication getInstance(){
        if (instance==null){
            instance = new FuLiCenterApplication();
        }
        return instance;
    }
}
