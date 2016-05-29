package com.huhanghao.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.huhanghao.mobilesafe.R;
import com.huhanghao.mobilesafe.utils.ToastaUtil;

/**
 * Created by huhanghao on 2016/4/4.
 */
public class HomeActivity extends Activity {

    private String[] names = new String[]{
            "手机防盗",
            "通讯卫士",
            "软件管家",
            "进程控制",
            "测量统计",
            "手机杀毒",
            "缓存清理",
            "高级工具",
            "设置中心"
    };

    private int[] images = new int[]{
            R.drawable.app,
            R.drawable.app,
            R.drawable.app,
            R.drawable.app,
            R.drawable.app,
            R.drawable.app,
            R.drawable.app,
            R.drawable.app,
            R.drawable.app,
    };



    // 控件
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        gridView = (GridView) findViewById(R.id.gv_items_incom);

        MyAdapter myAdapter = new MyAdapter();
        gridView.setAdapter(myAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position)
                {
                    case 0:
                        String pwd = getSharedPreferences("config", Context.MODE_PRIVATE).getString("pwd",null);
                        if(pwd == null)
                        {
                            showRegisterDialog();
                        }else
                        {
                            showLoginDialog();
                        }
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                    case 3:
                        break;
                    case 4:
                        break;
                    case 5:
                        break;
                    case 6:
                        break;
                    case 7:
                        break;
                    case 8:
                        break;
                    case 9:
                        break;
                }
            }
        });
    }

    private void showRegisterDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(null);
        final View diaglogView = View.inflate(getBaseContext(), R.layout.view_dialog_register,null);
        builder.setView(diaglogView);
        final Dialog dialog = builder.create();
        dialog.show();

        final EditText pwd1 = (EditText) diaglogView.findViewById(R.id.pwd1);
        EditText pwd2 = (EditText) diaglogView.findViewById(R.id.pwd2);
        View okbtn = diaglogView.findViewById(R.id.ok);
        View canclebtn = diaglogView.findViewById(R.id.cancle);

        // 取消按钮
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 确定按钮
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwd1Str = pwd1.getText().toString().trim();
                String pwd2Str = pwd1.getText().toString().trim();
                if("".equals(pwd1Str) || "".equals(pwd2Str))
                {
                    ToastaUtil.shortToast(HomeActivity.this , "密码不能为空");
                    return;
                }
                if(!pwd1Str.equals(pwd2Str))
                {
                    ToastaUtil.shortToast(HomeActivity.this ,"密码不一致");
                    return;
                }
                SharedPreferences.Editor editor = getSharedPreferences("config", Context.MODE_APPEND).edit();
                editor.putString("pwd", pwd1Str);
                editor.commit();

                dialog.dismiss();
            }
        });



    }

    private void showLoginDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setTitle(null);
        View diaglogView = View.inflate(getBaseContext(), R.layout.view_dialog_login,null);
        builder.setView(diaglogView);
        final Dialog dialog = builder.create();
        dialog.show();

        final EditText pwd1 = (EditText) diaglogView.findViewById(R.id.pwd1);
        CheckBox checkBox = (CheckBox) diaglogView.findViewById(R.id.show_password);
        View okbtn = diaglogView.findViewById(R.id.ok);
        View canclebtn = diaglogView.findViewById(R.id.cancle);

        // 取消按钮
        canclebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        // 确定按钮
        okbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pwdFromSp = getSharedPreferences("config", Context.MODE_APPEND).getString("pwd","");

                String pwd1Str = pwd1.getText().toString().trim();
                if("".equals(pwd1Str))
                {
                    ToastaUtil.shortToast(HomeActivity.this , "密码不能为空");
                    return;
                }
                if(!pwd1Str.equals(pwdFromSp))
                {
                    ToastaUtil.shortToast(HomeActivity.this ,"密码错误");
                    return;
                }
                ToastaUtil.shortToast(HomeActivity.this ,"登录成功");
                dialog.dismiss();
            }
        });

    }


    /**
     * 初始化数据
     */
    private void initData() {
    }

    /**
     * 初始化监听
     */
    private void initListener() {

    }

    class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String name = names[position];
            int resId = images[position];
            View view = View.inflate(getApplicationContext(),R.layout.item_incom,null);

            ImageView icon = (ImageView) view.findViewById(R.id.iv_icon);
            TextView title = (TextView) view.findViewById(R.id.tv_title);

            icon.setImageResource(resId);
            title.setText(name);

            return view;
        }
    }
}
