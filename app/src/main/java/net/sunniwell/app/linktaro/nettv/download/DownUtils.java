package net.sunniwell.app.linktaro.nettv.download;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.StatFs;
import android.os.SystemProperties;
import android.text.format.Formatter;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.sunniwell.aidl.SDKRemoteManager;
import net.sunniwell.app.linktaro.nettv.Constants.PageLiveConstants;
import net.sunniwell.app.linktaro.nettv.content.PageDownLoad;
import net.sunniwell.app.linktaro.nettv.download.SwDownFile.DownFileCallBackListener;
import net.sunniwell.app.linktaro.nettv.entry.NettvActivity;
import net.sunniwell.app.linktaro.nettv.processor.imp.DBProcessor;
import net.sunniwell.app.linktaro.tools.DateTimeUtil;
import net.sunniwell.app.linktaro.tools.FileReadOrWrite;
import net.sunniwell.app.linktaro.tools.JsonUtil;
import net.sunniwell.app.linktaro.tools.NetworkInfoUtil;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.app.linktaro.tools.SortList;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.common.tools.DateTime;
import net.sunniwell.sz.mop4.sdk.log.LogBean1;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.p019v1.XmlPullParser;

public class DownUtils {
    private static final int CMD_CHECK_LIVE_TASK = 0;
    private static final int CMD_LOOP_CHECK_LIVE_TASK = 1;
    private static final int CMD_LOOP_CHECK_USB_SPACE = 2;
    private static final long CONFIG_SIZE = 5242880;
    public static final String DOWNLOAD_PATH = "/P2P_DOWNLOAD/";
    public static final String DOWN_FLAG = "downFlag";
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(DownUtils.class);
    private static final int LOOP_CHECK_LIVE_TASK_TIME = 180000;
    private static final int LOOP_CHECK_USB_TASK_TIME = 5000;
    /* access modifiers changed from: private */
    public static DownUtils mDownUtils;
    private static FileReadOrWrite mReadOrWrite;
    /* access modifiers changed from: private */
    public static Map<String, DownloadTask> mTaskInfo;
    public static String sUsbPath;
    private final int DOWN_TASK_MAX = 20;
    private final String MEDIA_STATE_ACTION = "net.sunniwell.action.MEDIA_STATE_CHANGE";
    public boolean isUsbAvailable = true;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DownUtils.LOG.mo8825d("action------>" + action);
            if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
                DownUtils.LOG.mo8825d("ACTION_MEDIA_MOUNTED----------->");
                String mountPath = intent.getData().getPath();
                DownUtils.LOG.mo8825d("mountPath ---->" + mountPath);
                if (mountPath.contains("/mnt") && !mountPath.contains("sdcard")) {
                    SWSysProp.setStringParam("mount_path", mountPath);
                    DownUtils.sUsbPath = mountPath;
                    DownUtils.this.checkLivetask();
                }
            } else if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
                DownUtils.LOG.mo8825d("ACTION_MEDIA_UNMOUNTED---------------->");
                DownUtils.LOG.mo8825d("...mountPath...===" + SWSysProp.getStringParam("mount_path", "/storage/sda1"));
                DownUtils.LOG.mo8825d("USB------->UNMOUNTED");
                String key = DownUtils.this.getDownLoadingKey();
                DownUtils.LOG.mo8825d("====key==" + key);
                if (StringUtils.isNotEmpty(key)) {
                    DownloadTask downLoadingTask = (DownloadTask) DownUtils.mTaskInfo.get(key);
                    DownUtils.LOG.mo8825d("downLoadingFile------>" + downLoadingTask);
                    if (downLoadingTask != null) {
                        DownUtils.LOG.mo8825d("downLoadingFile------>" + downLoadingTask.getChannelName() + "----------------->" + downLoadingTask.getProgramName());
                        downLoadingTask.setStatus(0);
                        DownUtils.this.updateDownFile(downLoadingTask);
                    }
                }
                if (DownUtils.this.mSwDownFile != null) {
                    DownUtils.this.mSwDownFile.setDownFileCallBackListener(null);
                    DownUtils.this.mSwDownFile.stop();
                    DownUtils.this.mSwDownFile.finish();
                    DownUtils.this.mSwDownFile = null;
                    DownUtils.this.mDownLoadPercent = 0.0f;
                    DownUtils.this.mDownLoadSpeed = "0KB/S";
                    if (DownUtils.this.mMainApp != null) {
                        PageDownLoad pageDownLoad = DownUtils.this.mMainApp.getPageDownLoad();
                        DownUtils.LOG.mo8825d("----pageDownLoad=" + pageDownLoad);
                        boolean isAtDownPage = DownUtils.this.mMainApp.isAtDownPage;
                        DownUtils.LOG.mo8825d("isAtDownPage----->" + isAtDownPage);
                        if (pageDownLoad != null && isAtDownPage) {
                            DownUtils.this.mMainApp.goBackPage();
                            PageDownLoad.refreshData();
                            pageDownLoad.onDestroy();
                            return;
                        }
                        return;
                    }
                    return;
                }
                DownUtils.LOG.mo8825d("mSwDownFile  is null------>");
                if (DownUtils.this.mMainApp != null) {
                    PageDownLoad pageDownLoad2 = DownUtils.this.mMainApp.getPageDownLoad();
                    DownUtils.LOG.mo8825d("----pageDownLoad=" + pageDownLoad2);
                    boolean isAtDownPage2 = DownUtils.this.mMainApp.isAtDownPage;
                    DownUtils.LOG.mo8825d("isAtDownPage----->" + isAtDownPage2);
                    if (pageDownLoad2 != null && isAtDownPage2) {
                        DownUtils.mTaskInfo.clear();
                        DownUtils.this.mMainApp.goBackPage();
                        PageDownLoad.refreshData();
                        pageDownLoad2.onDestroy();
                    }
                }
            } else if ("android.net.ethernet.ETHERNET_STATE_CHANGE".equals(action)) {
                int state = intent.getIntExtra("ethernet_state", 11);
                DownUtils.LOG.mo8825d("==state ==" + state);
                if (state == 10) {
                    DownUtils.this.mWorkThreadHandle.removeMessages(0);
                    DownUtils.this.mWorkThreadHandle.sendMessage(DownUtils.this.mWorkThreadHandle.obtainMessage(0));
                }
            } else if ("android.net.wifi.STATE_CHANGE".equals(action)) {
                int state2 = intent.getIntExtra("wifi_state", 4);
                DownUtils.LOG.mo8825d("==state ==" + state2);
                if (state2 == 3) {
                    DownUtils.this.mWorkThreadHandle.removeMessages(0);
                    DownUtils.this.mWorkThreadHandle.sendMessage(DownUtils.this.mWorkThreadHandle.obtainMessage(0));
                }
            } else if ("android.net.pppoe.PPPOE_STATE_CHANGE".equals(action)) {
                int state3 = intent.getIntExtra("pppoe_state", 0);
                DownUtils.LOG.mo8825d("==state ===" + state3);
                if (state3 == 1) {
                    DownUtils.this.mWorkThreadHandle.removeMessages(0);
                    DownUtils.this.mWorkThreadHandle.sendMessage(DownUtils.this.mWorkThreadHandle.obtainMessage(0));
                }
            } else if ("net.sunniwell.action.MEDIA_STATE_CHANGE".equals(action)) {
                String newState = intent.getStringExtra("newState");
                DownUtils.LOG.mo8825d("-----MEDIA_STATE_ACTION----newState=" + newState);
                if ("prepared".equals(newState)) {
                    DownUtils.LOG.mo8825d("----------getDownLoadStatus=2");
                    DownUtils.mDownUtils.pauseDownTask();
                } else if (LogBean1.TERMINAL_ACTION_STOP.equals(newState) && DownUtils.mDownUtils.getDownLoadStatus() == 2) {
                    DownUtils.mDownUtils.resumeDownTask();
                }
            }
        }
    };
    /* access modifiers changed from: private */
    public Context mContext;
    private DBProcessor mDProcessor;
    private long mDownErrorTime = 0;
    private int mDownFlag;
    /* access modifiers changed from: private */
    public float mDownLoadPercent;
    /* access modifiers changed from: private */
    public String mDownLoadSpeed;
    /* access modifiers changed from: private */
    public NettvActivity mMainApp;
    /* access modifiers changed from: private */
    public String mMountPath;
    private SDKRemoteManager mSdkRemoteManager;
    /* access modifiers changed from: private */
    public SwDownFile mSwDownFile;
    private String mTerminalid;
    /* access modifiers changed from: private */
    public WorkThreadHandle mWorkThreadHandle;

    @SuppressLint({"HandlerLeak"})
    private class WorkThreadHandle extends Handler {
        @SuppressLint({"HandlerLeak"})
        public WorkThreadHandle(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    DownUtils.this.checkLivetask();
                    return;
                case 1:
                    DownUtils.LOG.mo8825d("*******CMD_LOOP_CHECK_LIVE_TASK********");
                    DownUtils.LOG.mo8825d("isUsbAvailable----->" + DownUtils.this.isUsbAvailable);
                    if (DownUtils.this.isUsbAvailable) {
                        DownUtils.this.checkLivetask();
                    }
                    DownUtils.this.mWorkThreadHandle.sendMessageDelayed(DownUtils.this.mWorkThreadHandle.obtainMessage(1), 180000);
                    return;
                case 2:
                    if (DownUtils.this.mSwDownFile != null) {
                        int downLoadStatus = DownUtils.this.mSwDownFile.getDownLoadStatus();
                        DownUtils.LOG.mo8825d("downLoadStatus--->" + downLoadStatus);
                        DownUtils.LOG.mo8825d("SwDownFile.IS_PAUSED-------->" + (DownUtils.this.getDownLoadStatus() == 2));
                        File f = new File(DownUtils.this.mMountPath);
                        if (f.exists() && f.list().length > 0) {
                            StatFs stat = new StatFs(f.getPath());
                            long blockSize = (long) stat.getBlockSize();
                            long totalBlocks = (long) stat.getBlockCount();
                            long availableBlocks = (long) stat.getAvailableBlocks();
                            String usedSize = Formatter.formatFileSize(DownUtils.this.mContext, (totalBlocks - availableBlocks) * blockSize);
                            String availableSize = Formatter.formatFileSize(DownUtils.this.mContext, availableBlocks * blockSize);
                            DownUtils.LOG.mo8825d("blockSize----->" + blockSize + "----totalBlocks--->" + totalBlocks + "------availableBlocks------->" + availableBlocks);
                            DownUtils.LOG.mo8825d("usedSize------->" + usedSize + "-----availableSize---->" + availableSize);
                            if (availableBlocks * blockSize < DownUtils.CONFIG_SIZE) {
                                DownUtils.LOG.mo8825d("lack of space, stop download!!!");
                                DownUtils.this.isUsbAvailable = false;
                                if (downLoadStatus == 1 && DownUtils.this.getDownLoadStatus() != 2) {
                                    DownUtils.this.pauseDownload();
                                    DownUtils.this.showUSBLackOfSpace();
                                }
                            } else {
                                DownUtils.this.isUsbAvailable = true;
                            }
                        }
                    }
                    DownUtils.this.mWorkThreadHandle.sendMessageDelayed(DownUtils.this.mWorkThreadHandle.obtainMessage(2), PageLiveConstants.LIST_STAY_TIME);
                    return;
                default:
                    return;
            }
        }
    }

    private DownUtils(Context context) {
        this.mContext = context;
        SWSysProp.init(this.mContext);
        this.mDProcessor = DBProcessor.getDBProcessor(this.mContext);
        sUsbPath = SWSysProp.getStringParam("mount_path", "/storage/sda1");
        mReadOrWrite = new FileReadOrWrite();
        this.mSdkRemoteManager = SDKRemoteManager.getInstance(this.mContext, null);
        if (this.mContext instanceof NettvActivity) {
            this.mMainApp = (NettvActivity) this.mContext;
        }
        HandlerThread mLoadThread = new HandlerThread("LoadThread");
        mLoadThread.start();
        this.mWorkThreadHandle = new WorkThreadHandle(mLoadThread.getLooper());
        this.mMountPath = SWSysProp.getStringParam("mount_path", "/storage/sda1");
        this.mWorkThreadHandle.sendMessage(this.mWorkThreadHandle.obtainMessage(1));
        this.mWorkThreadHandle.sendMessage(this.mWorkThreadHandle.obtainMessage(2));
        File file = new File(sUsbPath + DOWNLOAD_PATH);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
    }

    public NettvActivity getmMainApp() {
        return this.mMainApp;
    }

    public void setmMainApp(NettvActivity mMainApp2) {
        this.mMainApp = mMainApp2;
    }

    public static DownUtils getDownUtilsInstance(Context context) {
        if (mDownUtils == null) {
            mDownUtils = new DownUtils(context);
        }
        return mDownUtils;
    }

    public void registReceiver() {
        LOG.mo8825d("[registerReceiver]");
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addAction("android.net.wifi.STATE_CHANGE");
        filter.addAction("android.net.ethernet.ETHERNET_STATE_CHANGE");
        filter.addAction("android.net.pppoe.PPPOE_STATE_CHANGE");
        filter.addDataScheme(LogBean1.TERMINAL_EXCEPTION_FILE);
        IntentFilter mediaFilter = new IntentFilter();
        mediaFilter.addAction("net.sunniwell.action.MEDIA_STATE_CHANGE");
        this.mContext.registerReceiver(this.mBroadcastReceiver, filter);
        this.mContext.registerReceiver(this.mBroadcastReceiver, mediaFilter);
    }

    public void unregisterReceiver() {
        LOG.mo8825d("[unregisterReceiver]");
        if (this.mBroadcastReceiver != null) {
            this.mContext.unregisterReceiver(this.mBroadcastReceiver);
        }
    }

    public boolean isContainsKey(String key) {
        try {
            if (!usbChecked()) {
                return false;
            }
            if (mTaskInfo == null || mTaskInfo.size() <= 0 || isAddTask() || isUpdateTask()) {
                LOG.mo8825d("isContainsKey----->isDownLoad");
                mTaskInfo = getStbTaskInfo();
            }
            return mTaskInfo.containsKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
            return false;
        }
    }

    private void initDownload() {
        LOG.mo8825d("initDownload------>");
        String key = getDownLoadingKey();
        if (StringUtils.isNotEmpty(key)) {
            DownloadTask downLoadingFile = (DownloadTask) mTaskInfo.get(key);
            LOG.mo8825d("downLoadingFile----ChannelName-->" + downLoadingFile.getChannelName() + "----------ProgramName------->" + downLoadingFile.getProgramName() + "---------status------->" + downLoadingFile.getStatus());
        }
        this.mSwDownFile = new SwDownFile(this.mContext, new DownFileCallBackListener() {
            public void onDownFileComplete() {
                DownUtils.LOG.mo8825d("********onDownFileComplete*********");
                String key = DownUtils.this.getDownLoadingKey();
                if (StringUtils.isNotEmpty(key)) {
                    DownloadTask downLoadingTask = (DownloadTask) DownUtils.mTaskInfo.get(key);
                    DownUtils.LOG.mo8825d("downLoadingFile----ChannelName-->" + downLoadingTask.getChannelName() + "----------ProgramName------->" + downLoadingTask.getProgramName() + "---------status------->" + downLoadingTask.getStatus());
                    downLoadingTask.setStatus(2);
                    DownUtils.this.updateDownFile(downLoadingTask);
                }
                DownUtils.this.mSwDownFile.setDownFileCallBackListener(null);
                DownUtils.this.mSwDownFile.finish();
                DownUtils.this.mSwDownFile = null;
                DownUtils.this.mDownLoadPercent = 0.0f;
                DownUtils.this.mDownLoadSpeed = "0KB/S";
                DownUtils.this.mWorkThreadHandle.removeMessages(0);
                DownUtils.this.mWorkThreadHandle.sendMessage(DownUtils.this.mWorkThreadHandle.obtainMessage(0));
                if (DownUtils.this.mMainApp != null) {
                    PageDownLoad pageDownLoad = DownUtils.this.mMainApp.getPageDownLoad();
                    boolean isAtDownPage = DownUtils.this.mMainApp.isAtDownPage;
                    DownUtils.LOG.mo8825d("isAtDownPage----->" + isAtDownPage);
                    if (pageDownLoad != null && isAtDownPage) {
                        DownUtils.this.mMainApp.callBackUpdateDownPage();
                    }
                }
            }

            public void onDownFileProgressUpdate(int downLoadPercent) {
            }

            public void onDownFileSpeedUpdate(int mSpeed) {
            }

            public void onDownFileErrorCallBack(int errorCode) {
                DownUtils.LOG.mo8825d("********onDownFileErrorCallBack*********-------errorCode------->" + errorCode);
                String key = DownUtils.this.getDownLoadingKey();
                if (StringUtils.isNotEmpty(key)) {
                    DownloadTask downLoadingTask = (DownloadTask) DownUtils.mTaskInfo.get(key);
                    DownUtils.LOG.mo8825d("downLoadingFile------>" + downLoadingTask.getChannelName() + "----------------->" + downLoadingTask.getProgramName());
                    downLoadingTask.setStatus(-1);
                    DownUtils.this.updateDownFile(downLoadingTask);
                }
                DownUtils.this.mSwDownFile.setDownFileCallBackListener(null);
                DownUtils.this.mSwDownFile.stop();
                DownUtils.this.mSwDownFile.finish();
                DownUtils.this.mSwDownFile = null;
                DownUtils.this.mDownLoadPercent = 0.0f;
                DownUtils.this.mDownLoadSpeed = "0KB/S";
                DownUtils.this.mWorkThreadHandle.removeMessages(0);
                DownUtils.this.mWorkThreadHandle.sendMessage(DownUtils.this.mWorkThreadHandle.obtainMessage(0));
                if (DownUtils.this.mMainApp != null) {
                    PageDownLoad pageDownLoad = DownUtils.this.mMainApp.getPageDownLoad();
                    boolean isAtDownPage = DownUtils.this.mMainApp.isAtDownPage;
                    DownUtils.LOG.mo8825d("isAtDownPage----->" + isAtDownPage);
                    if (pageDownLoad != null && isAtDownPage) {
                        DownUtils.this.mMainApp.callBackUpdateDownPage();
                    }
                }
            }
        });
    }

    private boolean isAddTask() {
        try {
            String isAddTask = this.mDProcessor.getProp("isAddTask");
            if (!StringUtils.isNotEmpty(isAddTask) || !isAddTask.equals("1")) {
                return false;
            }
            this.mDProcessor.setProp("isAddTask", "0");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
            return false;
        }
    }

    private boolean isUpdateTask() {
        boolean flag = false;
        try {
            String isUpdateTask = this.mDProcessor.getProp("isUpdateTask");
            if (StringUtils.isNotEmpty(isUpdateTask) && isUpdateTask.equals("1")) {
                flag = true;
                this.mDProcessor.setProp("isUpdateTask", "0");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
        LOG.mo8825d("isUpdateTask----->" + flag);
        return flag;
    }

    public boolean isHasTask() {
        boolean flag = false;
        try {
            ArrayList arrayList = new ArrayList();
            try {
                ArrayList arrayList2 = arrayList;
                for (Entry<String, List<DownloadTask>> downFile : getStbTaskList().entrySet()) {
                    if (((List) downFile.getValue()).size() - getDownListByFlag(2).size() > 0) {
                        flag = true;
                    }
                }
            } catch (Exception e) {
                e = e;
                ArrayList arrayList3 = arrayList;
                e.printStackTrace();
                LOG.mo8825d("...Exception...===" + e.toString());
                LOG.mo8825d("...isHasTask...." + flag);
                return flag;
            }
        } catch (Exception e2) {
            e = e2;
        }
        LOG.mo8825d("...isHasTask...." + flag);
        return flag;
    }

    public boolean isDownTaskMAX() {
        LOG.mo8825d("...isDownTaskMAX...");
        boolean flag = false;
        try {
            List<DownloadTask> arrayList = new ArrayList<>();
            try {
                Map<String, List<DownloadTask>> stbTaskMap = getStbTaskList();
                int allListSize = 0;
                if (stbTaskMap != null && stbTaskMap.size() > 0) {
                    for (Entry<String, List<DownloadTask>> downFile : stbTaskMap.entrySet()) {
                        if (((String) downFile.getKey()).equals("全部")) {
                            allListSize = ((List) downFile.getValue()).size();
                        }
                    }
                    List<String> mKeList = getDownListByFlag(2);
                    LOG.mo8825d("mKeList------>" + mKeList.size());
                    LOG.mo8825d("allListSize--->" + allListSize);
                    if (allListSize - mKeList.size() >= 20) {
                        flag = true;
                        ArrayList arrayList2 = arrayList;
                        LOG.mo8825d("----flag=" + flag);
                        return flag;
                    }
                }
                List<DownloadTask> mDowList = arrayList;
            } catch (Exception e) {
                e = e;
                ArrayList arrayList3 = arrayList;
                e.printStackTrace();
                LOG.mo8825d("...Exception...===" + e.toString());
                LOG.mo8825d("----flag=" + flag);
                return flag;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
            LOG.mo8825d("----flag=" + flag);
            return flag;
        }
        LOG.mo8825d("----flag=" + flag);
        return flag;
    }

    public boolean isDownLoad(int status) {
        boolean flag = false;
        try {
            if (mTaskInfo == null || mTaskInfo.size() <= 0 || isAddTask() || isUpdateTask()) {
                LOG.mo8825d("mTaskInfo----->isDownLoad");
                mTaskInfo = getStbTaskInfo();
            }
            for (Entry<String, DownloadTask> entry : mTaskInfo.entrySet()) {
                LOG.mo8825d("mTaskInfo------key--->" + ((String) entry.getKey()) + "---------value------>" + entry.getValue());
            }
            Iterator it = mTaskInfo.keySet().iterator();
            while (true) {
                if (it.hasNext()) {
                    String key = (String) it.next();
                    if (StringUtils.isNotEmpty(key) && ((DownloadTask) mTaskInfo.get(key)).getStatus() == status) {
                        flag = true;
                        break;
                    }
                } else {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
        LOG.mo8825d("...isDownLoad..." + status + "flag----->" + flag);
        return flag;
    }

    public boolean addDownTask(DownloadTask downloadTask) {
        long dateimeMillions;
        LOG.mo8825d("...addDownTask...");
        String taskName = downloadTask.getTaskName();
        if (!usbChecked() || isContainsKey(taskName)) {
            return false;
        }
        String fileType = downloadTask.getType();
        LOG.mo8825d("...downfileType==.." + fileType);
        LOG.mo8825d("...isDownLoad(DOWNLOAD_STATUS_DOWNLOADING)==.." + isDownLoad(1));
        if (isDownLoad(1)) {
            this.mDownFlag = 0;
            downloadTask.setStatus(this.mDownFlag);
            addTaskInfo(taskName, downloadTask);
            return true;
        }
        if (fileType.equals(DownloadTask.TYPE_EPG)) {
            this.mDownFlag = 0;
            downloadTask.setStatus(this.mDownFlag);
            addTaskInfo(taskName, downloadTask);
            this.mWorkThreadHandle.removeMessages(0);
            this.mWorkThreadHandle.sendMessage(this.mWorkThreadHandle.obtainMessage(0));
        } else if (this.isUsbAvailable) {
            this.mDownFlag = 1;
            downloadTask.setStatus(this.mDownFlag);
            addTaskInfo(taskName, downloadTask);
            LOG.mo8825d("mSwDownFile------>" + this.mSwDownFile);
            String date = downloadTask.getDate();
            String startTime1 = downloadTask.getStartTime();
            String time = downloadTask.getEndTime();
            String startDate = new StringBuilder(String.valueOf(date)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime1).toString();
            String endDate = new StringBuilder(String.valueOf(date)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time).toString();
            long startLong = StringUtils.getTimeMillions(startDate);
            long endLong = StringUtils.getTimeMillions(endDate);
            String str = new StringBuilder(String.valueOf(date)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time).toString();
            LOG.mo8825d("startLong----->" + startLong + "----endLong---->" + endLong);
            if (startLong - endLong > 0) {
                dateimeMillions = StringUtils.getTimeMillions(str) + DateTime.MILLIS_PER_DAY;
            } else {
                dateimeMillions = StringUtils.getTimeMillions(str);
            }
            LOG.mo8825d("startTime1---> " + startTime1 + "-----EndTime----->" + time + "----date--->" + date);
            String startTime = String.valueOf(startLong / 1000);
            String endTime = String.valueOf(dateimeMillions / 1000);
            initDownload();
            String fileVodUrl = downloadTask.getVodUrl();
            LOG.mo8825d("downFile----->" + downloadTask.getChannelMask());
            if (fileVodUrl.startsWith("mop://")) {
                fileVodUrl = mopUrl2RealUrl(fileVodUrl, downloadTask.getChannelMask());
            }
            downloadTask.setSavePath(sUsbPath + DOWNLOAD_PATH);
            this.mSwDownFile.startDownLoad(downloadTask.getTaskName(), fileVodUrl, startTime, endTime, downloadTask.getSavePath() + downloadTask.getLocalFile());
        } else {
            showUSBLackOfSpace();
        }
        return true;
    }

    public void addTaskInfo(String key, DownloadTask downloadTask) {
        LOG.mo8825d("...addTaskInfo...");
        try {
            if (usbChecked()) {
                if (mTaskInfo == null || mTaskInfo.size() <= 0 || isAddTask() || isUpdateTask()) {
                    LOG.mo8825d("addTaskInfo----->isDownLoad");
                    mTaskInfo = getStbTaskInfo();
                }
                mTaskInfo.put(key, downloadTask);
                String json = JsonUtil.toJson((Object) downloadTask);
                if (StringUtils.isNotEmpty(json)) {
                    mReadOrWrite.writeFileToDisk(json, sUsbPath + DOWNLOAD_PATH + downloadTask.getTaskName() + ".info");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
    }

    public void removeTaskInfo(String key) {
        LOG.mo8825d("...removeTaskInfo...");
        try {
            if (StringUtils.isNotEmpty(key)) {
                String key2 = getDownLoadingKey();
                if (mTaskInfo == null || mTaskInfo.size() <= 0 || isAddTask() || isUpdateTask()) {
                    LOG.mo8825d("removeTaskInfo----->isDownLoad");
                    mTaskInfo = getStbTaskInfo();
                }
                deleteFile(sUsbPath + DOWNLOAD_PATH + key + ".info");
                deleteFile(sUsbPath + DOWNLOAD_PATH + key + ".ts");
                mTaskInfo.remove(key);
                if (key.equals(key2)) {
                    if (this.mSwDownFile != null) {
                        this.mSwDownFile.stop();
                        this.mSwDownFile.setDownFileCallBackListener(null);
                        this.mSwDownFile.finish();
                        this.mSwDownFile = null;
                    }
                    if (isDownLoad(0)) {
                        List<String> mKeyList = getDownListByFlag(0);
                        if (mKeyList != null && mKeyList.size() > 0) {
                            DownloadTask downFile = getTaskInfoFromKey((String) mKeyList.get(0));
                            String fileType = downFile.getType();
                            int downFlag = downFile.getStatus();
                            LOG.mo8825d("downFlag----->" + downFlag + "----fileType------>" + fileType);
                            if (downFlag != 0 || !DownloadTask.TYPE_EPG.equals(fileType)) {
                                LOG.mo8825d("DownloadTask.DOWNLOAD_STATUS_DOWNLOADING");
                                downFile.setStatus(1);
                                reStartDownTask();
                                return;
                            }
                            LOG.mo8825d("DownloadTask.TYPE_EPG");
                            this.mDownFlag = 0;
                            this.mWorkThreadHandle.removeMessages(0);
                            this.mWorkThreadHandle.sendMessage(this.mWorkThreadHandle.obtainMessage(0));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
    }

    public void deleteFile(String filmPath) {
        LOG.mo8825d("....deleteFile...");
        LOG.mo8825d("mReadOrWrite------>" + mReadOrWrite);
        if (mReadOrWrite != null) {
            mReadOrWrite.delteFile(filmPath);
        }
    }

    public boolean checkIsTaskInfoEmpty() {
        LOG.mo8825d("....checkIsTaskInfoEmpty...");
        boolean ret = false;
        if (mReadOrWrite != null) {
            ret = getStbTaskInfo().isEmpty();
        }
        LOG.mo8825d("....isTaskInfoEmpty===..." + ret);
        return ret;
    }

    public static void refreshData() {
        mTaskInfo = getStbTaskInfo();
    }

    public Map<String, List<DownloadTask>> getStbTaskList() {
        LOG.mo8825d("...getStbTaskList...");
        LOG.mo8825d(mTaskInfo);
        Map<String, List<DownloadTask>> allDataMap = null;
        try {
            List<DownloadTask> arrayList = new ArrayList<>();
            try {
                if (mTaskInfo == null || mTaskInfo.size() <= 0 || isAddTask() || isUpdateTask()) {
                    LOG.mo8825d("getStbTaskList----->isDownLoad");
                    mTaskInfo = getStbTaskInfo();
                }
                SortList<Map<String, String>> mSortList = new SortList<>();
                if (mTaskInfo == null || mTaskInfo.size() <= 0) {
                    List<DownloadTask> mTaskList = arrayList;
                    return null;
                }
                if (isDownLoad(1)) {
                    LOG.mo8825d("isDownLoad(DownloadTask.DOWNLOAD_STATUS_DOWNLOADING)");
                    String key = getDownLoadingKey();
                    if (StringUtils.isNotEmpty(key)) {
                        arrayList.add((DownloadTask) mTaskInfo.get(key));
                    }
                }
                if (isDownLoad(0)) {
                    LOG.mo8825d("isDownLoad(DownloadTask.DOWNLOAD_STATUS_WAIT)");
                    List<String> mKeyList = getDownListByFlag(0);
                    ArrayList arrayList2 = new ArrayList();
                    for (String key2 : mKeyList) {
                        if (StringUtils.isNotEmpty(key2)) {
                            arrayList2.add((DownloadTask) mTaskInfo.get(key2));
                        }
                    }
                    mSortList.Sort(arrayList2);
                    LOG.mo8825d("...mWaitList...size===" + arrayList2.size());
                    arrayList.addAll(arrayList2);
                }
                if (isDownLoad(2)) {
                    LOG.mo8825d("isDownLoad(DownloadTask.DOWNLOAD_STATUS_FINISHED)");
                    List<String> mKeyList2 = getDownListByFlag(2);
                    List<DownloadTask> mDownlaodList = new ArrayList<>();
                    for (String key3 : mKeyList2) {
                        if (StringUtils.isNotEmpty(key3)) {
                            mDownlaodList.add((DownloadTask) mTaskInfo.get(key3));
                        }
                    }
                    mSortList.Sort(mDownlaodList);
                    LOG.mo8825d("...mDownlaodList...size===" + mDownlaodList.size());
                    arrayList.addAll(mDownlaodList);
                }
                if (isDownLoad(-1)) {
                    List<String> mKeyList3 = getDownListByFlag(-1);
                    List<DownloadTask> mFailedList = new ArrayList<>();
                    for (String key4 : mKeyList3) {
                        if (StringUtils.isNotEmpty(key4)) {
                            mFailedList.add((DownloadTask) mTaskInfo.get(key4));
                        }
                    }
                    mSortList.Sort(mFailedList);
                    LOG.mo8825d("...mFailedList...size===" + mFailedList.size());
                    arrayList.addAll(mFailedList);
                }
                Map<String, List<DownloadTask>> allDataMap2 = new HashMap<>();
                try {
                    List<String> cateList = new ArrayList<>();
                    downAllCategoryList(cateList);
                    LOG.mo8825d("mTaskList------->" + arrayList);
                    allDataMap2.put("全部", arrayList);
                    for (DownloadTask item : arrayList) {
                        String category = item.getCategory();
                        if (StringUtils.isEmpty(category) || !cateList.contains(category)) {
                            category = "その他";
                        }
                        if (allDataMap2.containsKey(category)) {
                            ((List) allDataMap2.get(category)).add(item);
                        } else {
                            List<DownloadTask> list = new ArrayList<>();
                            list.add(item);
                            allDataMap2.put(category, list);
                        }
                    }
                    for (Entry<String, List<DownloadTask>> entry : allDataMap2.entrySet()) {
                        SWLogger sWLogger = LOG;
                        StringBuilder sb = new StringBuilder("allDataMap-----size----->");
                        sWLogger.mo8825d(sb.append(allDataMap2.size()).append("-------key-------->").append((String) entry.getKey()).toString());
                        List<DownloadTask> list2 = (List) entry.getValue();
                        for (DownloadTask downFile : list2) {
                            LOG.mo8825d("list-----size----->" + list2.size() + "-------downFile-------->" + downFile);
                        }
                    }
                    ArrayList arrayList3 = arrayList;
                    return allDataMap2;
                } catch (Exception e) {
                    e = e;
                    allDataMap = allDataMap2;
                    ArrayList arrayList4 = arrayList;
                    e.printStackTrace();
                    LOG.mo8825d("...Exception...===" + e.toString());
                    return allDataMap;
                }
            } catch (Exception e2) {
                e = e2;
                ArrayList arrayList5 = arrayList;
                e.printStackTrace();
                LOG.mo8825d("...Exception...===" + e.toString());
                return allDataMap;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
            return allDataMap;
        }
    }

    private void downAllCategoryList(List<String> list) {
        LOG.mo8825d("*******downAllCategoryList********");
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add("ドラマ");
        list.add("情報/ワイドショー");
        list.add("ドキュメンタリー/教養");
        list.add("ニュース/報道");
        list.add("スポーツ");
        list.add("趣味/教育");
        list.add("アニメ/特撮");
        list.add("福祉");
    }

    public boolean isLiveDownTask() {
        LOG.mo8825d(".......isLiveDownTask.......");
        try {
            if (!usbChecked()) {
                return false;
            }
            List<DownloadTask> downFiles = getStbLiveTaskList();
            if (downFiles == null || downFiles.size() <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
            return false;
        }
    }

    public List<DownloadTask> getStbLiveTaskList() {
        LOG.mo8825d("...getStbLiveTaskList...");
        List<DownloadTask> mTaskInfoList = null;
        try {
            List<DownloadTask> mTaskInfoList2 = new ArrayList<>();
            try {
                if (!usbChecked()) {
                    return mTaskInfoList2;
                }
                for (String key : mTaskInfo.keySet()) {
                    if (StringUtils.isNotEmpty(key)) {
                        DownloadTask downFile = (DownloadTask) mTaskInfo.get(key);
                        if (downFile.getType().equals(DownloadTask.TYPE_EPG)) {
                            mTaskInfoList2.add(downFile);
                            return mTaskInfoList2;
                        }
                    }
                }
                return mTaskInfoList2;
            } catch (Exception e) {
                e = e;
                mTaskInfoList = mTaskInfoList2;
                e.printStackTrace();
                LOG.mo8825d("...Exception...===" + e.toString());
                return mTaskInfoList;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
            return mTaskInfoList;
        }
    }

    private static Map<String, DownloadTask> getStbTaskInfo() {
        LOG.mo8825d("...getStbTaskInfo...");
        Map<String, DownloadTask> mTaskInfo2 = new HashMap<>();
        if (usbChecked()) {
            mTaskInfo2 = new HashMap<>();
            LOG.mo8825d("path=" + sUsbPath + DOWNLOAD_PATH);
            File[] files = new File(sUsbPath + DOWNLOAD_PATH).listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.getName().endsWith(".info")) {
                        try {
                            String json = mReadOrWrite.readFilefromFile(f);
                            LOG.mo8825d("json = " + json);
                            if (StringUtils.isNotEmpty(json)) {
                                DownloadTask task = (DownloadTask) new ObjectMapper().readValue(json, (TypeReference) new TypeReference<DownloadTask>() {
                                });
                                task.setSavePath(sUsbPath + DOWNLOAD_PATH);
                                mTaskInfo2.put(task.getTaskName(), task);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            LOG.mo8825d("...Exception...===" + e.toString());
                        }
                    }
                }
            }
        }
        return mTaskInfo2;
    }

    public boolean updateDownFile(DownloadTask task) {
        LOG.mo8825d("...updateDownFile....");
        try {
            String json = JsonUtil.toJson((Object) task);
            if (StringUtils.isNotEmpty(json)) {
                mReadOrWrite.writeFileToDisk(json, sUsbPath + DOWNLOAD_PATH + task.getTaskName() + ".info");
                this.mDProcessor.setProp("isUpdateTask", "1");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
        return false;
    }

    public int getDownFlagByKey(String key) {
        LOG.mo8825d("...getDownFlagByKey....key===" + key);
        try {
            DownloadTask mTaskInfo2 = getTaskInfoFromKey(key);
            if (mTaskInfo2 != null) {
                return mTaskInfo2.getStatus();
            }
            return -1;
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
            return -1;
        }
    }

    private DownloadTask getTaskInfoFromKey(String key) {
        LOG.mo8825d("...getTaskInfoFromKey....key===" + key);
        DownloadTask mTakInfo = null;
        try {
            try {
                if (mTaskInfo == null || mTaskInfo.size() <= 0 || isAddTask() || isUpdateTask()) {
                    mTaskInfo = getStbTaskInfo();
                }
                mTakInfo = (DownloadTask) mTaskInfo.get(key);
            } catch (Exception e) {
                e = e;
                mTakInfo = new DownloadTask();
                e.printStackTrace();
                LOG.mo8825d("...Exception...===" + e.toString());
                LOG.mo8825d("mTakInfo------>" + mTakInfo);
                return mTakInfo;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
            LOG.mo8825d("mTakInfo------>" + mTakInfo);
            return mTakInfo;
        }
        LOG.mo8825d("mTakInfo------>" + mTakInfo);
        return mTakInfo;
    }

    public String getDownLoadingKey() {
        String str = XmlPullParser.NO_NAMESPACE;
        try {
            if (mTaskInfo == null || mTaskInfo.size() <= 0 || isAddTask() || isUpdateTask()) {
                mTaskInfo = getStbTaskInfo();
            }
            for (String key : mTaskInfo.keySet()) {
                if (StringUtils.isNotEmpty(key) && ((DownloadTask) mTaskInfo.get(key)).getStatus() == 1) {
                    str = key;
                }
            }
            LOG.mo8825d("...getDownLoadingKey....key===" + str);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
        return str;
    }

    private List<String> getDownListByFlag(int flag) {
        LOG.mo8825d("...getDownListByFlag....");
        List<String> list = null;
        try {
            List<String> list2 = new ArrayList<>();
            try {
                if (mTaskInfo == null || mTaskInfo.size() <= 0 || isAddTask() || isUpdateTask()) {
                    mTaskInfo = getStbTaskInfo();
                }
                for (String key : mTaskInfo.keySet()) {
                    if (StringUtils.isNotEmpty(key) && ((DownloadTask) mTaskInfo.get(key)).getStatus() == flag) {
                        list2.add(key);
                    }
                }
                return list2;
            } catch (Exception e) {
                e = e;
                list = list2;
                e.printStackTrace();
                LOG.mo8825d("...Exception...===" + e.toString());
                return list;
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
            return list;
        }
    }

    public void reStartDownTask() {
        long dateimeMillions;
        LOG.mo8825d("....重新发送指令reStartDownTask....");
        try {
            if (usbChecked() && isDownLoad(1)) {
                String key = getDownLoadingKey();
                LOG.mo8825d("*****reStartDownTask*****key===********" + key);
                DownloadTask downloadTask = getTaskInfoFromKey(key);
                if (this.isUsbAvailable) {
                    Thread.sleep(1000);
                    LOG.mo8825d("mSwDownFile----->" + this.mSwDownFile);
                    String date = downloadTask.getDate();
                    String startTime1 = downloadTask.getStartTime();
                    String time = downloadTask.getEndTime();
                    String startDate = new StringBuilder(String.valueOf(date)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime1).toString();
                    String endDate = new StringBuilder(String.valueOf(date)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time).toString();
                    long startLong = StringUtils.getTimeMillions(startDate);
                    long endLong = StringUtils.getTimeMillions(endDate);
                    String str = new StringBuilder(String.valueOf(date)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time).toString();
                    LOG.mo8825d("startLong----->" + startLong + "----endLong---->" + endLong);
                    if (startLong - endLong > 0) {
                        dateimeMillions = StringUtils.getTimeMillions(str) + DateTime.MILLIS_PER_DAY;
                    } else {
                        dateimeMillions = StringUtils.getTimeMillions(str);
                    }
                    LOG.mo8825d("startTime1---> " + startTime1 + "-----EndTime----->" + time + "----date--->" + date);
                    String startTime = String.valueOf(startLong / 1000);
                    String endTime = String.valueOf(dateimeMillions / 1000);
                    this.mDownFlag = 1;
                    initDownload();
                    String fileVodUrl = downloadTask.getVodUrl();
                    if (fileVodUrl.startsWith("mop://")) {
                        fileVodUrl = mopUrl2RealUrl(fileVodUrl, downloadTask.getChannelMask());
                    }
                    downloadTask.setSavePath(sUsbPath + DOWNLOAD_PATH);
                    this.mSwDownFile.startDownLoad(downloadTask.getTaskName(), fileVodUrl, startTime, endTime, downloadTask.getSavePath() + downloadTask.getLocalFile());
                    return;
                }
                pauseDownload();
                showUSBLackOfSpace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
    }

    public void pauseDownTask() {
        LOG.mo8825d("....pauseDownTask....");
        try {
            if (usbChecked() && isDownLoad(1)) {
                this.mDownFlag = 1;
                this.mSwDownFile.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
    }

    public void resumeDownTask() {
        LOG.mo8825d("....resumeDownTask....");
        try {
            if (usbChecked() && isDownLoad(1)) {
                if (this.isUsbAvailable) {
                    this.mDownFlag = 1;
                    this.mSwDownFile.resume();
                    return;
                }
                pauseDownload();
                showUSBLackOfSpace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
    }

    public int getStbDownLoadSpeed() {
        LOG.mo8825d("...getStbDownLoadSpeed.....");
        if (this.mSwDownFile != null) {
            return this.mSwDownFile.getDownloadSpeed();
        }
        return 0;
    }

    public int getDownLoadStatus() {
        if (this.mSwDownFile != null) {
            return this.mSwDownFile.getDownLoadStatus();
        }
        return 0;
    }

    public long getDownProgess() {
        LOG.mo8825d("...getDownProgess.....");
        if (this.mSwDownFile != null) {
            return this.mSwDownFile.getDownloadPercent();
        }
        return 0;
    }

    public void checkLivetask() {
        long dateimeMillions2;
        long dateimeMillions;
        long dateimeMillions22;
        LOG.mo8825d("..检查预约信息..checkLivetask...");
        if (usbChecked() && NetworkInfoUtil.isNetworkConnected(this.mContext)) {
            SortList<Map<String, String>> mSortList = new SortList<>();
            if (isDownLoad(1)) {
                if (this.mSwDownFile == null) {
                    reStartDownTask();
                    return;
                }
                int downStatus = this.mSwDownFile.getDownLoadStatus();
                if (!(downStatus == 1 || downStatus == 2)) {
                    reStartDownTask();
                    return;
                }
            }
            if (isDownLoad(0)) {
                List<String> mKeyList = getDownListByFlag(0);
                ArrayList arrayList = new ArrayList();
                for (String key : mKeyList) {
                    if (StringUtils.isNotEmpty(key)) {
                        arrayList.add((DownloadTask) mTaskInfo.get(key));
                    }
                }
                mSortList.Sort(arrayList);
                LOG.mo8825d("...mWaitList...size===" + arrayList.size());
                String curDate = DateTimeUtil.getCurrentTime("yyyy-MM-dd HH:mm");
                DownloadTask downloadTask = (DownloadTask) arrayList.get(0);
                String date = downloadTask.getDate();
                String startTime1 = downloadTask.getStartTime();
                String time = downloadTask.getEndTime();
                String startDate = new StringBuilder(String.valueOf(date)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime1).toString();
                String endDate = new StringBuilder(String.valueOf(date)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time).toString();
                long startLong = StringUtils.getTimeMillions(startDate);
                long endLong = StringUtils.getTimeMillions(endDate);
                String str = new StringBuilder(String.valueOf(date)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time).toString();
                LOG.mo8825d("startLong----->" + startLong + "----endLong---->" + endLong);
                if (startLong - endLong > 0) {
                    dateimeMillions = StringUtils.getTimeMillions(str) + DateTime.MILLIS_PER_DAY;
                } else {
                    dateimeMillions = StringUtils.getTimeMillions(str);
                }
                long curTimeMillions = StringUtils.getTimeMillions(curDate);
                long intervel = dateimeMillions - curTimeMillions;
                LOG.mo8825d("startTime1---> " + startTime1 + "-----EndTime----->" + time + "----date--->" + date);
                LOG.mo8825d("curDate------>" + curDate);
                LOG.mo8825d("dateimeMillions------->" + dateimeMillions + "--------curTimeMillions------>" + curTimeMillions);
                if (intervel <= 0) {
                    if (this.isUsbAvailable) {
                        if (!isDownLoad(1)) {
                            downloadTask.setStatus(1);
                            this.mDownFlag = 1;
                            LOG.mo8825d("mSwDownFile------>" + this.mSwDownFile);
                            String date2 = downloadTask.getDate();
                            String startTime2 = downloadTask.getStartTime();
                            String time2 = downloadTask.getEndTime();
                            String startDate2 = new StringBuilder(String.valueOf(date2)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime2).toString();
                            String endDate2 = new StringBuilder(String.valueOf(date2)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time2).toString();
                            long startLong2 = StringUtils.getTimeMillions(startDate2);
                            long endLong2 = StringUtils.getTimeMillions(endDate2);
                            String str2 = new StringBuilder(String.valueOf(date2)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time2).toString();
                            LOG.mo8825d("startLong----->" + startLong2 + "----endLong---->" + endLong2);
                            if (startLong2 - endLong2 > 0) {
                                dateimeMillions22 = StringUtils.getTimeMillions(str2) + DateTime.MILLIS_PER_DAY;
                            } else {
                                dateimeMillions22 = StringUtils.getTimeMillions(str2);
                            }
                            LOG.mo8825d("startTime1---> " + startTime2 + "-----EndTime----->" + time2 + "----date--->" + date2);
                            String startTime = String.valueOf(startLong2 / 1000);
                            String endTime = String.valueOf(dateimeMillions22 / 1000);
                            initDownload();
                            String fileVodUrl = downloadTask.getVodUrl();
                            if (fileVodUrl.startsWith("mop://")) {
                                fileVodUrl = mopUrl2RealUrl(fileVodUrl, downloadTask.getChannelMask());
                            }
                            downloadTask.setSavePath(sUsbPath + DOWNLOAD_PATH);
                            this.mSwDownFile.startDownLoad(downloadTask.getTaskName(), fileVodUrl, startTime, endTime, downloadTask.getSavePath() + downloadTask.getLocalFile());
                        }
                        trastWiteToLive(downloadTask);
                        updateDownFile(downloadTask);
                        return;
                    }
                    showUSBLackOfSpace();
                    return;
                }
            }
            if (isDownLoad(-1)) {
                LOG.mo8825d("mDownErrorTime----->" + this.mDownErrorTime);
                List<String> mKeyList2 = getDownListByFlag(-1);
                ArrayList arrayList2 = new ArrayList();
                for (String key2 : mKeyList2) {
                    if (StringUtils.isNotEmpty(key2)) {
                        arrayList2.add((DownloadTask) mTaskInfo.get(key2));
                    }
                }
                mSortList.Sort(arrayList2);
                DownloadTask downFile = (DownloadTask) arrayList2.get(0);
                downFile.setStatus(1);
                this.mDownFlag = 1;
                LOG.mo8825d("mSwDownFile------>" + this.mSwDownFile);
                String date22 = downFile.getDate();
                String startTime22 = downFile.getStartTime();
                String time22 = downFile.getEndTime();
                String startDate22 = new StringBuilder(String.valueOf(date22)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime22).toString();
                String endDate22 = new StringBuilder(String.valueOf(date22)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time22).toString();
                long startLong22 = StringUtils.getTimeMillions(startDate22);
                long endLong22 = StringUtils.getTimeMillions(endDate22);
                String str22 = new StringBuilder(String.valueOf(date22)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(time22).toString();
                LOG.mo8825d("startLong----->" + startLong22 + "----endLong---->" + endLong22);
                if (startLong22 - endLong22 > 0) {
                    dateimeMillions2 = StringUtils.getTimeMillions(str22) + DateTime.MILLIS_PER_DAY;
                } else {
                    dateimeMillions2 = StringUtils.getTimeMillions(str22);
                }
                LOG.mo8825d("startTime1---> " + startTime22 + "-----EndTime----->" + time22 + "----date--->" + date22);
                String startTime3 = String.valueOf(startLong22 / 1000);
                String endTime2 = String.valueOf(dateimeMillions2 / 1000);
                initDownload();
                String fileVodUrl2 = downFile.getVodUrl();
                if (fileVodUrl2.startsWith("mop://")) {
                    fileVodUrl2 = mopUrl2RealUrl(fileVodUrl2, downFile.getChannelMask());
                }
                this.mSwDownFile.startDownLoad(downFile.getTaskName(), fileVodUrl2, startTime3, endTime2, downFile.getSavePath() + downFile.getLocalFile());
                updateDownFile(downFile);
            }
        }
    }

    private void trastWiteToLive(DownloadTask taskInfo) {
        LOG.mo8825d("....trastLiveToTask...");
        try {
            if (usbChecked() && taskInfo != null) {
                if (taskInfo.getType().equals(DownloadTask.TYPE_EPG)) {
                    taskInfo.setType(DownloadTask.TYPE_VOD);
                }
                mTaskInfo.put(taskInfo.getTaskName(), taskInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean usbChecked() {
        boolean flag = false;
        String mountPath = SWSysProp.getStringParam("mount_path", "/storage/sda1");
        LOG.mo8825d("...mountPath...===" + mountPath);
        File f = new File(mountPath);
        if (f.exists() && f.list() != null && f.list().length > 0) {
            flag = true;
        }
        LOG.mo8825d("...usbChecked...=flag==" + flag);
        return flag;
    }

    /* access modifiers changed from: private */
    public void pauseDownload() {
        LOG.mo8825d("pauseDownload------>");
        pauseDownTask();
        if (this.mMainApp != null) {
            PageDownLoad pageDownLoad = this.mMainApp.getPageDownLoad();
            if (pageDownLoad != null) {
                pageDownLoad.callBack2PauseIconUpdate();
            }
        }
    }

    /* access modifiers changed from: private */
    public void showUSBLackOfSpace() {
        LOG.mo8825d("showUSBLackOfSpace----->");
        if (this.mMainApp != null) {
            boolean isAtDownPage = this.mMainApp.isAtDownPage;
            LOG.mo8825d("isAtDownPage----->" + isAtDownPage);
            if (isAtDownPage) {
                PageDownLoad pageDownLoad = this.mMainApp.getPageDownLoad();
                if (pageDownLoad != null) {
                    pageDownLoad.showLackSpaceDialog();
                }
            }
        }
    }

    private String mopUrl2RealUrl(String url, String sId) {
        String curOisUrl = this.mSdkRemoteManager.getCurOisUrl();
        LOG.mo8825d("ois------>" + curOisUrl);
        String oisToken = this.mSdkRemoteManager.getOisToken();
        LOG.mo8825d("oisToken------>" + oisToken);
        String user = SWSysProp.getStringParam("user_name", XmlPullParser.NO_NAMESPACE);
        LOG.mo8825d("user------->" + user);
        if (StringUtils.isEmpty(this.mTerminalid)) {
            this.mTerminalid = SystemProperties.get("ro.serialno");
        }
        String ret = "http://" + curOisUrl.replace("5001", "5000/") + url.substring(6) + ".m3u8?protocal=hls&user=" + user + "?tid=" + this.mTerminalid + "&?sid=" + sId + "&type=stb&token=" + oisToken;
        LOG.mo8825d("mopUrl2RealUrl =" + ret);
        return ret;
    }
}
