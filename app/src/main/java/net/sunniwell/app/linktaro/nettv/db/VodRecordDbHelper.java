package net.sunniwell.app.linktaro.nettv.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import net.sunniwell.common.log.SWLogger;

/* renamed from: net.sunniwell.app.linktaro.nettv.db.VodRecordDbHelper */
public class VodRecordDbHelper extends SQLiteOpenHelper {
    public static final String CURRENT_PLAYTIME = "current_playtime";
    public static final int DATABASE_VERSION = 1;

    /* renamed from: ID */
    public static final String f332ID = "_id";
    private static final SWLogger LOG = SWLogger.getLogger(VodRecordDbHelper.class);
    public static final String MEDIA_DURATION = "media_duration";
    public static final String PROGRAM_DATE = "program_date";
    public static final String PROGRAM_MASK = "program_mask";
    public static final String TABLE_NAME = "VodRecord";
    private static SQLiteDatabase sDB;
    private static String sDatabaseName = "VodRecord.db";

    public VodRecordDbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, sDatabaseName, factory, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        LOG.mo8825d("onCreate--->");
        db.execSQL("CREATE TABLE IF NOT EXISTS VodRecord (_id INTEGER PRIMARY KEY , program_mask VARCHAR , program_date VARCHAR , current_playtime VARCHAR , media_duration VARCHAR) ");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        LOG.mo8825d("onUpgrade--->");
        db.execSQL("DROP TABLE IF EXISTS VodRecord");
        onCreate(db);
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
        if (sDB != null && sDB.isOpen()) {
            sDB.close();
        }
    }

    public static synchronized boolean isOpen() {
        boolean isOpened;
        synchronized (VodRecordDbHelper.class) {
            isOpened = false;
            if (sDB != null) {
                isOpened = sDB.isOpen();
            }
        }
        return isOpened;
    }

    public synchronized long insertDataByContentValue(ContentValues cv) {
        LOG.mo8825d("insertDataByContentValue---->");
        return sDB.insert(TABLE_NAME, "_id", cv);
    }

    public synchronized int updateData(ContentValues cv, String tid) {
        int id;
        LOG.mo8825d("updateData---->");
        id = -1;
        try {
            id = sDB.update(TABLE_NAME, cv, "_id='" + tid + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public synchronized Cursor queryData() {
        return sDB.query(TABLE_NAME, null, null, null, null, null, null);
    }

    public synchronized Cursor queryOneData(String mask) {
        return sDB.query(TABLE_NAME, null, "program_mask='" + mask + "'", null, null, null, null);
    }

    public synchronized Cursor query(String[] columns, String selection, String[] selectionArgs, String sortOrder) {
        return sDB.query(TABLE_NAME, columns, selection, selectionArgs, null, null, sortOrder);
    }

    public synchronized int deleteOneDataByMask(String mask) {
        LOG.mo8825d("deleteOneData---->mask=" + mask);
        return sDB.delete(TABLE_NAME, "program_mask='" + mask + "'", null);
    }

    public synchronized int deleteOneData(String tid) {
        LOG.mo8825d("deleteOneData----tid=" + tid);
        return sDB.delete(TABLE_NAME, "_id='" + tid + "'", null);
    }

    public synchronized int deleteData(String whereClause, String[] whereArgs) {
        LOG.mo8825d("deleteData---->");
        return sDB.delete(TABLE_NAME, whereClause, whereArgs);
    }
}
