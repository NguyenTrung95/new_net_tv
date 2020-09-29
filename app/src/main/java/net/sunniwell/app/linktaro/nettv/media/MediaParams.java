package net.sunniwell.app.linktaro.nettv.media;

import java.util.HashMap;
import java.util.Map;

public class MediaParams {
    private int allowTrickmodeFlag;
    private int audioTrackUIFlag = 0;
    private int audioType;
    private int audioVolumeUIFlag = 0;
    private String channelCode;
    private int channelNoUIFlag = 0;
    private int copyProtectio;
    private int displayArea_height = 0;
    private int displayArea_left = 0;
    private int displayArea_top = 0;
    private int displayArea_width = 0;
    private int drmType;
    private String endTime;
    private String entryID;
    private int errorCode;
    private int eventCode;
    private int eventResult;
    private int fingerprint;
    private boolean isFullScreen = false;
    private boolean isInit = true;
    private boolean mIsVideoReadyToBePlayed = false;
    private boolean mIsVideoSizeKnown = false;
    private String mediaCode;
    private int mediaType;
    private String mediaUrl;
    private String message;
    private int muteFlag = 0;
    private int muteUIFlag = 0;
    private int nativePlayerInstanceID = 0;
    private int nativeUIFlag = 0;
    private int nowPlayMode = -1;
    private int nowPlayRate;
    private int oldPlayMode = -1;
    private int oldPlayRate;
    private int playMode;
    private int progressBarUIFlag = 0;
    private String startTime;
    private int streamType;
    private int subtitileFlag = 0;
    private Map<String, Object> tempMap = new HashMap();
    private int timeShift;
    private String timeShiftURL;
    private int videoAlpha = 0;
    private int videoDisplayMode = 1;
    private int videoType;

    public int getNativePlayerInstanceID() {
        return this.nativePlayerInstanceID;
    }

    public void setNativePlayerInstanceID(int nativePlayerInstanceID2) {
        this.nativePlayerInstanceID = nativePlayerInstanceID2;
    }

    public String getMediaUrl() {
        return this.mediaUrl;
    }

    public void setMediaUrl(String mediaUrl2) {
        this.mediaUrl = mediaUrl2;
    }

    public int getPlayMode() {
        return this.playMode;
    }

    public void setPlayMode(int playMode2) {
        this.playMode = playMode2;
    }

    public int getVideoDisplayMode() {
        return this.videoDisplayMode;
    }

    public void setVideoDisplayMode(int videoDisplayMode2) {
        this.videoDisplayMode = videoDisplayMode2;
    }

    public int getDisplayArea_left() {
        return this.displayArea_left;
    }

    public void setDisplayArea_left(int displayAreaLeft) {
        this.displayArea_left = displayAreaLeft;
    }

    public int getDisplayArea_top() {
        return this.displayArea_top;
    }

    public void setDisplayArea_top(int displayAreaTop) {
        this.displayArea_top = displayAreaTop;
    }

    public int getDisplayArea_width() {
        return this.displayArea_width;
    }

    public void setDisplayArea_width(int displayAreaWidth) {
        this.displayArea_width = displayAreaWidth;
    }

    public int getDisplayArea_height() {
        return this.displayArea_height;
    }

    public void setDisplayArea_height(int displayAreaHeight) {
        this.displayArea_height = displayAreaHeight;
    }

    public int getMuteFlag() {
        return this.muteFlag;
    }

    public void setMuteFlag(int muteFlag2) {
        this.muteFlag = muteFlag2;
    }

    public int getNativeUIFlag() {
        return this.nativeUIFlag;
    }

    public void setNativeUIFlag(int nativeUIFlag2) {
        this.nativeUIFlag = nativeUIFlag2;
    }

    public boolean ismIsVideoSizeKnown() {
        return this.mIsVideoSizeKnown;
    }

    public void setmIsVideoSizeKnown(boolean mIsVideoSizeKnown2) {
        this.mIsVideoSizeKnown = mIsVideoSizeKnown2;
    }

    public boolean ismIsVideoReadyToBePlayed() {
        return this.mIsVideoReadyToBePlayed;
    }

    public void setmIsVideoReadyToBePlayed(boolean mIsVideoReadyToBePlayed2) {
        this.mIsVideoReadyToBePlayed = mIsVideoReadyToBePlayed2;
    }

    public Map<String, Object> getTempMap() {
        return this.tempMap;
    }

    public void setTempMap(Map<String, Object> tempMap2) {
        this.tempMap = tempMap2;
    }

    public boolean isInit() {
        return this.isInit;
    }

    public void setInit(boolean isInit2) {
        this.isInit = isInit2;
    }

    public boolean isFullScreen() {
        return this.isFullScreen;
    }

    public void setFullScreen(boolean isFullScreen2) {
        this.isFullScreen = isFullScreen2;
    }

    public String getMediaCode() {
        return this.mediaCode;
    }

    public void setMediaCode(String mediaCode2) {
        this.mediaCode = mediaCode2;
    }

