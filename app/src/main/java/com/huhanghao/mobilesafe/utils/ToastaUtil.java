package com.huhanghao.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by huhanghao on 2016/4/6.
 */
public class ToastaUtil {

    public static void shortToast(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context,String message)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }

}
