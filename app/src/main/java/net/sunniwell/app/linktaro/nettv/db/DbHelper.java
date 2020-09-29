package net.sunniwell.app.linktaro.nettv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import net.sunniwell.app.linktaro.nettv.bean.Parameter;

/* renamed from: net.sunniwell.app.linktaro.nettv.db.DbHelper */
public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    private static final String FEED_DELETEABLE = "deleteable";
    private static final String FEED_NAME = "name";
    private static final String FEED_SYSTEMABLE = "systemable";
    private static final String FEED_UPDATEABLE = "updateable";
    private static final String FEED_VALUE = "value";

    /* renamed from: ID */
    private static final String f331ID = "_id";
    private static final String TABLE_NAME = "parameterTb";
    private static final String TAG = DbHelper.class.getSimpleName();
    private static SQLiteDatabase sDB;
    private static String sDatabaseName = "parameter.db";

    public DbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, sDatabaseName, factory, 1);
        Log.d(TAG, "DbHelper()");
    }

    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");
        db.execSQL("CREATE TABLE IF NOT EXISTS parameterTb (_id INTEGER PRIMARY KEY , name TEXT UNIQUE, value TEXT,deleteable VARCHAR(1),updateable VARCHAR(1),systemable VARCHAR(1))");
        initDbData(db);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS parameterTb");
        onCreate(db);
    }

    public void onOpen(SQLiteDatabase mDB) {
        super.onOpen(mDB);
    }

    public long insertDataByContentValue(ContentValues cv) {
        return sDB.insert("parameterTb", "_id", cv);
    }

    public long insertData(Parameter parameter) {
        long id = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put("name", parameter.getName());
            cv.put("value", parameter.getValue());
            cv.put("deleteable", parameter.getDeletable());
            cv.put("updateable", parameter.getUpdateable());
            cv.put("systemable", parameter.getSystemable());
            return sDB.insert("parameterTb", "_id", cv);
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public int updateData(String name, String value) {
        int id = -1;
        try {
            ContentValues cv = new ContentValues();
            cv.put("name", name);
            cv.put("value", value);
            return sDB.update("parameterTb", cv, "name='" + name + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
            return id;
        }
    }

    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
        int count = -1;
        try {
            return sDB.update("parameterTb", values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return count;
        }
    }

    public Cursor queryData() {
        return sDB.query("parameterTb", null, null, null, null, null, null);
    }

    public Cursor query(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        return sDB.query("parameterTb", columns, selection, selectionArgs, null, null, sortOrder);
    }

    public Cursor queryOneData(String name) {
        return sDB.query("parameterTb", null, "name='" + name + "'", null, null, null, null);
    }

    public int deleteData(String name) {
        return sDB.delete("parameterTb", "name='" + name + "'", null);
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        return sDB.delete(table, whereClause, whereArgs);
    }

    public void openDb() {
        if (sDB == null || !sDB.isOpen()) {
            try {
                sDB = getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void close() {
        Log.d(TAG, "close()");
        if (sDB != null && sDB.isOpen()) {
            sDB.close();
        }
    }

    public static boolean isOpen() {
        if (sDB != null) {
            return sDB.isOpen();
        }
        return false;
    }

    private void initDbData(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put("name", "initialized");
        cv.put("value", "0");
        cv.put("deleteable", "0");
        cv.put("updateable", "1");
        cv.put("systemable", "0");
        db.insert("parameterTb", "_id", cv);
    }
}
