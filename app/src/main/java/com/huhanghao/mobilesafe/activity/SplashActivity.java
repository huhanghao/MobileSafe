package com.huhanghao.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.huhanghao.mobilesafe.R;
import com.huhanghao.mobilesafe.utils.ToastaUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SplashActivity extends Activity {

    // 控件
    private View root; // 底部布局
    private View progressBar; // 进度bar
    private TextView tvProgress;  // 进度展示内容
    private TextView tvVersion; // 版本号

    // 局部属性变量
    private int curVersionCode; // 版本号

    // 静态变量
    public static final int CODE_UDATE = 1;
    public static final int CODE_NOUPDATE = 2;
    public static final int CODE_NET_ERROR = 3;
    public static final int SPLASHACTIVITY_REQUEST_CODE = 10;
    public static final int SPLASHACTIVITY_REQUEST_UPDATE_CODE = 11;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = null;
            switch (msg.what) {
                case CODE_UDATE:
                    showUpdateDialog();
                    break;
                case CODE_NOUPDATE:
                    enterHome();
                    break;
                case CODE_NET_ERROR:
                    ToastaUtil.shortToast(getApplicationContext(),"网络访问错误");
                    enterHome();
                    break;
            }
        }
    };

    /**
     * 提示更新对话框
     */
    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("更新提示");
        builder.setMessage("对软件进行更新");
        builder.setPositiveButton("后台更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                downLoad("http://cdn.yoloho.com/upload/filedownload/2016/03/30/dayima_v6.61_201603291026_official.apk?ver=1");
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });

        Dialog dialog = builder.create();
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                enterHome();
            }
        });
        dialog.show();
    }

    /**
     * 进入主页
     */
    private void enterHome()
    {
        startActivityForResult(new Intent(this,HomeActivity.class),SPLASHACTIVITY_REQUEST_CODE);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 初始化控件
        initView();
        // 初始化数据
        initData();
    }

    /**
     * 初始化VIEW
     */
    private void initView() {
        root = findViewById(R.id.rl_root);
        progressBar = findViewById(R.id.progressBar_splash);
        tvProgress = (TextView)findViewById(R.id.tv_progress);
        tvVersion = (TextView) findViewById(R.id.tv_version);

        AlphaAnimation anim = new AlphaAnimation(0.5f, 1.0f);
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
            packageInfo = pm.getPackageInfo("com.huhanghao.mobilesafe", 0);
            if (packageInfo != null) {
                curVersionCode = packageInfo.versionCode;
                String verName = packageInfo.versionName;
                tvVersion.setText("当前版本：" + verName);
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
        new Thread() {
            @Override
            public void run() {
                String json = "{versionCode:2,versionName:\"友情测试2.0\",url:\"http://localhost:8080/data_For_Self_Test/MobileSafe70.apk\",desc:\"下载吧\"}";
//                String url = "http://192.168.1.139:8080/data_For_Self_Test/jsoninfo";
//                String json = HttpUtil.get(url);
                if (!TextUtils.isEmpty(json)) {
                    try {
                        JSONObject obj = new JSONObject(json);
                        int versionCode = obj.getInt("versionCode");
                        String versionName = obj.getString("versionName");
                        String url = obj.getString("url");
                        String desc = obj.getString("desc");
                        if(curVersionCode < versionCode)
                        {
                            Message msg = handler.obtainMessage();
                            msg.what = CODE_UDATE;
                            handler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    // 网络访问异常
                    Message msg = handler.obtainMessage();
                    msg.what = CODE_NET_ERROR;
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }


    public void downLoad(String urlStr) {
        String url = urlStr;
        final String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/dayima_v6.61_201603291026_official.apk";
        HttpUtils httpUtils = new HttpUtils(5000);
        httpUtils.download(url, savePath, new RequestCallBack<File>() {
            @Override
            public void onLoading(long total, long current, boolean isUploading) {
                super.onLoading(total, current, isUploading);
                tvProgress.setText(current+"/"+total);
            }

            @Override
            public void onSuccess(ResponseInfo<File> responseInfo) {
                Log.d("hhh", "下载成功");
                showInstallDialog(savePath);
            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.d("hhh", "下载失败");
                enterHome();
            }
        });
    }

    public void showInstallDialog(final  String savePath)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("安装提示");
        builder.setMessage("当前安装版本");
        builder.setPositiveButton("安装", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                // 安装流程
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.addCategory("android.intent.category.DEFAULT");
                Uri data = Uri.parse("file://"+ savePath);
                intent.setDataAndType(data, "application/vnd.android.package-archive");
                startActivityForResult(intent,SPLASHACTIVITY_REQUEST_UPDATE_CODE);
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                enterHome();
            }
        });

        Dialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SPLASHACTIVITY_REQUEST_UPDATE_CODE)
        {
            enterHome();
        }
        else if(requestCode == SPLASHACTIVITY_REQUEST_CODE)
        {
            finish();
        }
    }

}
