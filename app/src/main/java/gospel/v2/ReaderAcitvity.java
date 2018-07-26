/*
 * copyright (c)2018-8-15
 * DXC technology
 */

package gospel.v2;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import gospel.v2.dbhelp.SqliteUtils;
import gospel.v2.logs.Logger;
import gospel.v2.model.Reader;
import gospel.v2.utils.Utils;

/**
 * Created by gospel on 2017/8/18.
 * About Register
 */
public class ReaderAcitvity extends AppCompatActivity {
    String TAG = ReaderAcitvity.class.getSimpleName();
    Context context;
    private Button button;
    private EditText username;
    private EditText gender;
    private EditText syear;
    private EditText phone;
    private EditText realname;
    private EditText area;
    private EditText email;
    public EditText locationa;
    private Dialog mWeiboDialog;//对话框

    public double latitude;
    public double longitude;

    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_reader);
        // setSpinner();
        context = this;
        button = (Button) findViewById(R.id.register);
        username = (EditText) findViewById(R.id.username);
        gender = (EditText) findViewById(R.id.gender);
        syear = (EditText) findViewById(R.id.sYear);
        phone = (EditText) findViewById(R.id.phone);
        realname = (EditText) findViewById(R.id.realname);
        area = (EditText) findViewById(R.id.area);
        email = (EditText) findViewById(R.id.email);
        locationa = (EditText) findViewById(R.id.location);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waitingDialog();//加载等待页面对话框方法
                if (username.getText().toString() != null && gender.getText().toString() != null && area.getText().toString() != null) {
                    Reader reader = new Reader();
                    reader.setUsername(username.getText().toString());
                    reader.setGender(gender.getText().toString());
                    reader.setPhone(phone.getText().toString());
                    reader.setRealname(realname.getText().toString());
                    reader.setsYear(syear.getText().toString());
                    reader.setArea(area.getText().toString());
                    reader.setEmail(email.getText().toString());
                    reader.setLocation(locationa.getText().toString());
                    int isTure = SqliteUtils.getInstance(context).saveReader(reader);
                    if (isTure == 1) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new AlertDialog.Builder(context)
                                .setTitle("System alert")
                                .setIcon(R.drawable.success_small)
                                .setMessage("book add success!")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Logger.i(TAG, username.getText().toString() + " books save success.");
                                        startActivity(new Intent(ReaderAcitvity.this, ReaderListActivity.class));
                                        finish();
                                    }
                                })
                                .show();
                    } else if (isTure == -1) {
                        Toast.makeText(ReaderAcitvity.this, "book name exist...", Toast.LENGTH_SHORT).show();
                        Logger.i(TAG, username.getText().toString() + " book name exist. book save failed.");
                    } else {
                        Toast.makeText(ReaderAcitvity.this, "book save failed", Toast.LENGTH_SHORT).show();
                        Logger.i(TAG, username.getText().toString() + "book save failed.");
                    }
                } else {
                    Toast.makeText(ReaderAcitvity.this, "name、gender、area、realname are both not null", Toast.LENGTH_SHORT).show();
                    Logger.i(TAG, username.getText().toString() + "book name not null");
                }
            }
        });

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        //必须加2句
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);  //根据字面意思是显示类型为显示自定义
        actionBar.setDisplayShowCustomEnabled(true); //自定义界面是否可显示
        // 使用setText的方法对textview动态赋值
        ((TextView) findViewById(R.id.title_name)).setText("Add Reader");

        //以下代码用于去除阴影
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }

        initLocation();


        findViewById(R.id.getlcation).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReaderAcitvity.this, MapViewAcitvity.class));
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //DialogThridUtils.closeDialog(mDialog);
                    if (context != null && mWeiboDialog != null) {
                        WeiboDialogUtils.closeDialog(mWeiboDialog);
                    }
                    break;
            }
        }
    };

    //定义加载等待页面方法
    public void waitingDialog() {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(context, "Loading...");//加载对话框
        mHandler.sendEmptyMessageDelayed(1, 1000);//处理消息
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation() {
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);

        // 启动定位
        locationClient.startLocation();
    }


    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }


    /**
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (null != location) {

                StringBuffer sb = new StringBuffer();
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (location.getErrorCode() == 0) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    locationa.setText(location.getAddress());
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mapView.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
        }
    }
}
