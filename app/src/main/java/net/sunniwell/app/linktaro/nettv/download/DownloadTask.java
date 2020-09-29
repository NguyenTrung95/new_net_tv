package net.sunniwell.app.linktaro.nettv.download;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.lang.reflect.Field;

public class DownloadTask implements Parcelable {
    public static final String ACTION_ADD_TASK = "net.sunniwell.action.DOWNLOAD_ADD_TASK";
    public static final String ACTION_REMOVE_TASK = "net.sunniwell.action.DOWNLOAD_REMOVE_TASK";
    public static final String ACTION_UPDATE_TASK = "net.sunniwell.action.DOWNLOAD_UPDATE_TASK";
    public static final Creator<DownloadTask> CREATOR = new Creator<DownloadTask>() {
        public DownloadTask createFromParcel(Parcel source) {
            Field[] fields;
            DownloadTask data = new DownloadTask();
            new Bundle();
            Bundle mBundle = source.readBundle();
            for (Field f : data.getClass().getDeclaredFields()) {
                f.setAccessible(true);
                try {
                    if (f.getType().getSimpleName().equals("String")) {
                        f.set(data, mBundle.getString(f.getName()));
                    } else if (f.getType().getSimpleName().equals("Boolean")) {
                        f.setBoolean(data, mBundle.getBoolean(f.getName()));
                    } else if (f.getType().getSimpleName().equals("int[]")) {
                        f.set(data, mBundle.getIntArray(f.getName()));
                    } else if (f.getType().getSimpleName().equals("int")) {
                        f.setInt(data, mBundle.getInt(f.getName()));
                    } else if (f.getType().getSimpleName().equals("byte[]")) {
                        f.set(data, mBundle.getByteArray(f.getName()));
                    } else if (f.getType().getSimpleName().equals("byte")) {
                        f.setByte(data, mBundle.getByte(f.getName()));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                f.setAccessible(false);
            }
            return data;
        }

        public DownloadTask[] newArray(int size) {
            return new DownloadTask[size];
        }
    };
    public static final int DOWNLOAD_STATUS_DOWNLOADING = 1;
    public static final int DOWNLOAD_STATUS_FAILED = -1;
    public static final int DOWNLOAD_STATUS_FINISHED = 2;
    public static final int DOWNLOAD_STATUS_UNKOWN = -2;
    public static final int DOWNLOAD_STATUS_WAIT = 0;
    public static final int RESULT_EXIST = -2;
    public static final int RESULT_MAX = -1;
    public static final int RESULT_NOT_EXIST = -3;
    public static final int RESULT_NO_DISK = -4;
    public static final int RESULT_OK = 0;
    public static final int TVFORMT_BS = 1;
    public static final int TVFORMT_CS = 2;
    public static final int TVFORMT_DS = 3;
    public static final String TYPE_EPG = "EPG";
    public static final String TYPE_VOD = "VOD";
    private String category;
    private String channelIconUrl;
    private String channelMask;
    private String channelName;
    private int channelNum;
    private String date;
    private String desc;
    private String endTime;
    private String localFile;
    private String programName;
    private String savePath;
    private String startTime;
    private int status;
    private String taskName;
    private int tvFormt;
    private String type;
    private String vodUrl;

    public final String getTaskName() {
        return this.taskName;
    }

    public final void setTaskName(String taskName2) {
        this.taskName = taskName2;
    }

    public final String getDate() {
        return this.date;
    }

    public final void setDate(String date2) {
        this.date = date2;
    }

    public final String getDesc() {
        return this.desc;
    }

    public final void setDesc(String desc2) {
        this.desc = desc2;
    }

    public final String getType() {
        return this.type;
    }

    public final void setType(String type2) {
        this.type = type2;
    }

    public final String getVodUrl() {
        return this.vodUrl;
    }

    public final void setVodUrl(String vodUrl2) {
        this.vodUrl = vodUrl2;
    }

    public final String getStartTime() {
        return this.startTime;
    }

    public final void setStartTime(String startTime2) {
        this.startTime = startTime2;
    }

    public final String getEndTime() {
        return this.endTime;
    }

    public final void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public final String getProgramName() {
        return this.programName;
    }

    public final void setProgramName(String programName2) {
        this.programName = programName2;
    }

    public final String getChannelName() {
        return this.channelName;
    }

    public final void setChannelName(String channelName2) {
        this.channelName = channelName2;
    }

    public final String getChannelIconUrl() {
        return this.channelIconUrl;
    }

    public final void setChannelIconUrl(String channelIconUrl2) {
        this.channelIconUrl = channelIconUrl2;
    }

    public final int getChannelNum() {
        return this.channelNum;
    }

    public final void setChannelNum(int channelNum2) {
        this.channelNum = channelNum2;
    }

    public final int getTvFormt() {
        return this.tvFormt;
    }

    public final void setTvFormt(int tvFormt2) {
        this.tvFormt = tvFormt2;
    }

    public final String getCategory() {
        return this.category;
    }

    public final void setCategory(String category2) {
        this.category = category2;
    }

    public final String getChannelMask() {
        return this.channelMask;
    }

    public final void setChannelMask(String channelMask2) {
        this.channelMask = channelMask2;
    }

    public final int getStatus() {
        return this.status;
    }

    public final void setStatus(int status2) {
        this.status = status2;
    }

    public final String getSavePath() {
        return this.savePath;
    }

    public final void setSavePath(String savePath2) {
        this.savePath = savePath2;
    }

    public final String getLocalFile() {
        return this.localFile;
    }

    public final void setLocalFile(String localFile2) {
        this.localFile = localFile2;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        Field[] fields;
        Bundle mBundle = new Bundle();
        for (Field f : getClass().getDeclaredFields()) {
            try {
                if (f.getType().getSimpleName().equals("String")) {
                    mBundle.putString(f.getName(), (String) f.get(this));
                } else if (f.getType().getSimpleName().equals("Boolean")) {
                    mBundle.putBoolean(f.getName(), f.getBoolean(this));
                } else if (f.getType().getSimpleName().equals("int[]")) {
                    mBundle.putIntArray(f.getName(), (int[]) f.get(this));
                } else if (f.getType().getSimpleName().equals("int")) {
                    mBundle.putInt(f.getName(), f.getInt(this));
                } else if (f.getType().getSimpleName().equals("byte[]")) {
                    mBundle.putByteArray(f.getName(), (byte[]) f.get(this));
                } else if (f.getType().getSimpleName().equals("byte")) {
                    mBundle.putByte(f.getName(), f.getByte(this));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        dest.writeBundle(mBundle);
    }

    public String toString() {
        return "DownloadTask [taskName=" + this.taskName + ", date=" + this.date + ", desc=" + this.desc + ", type=" + this.type + ", vodUrl=" + this.vodUrl + ", startTime=" + this.startTime + ", endTime=" + this.endTime + ", programName=" + this.programName + ", channelName=" + this.channelName + ", channelIconUrl=" + this.channelIconUrl + ", channelNum=" + this.channelNum + ", tvFormt=" + this.tvFormt + ", category=" + this.category + ", channelMask=" + this.channelMask + ", status=" + this.status + ", savePath=" + this.savePath + ", localFile=" + this.localFile + "]";
    }
}
