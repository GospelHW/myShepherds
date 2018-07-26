package gospel.v2;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import gospel.v2.base.DLApplication;
import gospel.v2.logs.Logger;
import gospel.v2.utils.DeviceUtil;

/**
 * Created by gospel on 2017/8/18.
 * About PersonWellcom
 */
public class PersonAcitvity extends BaseActivity {

    boolean isOpen = false;
    TextView textView;
    String result = null;
    ImageView mv;
    Context context;
    //退出时的时间
    private long mExitTime;

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_homepage_layout);
        TextView t = (TextView) findViewById(R.id.showlogname);
        t.setText("Housekeeper        |        " + (DLApplication.userName != null ? DLApplication.userName : android.os.Build.MODEL));
        context = this;

        if (!DeviceUtil.isNetworkConnected(context)) {
            Logger.i(TAG, "设备未联网...");
            new AlertDialog.Builder(context)
                    .setTitle("系统提示")
                    .setCancelable(false)//按返回键不关闭窗口
                    .setIcon(R.drawable.danger_small)
                    .setMessage("系统检测到您的设备没有连接网络，任务将无法下载。")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                            Logger.i(TAG, "已经提醒用户连接网络");
                        }
                    }).show();
        }
        findViewById(R.id.clgl_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonAcitvity.this, MyMeasureList.class));
            }
        });
        findViewById(R.id.sjgl_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonAcitvity.this, ReadRecordListActivity.class));
            }
        });
        findViewById(R.id.rwgl_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonAcitvity.this, ReaderListActivity.class));
            }
        });
        findViewById(R.id.aqgl_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PersonAcitvity.this, Location_Activity.class));
            }
        });
        findViewById(R.id.cadtz_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(PersonAcitvity.this, LocalFilesActivity.class));
                Toast.makeText(PersonAcitvity.this, "敬请期待......", Toast.LENGTH_LONG).show();
            }
        });
        findViewById(R.id.tjgl_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(PersonAcitvity.this, "敬请期待......", Toast.LENGTH_LONG).show();
            }
        });


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar_plus);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);  //根据字面意思是显示类型为显示自定义
        actionBar.setDisplayShowCustomEnabled(true); //自定义界面是否可显示
        ((TextView) findViewById(R.id.title_name)).setText("HOME");


//        mv = (ImageView) findViewById(R.id.left_imbt);
        findViewById(R.id.title_personal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (isOpen) {
                    drawerLayout.closeDrawers();
                    isOpen = false;
                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                    isOpen = true;
                }
            }
        });

        //以下代码用于去除阴影
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }


//        webView = (WebView) findViewById(R.id.wv_web);
//        webView.loadUrl("file:///android_res/raw/test.html");
//        webView.setVerticalScrollBarEnabled(false);
//        webView.setHorizontalScrollBarEnabled(false);
//        webView.getSettings().setJavaScriptEnabled(true); //加上这句话才能使用javascript方法;
//        webView.addJavascriptInterface(new PayJavaScriptInterface(), "demo");

    }

    //按返回键到最后一个activity推出侧滑菜单
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Object mHelperUtils;
                mExitTime = System.currentTimeMillis();
                drawerLayout.openDrawer(Gravity.LEFT);//侧滑菜单栏
                isOpen = true;//设置为打开状态
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
//    class HelloMessage extends React.Component {
//        render() {
//            return React.createElement(
//                    "div",
//                    null,
//                    "Hello ",
//                    this.props.name
//            );
//        }
//    }


    /**
     * @desc Created by Gospel on 2017/9/12 12:28
     * DXC technology
     */

    final class PayJavaScriptInterface {
        PayJavaScriptInterface() {
        }

        @JavascriptInterface
        public String getUserinfo() {
            return "getUserinfo";
        }


        @JavascriptInterface
        public boolean needLogin() {
            return true;
        }

        @JavascriptInterface
        public void haha() {
            Toast.makeText(context, "hahaa", Toast.LENGTH_SHORT).show();
            Log.e("sssssssssssssssssssssss", "sdfddddddddd");
        }
    }
}

