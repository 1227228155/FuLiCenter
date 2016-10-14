package cn.ucai.fulicenter.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import cn.ucai.fulicenter.R;

public class SplashActivity extends Activity {
    private  static final long  SLEEP_TIME=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long  start =System.currentTimeMillis();
                long  costTime =System.currentTimeMillis()-start;
                if (SLEEP_TIME-costTime>0){
                    try {
                        Thread.sleep(SLEEP_TIME-costTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startActivity(new Intent(SplashActivity.this,MainActivity.class));

            }
        }).start();
    }
}
