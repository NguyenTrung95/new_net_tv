package net.sunniwell.app.linktaro.nettv.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.util.HashMap;
import net.sunniwell.app.linktaro.nettv.db.DbHelper;
import net.sunniwell.app.linktaro.nettv.provider.ConstantsParameter.TableParameter;
import org.xmlpull.p019v1.XmlPullParser;

public class ParameterContentProvider extends ContentProvider {
    private static final int DATA_ID = 2;
    private static final int DATA_LIST = 1;
    private static final String TAG = ParameterContentProvider.class.getSimpleName();
    private static HashMap<String, String> sParamsHashMap = new HashMap<>();
    private static final UriMatcher uriMatcher = new UriMatcher(-1);
    private DbHelper mDbHelper;

    static {
        Log.d(TAG, "uriMatcher start");
        uriMatcher.addURI(ConstantsParameter.AUTHORITY, TableParameter.TABLE_NAME, 1);
        uriMatcher.addURI(ConstantsParameter.AUTHORITY, "parameterTb/#", 2);
        sParamsHashMap.put("_id", "_id");
        sParamsHashMap.put("name", "name");
        sParamsHashMap.put(TableParameter.FEED_VALUE, TableParameter.FEED_VALUE);
        sParamsHashMap.put(TableParameter.FEED_DELETEABLE, TableParameter.FEED_DELETEABLE);
        sParamsHashMap.put(TableParameter.FEED_UPDATEABLE, TableParameter.FEED_UPDATEABLE);
        sParamsHashMap.put(TableParameter.FEED_SYSTEMABLE, TableParameter.FEED_SYSTEMABLE);
    }

    public boolean onCreate() {
        this.mDbHelper = new DbHelper(getContext(), null, null, 1);
        if (!DbHelper.isOpen()) {
            this.mDbHelper.openDb();
        }
        return true;
    }

    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case 1:
                return TableParameter.CONTENT_TYPE;
            case 2:
                return TableParameter.CONTENT_TYPE_ITEM;
            default:
                new IllegalAccessException("Unknown URI...");
                return null;
        }
    }

    public Uri insert(Uri uri, ContentValues values) {
        try {
            long rowid = this.mDbHelper.insertDataByContentValue(values);
            if (rowid <= 0) {
                return null;
            }
            Uri suri = ContentUris.withAppendedId(uri, rowid);
            getContext().getContentResolver().notifyChange(suri, null);
            return suri;
        } catch (Exception e) {
            return null;
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = this.mDbHelper.update(TableParameter.TABLE_NAME, values, selection, selectionArgs);
                break;
            case 2:
                String where = "_id=" + ContentUris.parseId(uri);
                if (selection != null && !XmlPullParser.NO_NAMESPACE.equals(selection)) {
                    where = new StringBuilder(String.valueOf(where)).append(" and ").append(selection).toString();
                }
                count = this.mDbHelper.update(TableParameter.TABLE_NAME, values, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Uri IllegalArgument:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        String orderBy;
        Cursor c;
        if (TextUtils.isEmpty(sortOrder)) {
            orderBy = TableParameter.DEFAULT_SORT_ORDER;
        } else {
            orderBy = sortOrder;
        }
        switch (uriMatcher.match(uri)) {
            case 1:
                c = this.mDbHelper.query(projection, selection, selectionArgs, orderBy);
                break;
            case 2:
                String where = "_id=" + ContentUris.parseId(uri);
                if (selection != null && !XmlPullParser.NO_NAMESPACE.equals(selection)) {
                    where = new StringBuilder(String.valueOf(where)).append(" and ").append(selection).toString();
                }
                c = this.mDbHelper.query(projection, where, selectionArgs, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Uri IllegalArgument:" + uri);
        }
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count;
        switch (uriMatcher.match(uri)) {
            case 1:
                count = this.mDbHelper.delete(TableParameter.TABLE_NAME, selection, selectionArgs);
                break;
            case 2:
                String where = "_id=" + ContentUris.parseId(uri);
                if (selection != null && !XmlPullParser.NO_NAMESPACE.equals(selection)) {
                    where = new StringBuilder(String.valueOf(where)).append(" and ").append(selection).toString();
                }
                count = this.mDbHelper.delete(TableParameter.TABLE_NAME, where, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Uri IllegalArgument:" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
