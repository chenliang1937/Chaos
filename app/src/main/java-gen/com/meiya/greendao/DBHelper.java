package com.meiya.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Config;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by chenliang3 on 2016/5/7.
 */
public class DBHelper {

    private static DBHelper instance;
    private static final Object WATCH_DOG = new Object();
    private static final String DB_NAME = "chaos";
    private DaoSession daoSession;
    private SQLiteDatabase db;

    public static DBHelper getInstance(Context context){
        synchronized (WATCH_DOG){
            if (instance == null){
                instance = new DBHelper(context);
            }
            return instance;
        }
    }

    private DBHelper(Context context){
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
        db = helper.getWritableDatabase();

        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        if (Config.DEBUG){
            QueryBuilder.LOG_SQL = true;
            QueryBuilder.LOG_VALUES = true;
        }
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    public SQLiteDatabase getDb(){
        return db;
    }

}