    public int getMediaType() {
        return this.mediaType;
    }

    public void setMediaType(int mediaType2) {
        this.mediaType = mediaType2;
    }

    public int getAudioType() {
        return this.audioType;
    }

    public void setAudioType(int audioType2) {
        this.audioType = audioType2;
    }

    public int getVideoType() {
        return this.videoType;
    }

    public void setVideoType(int videoType2) {
        this.videoType = videoType2;
    }

    public int getStreamType() {
        return this.streamType;
    }

    public void setStreamType(int streamType2) {
        this.streamType = streamType2;
    }

    public int getDrmType() {
        return this.drmType;
    }

    public void setDrmType(int drmType2) {
        this.drmType = drmType2;
    }

    public int getFingerprint() {
        return this.fingerprint;
    }

    public void setFingerprint(int fingerprint2) {
        this.fingerprint = fingerprint2;
    }

    public int getCopyProtectio() {
        return this.copyProtectio;
    }

    public void setCopyProtectio(int copyProtectio2) {
        this.copyProtectio = copyProtectio2;
    }

    public int getAllowTrickmodeFlag() {
        return this.allowTrickmodeFlag;
    }

    public void setAllowTrickmodeFlag(int allowTrickmodeFlag2) {
        this.allowTrickmodeFlag = allowTrickmodeFlag2;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime2) {
        this.startTime = startTime2;
    }

    public String getEndTime() {
        return this.endTime;
    }

    public void setEndTime(String endTime2) {
        this.endTime = endTime2;
    }

    public String getTimeShiftURL() {
        return this.timeShiftURL;
    }

    public void setTimeShiftURL(String timeShiftURL2) {
        this.timeShiftURL = timeShiftURL2;
    }

    public int getTimeShift() {
        return this.timeShift;
    }

    public void setTimeShift(int timeShift2) {
        this.timeShift = timeShift2;
    }

    public String getEntryID() {
        return this.entryID;
    }

    public void setEntryID(String entryID2) {
        this.entryID = entryID2;
    }

    public String getChannelCode() {
        return this.channelCode;
    }

    public void setChannelCode(String channelCode2) {
        this.channelCode = channelCode2;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(int errorCode2) {
        this.errorCode = errorCode2;
    }

    public int getNowPlayMode() {
        return this.nowPlayMode;
    }

    public void setNowPlayMode(int nowPlayMode2) {
        this.nowPlayMode = nowPlayMode2;
    }

    public int getNowPlayRate() {
        return this.nowPlayRate;
    }

    public void setNowPlayRate(int nowPlayRate2) {
        this.nowPlayRate = nowPlayRate2;
    }

    public int getOldPlayMode() {
        return this.oldPlayMode;
    }

    public void setOldPlayMode(int oldPlayMode2) {
        this.oldPlayMode = oldPlayMode2;
    }

    public int getOldPlayRate() {
        return this.oldPlayRate;
    }

    public void setOldPlayRate(int oldPlayRate2) {
        this.oldPlayRate = oldPlayRate2;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message2) {
        this.message = message2;
    }

    public int getEventCode() {
        return this.eventCode;
    }

    public void setEventCode(int eventCode2) {
        this.eventCode = eventCode2;
    }

    public int getEventResult() {
        return this.eventResult;
    }

    public void setEventResult(int eventResult2) {
        this.eventResult = eventResult2;
    }

    public int getMuteUIFlag() {
        return this.muteUIFlag;
    }

    public void setMuteUIFlag(int muteUIFlag2) {
        this.muteUIFlag = muteUIFlag2;
    }

    public int getAudioVolumeUIFlag() {
        return this.audioVolumeUIFlag;
    }

    public void setAudioVolumeUIFlag(int audioVolumeUIFlag2) {
        this.audioVolumeUIFlag = audioVolumeUIFlag2;
    }

    public int getAudioTrackUIFlag() {
        return this.audioTrackUIFlag;
    }

    public void setAudioTrackUIFlag(int audioTrackUIFlag2) {
        this.audioTrackUIFlag = audioTrackUIFlag2;
    }

    public int getProgressBarUIFlag() {
        return this.progressBarUIFlag;
    }

    public void setProgressBarUIFlag(int progressBarUIFlag2) {
        this.progressBarUIFlag = progressBarUIFlag2;
    }

    public int getChannelNoUIFlag() {
        return this.channelNoUIFlag;
    }

    public void setChannelNoUIFlag(int channelNoUIFlag2) {
        this.channelNoUIFlag = channelNoUIFlag2;
    }

    public int getSubtitileFlag() {
        return this.subtitileFlag;
    }

    public void setSubtitileFlag(int subtitileFlag2) {
        this.subtitileFlag = subtitileFlag2;
    }

    public int getVideoAlpha() {
        return this.videoAlpha;
    }

    public void setVideoAlpha(int videoAlpha2) {
        this.videoAlpha = videoAlpha2;
    }
}
