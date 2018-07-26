package gospel.v2.dbhelp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import gospel.v2.logs.Logger;

import java.io.File;

/**
 * Created by gospel on 2017/8/18.
 * DatabaseHelper
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    static String TAG = DatabaseHelper.class.getSimpleName();
    static String name = "user.db";
    static int dbVersion = 1;
    private static final String mDatabasename = "mybooks_db";
    private static SQLiteDatabase.CursorFactory mFactory = null;
    private static final int mVersion = 1;

    public DatabaseHelper(Context context) {
        super(context, mDatabasename, mFactory, mVersion);
    }

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                          int version) {
//        DatabaseContext dbContext = new DatabaseContext(context);
        super(context, getMyDatabaseName(name), factory, version);
        Logger.i(TAG, "DatabaseHelper init success.");
    }

    private static String getMyDatabaseName(String name) {
        String databasename = name;
        boolean isSdcardEnable = false;
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {//SDCard是否插入
            isSdcardEnable = true;
        }
        String dbPath = null;
        if (isSdcardEnable) {
            dbPath = Environment.getExternalStorageDirectory().getPath() + "/myBooks/database/";
        } else {//未插入SDCard，建在内存中
            Logger.i(TAG, "没有内存卡，数据库无法创建");
        }
        File dbp = new File(dbPath);
        if (!dbp.exists()) {
            dbp.mkdirs();
            Logger.i(TAG, "创建数据库存放目录：" + dbPath);
        }
        databasename = dbPath + databasename;
        return databasename;
    }

    public static final String TABLE_USERS = "tbl_users"; //用户信息
    public static final String TABLE_TASKS = "tbl_task"; //量测任务信息表
    public static final String TABLE_MEASURE = "tbl_measure"; //量测数据结果表
    public static final String TABLE_SECURITY = "tbl_security"; //安全检查表
    public static final String TABLE_GAS = "tbl_gas"; //气体检测表
    public static final String TABLE_BOOKS = "tbl_books"; //books
    public static final String TABLE_READER = "tbl_reader"; //reader
    public static final String TABLE_READRECORD = "tbl_readrecord"; //readrecord

//    public SQLiteHelper(Context context) {
//        super(context, mDatabasename, mFactory, mVersion);
//    }
//
//    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
//                        int version) {
//        super(context, name, factory, version);
//    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        createUser(db);
        Logger.i(TAG, "tbl_users create success.");
//        createDownloadinfo(db);
//        Logger.i(TAG, "tbl_task create success.");
//        createMeasure(db);
//        Logger.i(TAG, "tbl_measure create success.");
//        createGas(db);
//        Logger.i(TAG, "tbl_gas create success.");
//        createSecurity(db);
//        Logger.i(TAG, "tbl_security create success.");
        createBooks(db);
        Logger.i(TAG, "tbl_books create success.");
        createReader(db);
        Logger.i(TAG, "tbl_reader create success.");
        createReadRecord(db);
        Logger.i(TAG, "tbl_readrecord create success.");

//        formatTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int _oldVersion, int _newVersion) {
        //数据库版本号升级，会执行的方法，一般用于数据库表或者字段更新（增加，删除，修改）等
//        createUser(db);
//        Logger.i(TAG, "tbl_users upgrade success.");
//        createDownloadinfo(db);
//        Logger.i(TAG, "tbl_task upgrade success.");
//        createMeasure(db);
//        Logger.i(TAG, "tbl_measure upgrade success.");
//        createGas(db);
//        Logger.i(TAG, "tbl_gas upgrade success.");
//        createSecurity(db);
//        Logger.i(TAG, "tbl_security upgrade success.");
//        db.execSQL("alter table tbl_gas add checkTime varchar");
//        db.execSQL("alter table tbl_gas add status varchar");
    }

    public void formatTable(SQLiteDatabase db) {
        try {
            db.execSQL("DELETE FROM tbl_users");
            Logger.i(TAG, "tbl_users format success.");
            db.execSQL("DELETE FROM tbl_task");
            Logger.i(TAG, "tbl_task format success.");
            db.execSQL("DELETE FROM tbl_measure");
            Logger.i(TAG, "tbl_measure format success.");
        } catch (Exception e) {
            Logger.e(TAG, "format table failed." + e.getMessage());
        }
    }

    /**
     * 订阅记录表
     *
     * @param db
     */
    public void createReadRecord(SQLiteDatabase db) {
        //创建用户信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_READRECORD +
                "(id integer primary key autoincrement," +
                "bookid varchar(50)," +
                "bookname varchar(200)," +
                "readerid varchar(50)," +
                "readername varchar(200)," +
                "readtime varchar(50))";
        try {
            db.execSQL(sql);
            db.execSQL("insert into tbl_readrecord(bookid,readerid,readtime) values(?,?,?,?,?) ",
                    new String[]{"", "利未记结晶读经（一）", "", "Dw", "2018年1月"});
        } catch (Exception e) {
            Logger.e(TAG, "tbl_readrecord create failed." + e.getMessage());
        }
    }


    /**
     * 创建用户表
     *
     * @param db
     */
    public void createBooks(SQLiteDatabase db) {
        //创建用户信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_BOOKS +
                "(id integer primary key autoincrement," +
                "bookname varchar(200)," +
                "booktime varchar(100))";
        try {
            db.execSQL(sql);
            db.execSQL("insert into tbl_books(bookname,booktime) values(?,?) ",
                    new String[]{"利未记结晶读经（一）", "2018年夏季训练"});
        } catch (Exception e) {
            Logger.e(TAG, "tbl_books create failed." + e.getMessage());
        }
    }

    /**
     * 创建用户表
     *
     * @param db
     */
    public void createReader(SQLiteDatabase db) {
        //创建用户信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_READER +
                "(id integer primary key autoincrement," +
                "username varchar(20)," +
                "gender varchar(20)," +
                "s_year varchar(20)," +
                "phone varchar(20)," +
                "realname varchar(20)," +
                "area varchar(20)," +
                "email varchar(50)," +
                "location varchar(500))";
        try {
            db.execSQL(sql);
            db.execSQL("insert into tbl_reader(username,gender,s_year,phone,realname,area,email,location) values(?,?,?,?,?,?,?,?) ",
                    new String[]{"dw", "bro", "13Y", "152****9058", "gospel", "wj", "gospel721@gmail.com", "北京市朝阳区"});
        } catch (Exception e) {
            Logger.e(TAG, "tbl_reader create failed." + e.getMessage());
        }
    }


    /**
     * 创建用户表
     *
     * @param db
     */
    public void createUser(SQLiteDatabase db) {
        //创建用户信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_USERS +
                "(id integer primary key autoincrement," +
                "username varchar(20)," +
                "password varchar(20)," +
                "repassword varchar(20)," +
                "phone varchar(20)," +
                "realname varchar(20)," +
                "idcard varchar(20)," +
                "address varchar(20)," +
                "gongdian varchar(20))";
        try {
            db.execSQL(sql);
            db.execSQL("insert into tbl_users(username,password,repassword,phone,realname,idcard, address,gongdian) values(?,?,?,?,?,?,?,?) ",
                    new String[]{"gospel", "gospel5200", "gospel5200", "11", "gospel", "1111", "beijing", "1"});
        } catch (Exception e) {
            Logger.e(TAG, "tbl_users create failed." + e.getMessage());
        }
    }

    /**
     * 创建任务下载表
     *
     * @param db
     */
    public void createDownloadinfo(SQLiteDatabase db) {
        //创建任务下载信息数据表
        String downloadsql = "CREATE TABLE IF NOT EXISTS " + TABLE_TASKS + " ("
                + "id integer primary key autoincrement, "
                + "taskId VARCHAR, "
                + "userId VARCHAR, "
                + "taskType VARCHAR, "
                + "measureType VARCHAR, "
                + "startTime VARCHAR, "
                + "endTime VARCHAR, "
                + "proName VARCHAR,"
                + "section VARCHAR,"
                + "mileageLabel VARCHAR,"
                + "mileageId VARCHAR,"
                + "pointLabel VARCHAR,"
                + "pointId VARCHAR,"
                + "initialValue VARCHAR,"
                + "status VARCHAR,"
                + "sjz VARCHAR,"
                + "cz VARCHAR)";
        try {
            db.execSQL(downloadsql);
        } catch (Exception e) {
            Logger.i(TAG, "tbl_task create failed." + e.getMessage());
        }
    }


    /**
     * 解析测量数据
     *
     * @param db
     */
    public void createMeasure(SQLiteDatabase db) {
        //量测结果信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_MEASURE
                + " (id integer primary key autoincrement," +
                "taskId  varchar(100)," +
                "cllicheng varchar(200)," +
                "cldian varchar(200)," +
                "cllichengId varchar(200)," +
                "cldianId varchar(200)," +
                "clren varchar(100)," +
                "cltime varchar(50)," +
                "gaocheng varchar(100)," +
                "shoulian varchar(100)," +
                "status varchar(100)," +
                "datatype varchar(100)," +
                "createtime varchar(50)," +
                "updatetime varchar(50)," +
                "sources varchar(2000)," +
                "chushizhi varchar(50)," +
                "chazhi varchar(50))";
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Logger.i(TAG, "tbl_measure create failed." + e.getMessage());
        }
    }

    /**
     * 安全检查表
     *
     * @param db
     */
    public void createSecurity(SQLiteDatabase db) {
        //安全检查信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_SECURITY
                + " (id integer primary key autoincrement," +
                "type varchar(50)," +
                "isHg varchar(50)," +
                "checkTime varchar(50)," +
                "status varchar(50))";
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Logger.i(TAG, "tbl_security create failed." + e.getMessage());
        }
    }

    /**
     * 气体检测表
     *
     * @param db
     */
    public void createGas(SQLiteDatabase db) {
        //气体检测信息表
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_GAS
                + " (id integer primary key autoincrement," +
                "carbonDioxide varchar(50)," +
                "carbonMonoxide varchar(50)," +
                "methane varchar(50)," +
                "hydrogenSulfide varchar(50)," +
                "checkTime varchar(50)," +
                "status varchar(50))";
        try {
            db.execSQL(sql);
        } catch (Exception e) {
            Logger.i(TAG, "tbl_gas create failed." + e.getMessage());
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }
}
