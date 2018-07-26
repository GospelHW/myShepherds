/*
 * copyright (c)2018-8-15
 * DXC technology
 */

package gospel.v2.dbhelp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import gospel.v2.logs.Logger;
import gospel.v2.model.Books;
import gospel.v2.model.GasInfo;
import gospel.v2.model.MeasureData;
import gospel.v2.model.ReadRecord;
import gospel.v2.model.Reader;
import gospel.v2.model.TaskDetails;
import gospel.v2.model.TaskInfo;
import gospel.v2.model.User;
import gospel.v2.utils.DateConver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gospel on 2017/8/18.
 * DatabaseHelper
 */

public class SqliteUtils {
    static String TAG = SqliteUtils.class.getSimpleName();
    /**
     * 数据库名
     */
    public static final String DB_NAME = "myBooks.db";
    /**
     * 数据库版本
     */
    public static final int VERSION = 2018052118;

    private static SqliteUtils sqliteDB;

    private SQLiteDatabase db;

    public SqliteUtils(Context context) {
        DatabaseHelper dbHelper = new DatabaseHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取SqliteDB实例
     *
     * @param context
     */
    public synchronized static SqliteUtils getInstance(Context context) {
        if (sqliteDB == null) {
            sqliteDB = new SqliteUtils(context);
            Logger.i(TAG, "SqliteUtils init success.");
        }
        return sqliteDB;
    }

    /**
     * 将User实例存储到数据库。
     */
    public int saveBooks(String name, String time) {
        if (name != null) {
            Cursor cursor = db.rawQuery("select * from tbl_books where bookname=?", new String[]{name});
            if (cursor.getCount() > 0) {
                return -1;
            } else {
                try {
                    db.execSQL("insert into tbl_books(bookname,booktime)" +
                            "values(?,?) ", new String[]{name, time});
                } catch (Exception e) {
                    Log.d("保存书报信息错误", e.getMessage().toString());
                }
                return 1;
            }
        } else {
            return 0;
        }
    }

    /**
     * 将User实例存储到数据库。
     */
    public int saveReader(Reader reader) {
        if (reader != null) {
            Cursor cursor = db.rawQuery("select * from tbl_reader where username=?", new String[]{reader.getUsername()});
            if (cursor.getCount() > 0) {
                return -1;
            } else {
                try {
                    db.execSQL("insert into tbl_reader(username,gender,s_year,phone,realname,area,email,location)" +
                            "values(?,?,?,?,?,?,?,?) ", new String[]{reader.getUsername(), reader.getGender(), reader.getsYear(), reader.getPhone(), reader.getRealname(), reader.getArea(), reader.getEmail(), reader.getLocation()});
                } catch (Exception e) {
                    Log.d("保存读者信息错误", e.getMessage().toString());
                }
                return 1;
            }
        } else {
            return 0;
        }
    }

    /**
     * 将User实例存储到数据库。
     */
    public int saveUser(User user) {
        if (user != null) {
            Cursor cursor = db.rawQuery("select * from tbl_users where username=?", new String[]{user.getuName().toString()});
            if (cursor.getCount() > 0) {
                return -1;
            } else {
                try {
                    db.execSQL("insert into tbl_users(username,password,repassword,phone,realname,idcard,address, gongdian)" +
                            "values(?,?,?,?,?,?,?,?) ", new String[]{user.getuName().toString(), user.getuPwd().toString(), user.getuRePwd().toString(),
                            user.getuPhone().toString(), user.geturealName().toString(), user.getidCard().toString(), user.getuAddress().toString(), user.getgongDian().toString(),});
                } catch (Exception e) {
                    Log.d("保存用户信息错误", e.getMessage().toString());
                }
                return 1;
            }
        } else {
            return 0;
        }
    }

    /**
     * 从数据库读取User信息。
     */
    public List<User> loadUser() {
        List<User> list = new ArrayList<User>();
        Cursor cursor = db
                .query("tbl_users", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setuName(cursor.getString(cursor
                        .getColumnIndex("username")));
                user.setuPwd(cursor.getString(cursor
                        .getColumnIndex("password")));
                user.setuRePwd(cursor.getString(cursor
                        .getColumnIndex("repassword")));
                user.setuPhone(cursor.getString(cursor
                        .getColumnIndex("phone")));
                user.seturealName(cursor.getString(cursor
                        .getColumnIndex("realname")));
                user.setidCard(cursor.getString(cursor
                        .getColumnIndex("idcard")));
                user.setuAddress(cursor.getString(cursor
                        .getColumnIndex("address")));
                user.setgongDian(cursor.getString(cursor
                        .getColumnIndex("gongdian")));
                list.add(user);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 登录，根据用户名和密码查询
     *
     * @param pwd
     * @param name
     * @return
     */
    public int Quer(String pwd, String name) {
        HashMap<String, String> hashmap = new HashMap<String, String>();
        Cursor cursor = db.rawQuery("select * from tbl_users where username=?", new String[]{name});
        if (cursor.getCount() > 0) {
            Cursor pwdcursor = db.rawQuery("select * from tbl_users where password=? and username=?", new String[]{pwd, name});
            if (pwdcursor.getCount() > 0) {
                return 1;
            } else {
                return -1;
            }
        } else {
            return 0;
        }
    }

    /**
     * 修改用户信息
     *
     * @param user
     * @return
     */
    public int updateUserInfo(User user) {
        if (user != null) {
            try {
                db.execSQL("update tbl_users set phone=?,idcard=?,address=?,gongdian=? where id=?",
                        new String[]{user.getuPhone(), user.getidCard(), user.getuAddress(), user.getgongDian()
                                , String.valueOf(user.getId())});
            } catch (Exception e) {
                e.printStackTrace();
                Logger.i(TAG, "保存任务信息异常：" + e.getMessage().toString());
                return 0;
            }
        } else {
            return 0;
        }
        return 1;
    }

    /**
     * 将Measure存储到数据库。
     */
    public int saveMeasure(MeasureData measure) {
        if (measure != null) {
            try {
                Cursor cursor = db.rawQuery("select * from tbl_measure where cldian=? and taskId=?", new String[]{measure.getCldian().toString(), measure.getTaskId()});
                if (cursor.getCount() <= 0) {
                    db.execSQL("insert into tbl_measure(cllicheng,cldian,clren,cltime,gaocheng," +
                                    "shoulian,status,datatype,sources,taskId,cllichengId,cldianId," +
                                    "createtime,updatetime,chushizhi,chazhi) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ",
                            new String[]{measure.getCllicheng(), measure.getCldian(), measure.getClren(), measure.getCltime(), measure.getGaocheng(),
                                    measure.getShoulian(), measure.getStatus(), measure.getDataType(), measure.getSources(), measure.getTaskId(),
                                    measure.getCllichengId(), measure.getCldianId(), measure.getCreateTime(), "", measure.getChushizhi(), measure.getChazhi()});
                    return 1;
                }
                if (cursor.getCount() > 0) {
                    return 1;
                }
                return 0;
            } catch (Exception e) {
                Logger.e(TAG, "保存蓝牙读取测量数据异常：" + e.getMessage().toString());
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 查询未上传的测量数据
     */
    public List<MeasureData> queryMeasure(String status) {
        List<MeasureData> list = new ArrayList<MeasureData>();
        try {
            Cursor cursor = db.rawQuery("select * from tbl_measure where status =?", new String[]{status});
            if (cursor.moveToFirst()) {
                do {
                    MeasureData downLoadData = new MeasureData();
                    downLoadData.setTaskId(cursor.getString(cursor.getColumnIndex("taskId")));
                    downLoadData.setCllicheng(cursor.getString(cursor.getColumnIndex("cllicheng")));
                    downLoadData.setCldian(cursor.getString(cursor.getColumnIndex("cldian")));
                    downLoadData.setCllichengId(cursor.getString(cursor.getColumnIndex("cllichengId")));
                    downLoadData.setCldianId(cursor.getString(cursor.getColumnIndex("cldianId")));
                    downLoadData.setClren(cursor.getString(cursor.getColumnIndex("clren")));
                    downLoadData.setCltime(cursor.getString(cursor.getColumnIndex("cltime")));
                    downLoadData.setGaocheng(cursor.getString(cursor.getColumnIndex("gaocheng")));
                    downLoadData.setShoulian(cursor.getString(cursor.getColumnIndex("shoulian")));
                    downLoadData.setDataType(cursor.getString(cursor.getColumnIndex("datatype")));
                    downLoadData.setSources(cursor.getString(cursor.getColumnIndex("sources")));
                    downLoadData.setChushizhi(cursor.getString(cursor.getColumnIndex("chushizhi")));
                    downLoadData.setChazhi(cursor.getString(cursor.getColumnIndex("chazhi")));
                    downLoadData.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                    list.add(downLoadData);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Logger.e("查询未上传的测量数据异常：", e.getMessage().toString());
            return null;
        }
        return list;
    }

    /**
     * 查询未上传的测量数据条数
     */
    public long queryMeasureCount() {
        try {
            Cursor cursor = db.rawQuery("select count(*) from tbl_measure where status =?", new String[]{"1"});
            cursor.moveToFirst();
            long count = cursor.getLong(0);
            cursor.close();
            return count;
        } catch (Exception e) {
            Logger.e("查询未上传的测量数据异常：", e.getMessage().toString());
            return 0;
        }
    }

    /**
     * 将DownloadTask存储到数据库。
     */
    public int saveTaskInfo(TaskInfo downLoadData, TaskDetails taskDetails) {
        if (downLoadData != null) {
            try {
                Cursor cursor = db.rawQuery("select * from tbl_task where pointId=? and taskId=?", new String[]{taskDetails.getPointId(), downLoadData.getTaskId()});
                if (cursor.getCount() <= 0) {
                    db.execSQL("insert into tbl_task(taskId,userId,taskType,measureType," +
                                    "startTime,endTime,proName,section,mileageLabel,mileageId," +
                                    "pointLabel,pointId,initialValue,status,sjz,cz) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ",
                            new String[]{downLoadData.getTaskId(), downLoadData.getUserId(), downLoadData.getTaskType(),
                                    downLoadData.getMeasureType(), downLoadData.getStartTime(), downLoadData.getEndTime(),
                                    taskDetails.getProName(), taskDetails.getSection(),
                                    taskDetails.getMileageLabel(), taskDetails.getMileageId(),
                                    taskDetails.getPointLabel(), taskDetails.getPointId()
                                    , taskDetails.getInitialValue(), "1", "", ""});
                    return 1;
                } else {
                    return 2;
                }
            } catch (Exception e) {
                e.printStackTrace();
                Logger.i(TAG, "保存任务信息异常：" + e.getMessage().toString());
            }
        }
        return 0;
    }

    /**
     * 从数据库读取未完成任务信息。
     */
    public List<TaskInfo> loadMyMeasureList() {
        List<TaskInfo> list = new ArrayList<TaskInfo>();
        List<TaskDetails> listd = new ArrayList<TaskDetails>();
//        Cursor cursor = db
//                .query("tbl_task", null, null, null, null, null, null);
        Cursor cursor = db.rawQuery("select * from tbl_task where status=1 order by startTime desc", null);
        if (cursor.moveToFirst()) {
            do {
                TaskInfo downLoadData = new TaskInfo();
//                downLoadData.setId(cursor.getInt(cursor.getColumnIndex("id")));
                downLoadData.setTaskId(cursor.getString(cursor.getColumnIndex("taskId")));
                downLoadData.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                downLoadData.setTaskType(cursor.getString(cursor.getColumnIndex("taskType")));
                downLoadData.setMeasureType(cursor.getString(cursor.getColumnIndex("measureType")));
                downLoadData.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
                downLoadData.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
                downLoadData.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                downLoadData.setSjz(cursor.getString(cursor.getColumnIndex("sjz")));
                downLoadData.setCz(cursor.getString(cursor.getColumnIndex("cz")));
                //任务详情
                TaskDetails td = new TaskDetails();
                td.setProName(cursor.getString(cursor.getColumnIndex("proName")));
                td.setSection(cursor.getString(cursor.getColumnIndex("section")));
                td.setMileageLabel(cursor.getString(cursor.getColumnIndex("mileageLabel")));
                td.setMileageId(cursor.getString(cursor.getColumnIndex("mileageId")));
                td.setPointLabel(cursor.getString(cursor.getColumnIndex("pointLabel")));
                td.setPointId(cursor.getString(cursor.getColumnIndex("pointId")));
                td.setInitialValue(cursor.getString(cursor.getColumnIndex("initialValue")));
                listd.add(td);
                //任务详情
                downLoadData.setTaskDetail(td);
                list.add(downLoadData);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 从数据库读取书报信息。
     */
    public List<Books> loadBooksList() {
        List<Books> list = new ArrayList<Books>();
        Cursor cursor = db.rawQuery("select * from tbl_books order by booktime desc", null);
        if (cursor.moveToFirst()) {
            do {
                Books downLoadData = new Books();
                downLoadData.setBookname(cursor.getString(cursor.getColumnIndex("bookname")));
                downLoadData.setBooktime(cursor.getString(cursor.getColumnIndex("booktime")));
                list.add(downLoadData);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 从数据库读订阅报信息。
     */
    public List<Reader> loadReaderList() {
        List<Reader> list = new ArrayList<Reader>();
        Cursor cursor = db.rawQuery("select * from tbl_reader", null);
        if (cursor.moveToFirst()) {
            do {
                Reader downLoadData = new Reader();
                downLoadData.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                downLoadData.setGender(cursor.getString(cursor.getColumnIndex("gender")));
                downLoadData.setsYear(cursor.getString(cursor.getColumnIndex("s_year")));
                downLoadData.setRealname(cursor.getString(cursor.getColumnIndex("realname")));
                downLoadData.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                downLoadData.setArea(cursor.getString(cursor.getColumnIndex("area")));
                downLoadData.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                downLoadData.setLocation(cursor.getString(cursor.getColumnIndex("location")));
                list.add(downLoadData);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 从数据库读订阅报信息。
     */
    public List<ReadRecord> loadReadRecordList() {
        List<ReadRecord> list = new ArrayList<ReadRecord>();
        Cursor cursor = db.rawQuery("select * from tbl_readrecord", null);
        if (cursor.moveToFirst()) {
            do {
                ReadRecord downLoadData = new ReadRecord();
                downLoadData.setBookid(cursor.getString(cursor.getColumnIndex("bookid")));
                downLoadData.setBookname(cursor.getString(cursor.getColumnIndex("bookname")));
                downLoadData.setUserid(cursor.getString(cursor.getColumnIndex("userid")));
                downLoadData.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                downLoadData.setReadtime(cursor.getString(cursor.getColumnIndex("readtime")));
                list.add(downLoadData);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 从数据库读取任务信息。
     */
    public List<TaskInfo> loadTasks() {
        List<TaskInfo> list = new ArrayList<TaskInfo>();
        List<TaskDetails> listd = new ArrayList<TaskDetails>();
        Cursor cursor = db.rawQuery("select * from tbl_task order by startTime desc", null);
        if (cursor.moveToFirst()) {
            do {
                TaskInfo downLoadData = new TaskInfo();
//                downLoadData.setId(cursor.getInt(cursor.getColumnIndex("id")));
                downLoadData.setTaskId(cursor.getString(cursor.getColumnIndex("taskId")));
                downLoadData.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                downLoadData.setTaskType(cursor.getString(cursor.getColumnIndex("taskType")));
                downLoadData.setMeasureType(cursor.getString(cursor.getColumnIndex("measureType")));
                downLoadData.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
                downLoadData.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
                downLoadData.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                downLoadData.setSjz(cursor.getString(cursor.getColumnIndex("sjz")));
                downLoadData.setCz(cursor.getString(cursor.getColumnIndex("cz")));
                //任务详情
                TaskDetails td = new TaskDetails();
                td.setProName(cursor.getString(cursor.getColumnIndex("proName")));
                td.setSection(cursor.getString(cursor.getColumnIndex("section")));
                td.setMileageLabel(cursor.getString(cursor.getColumnIndex("mileageLabel")));
                td.setMileageId(cursor.getString(cursor.getColumnIndex("mileageId")));
                td.setPointLabel(cursor.getString(cursor.getColumnIndex("pointLabel")));
                td.setPointId(cursor.getString(cursor.getColumnIndex("pointId")));
                td.setInitialValue(cursor.getString(cursor.getColumnIndex("initialValue")));
                listd.add(td);
                //任务详情
                downLoadData.setTaskDetail(td);
                list.add(downLoadData);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 未完成量测任务列表
     *
     * @return
     */
    public List<TaskInfo> loadTasksByStatus(String status) {
        List<TaskInfo> list = new ArrayList<TaskInfo>();
        List<TaskDetails> listd = new ArrayList<TaskDetails>();
        Cursor cursor = db.rawQuery("select * from tbl_task where status =? order by startTime desc", new String[]{status});
        if (cursor.moveToFirst()) {
            do {
                TaskInfo downLoadData = new TaskInfo();
//                downLoadData.setId(cursor.getInt(cursor.getColumnIndex("id")));
                downLoadData.setTaskId(cursor.getString(cursor.getColumnIndex("taskId")));
                downLoadData.setUserId(cursor.getString(cursor.getColumnIndex("userId")));
                downLoadData.setTaskType(cursor.getString(cursor.getColumnIndex("taskType")));
                downLoadData.setMeasureType(cursor.getString(cursor.getColumnIndex("measureType")));
                downLoadData.setStartTime(cursor.getString(cursor.getColumnIndex("startTime")));
                downLoadData.setEndTime(cursor.getString(cursor.getColumnIndex("endTime")));
                downLoadData.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                downLoadData.setSjz(cursor.getString(cursor.getColumnIndex("sjz")));
                downLoadData.setCz(cursor.getString(cursor.getColumnIndex("cz")));
                //任务详情
                TaskDetails td = new TaskDetails();
                td.setProName(cursor.getString(cursor.getColumnIndex("proName")));
                td.setSection(cursor.getString(cursor.getColumnIndex("section")));
                td.setMileageLabel(cursor.getString(cursor.getColumnIndex("mileageLabel")));
                td.setMileageId(cursor.getString(cursor.getColumnIndex("mileageId")));
                td.setPointLabel(cursor.getString(cursor.getColumnIndex("pointLabel")));
                td.setPointId(cursor.getString(cursor.getColumnIndex("pointId")));
                td.setInitialValue(cursor.getString(cursor.getColumnIndex("initialValue")));
                listd.add(td);
                //任务详情
                downLoadData.setTaskDetail(td);
                list.add(downLoadData);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 从数据库读取任务总条数。
     */
    public long loadTasksCount() {
        try {
            Cursor cursor = db.rawQuery("select count(*) from tbl_task where status =?", new String[]{"1"});
            cursor.moveToFirst();
            long count = cursor.getLong(0);
            cursor.close();
            return count;
        } catch (Exception e) {
            Logger.e(TAG, "查询任务总数异常" + e.getMessage());
            return 0;
        }
    }

    /**
     * 手动输入测量数据保存方法
     *
     * @param taskIdS
     * @param td
     * @param taskname
     * @param nowtime
     * @param gc
     * @param sl
     * @return
     */
    public int saveCustomMeasure(String taskIdS, TaskDetails td, String taskname, String nowtime, String gc, String sl, String csz, String cz) {
        if (td != null) {
            try {
                Cursor cursor = db.rawQuery("select * from tbl_measure where cldianId=? and taskId=? ", new String[]{td.getPointId(), taskIdS});
                if (cursor.getCount() <= 0) {
                    db.execSQL("insert into tbl_measure(cllicheng,cldian,cllichengId,cldianId,clren,cltime,gaocheng," +
                                    "shoulian,status,datatype,sources,taskId," +
                                    "createtime,updatetime,chushizhi,chazhi) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ",
                            new String[]{td.getMileageLabel(), td.getPointLabel(), td.getMileageId(), td.getPointId(),
                                    taskname, nowtime, gc.trim(), sl.trim(), "1", "0",
                                    "", taskIdS, nowtime, "", csz, cz});
                    return 1;
                }
                if (cursor.getCount() > 0) {
                    return 1;
                }
                return 0;
            } catch (Exception e) {
                Log.d("手动输入测量值保存异常：", e.getMessage().toString());
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 更新数据上传状态
     *
     * @param pointId
     * @return
     */
    public int updateUpLoadStatus(String taskId, String pointId, boolean isOK) {
        if (pointId != null) {
            try {
//                Cursor cursor = db.rawQuery("select * from tbl_measure where taskId=?", new String[]{taskId});
//                if (cursor.getCount() <= 0) {
                if (!isOK) {
                    db.execSQL("update tbl_measure set status =?,updatetime =? where cldianId=? and taskId =? ",
                            new String[]{"0", DateConver.getStringDate(), pointId, taskId});
                    return 1;
                } else {//平台找不到这个任务，先以这种方式处理，不在上传列表显示-----没执行
                    db.execSQL("update tbl_measure set status =?,updatetime =? where cldianId=? and taskId =? ",
                            new String[]{"2", DateConver.getStringDate(), pointId, taskId});
                    return 1;
                }

//                } else {
//                    return 0;
//                }
            } catch (
                    Exception e)

            {
                Logger.e("上传成功，更新上传状态失败：", e.getMessage().toString());
                return 0;
            }
        } else

        {
            return 0;
        }

    }

    /**
     * 更新任务处理状态
     *
     * @param pointId
     * @return
     */
    public int updateTaskStatus(String taskId, String sjz, String cz, String pointId) {
        if (pointId != null) {
            try {
                db.execSQL("update tbl_task set status =?,sjz=?,cz=? where pointId=? and taskId =?",
                        new String[]{"0", sjz, cz, pointId, taskId});
                return 1;
            } catch (Exception e) {
                Logger.e("更新任务处理状态失败：", e.getMessage().toString());
                return 0;
            }
        } else {
            return 0;
        }
    }

    /**
     * 获取所有气体检查数据
     *
     * @return
     */
    public List<GasInfo> loadGasList() {
        List<GasInfo> list = new ArrayList<GasInfo>();
        Cursor cursor = db.rawQuery("select * from tbl_gas order by checkTime desc", null);
        if (cursor.moveToFirst()) {
            do {
                GasInfo gasInfo = new GasInfo();
                gasInfo.setCarbonMonoxide(cursor.getString(cursor.getColumnIndex("carbonMonoxide")));
                gasInfo.setCarbonDioxide(cursor.getString(cursor.getColumnIndex("carbonDioxide")));
                gasInfo.setMethane(cursor.getString(cursor.getColumnIndex("methane")));
                gasInfo.setHydrogenSulfide(cursor.getString(cursor.getColumnIndex("hydrogenSulfide")));
                gasInfo.setCheckTime(cursor.getString(cursor.getColumnIndex("checkTime")));
                gasInfo.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                list.add(gasInfo);
            } while (cursor.moveToNext());
        }
        return list;
    }

    /**
     * 手动录入气体检查结果
     *
     * @param gasInfo
     * @return
     */
    public int saveGasByManual(GasInfo gasInfo) {
        if (gasInfo != null) {
            try {
                db.execSQL("insert into tbl_gas(carbonMonoxide,carbonDioxide,methane,hydrogenSulfide,checkTime,status)" +
                        "values(?,?,?,?,?,?) ", new String[]{gasInfo.getCarbonMonoxide(), gasInfo.getCarbonDioxide(),
                        gasInfo.getMethane(), gasInfo.getHydrogenSulfide(), gasInfo.getCheckTime(), gasInfo.getStatus()});
            } catch (Exception e) {
                Log.d("手动录入气体检查结果错误", e.getMessage().toString());
                e.printStackTrace();
            }
            return 1;
        } else {
            return 0;
        }
    }
}