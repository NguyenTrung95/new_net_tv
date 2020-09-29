package net.sunniwell.app.linktaro.tools;

import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class UtilFile {
    private static final String TAG = "UtilFile";
    private static final long TIMEOUT = 21600;

    public static String getCacheDir() {
        if (Environment.getExternalStorageState().equals("mounted")) {
            File sdDir = Environment.getExternalStorageDirectory();
            try {
                File dirTest = new File(sdDir.toString() + "/sunniwell/tmp");
                if (dirTest.exists()) {
                    return sdDir.toString() + "/sunniwell/tmp";
                }
                Runtime.getRuntime().exec("chmod 777 " + sdDir.toString());
                dirTest.mkdirs();
                if (dirTest.exists()) {
                    Runtime.getRuntime().exec("chmod 777 " + sdDir.toString() + "/sunniwell");
                    Runtime.getRuntime().exec("chmod 777 " + sdDir.toString() + "/sunniwell/tmp");
                    return sdDir.toString() + "/sunniwell/tmp";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String tmp = "/data/sunniwell";
        try {
            File dirTest2 = new File(tmp);
            if (dirTest2.exists()) {
                return tmp;
            }
            Runtime.getRuntime().exec("chmod 777 " + tmp);
            dirTest2.mkdirs();
            if (dirTest2.exists()) {
                return tmp;
            }
            return "/tmp";
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        return "";
    }

    public static boolean makeDirExist(String dirName) {
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return true;
    }

    public static String readCacheFile(String fileName) {
        String dirName = getCacheDir();
        File file = new File(dirName, fileName);
        String ret = XmlPullParser.NO_NAMESPACE;
        if (!file.exists() || file.length() == 0) {
            Log.d(TAG, "readCacheFile(" + dirName + "  " + fileName + ")" + " file not exists...length = " + file.length() + " exists = " + file.exists() + " can Read = " + file.canRead());
            return null;
        }
        try {
            FileInputStream inputStream = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            while (true) {
                String tmp = br.readLine();
                if (tmp == null) {
                    break;
                }
                ret = new StringBuilder(String.valueOf(ret)).append(tmp).toString();
            }
            inputStream.close();
            if (!ret.equals(XmlPullParser.NO_NAMESPACE)) {
                return ret;
            }
            Log.e(TAG, "getCache error fileName =" + dirName + fileName);
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readFile(String dirName, String fileName) {
        File file = new File(dirName, fileName);
        String ret = XmlPullParser.NO_NAMESPACE;
        Log.d(TAG, "readFile(" + fileName + ")");
        if (!file.exists() || file.length() == 0) {
            return null;
        }
        try {
            byte[] temp = new byte[8192];
            FileInputStream in = new FileInputStream(file);
            while (true) {
                int byte_read = in.read(temp);
                if (byte_read == -1) {
                    break;
                }
                ret = new StringBuilder(String.valueOf(ret)).append(new String(temp, 0, byte_read)).toString();
            }
            in.close();
            if (ret.equals(XmlPullParser.NO_NAMESPACE)) {
                Log.e(TAG, "readFile error fileName =" + dirName + fileName);
                return null;
            }
            Log.d(TAG, "readFile  dirName=" + dirName + " fileName =" + fileName + " ret = " + ret);
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean saveCacheFile(String fileName, String content) {
        if (content == null) {
            return false;
        }
        Log.d(TAG, "saveCacheFile(" + fileName + ")");
        String dirName = getCacheDir();
        File dir = new File(dirName);
        if (!dir.exists()) {
            dir.mkdirs();
            try {
                Runtime.getRuntime().exec("chmod 777 " + dir.toString());
            } catch (IOException e) {
            }
        }
        File file = new File(dir, fileName);
        if (file.exists()) {
            file.delete();
        }
        if (file != null) {
            try {
                FileOutputStream output = new FileOutputStream(file);
                OutputStreamWriter outputStream = new OutputStreamWriter(output, "utf-8");
                outputStream.write(content);
                outputStream.close();
                output.flush();
                output.close();
                Runtime.getRuntime().exec("chmod 777 " + file.toString());
            } catch (FileNotFoundException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        Log.d(TAG, "saveCacheFile ok....file.length = " + file.length() + " file = " + dirName + fileName + " readable = " + file.canRead());
        return true;
    }

    public static void deleteCacheFile(String fileName) {
        File file = new File(getCacheDir(), fileName);
        if (file != null) {
            try {
                file.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean deleteCacheDir() {
        try {
            File dir = new File(getCacheDir());
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    files[i].delete();
                }
            }
            dir.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getCacheFileError:" + e.getMessage());
            return false;
        }
    }

    public static File getCacheFile(String fileName) {
        try {
            return new File(getCacheDir(), fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int[] getTandR(StatFs sf) {
        int availableBlocks = sf.getAvailableBlocks();
        int blockCount = sf.getBlockCount();
        int size = sf.getBlockSize();
        return new int[]{Integer.parseInt(new BigInteger(new StringBuilder(String.valueOf(blockCount)).toString()).multiply(new BigInteger(new StringBuilder(String.valueOf(size)).toString())).divide(new BigInteger("1048576")).toString(10)), Integer.parseInt(new BigInteger(new StringBuilder(String.valueOf(availableBlocks)).toString()).multiply(new BigInteger(new StringBuilder(String.valueOf(size)).toString())).divide(new BigInteger("1048576")).toString(10))};
    }

    public static boolean isFileTimeOut(String fileName) {
        if (fileName == null || fileName.equals(XmlPullParser.NO_NAMESPACE)) {
            return true;
        }
        File file = new File(getCacheDir(), fileName);
        if (!file.exists()) {
            return true;
        }
        new Date(file.lastModified());
        long timeGap = System.currentTimeMillis() - file.lastModified();
        new Date(System.currentTimeMillis());
        if (timeGap / 1000 <= TIMEOUT) {
            return false;
        }
        file.delete();
        return true;
    }

    public static ArrayList<File> getDirFile(String dirStr, String filter) {
        ArrayList<File> list = new ArrayList<>();
        File dir = new File(dirStr);
        if (!dir.isDirectory() || !dir.exists()) {
            return null;
        }
        File[] flist = dir.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (filter == null || filter.equals(XmlPullParser.NO_NAMESPACE)) {
                list.add(flist[i]);
            } else if (flist[i].getName().endsWith(filter)) {
                list.add(flist[i]);
            }
        }
        return list;
    }

    public static int dirFreeSize(String cacheDir) {
        try {
            return getTandR(new StatFs(new File(cacheDir).getPath()))[1];
        } catch (Exception e) {
            return 0;
        }
    }

    public static int removeOldFile(String cacheDir) {
        int cnt = 0;
        try {
            File dir = new File(cacheDir);
            if (!dir.exists()) {
                return 0;
            }
            int[] sizeArr = getTandR(new StatFs(dir.getPath()));
            int totalSize = sizeArr[0];
            int emptySize = sizeArr[1];
            int totalFileCount = 0;
            double dirSize = 0.0d;
            File[] flist = dir.listFiles();
            ArrayList<File> list = new ArrayList<>();
            if (flist == null) {
                return 0;
            }
            for (int i = 0; i < flist.length; i++) {
                if (!flist[i].isDirectory()) {
                    dirSize += (double) flist[i].length();
                    list.add(flist[i]);
                    totalFileCount++;
                }
            }
            double dirSize2 = (dirSize / 1024.0d) / 1024.0d;
            Log.d(TAG, "BitmapManager total=" + totalSize + "M  empty=" + emptySize + "M" + " dirSize = " + dirSize2 + " M" + " fileCnt " + totalFileCount);
            Collections.sort(list, new Comparator<File>() {
                public int compare(File arg0, File arg1) {
                    if (arg0.lastModified() - arg1.lastModified() > 0) {
                        return 1;
                    }
                    if (arg0.lastModified() - arg1.lastModified() < 0) {
                        return -1;
                    }
                    return 0;
                }
            });
            while (true) {
                if ((dirSize2 <= ((double) ((totalSize * 2) / 3)) && emptySize >= 100 && totalFileCount <= 10000) || list.size() <= 0) {
                    break;
                }
                File file = (File) list.get(0);
                double length = (double) file.length();
                Log.d(TAG, "delete file(" + file.getName() + ")");
                dirSize2 -= (length / 1024.0d) / 1024.0d;
                emptySize = (int) (((double) emptySize) + ((length / 1024.0d) / 1024.0d));
                ((File) list.get(0)).delete();
                list.remove(0);
                cnt++;
                totalFileCount--;
                Thread.sleep(100);
            }
            return cnt;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cnt;
    }
}
