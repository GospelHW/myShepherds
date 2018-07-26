/*
 * copyright (c)2018-8-15
 * DXC technology
 */

package gospel.v2;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import gospel.v2.dbhelp.SqliteUtils;
import gospel.v2.model.Books;
import gospel.v2.pullableview.MyListener;
import gospel.v2.pullableview.PullToRefreshLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by gospel on 2017/8/21.
 * About ShowMeasure
 */

public class MyMeasureList extends BaseActivity {
    private ListView listview;
    TextView textView;
    String result = null;
    private Button taskAdd;
    Context context;
    List<Books> listtasks = null;
    String potid = "";
    public static MyMeasureList instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.my_measure_main_layout);
        instance = this;
        waitingDialog();//加载等待页面对话框方法
        context = this;
        //获取任务完成标识
        potid = this.getIntent().getStringExtra("potid");


        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setCustomView(R.layout.actionbar);
        //必须加2句
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP);  //根据字面意思是显示类型为显示自定义
        actionBar.setDisplayShowCustomEnabled(true); //自定义界面是否可显示
        // 使用setText的方法对textview动态赋值
        ((TextView) findViewById(R.id.title_name)).setText("我的任务列表");
        //以下代码用于去除阴影
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(0);
        }

        //下拉刷新
        ((PullToRefreshLayout) findViewById(R.id.refresh_view1))
                .setOnRefreshListener(new MyListener());
        context = this;
        //初始化listview对象
        listview = (ListView) findViewById(R.id.task_listView);
        //获取已经下载的任务信息
        getAllTasks();
        //初始化ListView
        initDrawerList();
        //
        TextView txvEmpty = (TextView) findViewById(R.id.empty);//获取textview对象
        /**
         * 判断listview是是否为空，如果为空时显示提示信息，如果不为空时设置为gone
         */
        if (listtasks != null && listtasks.size() > 0) {
            txvEmpty.setVisibility(View.GONE);
        } else {
            txvEmpty.setVisibility(View.VISIBLE);
        }
        //
        Button toggleButton = (Button) findViewById(R.id.button);//获取textview对象
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, BooksAcitvity.class));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAllTasks() {
        listtasks = new ArrayList<>();
        List<Books> alltask = SqliteUtils.getInstance(this).loadBooksList();
        for (Books books : alltask) {
            listtasks.add(books);
        }
    }

    BaseAdapter adapter = new BaseAdapter() {
        @SuppressLint("NewApi")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Books taskInfo = listtasks.get(position);
            Holder holder = null;
            View layout = View.inflate(getApplicationContext(), R.layout.my_measure_list_item_layout, null);

            if (convertView == null) {

                holder = new Holder();
                convertView = LayoutInflater.from(context).inflate(R.layout.task_download_list_item_layout, null);
                holder.tasknamepoint = (TextView) convertView.findViewById(R.id.show_task_name_point);
                holder.taskname = (TextView) convertView.findViewById(R.id.show_task_name);
                convertView.setTag(holder);
            } else {
                holder = (Holder) convertView.getTag();
            }

            holder.tasknamepoint.setText(taskInfo.getBookname());
            holder.taskname.setText(taskInfo.getBooktime());

            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return listtasks.get(position);
        }

        @Override
        public int getCount() {
            return listtasks.size();
        }
    };

    private void initDrawerList() {
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new DrawerItemClickListener());
    }

    public String getMeasureType(String measureType) {
        String str = "";
        if (measureType.equals("T0201")) {
            str = "水准仪";
        }
        if (measureType.equals("T0202")) {
            str = "刚挂尺";
        }
        if (measureType.equals("T0203")) {
            str = "全站仪";
        }
        if (measureType.equals("T0204")) {
            str = "收敛计";
        }
        return str;
    }

    static class Holder {
        TextView tasknamepoint = null;
        TextView tasknamepoint1 = null;
        TextView taskname = null;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//            Toast.makeText(context, "开始测量", Toast.LENGTH_SHORT).show();
            Books taskInfo = listtasks.get(position);
//            if (taskInfo != null) {
            new AlertDialog.Builder(context)
                    .setTitle("System alert")
                    .setIcon(R.drawable.success_small)
                    .setMessage("Choose reader")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .show();
//            } else {
//            Intent intent = new Intent(MyMeasureList.this, CeLiangActivity.class);
////                TaskDetails detailDatas = taskInfo.getTaskDetail();
////                detailDatas.setDateTime(taskInfo.getStartTime());
////                intent.putExtra("detailDatas", detailDatas);
////                intent.putExtra("taskId", taskInfo.getTaskId());
////                intent.putExtra("tasktypes", taskInfo.getTaskType());
//            startActivity(intent);
//            }
        }
    }

//    /**
//     * 广播接收器，用于销毁Activity
//     * @author honest
//     *
//     */
//    private class MyReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Parent.this.finish();
//        }
//    }
}
