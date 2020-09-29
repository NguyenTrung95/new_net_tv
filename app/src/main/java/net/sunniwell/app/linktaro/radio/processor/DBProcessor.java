package net.sunniwell.app.linktaro.radio.processor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import net.sunniwell.app.linktaro.radio.bean.Parameter;
import net.sunniwell.common.log.SWLogger;

import org.codehaus.jackson.map.ObjectMapper;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;

public class DBProcessor implements PropProcessor {
    private static final String AUTHORITY = "pmContentProvider";
    private static final Uri CONTENT_URI = Uri.parse("content://pmContentProvider/parameterTb");
    private static final String FEED_DELETEABLE = "deleteable";
    private static final String FEED_NAME = "name";
    private static final String FEED_SYSTEMABLE = "systemable";
    private static final String FEED_UPDATEABLE = "updateable";
    private static final String FEED_VALUE = "value";
    private static final SWLogger LOG = SWLogger.getLogger(DBProcessor.class);
    private Context mContext;

    public DBProcessor(Context ctx) {
        this.mContext = ctx;
    }

    public boolean isSupport(String prop) {
        return true;
    }

    public String getProp(String name) {
        String ret = XmlPullParser.NO_NAMESPACE;
        Cursor c = null;
        try {
            String where = "name='" + name + "'";
            c = this.mContext.getContentResolver().query(CONTENT_URI, new String[]{"value"}, where, null, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();
                if (c.getString(0) != null) {
                    ret = c.getString(0);
                }
            }
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            LOG.mo8825d("get " + name + " exception :" + e2.getMessage());
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e3) {
                }
            }
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e4) {
                }
            }
        }
        return ret;
    }

    public String setProp(String name, String value) {
        String ret = "0";
        if (value == null) {
            value = XmlPullParser.NO_NAMESPACE;
        }
        Cursor c = null;
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("value", value);
        try {
            String where = "name='" + name + "'";
            c = this.mContext.getContentResolver().query(CONTENT_URI, new String[]{"updateable"}, where, null, null);
            if (c == null || c.getCount() <= 0) {
                values.put("deleteable", "1");
                values.put("updateable", "1");
                values.put("systemable", "0");
                this.mContext.getContentResolver().insert(CONTENT_URI, values);
                LOG.mo8825d(" insert " + name);
            } else {
                c.moveToFirst();
                if (c.getString(0).equals("1")) {
                    this.mContext.getContentResolver().update(CONTENT_URI, values, where, null);
                    LOG.mo8825d(" update " + name);
                } else {
                    LOG.mo8825d(" can't update " + name);
                }
            }
            ret = "1";
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            LOG.mo8825d("====set " + name + " exception==" + e2.getMessage());
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e3) {
                }
            }
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e4) {
                }
            }
        }
        return ret;
    }

    public String setProp(String name, int value) {
        return setProp(name, new StringBuilder(String.valueOf(value)).toString());
    }

    /* access modifiers changed from: protected */
    public void insertParameter(Parameter parameter) {
        try {
            ContentValues values = new ContentValues();
            values.put("name", parameter.getName());
            values.put("value", parameter.getValue());
            values.put("deleteable", parameter.getDeletable());
            values.put("updateable", parameter.getUpdateable());
            values.put("systemable", parameter.getSystemable());
            this.mContext.getContentResolver().insert(CONTENT_URI, values);
            LOG.mo8825d("insertParameter:" + parameter.getName() + "=" + parameter.getValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void updateParameter(Parameter parameter) {
        try {
            ContentValues values = new ContentValues();
            values.put("name", parameter.getName());
            values.put("value", parameter.getValue());
            values.put("deleteable", parameter.getDeletable());
            values.put("updateable", parameter.getUpdateable());
            values.put("systemable", parameter.getSystemable());
            this.mContext.getContentResolver().update(CONTENT_URI, values, "name='" + parameter.getName() + "'", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public String deleteParameterByName(String name) {
        LOG.mo8825d("delete " + name);
        String ret = "0";
        if (name.equals("ntpserver")) {
            name = "NTPDomain";
        } else if (name.equals("ntpserver2")) {
            name = "NTPDomainBackup";
        }
        String where = "name='" + name + "'";
        try {
            if (canDelete(name)) {
                this.mContext.getContentResolver().delete(CONTENT_URI, where, null);
                return "1";
            }
            LOG.mo8825d("the parameter:" + name + " cann't delete");
            return ret;
        } catch (Exception e) {
            LOG.mo8825d("deleteParameterByName " + e.getMessage());
            return ret;
        }
    }

    /* access modifiers changed from: protected */
    public boolean canDelete(String name) {
        boolean isDeleteable = false;
        String where = "name='" + name + "'";
        Cursor c = this.mContext.getContentResolver().query(CONTENT_URI, new String[]{"deleteable"}, where, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            if (c.getString(0).equals("1")) {
                isDeleteable = true;
            }
            c.close();
        }
        return isDeleteable;
    }

    /* access modifiers changed from: protected */
    public String getAll() {
        String ret = XmlPullParser.NO_NAMESPACE;
        List<Parameter> parameters = new ArrayList<>();
        Cursor c = this.mContext.getContentResolver().query(CONTENT_URI, null, null, null, null);
        if (c != null && c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                Parameter parameter = new Parameter();
                parameter.setName(c.getString(1));
                parameter.setValue(c.getString(2));
                parameter.setDeletable(c.getString(3));
                parameter.setUpdateable(c.getString(4));
                parameter.setSystemable(c.getString(5));
                parameters.add(parameter);
                c.moveToNext();
            }
            c.close();
        }
        try {
            ret = new ObjectMapper().writeValueAsString(parameters);
            LOG.mo8825d("All parameters :" + ret);
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e3) {
                }
            }
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e4) {
                }
            }
        }
        return ret;
    }
}
