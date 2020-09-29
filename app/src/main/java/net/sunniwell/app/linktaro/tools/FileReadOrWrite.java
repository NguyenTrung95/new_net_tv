package net.sunniwell.app.linktaro.tools;

import net.sunniwell.common.log.SWLogger;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileReadOrWrite {
    private static final SWLogger LOG = SWLogger.getLogger(FileReadOrWrite.class);
    private byte[] mBufferSize = new byte[4096];
    private String mCurrentFileName = XmlPullParser.NO_NAMESPACE;
    private String mDir;

    public synchronized String readFilefromPath(String path) throws IOException {
        return readFilefromFile(new File(path));
    }

    /* JADX WARNING: Removed duplicated region for block: B:32:0x0070 A[SYNTHETIC, Splitter:B:32:0x0070] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0075 A[Catch:{ all -> 0x0079 }] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x007f A[SYNTHETIC, Splitter:B:41:0x007f] */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x0084 A[Catch:{ all -> 0x0079 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String readFilefromFile(java.io.File r12) throws java.io.IOException {
        /*
            r11 = this;
            monitor-enter(r11)
            r3 = 0
            r0 = 0
            r6 = 0
            java.lang.StringBuffer r7 = new java.lang.StringBuffer     // Catch:{ Exception -> 0x0094 }
            r7.<init>()     // Catch:{ Exception -> 0x0094 }
            boolean r8 = r12.exists()     // Catch:{ Exception -> 0x0096, all -> 0x0088 }
            if (r8 != 0) goto L_0x0012
            r12.createNewFile()     // Catch:{ Exception -> 0x0096, all -> 0x0088 }
        L_0x0012:
            java.io.FileReader r4 = new java.io.FileReader     // Catch:{ Exception -> 0x0096, all -> 0x0088 }
            r4.<init>(r12)     // Catch:{ Exception -> 0x0096, all -> 0x0088 }
            java.io.BufferedReader r1 = new java.io.BufferedReader     // Catch:{ Exception -> 0x0099, all -> 0x008b }
            r1.<init>(r4)     // Catch:{ Exception -> 0x0099, all -> 0x008b }
            java.lang.String r5 = ""
        L_0x001e:
            java.lang.String r5 = r1.readLine()     // Catch:{ Exception -> 0x004f, all -> 0x008f }
            if (r5 != 0) goto L_0x0038
            if (r1 == 0) goto L_0x0029
            r1.close()     // Catch:{ all -> 0x009d }
        L_0x0029:
            if (r4 == 0) goto L_0x002e
            r4.close()     // Catch:{ all -> 0x009d }
        L_0x002e:
            r6 = r7
            r0 = r1
            r3 = r4
        L_0x0031:
            java.lang.String r8 = new java.lang.String     // Catch:{ all -> 0x0079 }
            r8.<init>(r6)     // Catch:{ all -> 0x0079 }
            monitor-exit(r11)
            return r8
        L_0x0038:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x004f, all -> 0x008f }
            java.lang.String r9 = java.lang.String.valueOf(r5)     // Catch:{ Exception -> 0x004f, all -> 0x008f }
            r8.<init>(r9)     // Catch:{ Exception -> 0x004f, all -> 0x008f }
            java.lang.String r9 = "\n"
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ Exception -> 0x004f, all -> 0x008f }
            java.lang.String r8 = r8.toString()     // Catch:{ Exception -> 0x004f, all -> 0x008f }
            r7.append(r8)     // Catch:{ Exception -> 0x004f, all -> 0x008f }
            goto L_0x001e
        L_0x004f:
            r2 = move-exception
            r6 = r7
            r0 = r1
            r3 = r4
        L_0x0053:
            r2.printStackTrace()     // Catch:{ all -> 0x007c }
            net.sunniwell.common.log.SWLogger r8 = LOG     // Catch:{ all -> 0x007c }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x007c }
            java.lang.String r10 = "....Exception====...."
            r9.<init>(r10)     // Catch:{ all -> 0x007c }
            java.lang.String r10 = r2.toString()     // Catch:{ all -> 0x007c }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ all -> 0x007c }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x007c }
            r8.mo8825d(r9)     // Catch:{ all -> 0x007c }
            if (r0 == 0) goto L_0x0073
            r0.close()     // Catch:{ all -> 0x0079 }
        L_0x0073:
            if (r3 == 0) goto L_0x0031
            r3.close()     // Catch:{ all -> 0x0079 }
            goto L_0x0031
        L_0x0079:
            r8 = move-exception
        L_0x007a:
            monitor-exit(r11)
            throw r8
        L_0x007c:
            r8 = move-exception
        L_0x007d:
            if (r0 == 0) goto L_0x0082
            r0.close()     // Catch:{ all -> 0x0079 }
        L_0x0082:
            if (r3 == 0) goto L_0x0087
            r3.close()     // Catch:{ all -> 0x0079 }
        L_0x0087:
            throw r8     // Catch:{ all -> 0x0079 }
        L_0x0088:
            r8 = move-exception
            r6 = r7
            goto L_0x007d
        L_0x008b:
            r8 = move-exception
            r6 = r7
            r3 = r4
            goto L_0x007d
        L_0x008f:
            r8 = move-exception
            r6 = r7
            r0 = r1
            r3 = r4
            goto L_0x007d
        L_0x0094:
            r2 = move-exception
            goto L_0x0053
        L_0x0096:
            r2 = move-exception
            r6 = r7
            goto L_0x0053
        L_0x0099:
            r2 = move-exception
            r6 = r7
            r3 = r4
            goto L_0x0053
        L_0x009d:
            r8 = move-exception
            r6 = r7
            r0 = r1
            r3 = r4
            goto L_0x007a
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.FileReadOrWrite.readFilefromFile(java.io.File):java.lang.String");
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x005f A[SYNTHETIC, Splitter:B:26:0x005f] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0064 A[Catch:{ IOException -> 0x0068 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x0073 A[SYNTHETIC, Splitter:B:38:0x0073] */
    /* JADX WARNING: Removed duplicated region for block: B:41:0x0078 A[Catch:{ IOException -> 0x007c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean writeFileToDisk(java.lang.String r11, java.lang.String r12) {
        /*
            r10 = this;
            monitor-enter(r10)
            java.io.File r3 = new java.io.File     // Catch:{ all -> 0x006d }
            r3.<init>(r12)     // Catch:{ all -> 0x006d }
            java.io.File r7 = r3.getParentFile()     // Catch:{ all -> 0x006d }
            boolean r7 = r7.exists()     // Catch:{ all -> 0x006d }
            if (r7 != 0) goto L_0x0017
            java.io.File r7 = r3.getParentFile()     // Catch:{ all -> 0x006d }
            r7.mkdirs()     // Catch:{ all -> 0x006d }
        L_0x0017:
            r4 = 0
            r5 = 0
            r0 = 0
            java.io.FileWriter r6 = new java.io.FileWriter     // Catch:{ Exception -> 0x0040 }
            r7 = 0
            r6.<init>(r3, r7)     // Catch:{ Exception -> 0x0040 }
            java.io.BufferedWriter r1 = new java.io.BufferedWriter     // Catch:{ Exception -> 0x008f, all -> 0x0088 }
            r1.<init>(r6)     // Catch:{ Exception -> 0x008f, all -> 0x0088 }
            r1.write(r11)     // Catch:{ Exception -> 0x0092, all -> 0x008b }
            r1.flush()     // Catch:{ Exception -> 0x0092, all -> 0x008b }
            r1.close()     // Catch:{ Exception -> 0x0092, all -> 0x008b }
            r6.close()     // Catch:{ Exception -> 0x0092, all -> 0x008b }
            r4 = 1
            if (r1 == 0) goto L_0x0037
            r1.close()     // Catch:{ IOException -> 0x0081 }
        L_0x0037:
            if (r6 == 0) goto L_0x0085
            r6.close()     // Catch:{ IOException -> 0x0081 }
            r0 = r1
            r5 = r6
        L_0x003e:
            monitor-exit(r10)
            return r4
        L_0x0040:
            r2 = move-exception
        L_0x0041:
            r4 = 0
            r2.printStackTrace()     // Catch:{ all -> 0x0070 }
            net.sunniwell.common.log.SWLogger r7 = LOG     // Catch:{ all -> 0x0070 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0070 }
            java.lang.String r9 = "....Exception====...."
            r8.<init>(r9)     // Catch:{ all -> 0x0070 }
            java.lang.String r9 = r2.toString()     // Catch:{ all -> 0x0070 }
            java.lang.StringBuilder r8 = r8.append(r9)     // Catch:{ all -> 0x0070 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0070 }
            r7.mo8825d(r8)     // Catch:{ all -> 0x0070 }
            if (r0 == 0) goto L_0x0062
            r0.close()     // Catch:{ IOException -> 0x0068 }
        L_0x0062:
            if (r5 == 0) goto L_0x003e
            r5.close()     // Catch:{ IOException -> 0x0068 }
            goto L_0x003e
        L_0x0068:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ all -> 0x006d }
            goto L_0x003e
        L_0x006d:
            r7 = move-exception
            monitor-exit(r10)
            throw r7
        L_0x0070:
            r7 = move-exception
        L_0x0071:
            if (r0 == 0) goto L_0x0076
            r0.close()     // Catch:{ IOException -> 0x007c }
        L_0x0076:
            if (r5 == 0) goto L_0x007b
            r5.close()     // Catch:{ IOException -> 0x007c }
        L_0x007b:
            throw r7     // Catch:{ all -> 0x006d }
        L_0x007c:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ all -> 0x006d }
            goto L_0x007b
        L_0x0081:
            r2 = move-exception
            r2.printStackTrace()     // Catch:{ all -> 0x006d }
        L_0x0085:
            r0 = r1
            r5 = r6
            goto L_0x003e
        L_0x0088:
            r7 = move-exception
            r5 = r6
            goto L_0x0071
        L_0x008b:
            r7 = move-exception
            r0 = r1
            r5 = r6
            goto L_0x0071
        L_0x008f:
            r2 = move-exception
            r5 = r6
            goto L_0x0041
        L_0x0092:
            r2 = move-exception
            r0 = r1
            r5 = r6
            goto L_0x0041
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.FileReadOrWrite.writeFileToDisk(java.lang.String, java.lang.String):boolean");
    }

    public void delteFile(String name) {
        LOG.mo8825d("delteFile------>" + name);
        try {
            File mFile = new File(name);
            if (mFile.exists()) {
                LOG.mo8825d("delete--------->" + mFile.delete());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:18:0x006d A[Catch:{ IOException -> 0x007b }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized boolean open(java.lang.String r12) {
        /*
            r11 = this;
            monitor-enter(r11)
            net.sunniwell.common.log.SWLogger r8 = LOG     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x0080 }
            java.lang.String r10 = "...open()...."
            r9.<init>(r10)     // Catch:{ all -> 0x0080 }
            java.lang.String r10 = r11.mDir     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r9 = r9.append(r12)     // Catch:{ all -> 0x0080 }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x0080 }
            r8.mo8825d(r9)     // Catch:{ all -> 0x0080 }
            java.io.File r4 = new java.io.File     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ all -> 0x0080 }
            java.lang.String r9 = r11.mDir     // Catch:{ all -> 0x0080 }
            java.lang.String r9 = java.lang.String.valueOf(r9)     // Catch:{ all -> 0x0080 }
            r8.<init>(r9)     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r8 = r8.append(r12)     // Catch:{ all -> 0x0080 }
            java.lang.String r8 = r8.toString()     // Catch:{ all -> 0x0080 }
            r4.<init>(r8)     // Catch:{ all -> 0x0080 }
            r7 = 0
            r8 = 4096(0x1000, float:5.74E-42)
            byte[] r8 = new byte[r8]     // Catch:{ all -> 0x0080 }
            r11.mBufferSize = r8     // Catch:{ all -> 0x0080 }
            r5 = 0
            r0 = 0
            java.io.FileInputStream r6 = new java.io.FileInputStream     // Catch:{ FileNotFoundException -> 0x0056, Exception -> 0x0083 }
            r6.<init>(r4)     // Catch:{ FileNotFoundException -> 0x0056, Exception -> 0x0083 }
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch:{ FileNotFoundException -> 0x00a8, Exception -> 0x00a1 }
            r1.<init>(r6)     // Catch:{ FileNotFoundException -> 0x00a8, Exception -> 0x00a1 }
            byte[] r8 = r11.mBufferSize     // Catch:{ FileNotFoundException -> 0x00ab, Exception -> 0x00a4 }
            r1.read(r8)     // Catch:{ FileNotFoundException -> 0x00ab, Exception -> 0x00a4 }
            r1.close()     // Catch:{ FileNotFoundException -> 0x00ab, Exception -> 0x00a4 }
            r6.close()     // Catch:{ FileNotFoundException -> 0x00ab, Exception -> 0x00a4 }
            r7 = 1
            r0 = r1
            r5 = r6
        L_0x0054:
            monitor-exit(r11)
            return r7
        L_0x0056:
            r2 = move-exception
        L_0x0057:
            java.lang.String r8 = "TAG"
            java.lang.String r9 = r2.toString()     // Catch:{ all -> 0x0080 }
            android.util.Log.e(r8, r9)     // Catch:{ all -> 0x0080 }
            r2.printStackTrace()     // Catch:{ all -> 0x0080 }
            java.io.File r8 = r4.getParentFile()     // Catch:{ all -> 0x0080 }
            boolean r8 = r8.exists()     // Catch:{ all -> 0x0080 }
            if (r8 != 0) goto L_0x0074
            java.io.File r8 = r4.getParentFile()     // Catch:{ all -> 0x0080 }
            r8.mkdir()     // Catch:{ all -> 0x0080 }
        L_0x0074:
            boolean r7 = r4.createNewFile()     // Catch:{ IOException -> 0x007b }
            r11.mCurrentFileName = r12     // Catch:{ IOException -> 0x007b }
            goto L_0x0054
        L_0x007b:
            r3 = move-exception
            r3.printStackTrace()     // Catch:{ all -> 0x0080 }
            goto L_0x0054
        L_0x0080:
            r8 = move-exception
            monitor-exit(r11)
            throw r8
        L_0x0083:
            r2 = move-exception
        L_0x0084:
            r7 = 0
            r2.printStackTrace()     // Catch:{ all -> 0x0080 }
            net.sunniwell.common.log.SWLogger r8 = LOG     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r9 = new java.lang.StringBuilder     // Catch:{ all -> 0x0080 }
            java.lang.String r10 = "....Exception====...."
            r9.<init>(r10)     // Catch:{ all -> 0x0080 }
            java.lang.String r10 = r2.toString()     // Catch:{ all -> 0x0080 }
            java.lang.StringBuilder r9 = r9.append(r10)     // Catch:{ all -> 0x0080 }
            java.lang.String r9 = r9.toString()     // Catch:{ all -> 0x0080 }
            r8.mo8825d(r9)     // Catch:{ all -> 0x0080 }
            goto L_0x0054
        L_0x00a1:
            r2 = move-exception
            r5 = r6
            goto L_0x0084
        L_0x00a4:
            r2 = move-exception
            r0 = r1
            r5 = r6
            goto L_0x0084
        L_0x00a8:
            r2 = move-exception
            r5 = r6
            goto L_0x0057
        L_0x00ab:
            r2 = move-exception
            r0 = r1
            r5 = r6
            goto L_0x0057
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.FileReadOrWrite.open(java.lang.String):boolean");
    }

    public String read() {
        return new String(this.mBufferSize).trim();
    }

    public void write(String content) {
        this.mBufferSize = content.getBytes();
    }

    public synchronized String close() {
        String result;
        String str = "-1";
        try {
            if (this.mCurrentFileName == null || XmlPullParser.NO_NAMESPACE.equals(this.mCurrentFileName.trim())) {
                LOG.mo8825d("当前没有打开的文件,无法写入!");
                result = "0";
            } else {
                File file = new File(this.mDir, this.mCurrentFileName);
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdir();
                }
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(this.mBufferSize);
                bos.flush();
                bos.close();
                fos.close();
                result = "1";
                this.mCurrentFileName = XmlPullParser.NO_NAMESPACE;
                this.mBufferSize = new byte[4096];
            }
        } catch (IOException e) {
            LOG.mo8825d(e.toString());
            e.printStackTrace();
            result = "-1";
        }
        return result;
    }

    public synchronized String clear(String fileName) {
        String result;
        File file = new File(this.mDir + fileName);
        String str = "-1";
        this.mBufferSize = new byte[4096];
        try {
            if (this.mCurrentFileName.equals(fileName)) {
                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(this.mBufferSize);
                bos.flush();
                bos.close();
                fos.close();
                result = "1";
            } else {
                result = "0";
            }
        } catch (Exception e) {
            LOG.mo8825d(e.toString());
            e.printStackTrace();
            result = "-1";
        }
        return result;
    }

    public synchronized boolean delete(String fileName) {
        boolean flag;
        try {
            flag = new File(this.mDir + fileName).delete();
        } catch (Exception e) {
            LOG.mo8825d(e.toString());
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    public String getCurrentFileName() {
        return this.mCurrentFileName;
    }
}
