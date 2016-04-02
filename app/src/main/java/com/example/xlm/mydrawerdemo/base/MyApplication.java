package com.example.xlm.mydrawerdemo.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.example.xlm.mydrawerdemo.Dao.DaoMaster;
import com.example.xlm.mydrawerdemo.Dao.DaoSession;

import de.greenrobot.dao.AbstractDaoMaster;

/**
 * Created by xlm on 2016/1/15.
 */
public class MyApplication extends Application {
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private SQLiteDatabase db;
    private static MyApplication instance;


    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        setupDatabase();
    }

    public static  MyApplication getInstance(){
        if(instance==null){
            instance=new MyApplication();
        }
        return instance;
    }
    private void setupDatabase(){
        //通过DaoMaster的内部类DevOpenHelper你可以得到一个便利的SQLiteOpenHelper对象
        //可能你已经注意到了，你并不需要去编写[CREATE TABLE]这样的sql语句，因为greenDao已经帮你做了
        //注意：默认的DaoMaster.DevOpenHeler会在数据库升级时，删除所有的表，意味着这将导致数据的丢失
        //所以，在正式的项目中，你还应该做一层封装，来实现数据的安全升级
        DaoMaster.DevOpenHelper helper=new DaoMaster.DevOpenHelper(this,"forum-db",null);
        db=helper.getWritableDatabase();
        //注意：该数据库连接属于DaoMaster所以多个session指的是相同的数据库连接
        daoMaster=new DaoMaster(db);
        daoSession=daoMaster.newSession();
    }
}
