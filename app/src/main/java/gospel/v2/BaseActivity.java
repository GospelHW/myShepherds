package gospel.v2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.LayoutRes;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import gospel.v2.base.DLApplication;

import java.util.HashMap;
import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by sunyi on 2017/8/25.
 */

public class BaseActivity extends AppCompatActivity {
    String TAG = BaseActivity.class.getSimpleName();
    //    protected String[] planetTitles;
    public DrawerLayout drawerLayout;
    protected ListView drawerList;
    protected FrameLayout frameLayout;
    protected String[] planetTitles = null;//{"个人信息", "任务管理", "数据管理", "安全管理", "仪器设置", "系统升级", "关于系统"};
    protected int[] imagesId = {R.drawable.warn_1, R.drawable.assignment,
            //R.drawable.data,
            R.drawable.measure, R.drawable.update, R.drawable.system, R.drawable.safe};
    Context context;
    private Dialog mWeiboDialog;//对话框
    private ActionBarDrawerToggle toggle;

    long alltask = 0;//任务数
    long uploadtask = 0;//上传

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    //定义加载等待页面方法
    public void waitingDialog() {
        mWeiboDialog = WeiboDialogUtils.createLoadingDialog(context, "Loading...");//加载对话框
        mHandler.sendEmptyMessageDelayed(1, 1000);//处理消息
    }

    //消息处理线程
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

    /**
     * 重写setContentView，以便于在保留侧滑菜单的同时，让子Activity根据需要加载不同的界面布局
     */
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.menu_activity_first, null);
        frameLayout = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);
        // 将传入的layout加载到activity_base的content_frame里面
        getLayoutInflater().inflate(layoutResID, frameLayout, true);
        super.setContentView(drawerLayout);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        setUpNavigation();
    }

    /**
     * 初始化侧滑菜单
     */
    private void setUpNavigation() {
        planetTitles = getResources().getStringArray(R.array.planets_array);
        BaseAdapter adapter = new BaseAdapter() {
            @SuppressLint("NewApi")
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View layout = View.inflate(context, R.layout.menu_list_item, null);
                ImageView imgv = (ImageView) layout.findViewById(R.id.lgface);
                TextView name1 = (TextView) layout.findViewById(R.id.name1);
                name1.setText("Housekeeper | " + (DLApplication.userName != null ? DLApplication.userName : android.os.Build.MODEL));
                LinearLayout lin = (LinearLayout) layout.findViewById(R.id.item_lin_1);
                LinearLayout lin1 = (LinearLayout) layout.findViewById(R.id.item_lin_1_1);
                ImageView face = (ImageView) layout.findViewById(R.id.lgicon);
                TextView name = (TextView) layout.findViewById(R.id.menu_name);
                TextView num = (TextView) layout.findViewById(R.id.num);
                LinearLayout logout = (LinearLayout) layout.findViewById(R.id.item_tltle1);
                ImageView mm = (ImageView) layout.findViewById(R.id.lgface1);
                mm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context)
                                .setTitle("Exit")
                                .setIcon(android.R.drawable.ic_dialog_info)
                                .setMessage("Are your sure exit？")
                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        DLApplication.userName = "";
                                        finish();
                                        startActivity(new Intent(BaseActivity.this, MainActivity.class));
                                    }
                                })
                                .setNegativeButton("CANCEL", null)
                                .show();

                    }
                });
                if (position > 0) {
                    imgv.setVisibility(View.GONE);
                    name1.setVisibility(View.GONE);
                    logout.setVisibility(View.GONE);
                    mm.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    layout.invalidate();
                }
                //是否显示任务数
                if (position != 1 && position != 2) {
                    lin1.setVisibility(View.GONE);
                    num.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    layout.invalidate();
                } else {
                    lin1.setVisibility(View.GONE);
                    num.setVisibility(View.GONE);
                    //任务数
//                    if (position == 1) {
//                        {
//                            num.setVisibility(View.VISIBLE);
//                            alltask = SqliteUtils.getInstance(context).loadTasksCount();
//                            num.setText(String.valueOf(alltask));
//                            lin1.setBackground(getResources().getDrawable(R.drawable.textviewstyle));
//                        }
//                    }
                    //待上传的测量数
//                    if (position == 2) {
//                        {
//                            num.setVisibility(View.VISIBLE);
//                            uploadtask = SqliteUtils.getInstance(context).queryMeasureCount();
//                            num.setText(String.valueOf(uploadtask));
//                            lin1.setBackground(getResources().getDrawable(R.drawable.textviewstyle3));
//                        }
//                    }
                }

                //admin
                if (DLApplication.userName != null && !DLApplication.userName.equals(DLApplication.amdin)) {
                    if (position != 5) {
                        face.setImageResource(imagesId[position]);
                        name.setText(planetTitles[position]);
                    } else {//==7
                        lin.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        layout.invalidate();
                    }
                } else {
                    face.setImageResource(imagesId[position]);
                    name.setText(planetTitles[position]);
                }

                if (position != 5) {
//                    logout.setVisibility(View.GONE);
//                    btnexitall.setVisibility(View.GONE);
//                    mm.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    layout.invalidate();
                }
