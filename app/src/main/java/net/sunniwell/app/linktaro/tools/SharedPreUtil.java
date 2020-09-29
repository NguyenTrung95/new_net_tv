package net.sunniwell.app.linktaro.tools;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences.Editor;

import net.sunniwell.app.linktaro.nettv.bean.CategoryName;
import net.sunniwell.app.linktaro.nettv.bean.UserDetail;
import net.sunniwell.common.log.SWLogger;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SharedPreUtil {
    private static SharedPreUtil mSharedPreUtil;
    private SWLogger LOG = SWLogger.getLogger(SharedPreUtil.class);
    private Context context;

    private SharedPreUtil(Context context2) {
        this.context = context2;
    }

    public static SharedPreUtil getSharedPreUtil(Context context2) {
        if (mSharedPreUtil == null) {
            mSharedPreUtil = new SharedPreUtil(context2);
        }
        return mSharedPreUtil;
    }

    public void setMailFlag(String key, String value) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("mails", Context.MODE_APPEND).edit();
        editor.putString(key, value);
        editor.commit();
    }

    @SuppressLint("WrongConstant")
    public String getMarqueeInformation(String key) {
        return this.context.getSharedPreferences("MarqueeInformation", Context.MODE_APPEND).getString(key, "0");
    }

    public void setMarqueeInformation(String key, String value) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("MarqueeInformation", Context.MODE_APPEND).edit();
        editor.putString(key, value);
        editor.commit();
    }

    @SuppressLint("WrongConstant")
    public String getMailFlag(String key) {
        return this.context.getSharedPreferences("mails", Context.MODE_APPEND).getString(key, "0");
    }

    public void setStbImgs(String key, String value) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("StbImgs", Context.MODE_APPEND).edit();
        editor.putString(key, value);
        editor.commit();
    }

    @SuppressLint("WrongConstant")
    public String getStbImgs(String key) {
        return this.context.getSharedPreferences("StbImgs", Context.MODE_APPEND).getString(key, null);
    }

    public void removeMailFlag(String key) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("mails", Context.MODE_APPEND).edit();
        editor.remove(key);
        editor.commit();
    }

    public void saveCategoryNames(String categoryInx, String categoryNames) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("categorydata", Context.MODE_APPEND).edit();
        editor.putString(categoryInx, categoryNames);
        editor.commit();
    }

    public List<CategoryName> getCategoryName(String categoryInx) {
        List<CategoryName> list = null;
        @SuppressLint("WrongConstant") String categoryNames = this.context.getSharedPreferences("categorydata", Context.MODE_APPEND).getString(categoryInx, XmlPullParser.NO_NAMESPACE);
        if (!StringUtils.isNotEmpty(categoryNames)) {
            return list;
        }
        try {
            return (List) new ObjectMapper().readValue(categoryNames, (TypeReference) new TypeReference<List<CategoryName>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
            return list;
        }
    }

    public void saveStbTypeList(String key, String value) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("StbLiveTypeList", Context.MODE_APPEND).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void removeStbTypeList() {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("StbLiveTypeList", Context.MODE_APPEND).edit();
        editor.clear();
        editor.commit();
    }

    public List<Map<String, Object>> getStbTypeList(String key) {
        List<Map<String, Object>> mArrayList = null;
        try {
            List<Map<String, Object>> mArrayList2 = new ArrayList<>();
            try {
                @SuppressLint("WrongConstant") String ret = this.context.getSharedPreferences("StbLiveTypeList", Context.MODE_APPEND).getString(key, XmlPullParser.NO_NAMESPACE);
                if (StringUtils.isNotEmpty(ret)) {
                    return JsonUtil.getLiveList(ret);
                }
                return mArrayList2;
            } catch (Exception e) {
                e = e;
                mArrayList = mArrayList2;
                e.printStackTrace();
                return mArrayList;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return mArrayList;
        }
    }

    public void saveStbRecoedList(String key, String value) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("StbRecoedList", Context.MODE_APPEND).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public List<Map<String, Object>> getStbRecoedList(String key) {
        List<Map<String, Object>> mArrayList = null;
        try {
            List<Map<String, Object>> mArrayList2 = new ArrayList<>();
            try {
                @SuppressLint("WrongConstant") String ret = this.context.getSharedPreferences("StbRecoedList", Context.MODE_APPEND).getString(key, XmlPullParser.NO_NAMESPACE);
                if (StringUtils.isNotEmpty(ret)) {
                    return JsonUtil.getLiveList(ret);
                }
                return mArrayList2;
            } catch (Exception e) {
                mArrayList = mArrayList2;
                e.printStackTrace();
                return mArrayList;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return mArrayList;
        }
    }

    public void removeStbRecoedList() {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("StbRecoedList", Context.MODE_APPEND).edit();
        editor.clear();
        editor.commit();
    }

    public void saveStbChannelList1(String key, String value) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("StbLiveList1", Context.MODE_APPEND).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void removeStbChannelList1(String key) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("StbLiveList1", Context.MODE_APPEND).edit();
        editor.remove(key);
        editor.commit();
    }

    public List<Map<String, Object>> getStbChannelList1(String key) {
        List<Map<String, Object>> mArrayList = null;
        try {
            List<Map<String, Object>> mArrayList2 = new ArrayList<>();
            try {
                @SuppressLint("WrongConstant") String ret = this.context.getSharedPreferences("StbLiveList1", Context.MODE_APPEND).getString(key, XmlPullParser.NO_NAMESPACE);
                if (StringUtils.isNotEmpty(ret)) {
                    return JsonUtil.getLiveList(ret);
                }
                return mArrayList2;
            } catch (Exception e) {
                mArrayList = mArrayList2;
                e.printStackTrace();
                return mArrayList;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return mArrayList;
        }
    }

    public void removeStbChannelMarkList() {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("LiveChannelMarkList", Context.MODE_APPEND).edit();
        editor.clear();
        editor.commit();
    }

    public void saveStbChannelMarkList(String key, String value) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("LiveChannelMarkList", Context.MODE_APPEND).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public List<String> getStbChannelMarkList(String key) {
        List<String> markList = null;
        try {
            List<String> markList2 = new ArrayList<>();
            try {
                @SuppressLint("WrongConstant") String ret = this.context.getSharedPreferences("LiveChannelMarkList", Context.MODE_APPEND).getString(key, XmlPullParser.NO_NAMESPACE);
                if (StringUtils.isNotEmpty(ret)) {
                    return JsonUtil.getList(ret);
                }
                return markList2;
            } catch (Exception e) {
                e = e;
                markList = markList2;
                e.printStackTrace();
                return markList;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return markList;
        }
    }

    public void removeStbLiveTypeMarkList() {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("LiveTypeMarkList", Context.MODE_APPEND).edit();
        editor.clear();
        editor.commit();
    }

    public void saveStbLiveTypeMarkList(String key, String value) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("LiveTypeMarkList", Context.MODE_APPEND).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public List<String> getStbLiveTypeMarkList(String key) {
        List<String> markList = null;
        try {
            List<String> markList2 = new ArrayList<>();
            try {
                @SuppressLint("WrongConstant") String ret = this.context.getSharedPreferences("LiveTypeMarkList", Context.MODE_APPEND).getString(key, XmlPullParser.NO_NAMESPACE);
                if (StringUtils.isNotEmpty(ret)) {
                    return JsonUtil.getList(ret);
                }
                return markList2;
            } catch (Exception e) {
                e = e;
                markList = markList2;
                e.printStackTrace();
                return markList;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return markList;
        }
    }

    public void saveStbChannelList2(String key, String value) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("StbLiveList2", Context.MODE_APPEND).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public void removeStbChannelList2(String key) {
        @SuppressLint("WrongConstant") Editor editor = this.context.getSharedPreferences("StbLiveList2", Context.MODE_APPEND).edit();
        editor.remove(key);
        editor.commit();
    }

    public List<Map<String, Object>> getStbChannelList2(String key) {
        List<Map<String, Object>> mArrayList = null;
        try {
            List<Map<String, Object>> mArrayList2 = new ArrayList<>();
            try {
                @SuppressLint("WrongConstant") String ret = this.context.getSharedPreferences("StbLiveList2", Context.MODE_APPEND).getString(key, XmlPullParser.NO_NAMESPACE);
                if (StringUtils.isNotEmpty(ret)) {
                    return JsonUtil.getLiveList(ret);
                }
                return mArrayList2;
            } catch (Exception e) {
                e = e;
                mArrayList = mArrayList2;
                e.printStackTrace();
                return mArrayList;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return mArrayList;
        }
    }

    public void saveUserDetail(String key, String userStr) {
        Editor editor = this.context.getSharedPreferences("userdetail", 0).edit();
        editor.putString(key, userStr);
        editor.commit();
    }

    public UserDetail getUserDetail(String key) {
        String userStr = this.context.getSharedPreferences("userdetail", 0).getString(key, XmlPullParser.NO_NAMESPACE);
        this.LOG.mo8825d("userStr----->" + userStr);
        if (StringUtils.isNotEmpty(userStr)) {
            return (UserDetail) JsonUtil.toObject(userStr, UserDetail.class);
        }
        return null;
    }
}
