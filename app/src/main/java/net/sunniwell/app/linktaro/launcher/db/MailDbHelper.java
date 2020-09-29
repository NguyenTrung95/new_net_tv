package net.sunniwell.app.linktaro.launcher.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import net.sunniwell.app.linktaro.tools.LogcatUtils;

/* renamed from: net.sunniwell.app.linktaro.launcher.db.MailDbHelper */
public class MailDbHelper extends SQLiteOpenHelper {
    public static final String CONTENT = "content";
    public static final int DATABASE_VERSION = 1;
    public static final String DATE = "date";
    public static final String DATE_DETAILS = "date_details";
    public static final String FLAG = "flag";

    /* renamed from: ID */
    public static final String f325ID = "_id";
    public static final String NAME = "name";
    public static final String SUB_NAME = "subname";
    private static final String TABLE_NAME = "Mail";
    public static final String TYPE = "type";
    private static SQLiteDatabase sDB;
    private static String sDatabaseName = "MailData.db";
    private OnMailChangeListner onMailChangeListner;

    /* renamed from: net.sunniwell.app.linktaro.launcher.db.MailDbHelper$OnMailChangeListner */
    public interface OnMailChangeListner {
        void deleteOneData();

        void insertOneData();

        void updateOneData();
    }

    public MailDbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, sDatabaseName, factory, 1);
        LogcatUtils.m321d("........DbHelper.......");
    }

    public void onCreate(SQLiteDatabase db) {
        LogcatUtils.m321d("onCreate()");
        db.execSQL("CREATE TABLE IF NOT EXISTS Mail (_id INTEGER PRIMARY KEY , name VARCHAR , date_details VARCHAR , flag VARCHAR , type VARCHAR , date VARCHAR , subname VARCHAR , content VARCHAR ) ");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LogcatUtils.m321d("onUpgrade()");
        db.execSQL("DROP TABLE IF EXISTS Mail");
        onCreate(db);
    }

    public void onOpen(SQLiteDatabase mDB) {
        super.onOpen(mDB);
    }

    public synchronized long insertDataByContentValue(ContentValues cv) {
        long id;
        LogcatUtils.m321d("insertDataByContentValue---->");
        if (isAlreadyInsert(cv)) {
            id = -1;
        } else {
            long id2 = sDB.insert(TABLE_NAME, "_id", cv);
            if (this.onMailChangeListner != null) {
                this.onMailChangeListner.insertOneData();
            }
            id = id2;
        }
        return id;
    }

    public synchronized boolean isAlreadyInsert(ContentValues cv) {
        boolean z;
        String id = null;
        String name = null;
        String info = null;
        String title = cv.getAsString("name");
        String content = cv.getAsString(CONTENT);
        Cursor cursor = queryOneData(title);
        if (cursor.moveToNext()) {
            id = cursor.getString(cursor.getColumnIndex("_id"));
            name = cursor.getString(cursor.getColumnIndex("name"));
            info = cursor.getString(cursor.getColumnIndex(CONTENT));
        }
        LogcatUtils.m321d("id is " + id + " name is " + name + " info is " + info);
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(name) || TextUtils.isEmpty(info) || !TextUtils.equals(name, title) || !TextUtils.equals(info, content)) {
            z = false;
        } else {
            LogcatUtils.m321d("start update info id is " + id);
            updateData(cv, id);
            z = true;
        }
        return z;
    }

    public int updateData(ContentValues cv, String tid) {
        int id = -1;
        try {
            id = sDB.update(TABLE_NAME, cv, "_id='" + tid + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.onMailChangeListner != null) {
            this.onMailChangeListner.updateOneData();
        }
        return id;
    }

    public int update(ContentValues values, String whereClause, String[] whereArgs) {
        int count = -1;
        try {
            return sDB.update(TABLE_NAME, values, whereClause, whereArgs);
        } catch (Exception e) {
            e.printStackTrace();
            return count;
        }
    }

    public synchronized Cursor queryData() {
        return sDB.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public synchronized Cursor query(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        return sDB.query(TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
    }

    public synchronized Cursor queryOneData(String name) {
        return sDB.query(TABLE_NAME, null, "name='" + name + "'", null, null, null, null);
    }

    public synchronized int deleteData(String tid) {
        int id;
        id = sDB.delete(TABLE_NAME, "_id='" + tid + "'", null);
        if (this.onMailChangeListner != null) {
            this.onMailChangeListner.deleteOneData();
        }
        return id;
    }

    public synchronized int deleteDataByName(String name) {
        int id;
        id = sDB.delete(TABLE_NAME, "name='" + name + "'", null);
        if (this.onMailChangeListner != null) {
            this.onMailChangeListner.deleteOneData();
        }
        return id;
    }

    public synchronized int delete(String table, String whereClause, String[] whereArgs) {
        return sDB.delete(table, whereClause, whereArgs);
    }

    public synchronized void openDb() {
        if (sDB == null || !sDB.isOpen()) {
            try {
                sDB = getWritableDatabase();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public synchronized void close() {
        LogcatUtils.m321d("close()");
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

    public OnMailChangeListner getOnMailChangeListner() {
        return this.onMailChangeListner;
    }

    public void setOnMailChangeListner(OnMailChangeListner onMailChangeListner2) {
        this.onMailChangeListner = onMailChangeListner2;
    }
}
