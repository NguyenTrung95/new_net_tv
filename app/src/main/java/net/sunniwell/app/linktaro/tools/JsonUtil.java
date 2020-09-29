package net.sunniwell.app.linktaro.tools;

import android.util.Log;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonUtil {
    public static String toJson(Object value) {
        String ret = XmlPullParser.NO_NAMESPACE;
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (Exception e) {
            e.printStackTrace();
            return ret;
        }
    }

    public static Map<String, Map<String, String>> getTaskMap(String json) {
        Map<String, Map<String, String>> mTask = null;
        try {
            Map<String, Map<String, String>> mTask2 = new HashMap<>();
            try {
                return (Map) new ObjectMapper().readValue(json, mTask2.getClass());
            } catch (Exception e) {
                e = e;
                mTask = mTask2;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return mTask;
        }
        return mTask;
    }

    public static List<Map<String, String>> getTaskList(String json) {
        List<Map<String, String>> mTaskList = null;
        try {
            List<Map<String, String>> mTaskList2 = new ArrayList<>();
            try {
                return (List) new ObjectMapper().readValue(json, mTaskList2.getClass());
            } catch (Exception e) {
                e = e;
                mTaskList = mTaskList2;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return mTaskList;
        }
        return mTaskList;
    }

    public static List<Map<String, Object>> getLiveList(String json) {
        List<Map<String, Object>> mList = null;
        try {
            List<Map<String, Object>> mList2 = new ArrayList<>();
            try {
                return (List) new ObjectMapper().readValue(json, mList2.getClass());
            } catch (Exception e) {
                e = e;
                mList = mList2;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return mList;
        }
        return mList;
    }

    public static Map<String, String> toMap(String string) {
        if (string == null) {
            return null;
        }
        Map<String, String> map = new HashMap<>();
        try {
            JSONObject jsonObject = new JSONObject(string);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                map.put(key.trim(), jsonObject.getString(key).trim());
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
            return map;
        }
    }

    public static List<Map<String, String>> toList(String string) {
        if (string == null) {
            return null;
        }
        List<Map<String, String>> list = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Map<String, String> map = new HashMap<>();
                Iterator iterator = jsonObject.keys();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    map.put(key.trim(), jsonObject.getString(key).trim());
                }
                list.add(map);
            }
            return list;
        } catch (JSONException e) {
            e.printStackTrace();
            return list;
        }
    }

    public static Object toObject(String string, Class<?> clazz) {
        try {
            Object object = clazz.newInstance();
            Field[] fields = object.getClass().getDeclaredFields();
            JSONObject jsonObject = new JSONObject(string);
            Iterator iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (key.equals("result") && jsonObject.getString(key).trim().equals("0")) {
                    return new String("非法用户");
                }
                for (int i = 0; i < fields.length; i++) {
                    if (fields[i].getName().equalsIgnoreCase(key)) {
                        setJsonValue(object, fields[i].getName(), jsonObject.getString(key).trim());
                    }
                }
            }
            return object;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e2) {
            e2.printStackTrace();
            return null;
        } catch (JSONException e3) {
            e3.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> toBeanList(String jsonString, Class<T> cls, String listOrKey) {
        Map<String, String> map = toMap(jsonString);
        List<T> list = new ArrayList<>();
        for (Map<String, String> next : toList((String) map.get(listOrKey))) {
            Field[] fields = cls.getDeclaredFields();
            try {
                T bean = cls.newInstance();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fields[i].set(bean, next.get(fields[i].getName()));
                }
                list.add(bean);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e2) {
                e2.printStackTrace();
            }
        }
        return list;
    }

    public static String toJsonString(List<Map<String, String>> list) {
        JSONArray mJsonArray = new JSONArray();
        for (int i = 0; i < list.size(); i++) {
            mJsonArray.put(new JSONObject(new HashMap<>()));
        }
        return mJsonArray.toString();
    }

    public static List<String> getList(String json) {
        List<String> list = null;
        try {
            List<String> list2 = new ArrayList<>();
            try {
                JSONArray mJsonArray = new JSONArray(json);
                for (int i = 0; i < mJsonArray.length(); i++) {
                    list2.add(mJsonArray.getString(i));
                }
                return list2;
            } catch (JSONException e) {
                e = e;
                list = list2;
                e.printStackTrace();
                return list;
            }
        } catch (Exception e2) {
            e2.printStackTrace();
            return list;
        }
    }

    public static String toJson(List<String> list) {
        JSONArray mJsonArray = null;
        try {
            JSONArray mJsonArray2 = new JSONArray();
            int i = 0;
            while (i < list.size()) {
                try {
                    mJsonArray2.put(list.get(i));
                    i++;
                } catch (Exception e) {
                    e = e;
                    mJsonArray = mJsonArray2;
                }
            }
            mJsonArray = mJsonArray2;
        } catch (Exception e2) {
            e2.printStackTrace();
            return mJsonArray.toString();
        }
        return mJsonArray.toString();
    }

    public static <T> String toJsonString(List<T> list, Class<T> cls) {
        String ret = XmlPullParser.NO_NAMESPACE;
        JSONObject data = new JSONObject();
        try {
            Object object = cls.newInstance();
            for (T t : list) {
                T bean = t;
                Field[] fields = ((Class) bean).getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    data.put(fields[i].getName(), bean.getClass().getDeclaredField(fields[i].getName()).get(object).toString());
                }
                ret = data.toString();
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e2) {
            e2.printStackTrace();
        } catch (JSONException e3) {
            e3.printStackTrace();
        } catch (NoSuchFieldException e4) {
            e4.printStackTrace();
        }
        return ret;
    }

    private static void setJsonValue(Object t, String name, String value) {
        try {
            Field[] f = t.getClass().getDeclaredFields();
            for (int i = 0; i < f.length; i++) {
                if (f[i].getName().equalsIgnoreCase(name)) {
                    f[i].setAccessible(true);
                    Class<?> fieldType = f[i].getType();
                    if (fieldType == String.class) {
                        f[i].set(t, value);
                    } else if (fieldType == Integer.TYPE) {
                        f[i].set(t, Integer.valueOf(Integer.parseInt(value)));
                    } else if (fieldType == Float.TYPE) {
                        f[i].set(t, Float.valueOf(Float.parseFloat(value)));
                    } else if (fieldType == Double.TYPE) {
                        f[i].set(t, Double.valueOf(Double.parseDouble(value)));
                    } else if (fieldType == Long.TYPE) {
                        f[i].set(t, Long.valueOf(Long.parseLong(value)));
                    } else if (fieldType == Short.TYPE) {
                        f[i].set(t, Short.valueOf(Short.parseShort(value)));
                    } else if (fieldType == Boolean.TYPE) {
                        f[i].set(t, Boolean.valueOf(Boolean.parseBoolean(value)));
                    } else {
                        f[i].set(t, value);
                    }
                }
            }
        } catch (Exception e) {
            Log.e("xml error", e.toString());
        }
    }
}
