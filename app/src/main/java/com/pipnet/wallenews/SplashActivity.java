package com.pipnet.wallenews;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.pipnet.wallenews.bean.WeatherInfo;
import com.pipnet.wallenews.http.service.NetRequest;
import com.pipnet.wallenews.http.subscriber.BaseSubscriber;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        //测试
        NetRequest.test("101010100", new BaseSubscriber<WeatherInfo>() {
            @Override
            public void onNext(WeatherInfo weatherInfo) {

            }
        });
    }
}
