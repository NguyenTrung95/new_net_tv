package net.sunniwell.app.linktaro.nettv.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class ConstantsParameter {
    public static final String AUTHORITY = "pmContentProvider";
    public static final String DATABASE_NAME = "parameterDb";
    public static final int DATABASE_VERSION = 1;

    public static final class TableParameter implements BaseColumns {
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.constants.parameter";
        public static final String CONTENT_TYPE_ITEM = "vnd.android.cursor.item/vnd.constants.parameter";
        public static final Uri CONTENT_URI = Uri.parse("content://pmContentProvider/parameterTb");
        public static final String DEFAULT_SORT_ORDER = "name DESC";
        public static final String FEED_DELETEABLE = "deleteable";
        public static final String FEED_NAME = "name";
        public static final String FEED_SYSTEMABLE = "systemable";
        public static final String FEED_UPDATEABLE = "updateable";
        public static final String FEED_VALUE = "value";
        public static final String TABLE_NAME = "parameterTb";
    }
}
