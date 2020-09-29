package net.sunniwell.app.linktaro.tools;

import android.util.Log;
import android.util.Xml;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class XmlParse {
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0019, code lost:
        r2 = r8.next();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List getXmlList(java.io.InputStream r12, java.lang.Class<?> r13, java.lang.String r14) {
        /*
            r11 = this;
            r4 = 0
            org.xmlpull.v1.XmlPullParser r8 = android.util.Xml.newPullParser()
            r7 = 0
            java.lang.String r9 = "UTF-8"
            r8.setInput(r12, r9)     // Catch:{ Exception -> 0x0065 }
            int r2 = r8.getEventType()     // Catch:{ Exception -> 0x0065 }
            r5 = r4
        L_0x0010:
            r9 = 1
            if (r2 != r9) goto L_0x0015
            r4 = r5
        L_0x0014:
            return r4
        L_0x0015:
            switch(r2) {
                case 0: goto L_0x001f;
                case 1: goto L_0x0018;
                case 2: goto L_0x0025;
                case 3: goto L_0x0055;
                default: goto L_0x0018;
            }     // Catch:{ Exception -> 0x0065 }
        L_0x0018:
            r4 = r5
        L_0x0019:
            int r2 = r8.next()     // Catch:{ Exception -> 0x0065 }
            r5 = r4
            goto L_0x0010
        L_0x001f:
            java.util.ArrayList r4 = new java.util.ArrayList     // Catch:{ Exception -> 0x0070 }
            r4.<init>()     // Catch:{ Exception -> 0x0070 }
            goto L_0x0019
        L_0x0025:
            java.lang.String r6 = r8.getName()     // Catch:{ Exception -> 0x0070 }
            boolean r9 = r14.equals(r6)     // Catch:{ Exception -> 0x0070 }
            if (r9 == 0) goto L_0x004a
            java.lang.Object r7 = r13.newInstance()     // Catch:{ Exception -> 0x0070 }
            int r0 = r8.getAttributeCount()     // Catch:{ Exception -> 0x0070 }
            r3 = 0
        L_0x0038:
            if (r3 < r0) goto L_0x003c
            r4 = r5
            goto L_0x0019
        L_0x003c:
            java.lang.String r9 = r8.getAttributeName(r3)     // Catch:{ Exception -> 0x0070 }
            java.lang.String r10 = r8.getAttributeValue(r3)     // Catch:{ Exception -> 0x0070 }
            r11.setXmlValue(r7, r9, r10)     // Catch:{ Exception -> 0x0070 }
            int r3 = r3 + 1
            goto L_0x0038
        L_0x004a:
            if (r7 == 0) goto L_0x0018
            java.lang.String r9 = r8.nextText()     // Catch:{ Exception -> 0x0070 }
            r11.setXmlValue(r7, r6, r9)     // Catch:{ Exception -> 0x0070 }
            r4 = r5
            goto L_0x0019
        L_0x0055:
            java.lang.String r9 = r8.getName()     // Catch:{ Exception -> 0x0070 }
            boolean r9 = r14.equals(r9)     // Catch:{ Exception -> 0x0070 }
            if (r9 == 0) goto L_0x0018
            r5.add(r7)     // Catch:{ Exception -> 0x0070 }
            r7 = 0
            r4 = r5
            goto L_0x0019
        L_0x0065:
            r1 = move-exception
        L_0x0066:
            java.lang.String r9 = "xml pull error"
            java.lang.String r10 = r1.toString()
            android.util.Log.e(r9, r10)
            goto L_0x0014
        L_0x0070:
            r1 = move-exception
            r4 = r5
            goto L_0x0066
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.XmlParse.getXmlList(java.io.InputStream, java.lang.Class, java.lang.String):java.util.List");
    }

    public List getXmlList(String xmlDoc, Class<?> clazz, String startName) {
        return getXmlList(new ByteArrayInputStream(xmlDoc.getBytes()), clazz, startName);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r6 = r14.next();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0029, code lost:
        r11 = r10;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x00d7, code lost:
        r5 = e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0025, code lost:
        r10 = r11;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Object getXmlObject(java.io.InputStream r23, java.lang.Class<?> r24) {
        /*
            r22 = this;
            org.xmlpull.v1.XmlPullParser r14 = android.util.Xml.newPullParser()
            r13 = 0
            r10 = 0
            r17 = 0
            r16 = 0
            java.lang.String r19 = "UTF-8"
            r0 = r23
            r1 = r19
            r14.setInput(r0, r1)     // Catch:{ Exception -> 0x013b }
            int r6 = r14.getEventType()     // Catch:{ Exception -> 0x013b }
            r11 = r10
            r19 = r13
        L_0x001a:
            r20 = 1
            r0 = r20
            if (r6 != r0) goto L_0x0022
            r10 = r11
        L_0x0021:
            return r19
        L_0x0022:
            switch(r6) {
                case 0: goto L_0x002c;
                case 1: goto L_0x0025;
                case 2: goto L_0x0034;
                case 3: goto L_0x011f;
                default: goto L_0x0025;
            }
        L_0x0025:
            r10 = r11
        L_0x0026:
            int r6 = r14.next()     // Catch:{ Exception -> 0x00d7 }
            r11 = r10
            goto L_0x001a
        L_0x002c:
            java.lang.Object r13 = r24.newInstance()     // Catch:{ Exception -> 0x013f }
            r10 = r11
            r19 = r13
            goto L_0x0026
        L_0x0034:
            java.lang.String r12 = r14.getName()     // Catch:{ Exception -> 0x013f }
            r7 = 0
            if (r17 != 0) goto L_0x006a
            java.lang.Class r20 = r19.getClass()     // Catch:{ Exception -> 0x013f }
            java.lang.reflect.Field[] r7 = r20.getDeclaredFields()     // Catch:{ Exception -> 0x013f }
            int r4 = r14.getAttributeCount()     // Catch:{ Exception -> 0x013f }
            r9 = 0
        L_0x0048:
            if (r9 < r4) goto L_0x0054
        L_0x004a:
            r8 = 0
        L_0x004b:
            int r0 = r7.length     // Catch:{ Exception -> 0x013f }
            r20 = r0
            r0 = r20
            if (r8 < r0) goto L_0x0073
            r10 = r11
            goto L_0x0026
        L_0x0054:
            java.lang.String r20 = r14.getAttributeName(r9)     // Catch:{ Exception -> 0x013f }
            java.lang.String r21 = r14.getAttributeValue(r9)     // Catch:{ Exception -> 0x013f }
            r0 = r22
            r1 = r19
            r2 = r20
            r3 = r21
            r0.setXmlValue(r1, r2, r3)     // Catch:{ Exception -> 0x013f }
            int r9 = r9 + 1
            goto L_0x0048
        L_0x006a:
            java.lang.Class r20 = r17.getClass()     // Catch:{ Exception -> 0x013f }
            java.lang.reflect.Field[] r7 = r20.getDeclaredFields()     // Catch:{ Exception -> 0x013f }
            goto L_0x004a
        L_0x0073:
            r20 = r7[r8]     // Catch:{ Exception -> 0x013f }
            java.lang.String r20 = r20.getName()     // Catch:{ Exception -> 0x013f }
            r0 = r20
            boolean r20 = r0.equalsIgnoreCase(r12)     // Catch:{ Exception -> 0x013f }
            if (r20 == 0) goto L_0x011b
            r20 = r7[r8]     // Catch:{ Exception -> 0x013f }
            java.lang.Class r20 = r20.getType()     // Catch:{ Exception -> 0x013f }
            java.lang.String r20 = r20.getName()     // Catch:{ Exception -> 0x013f }
            java.lang.String r21 = "java.util.List"
            boolean r20 = r20.equals(r21)     // Catch:{ Exception -> 0x013f }
            if (r20 == 0) goto L_0x00f9
            r20 = r7[r8]     // Catch:{ Exception -> 0x013f }
            java.lang.reflect.Type r18 = r20.getGenericType()     // Catch:{ Exception -> 0x013f }
            r0 = r18
            boolean r0 = r0 instanceof java.lang.reflect.ParameterizedType     // Catch:{ Exception -> 0x013f }
            r20 = r0
            if (r20 == 0) goto L_0x0025
            java.lang.reflect.ParameterizedType r18 = (java.lang.reflect.ParameterizedType) r18     // Catch:{ Exception -> 0x013f }
            java.lang.reflect.Type[] r20 = r18.getActualTypeArguments()     // Catch:{ Exception -> 0x013f }
            r21 = 0
            r15 = r20[r21]     // Catch:{ Exception -> 0x013f }
            java.lang.Class r15 = (java.lang.Class) r15     // Catch:{ Exception -> 0x013f }
            java.lang.Object r17 = r15.newInstance()     // Catch:{ Exception -> 0x013f }
            r20 = r7[r8]     // Catch:{ Exception -> 0x013f }
            java.lang.String r16 = r20.getName()     // Catch:{ Exception -> 0x013f }
            int r4 = r14.getAttributeCount()     // Catch:{ Exception -> 0x013f }
            r9 = 0
        L_0x00bc:
            if (r9 < r4) goto L_0x00e3
            if (r11 != 0) goto L_0x0025
            java.util.ArrayList r10 = new java.util.ArrayList     // Catch:{ Exception -> 0x013f }
            r10.<init>()     // Catch:{ Exception -> 0x013f }
            r20 = r7[r8]     // Catch:{ Exception -> 0x00d7 }
            r21 = 1
            r20.setAccessible(r21)     // Catch:{ Exception -> 0x00d7 }
            r20 = r7[r8]     // Catch:{ Exception -> 0x00d7 }
            r0 = r20
            r1 = r19
            r0.set(r1, r10)     // Catch:{ Exception -> 0x00d7 }
            goto L_0x0026
        L_0x00d7:
            r5 = move-exception
        L_0x00d8:
            java.lang.String r20 = "xml pull error"
            java.lang.String r21 = r5.getMessage()
            android.util.Log.e(r20, r21)
            goto L_0x0021
        L_0x00e3:
            java.lang.String r20 = r14.getAttributeName(r9)     // Catch:{ Exception -> 0x013f }
            java.lang.String r21 = r14.getAttributeValue(r9)     // Catch:{ Exception -> 0x013f }
            r0 = r22
            r1 = r17
            r2 = r20
            r3 = r21
            r0.setXmlValue(r1, r2, r3)     // Catch:{ Exception -> 0x013f }
            int r9 = r9 + 1
            goto L_0x00bc
        L_0x00f9:
            if (r17 == 0) goto L_0x010b
            java.lang.String r20 = r14.nextText()     // Catch:{ Exception -> 0x013f }
            r0 = r22
            r1 = r17
            r2 = r20
            r0.setXmlValue(r1, r12, r2)     // Catch:{ Exception -> 0x013f }
            r10 = r11
            goto L_0x0026
        L_0x010b:
            java.lang.String r20 = r14.nextText()     // Catch:{ Exception -> 0x013f }
            r0 = r22
            r1 = r19
            r2 = r20
            r0.setXmlValue(r1, r12, r2)     // Catch:{ Exception -> 0x013f }
            r10 = r11
            goto L_0x0026
        L_0x011b:
            int r8 = r8 + 1
            goto L_0x004b
        L_0x011f:
            if (r17 == 0) goto L_0x0025
            java.lang.String r20 = r14.getName()     // Catch:{ Exception -> 0x013f }
            r0 = r16
            r1 = r20
            boolean r20 = r0.equalsIgnoreCase(r1)     // Catch:{ Exception -> 0x013f }
            if (r20 == 0) goto L_0x0025
            r0 = r17
            r11.add(r0)     // Catch:{ Exception -> 0x013f }
            r17 = 0
            r16 = 0
            r10 = r11
            goto L_0x0026
        L_0x013b:
            r5 = move-exception
            r19 = r13
            goto L_0x00d8
        L_0x013f:
            r5 = move-exception
            r10 = r11
            goto L_0x00d8
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.XmlParse.getXmlObject(java.io.InputStream, java.lang.Class):java.lang.Object");
    }

    public Object getXmlObject(String xmlDoc, Class<?> clazz) {
        return getXmlObject(new ByteArrayInputStream(xmlDoc.getBytes()), clazz);
    }

    private void setXmlValue(Object t, String name, String value) {
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

    public String getXmlText(String xmlDoc) {
        String ret = XmlPullParser.NO_NAMESPACE;
        InputStream inputStream = null;
        try {
            inputStream = new ByteArrayInputStream(xmlDoc.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        XmlPullParser parser = Xml.newPullParser();
        try {
            parser.setInput(inputStream, "UTF-8");
            for (int eventType = parser.getEventType(); eventType != 1; eventType = parser.next()) {
                switch (eventType) {
                    case 2:
                        inputStream = parser.getName().equals("txt");
                        if (inputStream) {
                            ret = parser.nextText();
                            break;
                        }
                        break;
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        } catch (XmlPullParserException e3) {
            e3.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
        } catch (IOException e5) {
            e5.printStackTrace();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
            }
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e7) {
                    e7.printStackTrace();
                }
            }
        }
        return ret;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r1 = r6.next();
     */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x006c A[SYNTHETIC, Splitter:B:42:0x006c] */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x0078 A[SYNTHETIC, Splitter:B:48:0x0078] */
    /* JADX WARNING: Removed duplicated region for block: B:69:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:39:0x0067=Splitter:B:39:0x0067, B:31:0x0058=Splitter:B:31:0x0058} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Map<java.lang.String, java.lang.String> getXmlMap(java.lang.String r12, java.lang.String[] r13) {
        /*
            r11 = this;
            r7 = 0
            r3 = 0
            java.io.ByteArrayInputStream r4 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0024 }
            byte[] r10 = r12.getBytes()     // Catch:{ Exception -> 0x0024 }
            r4.<init>(r10)     // Catch:{ Exception -> 0x0024 }
            r3 = r4
        L_0x000c:
            org.xmlpull.v1.XmlPullParser r6 = android.util.Xml.newPullParser()
            java.lang.String r10 = "UTF-8"
            r6.setInput(r3, r10)     // Catch:{ XmlPullParserException -> 0x0057, IOException -> 0x0066 }
            int r1 = r6.getEventType()     // Catch:{ XmlPullParserException -> 0x0057, IOException -> 0x0066 }
            r8 = r7
        L_0x001a:
            r10 = 1
            if (r1 != r10) goto L_0x0029
            if (r3 == 0) goto L_0x0085
            r3.close()     // Catch:{ IOException -> 0x0081 }
            r7 = r8
        L_0x0023:
            return r7
        L_0x0024:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x000c
        L_0x0029:
            switch(r1) {
                case 0: goto L_0x0033;
                case 1: goto L_0x002c;
                case 2: goto L_0x0039;
                default: goto L_0x002c;
            }
        L_0x002c:
            r7 = r8
        L_0x002d:
            int r1 = r6.next()     // Catch:{ XmlPullParserException -> 0x0057, IOException -> 0x0066 }
            r8 = r7
            goto L_0x001a
        L_0x0033:
            java.util.HashMap r7 = new java.util.HashMap     // Catch:{ XmlPullParserException -> 0x008d, IOException -> 0x008a, all -> 0x0087 }
            r7.<init>()     // Catch:{ XmlPullParserException -> 0x008d, IOException -> 0x008a, all -> 0x0087 }
            goto L_0x002d
        L_0x0039:
            java.lang.String r5 = r6.getName()     // Catch:{ XmlPullParserException -> 0x008d, IOException -> 0x008a, all -> 0x0087 }
            r2 = 0
        L_0x003e:
            int r10 = r13.length     // Catch:{ XmlPullParserException -> 0x008d, IOException -> 0x008a, all -> 0x0087 }
            if (r2 < r10) goto L_0x0043
            r7 = r8
            goto L_0x002d
        L_0x0043:
            r10 = r13[r2]     // Catch:{ XmlPullParserException -> 0x008d, IOException -> 0x008a, all -> 0x0087 }
            boolean r10 = r5.equals(r10)     // Catch:{ XmlPullParserException -> 0x008d, IOException -> 0x008a, all -> 0x0087 }
            if (r10 == 0) goto L_0x0054
            java.lang.String r9 = r6.nextText()     // Catch:{ XmlPullParserException -> 0x008d, IOException -> 0x008a, all -> 0x0087 }
            r10 = r13[r2]     // Catch:{ XmlPullParserException -> 0x008d, IOException -> 0x008a, all -> 0x0087 }
            r8.put(r10, r9)     // Catch:{ XmlPullParserException -> 0x008d, IOException -> 0x008a, all -> 0x0087 }
        L_0x0054:
            int r2 = r2 + 1
            goto L_0x003e
        L_0x0057:
            r0 = move-exception
        L_0x0058:
            r0.printStackTrace()     // Catch:{ all -> 0x0075 }
            if (r3 == 0) goto L_0x0023
            r3.close()     // Catch:{ IOException -> 0x0061 }
            goto L_0x0023
        L_0x0061:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0023
        L_0x0066:
            r0 = move-exception
        L_0x0067:
            r0.printStackTrace()     // Catch:{ all -> 0x0075 }
            if (r3 == 0) goto L_0x0023
            r3.close()     // Catch:{ IOException -> 0x0070 }
            goto L_0x0023
        L_0x0070:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x0023
        L_0x0075:
            r10 = move-exception
        L_0x0076:
            if (r3 == 0) goto L_0x007b
            r3.close()     // Catch:{ IOException -> 0x007c }
        L_0x007b:
            throw r10
        L_0x007c:
            r0 = move-exception
            r0.printStackTrace()
            goto L_0x007b
        L_0x0081:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0085:
            r7 = r8
            goto L_0x0023
        L_0x0087:
            r10 = move-exception
            r7 = r8
            goto L_0x0076
        L_0x008a:
            r0 = move-exception
            r7 = r8
            goto L_0x0067
        L_0x008d:
            r0 = move-exception
            r7 = r8
            goto L_0x0058
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.XmlParse.getXmlMap(java.lang.String, java.lang.String[]):java.util.Map");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002f, code lost:
        r10 = r11;
        r6 = r7;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2 = r9.next();
     */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x007a A[SYNTHETIC, Splitter:B:43:0x007a] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0089 A[SYNTHETIC, Splitter:B:51:0x0089] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0095 A[SYNTHETIC, Splitter:B:57:0x0095] */
    /* JADX WARNING: Removed duplicated region for block: B:82:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:84:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:48:0x0084=Splitter:B:48:0x0084, B:40:0x0075=Splitter:B:40:0x0075} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.util.Map<java.lang.String, java.lang.String>> getXmlList(java.lang.String r15, java.lang.String[] r16, java.lang.String r17) {
        /*
            r14 = this;
            r6 = 0
            r10 = 0
            r4 = 0
            java.io.ByteArrayInputStream r5 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0027 }
            byte[] r13 = r15.getBytes()     // Catch:{ Exception -> 0x0027 }
            r5.<init>(r13)     // Catch:{ Exception -> 0x0027 }
            r4 = r5
        L_0x000d:
            org.xmlpull.v1.XmlPullParser r9 = android.util.Xml.newPullParser()
            java.lang.String r13 = "UTF-8"
            r9.setInput(r4, r13)     // Catch:{ XmlPullParserException -> 0x0074, IOException -> 0x0083 }
            int r2 = r9.getEventType()     // Catch:{ XmlPullParserException -> 0x0074, IOException -> 0x0083 }
            r11 = r10
            r7 = r6
        L_0x001c:
            r13 = 1
            if (r2 != r13) goto L_0x002c
            if (r4 == 0) goto L_0x00a2
            r4.close()     // Catch:{ IOException -> 0x009e }
            r10 = r11
            r6 = r7
        L_0x0026:
            return r6
        L_0x0027:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x000d
        L_0x002c:
            switch(r2) {
                case 0: goto L_0x0038;
                case 1: goto L_0x002f;
                case 2: goto L_0x003f;
                case 3: goto L_0x006c;
                default: goto L_0x002f;
            }
        L_0x002f:
            r10 = r11
            r6 = r7
        L_0x0031:
            int r2 = r9.next()     // Catch:{ XmlPullParserException -> 0x0074, IOException -> 0x0083 }
            r11 = r10
            r7 = r6
            goto L_0x001c
        L_0x0038:
            java.util.ArrayList r6 = new java.util.ArrayList     // Catch:{ XmlPullParserException -> 0x00b3, IOException -> 0x00ac, all -> 0x00a5 }
            r6.<init>()     // Catch:{ XmlPullParserException -> 0x00b3, IOException -> 0x00ac, all -> 0x00a5 }
            r10 = r11
            goto L_0x0031
        L_0x003f:
            java.lang.String r8 = r9.getName()     // Catch:{ XmlPullParserException -> 0x00b3, IOException -> 0x00ac, all -> 0x00a5 }
            r0 = r17
            boolean r13 = r8.equals(r0)     // Catch:{ XmlPullParserException -> 0x00b3, IOException -> 0x00ac, all -> 0x00a5 }
            if (r13 == 0) goto L_0x002f
            java.util.HashMap r10 = new java.util.HashMap     // Catch:{ XmlPullParserException -> 0x00b3, IOException -> 0x00ac, all -> 0x00a5 }
            r10.<init>()     // Catch:{ XmlPullParserException -> 0x00b3, IOException -> 0x00ac, all -> 0x00a5 }
            r3 = 0
        L_0x0051:
            r0 = r16
            int r13 = r0.length     // Catch:{ XmlPullParserException -> 0x00b7, IOException -> 0x00b0, all -> 0x00a9 }
            if (r3 < r13) goto L_0x0058
            r6 = r7
            goto L_0x0031
        L_0x0058:
            r13 = r16[r3]     // Catch:{ XmlPullParserException -> 0x00b7, IOException -> 0x00b0, all -> 0x00a9 }
            boolean r13 = r8.equals(r13)     // Catch:{ XmlPullParserException -> 0x00b7, IOException -> 0x00b0, all -> 0x00a9 }
            if (r13 == 0) goto L_0x0069
            java.lang.String r12 = r9.nextText()     // Catch:{ XmlPullParserException -> 0x00b7, IOException -> 0x00b0, all -> 0x00a9 }
            r13 = r16[r3]     // Catch:{ XmlPullParserException -> 0x00b7, IOException -> 0x00b0, all -> 0x00a9 }
            r10.put(r13, r12)     // Catch:{ XmlPullParserException -> 0x00b7, IOException -> 0x00b0, all -> 0x00a9 }
        L_0x0069:
            int r3 = r3 + 1
            goto L_0x0051
        L_0x006c:
            if (r11 == 0) goto L_0x002f
            r7.add(r11)     // Catch:{ XmlPullParserException -> 0x00b3, IOException -> 0x00ac, all -> 0x00a5 }
            r10 = 0
            r6 = r7
            goto L_0x0031
        L_0x0074:
            r1 = move-exception
        L_0x0075:
            r1.printStackTrace()     // Catch:{ all -> 0x0092 }
            if (r4 == 0) goto L_0x0026
            r4.close()     // Catch:{ IOException -> 0x007e }
            goto L_0x0026
        L_0x007e:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0026
        L_0x0083:
            r1 = move-exception
        L_0x0084:
            r1.printStackTrace()     // Catch:{ all -> 0x0092 }
            if (r4 == 0) goto L_0x0026
            r4.close()     // Catch:{ IOException -> 0x008d }
            goto L_0x0026
        L_0x008d:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0026
        L_0x0092:
            r13 = move-exception
        L_0x0093:
            if (r4 == 0) goto L_0x0098
            r4.close()     // Catch:{ IOException -> 0x0099 }
        L_0x0098:
            throw r13
        L_0x0099:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0098
        L_0x009e:
            r1 = move-exception
            r1.printStackTrace()
        L_0x00a2:
            r10 = r11
            r6 = r7
            goto L_0x0026
        L_0x00a5:
            r13 = move-exception
            r10 = r11
            r6 = r7
            goto L_0x0093
        L_0x00a9:
            r13 = move-exception
            r6 = r7
            goto L_0x0093
        L_0x00ac:
            r1 = move-exception
            r10 = r11
            r6 = r7
            goto L_0x0084
        L_0x00b0:
            r1 = move-exception
            r6 = r7
            goto L_0x0084
        L_0x00b3:
            r1 = move-exception
            r10 = r11
            r6 = r7
            goto L_0x0075
        L_0x00b7:
            r1 = move-exception
            r6 = r7
            goto L_0x0075
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.XmlParse.getXmlList(java.lang.String, java.lang.String[], java.lang.String):java.util.List");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2 = r10.next();
     */
    /* JADX WARNING: Removed duplicated region for block: B:52:0x009d A[SYNTHETIC, Splitter:B:52:0x009d] */
    /* JADX WARNING: Removed duplicated region for block: B:58:0x00a9 A[SYNTHETIC, Splitter:B:58:0x00a9] */
    /* JADX WARNING: Removed duplicated region for block: B:80:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:49:0x0098=Splitter:B:49:0x0098, B:41:0x0089=Splitter:B:41:0x0089} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.List<java.util.Map<java.lang.String, java.lang.String>> getXmlList2(java.lang.String r16, java.lang.String[] r17, java.lang.String r18) {
        /*
            r15 = this;
            r6 = 0
            r7 = 0
            r11 = 0
            r4 = 0
            java.io.ByteArrayInputStream r5 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0028 }
            byte[] r14 = r16.getBytes()     // Catch:{ Exception -> 0x0028 }
            r5.<init>(r14)     // Catch:{ Exception -> 0x0028 }
            r4 = r5
        L_0x000e:
            org.xmlpull.v1.XmlPullParser r10 = android.util.Xml.newPullParser()
            java.lang.String r14 = "UTF-8"
            r10.setInput(r4, r14)     // Catch:{ XmlPullParserException -> 0x0088, IOException -> 0x0097 }
            int r2 = r10.getEventType()     // Catch:{ XmlPullParserException -> 0x0088, IOException -> 0x0097 }
            r12 = r11
            r8 = r7
        L_0x001d:
            r14 = 1
            if (r2 != r14) goto L_0x002d
            if (r4 == 0) goto L_0x00b6
            r4.close()     // Catch:{ IOException -> 0x00b2 }
            r11 = r12
            r7 = r8
        L_0x0027:
            return r7
        L_0x0028:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x000e
        L_0x002d:
            switch(r2) {
                case 0: goto L_0x0039;
                case 1: goto L_0x0030;
                case 2: goto L_0x0040;
                case 3: goto L_0x0073;
                default: goto L_0x0030;
            }
        L_0x0030:
            r11 = r12
            r7 = r8
        L_0x0032:
            int r2 = r10.next()     // Catch:{ XmlPullParserException -> 0x0088, IOException -> 0x0097 }
            r12 = r11
            r8 = r7
            goto L_0x001d
        L_0x0039:
            java.util.ArrayList r7 = new java.util.ArrayList     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            r7.<init>()     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            r11 = r12
            goto L_0x0032
        L_0x0040:
            java.lang.String r9 = r10.getName()     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            r0 = r18
            boolean r14 = r9.equals(r0)     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            if (r14 == 0) goto L_0x0054
            java.util.HashMap r11 = new java.util.HashMap     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            r11.<init>()     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            r6 = 1
            r7 = r8
            goto L_0x0032
        L_0x0054:
            if (r6 == 0) goto L_0x0030
            r3 = 0
        L_0x0057:
            r0 = r17
            int r14 = r0.length     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            if (r3 < r14) goto L_0x005f
            r11 = r12
            r7 = r8
            goto L_0x0032
        L_0x005f:
            r14 = r17[r3]     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            boolean r14 = r9.equals(r14)     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            if (r14 == 0) goto L_0x0070
            java.lang.String r13 = r10.nextText()     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            r14 = r17[r3]     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            r12.put(r14, r13)     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
        L_0x0070:
            int r3 = r3 + 1
            goto L_0x0057
        L_0x0073:
            java.lang.String r14 = r10.getName()     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            r0 = r18
            boolean r14 = r14.equals(r0)     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            if (r14 == 0) goto L_0x0030
            if (r12 == 0) goto L_0x00c6
            r8.add(r12)     // Catch:{ XmlPullParserException -> 0x00c2, IOException -> 0x00be, all -> 0x00ba }
            r11 = 0
        L_0x0085:
            r6 = 0
            r7 = r8
            goto L_0x0032
        L_0x0088:
            r1 = move-exception
        L_0x0089:
            r1.printStackTrace()     // Catch:{ all -> 0x00a6 }
            if (r4 == 0) goto L_0x0027
            r4.close()     // Catch:{ IOException -> 0x0092 }
            goto L_0x0027
        L_0x0092:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0027
        L_0x0097:
            r1 = move-exception
        L_0x0098:
            r1.printStackTrace()     // Catch:{ all -> 0x00a6 }
            if (r4 == 0) goto L_0x0027
            r4.close()     // Catch:{ IOException -> 0x00a1 }
            goto L_0x0027
        L_0x00a1:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0027
        L_0x00a6:
            r14 = move-exception
        L_0x00a7:
            if (r4 == 0) goto L_0x00ac
            r4.close()     // Catch:{ IOException -> 0x00ad }
        L_0x00ac:
            throw r14
        L_0x00ad:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x00ac
        L_0x00b2:
            r1 = move-exception
            r1.printStackTrace()
        L_0x00b6:
            r11 = r12
            r7 = r8
            goto L_0x0027
        L_0x00ba:
            r14 = move-exception
            r11 = r12
            r7 = r8
            goto L_0x00a7
        L_0x00be:
            r1 = move-exception
            r11 = r12
            r7 = r8
            goto L_0x0098
        L_0x00c2:
            r1 = move-exception
            r11 = r12
            r7 = r8
            goto L_0x0089
        L_0x00c6:
            r11 = r12
            goto L_0x0085
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.XmlParse.getXmlList2(java.lang.String, java.lang.String[], java.lang.String):java.util.List");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r2 = r7.next();
     */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x006f A[SYNTHETIC, Splitter:B:41:0x006f] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x007b A[SYNTHETIC, Splitter:B:47:0x007b] */
    /* JADX WARNING: Removed duplicated region for block: B:66:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown top exception splitter block from list: {B:38:0x006a=Splitter:B:38:0x006a, B:30:0x005b=Splitter:B:30:0x005b} */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.Map<java.lang.String, java.lang.String> getXmlMap2(java.lang.String r13, java.lang.String r14, java.lang.String[] r15) {
        /*
            r12 = this;
            r8 = 0
            r4 = 0
            java.io.ByteArrayInputStream r5 = new java.io.ByteArrayInputStream     // Catch:{ Exception -> 0x0024 }
            byte[] r10 = r13.getBytes()     // Catch:{ Exception -> 0x0024 }
            r5.<init>(r10)     // Catch:{ Exception -> 0x0024 }
            r4 = r5
        L_0x000c:
            org.xmlpull.v1.XmlPullParser r7 = android.util.Xml.newPullParser()
            java.lang.String r10 = "UTF-8"
            r7.setInput(r4, r10)     // Catch:{ XmlPullParserException -> 0x005a, IOException -> 0x0069 }
            int r2 = r7.getEventType()     // Catch:{ XmlPullParserException -> 0x005a, IOException -> 0x0069 }
            r9 = r8
        L_0x001a:
            r10 = 1
            if (r2 != r10) goto L_0x0029
            if (r4 == 0) goto L_0x0088
            r4.close()     // Catch:{ IOException -> 0x0084 }
            r8 = r9
        L_0x0023:
            return r8
        L_0x0024:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x000c
        L_0x0029:
            switch(r2) {
                case 0: goto L_0x0033;
                case 1: goto L_0x002c;
                case 2: goto L_0x0039;
                default: goto L_0x002c;
            }
        L_0x002c:
            r8 = r9
        L_0x002d:
            int r2 = r7.next()     // Catch:{ XmlPullParserException -> 0x005a, IOException -> 0x0069 }
            r9 = r8
            goto L_0x001a
        L_0x0033:
            java.util.HashMap r8 = new java.util.HashMap     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008d, all -> 0x008a }
            r8.<init>()     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008d, all -> 0x008a }
            goto L_0x002d
        L_0x0039:
            java.lang.String r6 = r7.getName()     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008d, all -> 0x008a }
            boolean r10 = r6.equals(r14)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008d, all -> 0x008a }
            if (r10 == 0) goto L_0x002c
            int r0 = r7.getAttributeCount()     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008d, all -> 0x008a }
            r3 = 0
        L_0x0048:
            if (r3 < r0) goto L_0x004c
            r8 = r9
            goto L_0x002d
        L_0x004c:
            java.lang.String r10 = r7.getAttributeName(r3)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008d, all -> 0x008a }
            java.lang.String r11 = r7.getAttributeValue(r3)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008d, all -> 0x008a }
            r9.put(r10, r11)     // Catch:{ XmlPullParserException -> 0x0090, IOException -> 0x008d, all -> 0x008a }
            int r3 = r3 + 1
            goto L_0x0048
        L_0x005a:
            r1 = move-exception
        L_0x005b:
            r1.printStackTrace()     // Catch:{ all -> 0x0078 }
            if (r4 == 0) goto L_0x0023
            r4.close()     // Catch:{ IOException -> 0x0064 }
            goto L_0x0023
        L_0x0064:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0023
        L_0x0069:
            r1 = move-exception
        L_0x006a:
            r1.printStackTrace()     // Catch:{ all -> 0x0078 }
            if (r4 == 0) goto L_0x0023
            r4.close()     // Catch:{ IOException -> 0x0073 }
            goto L_0x0023
        L_0x0073:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x0023
        L_0x0078:
            r10 = move-exception
        L_0x0079:
            if (r4 == 0) goto L_0x007e
            r4.close()     // Catch:{ IOException -> 0x007f }
        L_0x007e:
            throw r10
        L_0x007f:
            r1 = move-exception
            r1.printStackTrace()
            goto L_0x007e
        L_0x0084:
            r1 = move-exception
            r1.printStackTrace()
        L_0x0088:
            r8 = r9
            goto L_0x0023
        L_0x008a:
            r10 = move-exception
            r8 = r9
            goto L_0x0079
        L_0x008d:
            r1 = move-exception
            r8 = r9
            goto L_0x006a
        L_0x0090:
            r1 = move-exception
            r8 = r9
            goto L_0x005b
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.XmlParse.getXmlMap2(java.lang.String, java.lang.String, java.lang.String[]):java.util.Map");
    }
}