//                android.view.ViewGroup.LayoutParams lp =mm.getLayoutParams();
//                lp.height=ScreenHieght();

                return layout;
            }


            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public Object getItem(int position) {
                return planetTitles[position];
            }

            @Override
            public int getCount() {
                return planetTitles.length;
            }
        };
        drawerList.setAdapter(adapter);
//        drawerList.setAdapter(new ArrayAdapter<>(BaseActivity.this,
//                R.layout.menu_list_item, planetTitles));
        drawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    public int ScreenHieght() {
        WindowManager windowManager = getWindowManager();
        Display defaultDisplay = windowManager.getDefaultDisplay();
        // int de= defaultDisplay.getHeight ()-1100;
        int de = defaultDisplay.getHeight() * 180 / 1280;
        return de;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
//        Toast.makeText(BaseActivity.this, planetTitles[position], Toast.LENGTH_SHORT).show();
        switch (position) {
            case 0:
//                startActivity(new Intent(this, HomeAcitvity.class));
                break;
            case 1:
                startActivity(new Intent(this, PersonAcitvity.class));
                //startActivity(new Intent(this, ShowTaskInfo.class));
//                Logger.i(TAG, "click task download.");
                break;
            case 2:
//                startActivity(new Intent(this, DeviceSettingActivity.class));
                //startActivity(new Intent(this, ShowTaskInfo.class));
//                Logger.i(TAG, "click task download.");
                break;
            case 3:
                showShare();
//                startActivity(new Intent(this, UpdateSystemActivity.class));
                //startActivity(new Intent(this, BlueToothFolder.class));
                //startActivity(new Intent(this, UploadBlueToothFolder.class));
//                Logger.i(TAG, "click bluetooth folder  search.");
                break;
//            case 3:
//                startActivity(new Intent(this, ShowExamineRecord.class));
//                Logger.i(TAG, "click safety examine.");
//                break;
            case 4:
//                startActivity(new Intent(this, AboutSystemActivity.class));
//                startActivity(new Intent(this, DeviceSettingActivity.class));
//                Logger.i(TAG, "click devices setting.");
                break;
            case 5:
                startActivity(new Intent(this, UserListAcitvity.class));
                //startActivity(new Intent(this, UpdateSystemActivity.class));
//                Logger.i(TAG, "click update system.");
                break;
//            case 6:
//                //startActivity(new Intent(this, AboutSystemActivity.class));
////                Logger.i(TAG, "click about system.");
//                break;
//            case 7:
////                startActivity(new Intent(this, UserListAcitvity.class));
////                Logger.i(TAG, "click user list.This operation belongs to the administrator.");
//                break;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，微信、QQ和QQ空间等平台使用
        oks.setTitle("Share");
        // titleUrl QQ和QQ空间跳转链接
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("Hi 我是晨晨复兴操练者，我分享今日的晨兴");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url在微信、微博，Facebook等平台中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网使用
        //oks.setComment("要不住祷告");
        oks.setCallback(new OneKeyShareCallback());
        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, Platform.ShareParams shareParams) {
                if (platform != null && platform.getName().equals(Wechat.NAME)) {
                    shareParams.setImageUrl("http://hseq.ccccltd.cn/accident/accident/helppage/helpimg/load.gif");
                    shareParams.setUrl("http://sharesdk.cn");
                    shareParams.setTitle("求你赐给我一颗清洁的新和正直的灵!");
                    shareParams.setText("求你赐给我一颗清洁的新和正直的灵!");
                    shareParams.setWxUserName("gh_654825e93787");//原始ID，在小程序后台能看到，比较不好找
                    shareParams.setWxPath("pages/index/index");
                    shareParams.setShareType(Platform.SHARE_WXMINIPROGRAM);
                }
            }
        });
        // 启动分享GUI
        oks.show(this);
    }

    public class OneKeyShareCallback implements PlatformActionListener {

        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
            Log.i("shareSDK", "SUCCESS");
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {
            Log.i("shareSDK", "***Error***", throwable);
        }

        @Override
        public void onCancel(Platform platform, int i) {
            Log.i("shareSDK", "Cancel");
        }
    }

    /**
     * @param list get All Html Key Vules
     */
    private void showDialog(List<String> list) {
        new AlertDialog.Builder(this)
                .setTitle("列表框")
                .setItems(new String[]{"列表项1", "列表项2", "列表项3"}, null)
                .setNegativeButton("确定", null)
                .show();
    }

}