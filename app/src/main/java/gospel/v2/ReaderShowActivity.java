/*
 * copyright (c)2018-8-15
 * DXC technology
 */

package gospel.v2;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import gospel.v2.base.DLApplication;
import gospel.v2.bluetooth.BlueToothListActivity;
import gospel.v2.bluetooth.DeviceListActivity;
import gospel.v2.model.Reader;
import gospel.v2.model.TaskDetails;

public class ReaderShowActivity extends BaseActivity {
    String TAG = ReaderShowActivity.class.getSimpleName();
    private TextView username;
    private TextView gender;
    private TextView syear;
    private TextView phone;
    private TextView realname;
    private TextView area;
    private TextView email;
    private TextView location;
    Reader reader;
    Intent intent;
    public static ReaderShowActivity ceLiangActivityinstance;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    public static String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reader_detail);

        ceLiangActivityinstance = this;

        username = (TextView) findViewById(R.id.username);
        gender = (TextView) findViewById(R.id.gender);
        syear = (TextView) findViewById(R.id.sYear);
        phone = (TextView) findViewById(R.id.phone);
        realname = (TextView) findViewById(R.id.realname);
        area = (TextView) findViewById(R.id.area);
        email = (TextView) findViewById(R.id.email);
        location = (TextView) findViewById(R.id.location);

        intent = getIntent();

        reader = (Reader) intent.getSerializableExtra("reader");

        if (reader != null) {
            username.setText(reader.getUsername());
            gender.setText(reader.getGender());
            syear.setText(reader.getsYear());
            phone.setText(reader.getPhone());
            realname.setText(reader.getRealname());
            area.setText(reader.getArea());
            email.setText(reader.getEmail());
            location.setText(reader.getLocation());
        }


        if (DLApplication.userName != null && !DLApplication.userName.equals(DLApplication.amdin)) {
            findViewById(R.id.supEdit).setVisibility(View.GONE);
        } else {
            findViewById(R.id.supEdit).setVisibility(View.VISIBLE);
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        //必须加2句
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//??????
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);  //根据字面意思是显示类型为显示自定义
        actionBar.setDisplayShowCustomEnabled(true); //自定义界面是否可显示
        ((TextView) findViewById(R.id.title_name)).setText("reader detail");

        //以下代码用于去除阴影
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // requestCode 与请求开启 Bluetooth 传入的 requestCode 相对应
//        switch (requestCode) {
//            case REQUEST_CONNECT_DEVICE:
//                // When DeviceListActivity returns with a device to connect
//                if (resultCode == Activity.RESULT_OK) {
//                    String address = data.getExtras()
//                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//                    String restr = data.getExtras()
//                            .getString("result");
////                    if (restr != null && "取消配对".equals(restr))
////                        button.setText("蓝牙连接读数\n" + address);
//                    if (restr != null) {
//                        Toast.makeText(context, restr, Toast.LENGTH_LONG).show();
////                        Logger.i(TAG, restr);
//                    }
//                    if (address != null) {
//                        button.setText("蓝牙连接读数\n" + address);
//                    }
//                }
//                break;
////            case REQUEST_CODE_BLUETOOTH_ON:
////                // When DeviceListActivity returns with a device to connect
////                if (resultCode == BLUETOOTH_DISCOVERABLE_DURATION) {
////                    ///开始搜索
////                    Intent serverIntent = new Intent(CeLiangActivity.this, DeviceListActivity.class);
////                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
////                } else {
////                    Toast.makeText(this, "用户拒绝开启蓝牙", Toast.LENGTH_SHORT).show();
////                    Logger.i(TAG, "用户拒绝开启蓝牙");
////                }
////
////                break;
//        }
//    }
}
