package com.huhanghao.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.huhanghao.mobilesafe.R;

public class SplashActivity extends Activity {

    private View root; // 底部布局
    private View progressBar; // 进度bar
    private View tvProgress;  // 进度展示内容
    private TextView tvVersion; // 版本号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 初始化控件
        initView();
        // 初始化数据
        initData();

        new Thread(){
            @Override
            public void run() {
                try{
                    Thread.sleep(3000);
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    finish();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 初始化VIEW
     */
    private void initView() {
        root = findViewById(R.id.rl_root);
        progressBar = findViewById(R.id.progressBar_splash);
        tvProgress = findViewById(R.id.tv_progress);
        tvVersion = (TextView) findViewById(R.id.tv_version);

        AlphaAnimation anim = new AlphaAnimation(0.5f,1.0f);
        anim.setDuration(3000);
    }


    /**
     * 初始化数据
     */
    private void initData() {

        // 获取当前包信息
        PackageManager pm = getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = pm.getPackageInfo("com.huhanghao.mobilesafe",0);
            if(packageInfo != null)
            {
                int versionCode = packageInfo.versionCode;
                String verName = packageInfo.versionName;
                tvVersion.setText("当前版本："+ verName);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        // 从网络获取版本信息
        getDataFromNet();

    }

    /**
     * 从网络获取数据
     */
    private void getDataFromNet() {
        new Thread()
        {
            @Override
            public void run() {
                String url = "http://192.168.1.107:8080/data_For_Self_Test/jsoninfo";
//                String json = HttpUtils.get(url);
            }
        }.start();
    }
}
