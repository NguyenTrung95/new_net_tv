package net.sunniwell.app.linktaro.nettv.media;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnSeekCompleteListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.Parcel;
import android.os.SystemProperties;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;
import java.util.Map;
import java.util.regex.Pattern;

import net.sunniwell.app.linktaro.nettv.content.PagePlayVideo;
import net.sunniwell.app.linktaro.nettv.processor.imp.DBProcessor;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xmlpull.p019v1.XmlPullParser;

public class JSExtMediaPlayer {
    private static final int CHANGE_SURFACE_FULL = 2;
    private static final int CMD_PAUSE = 2;
    private static final int CMD_PLAY = 1;
    private static final int CMD_RESUME = 3;
    private static final int CMD_SEEK = 5;
    private static final int CMD_STOP = 4;
    private static final int EPG_PAGE_PAGE_SIZE = 1;
    private static final int FULL_PAGE_PAGE_SIZE = 0;
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(JSExtMediaPlayer.class);
    private static float mediaVolume = 5.0f;
    private AudioManager audioManager = null;
    /* access modifiers changed from: private */
    public int currentPlayTimeTemp = -1;
    /* access modifiers changed from: private */
    public boolean isPassStop = false;
    /* access modifiers changed from: private */
    public boolean isPlaying = false;
    /* access modifiers changed from: private */
    public boolean isPrepared = false;
    /* access modifiers changed from: private */
    public boolean isPressPause = false;
    /* access modifiers changed from: private */
    public boolean isPressResume = false;
    private final boolean isPressSeek = false;
    /* access modifiers changed from: private */
    public boolean isSettingDatasource = false;
    /* access modifiers changed from: private */
    public MediaPlayerListeners listeners = new MediaPlayerListeners(this, null);
    /* access modifiers changed from: private */
    public Context mContext;
    private DBProcessor mDbProcessor;
    private HandlerThread mHandlerThread;
    private MediaCmdHandler mMediaCmdHandler;
    /* access modifiers changed from: private */
    public PagePlayVideo mPagePlayVideo;
    /* access modifiers changed from: private */
    public MediaPlayer mediaPlayer;
    private int mediaTrackFlag;
    /* access modifiers changed from: private */
    public MediaParams params = new MediaParams();
    /* access modifiers changed from: private */
    public SurfaceHolder surfaceHolder;
    /* access modifiers changed from: private */
    public SurfaceView surfaceView;
    private Handler svHandler = new Handler() {
        @SuppressLint({"ResourceAsColor"})
        public void handleMessage(Message msg) {
            JSExtMediaPlayer.this.surfaceHolder.removeCallback(JSExtMediaPlayer.this.listeners);
            JSExtMediaPlayer.this.surfaceHolder.addCallback(JSExtMediaPlayer.this.listeners);
            if (msg.what == 1) {
                Bundle bun = msg.getData();
                int width = bun.getInt("width");
                int height = bun.getInt("height");
                int top = bun.getInt("top");
                int left = bun.getInt("left");
                LayoutParams p = (LayoutParams) JSExtMediaPlayer.this.surfaceView.getLayoutParams();
                p.topMargin = top;
                p.leftMargin = left;
                p.width = width;
                p.height = height;
                JSExtMediaPlayer.this.surfaceView.setLayoutParams(p);
            } else if (msg.what == 0) {
                Bundle bun2 = msg.getData();
                int width2 = bun2.getInt("width");
                int height2 = bun2.getInt("height");
                int top2 = bun2.getInt("top");
                int left2 = bun2.getInt("left");
                LayoutParams p2 = (LayoutParams) JSExtMediaPlayer.this.surfaceView.getLayoutParams();
                p2.topMargin = top2;
                p2.leftMargin = left2;
                p2.width = width2;
                p2.height = height2;
                JSExtMediaPlayer.this.surfaceView.setLayoutParams(p2);
            } else if (2 == msg.what) {
                LayoutParams p3 = (LayoutParams) JSExtMediaPlayer.this.surfaceView.getLayoutParams();
                p3.topMargin = 0;
                p3.leftMargin = 0;
                p3.width = 1280;
                p3.height = 719;
                JSExtMediaPlayer.this.surfaceView.setLayoutParams(p3);
            }
        }
    };

    private class MediaCmdHandler extends Handler {
        public MediaCmdHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    _play();
                    break;
                case 2:
                    _pause();
                    break;
                case 3:
                    _resume();
                    break;
                case 4:
                    _stop();
                    break;
                case 5:
                    _seek(msg);
                    break;
            }
            super.handleMessage(msg);
        }

        private synchronized void _play() {
            JSExtMediaPlayer.LOG.mo8825d("_play");
            JSExtMediaPlayer.this.isSettingDatasource = true;
            String playUrl = JSExtMediaPlayer.this.params.getMediaUrl();
            try {
                JSExtMediaPlayer.this.isPassStop = false;
                JSExtMediaPlayer.LOG.mo8825d("[play_mediaUrl]" + playUrl + "&mediaPlayer=" + JSExtMediaPlayer.this.mediaPlayer);
                if (StringUtils.isNotEmpty(playUrl)) {
                    if (JSExtMediaPlayer.this.mediaPlayer == null) {
                        JSExtMediaPlayer.this.initMediaPlayer();
                    }
                    JSExtMediaPlayer.this.mediaPlayer.reset();
                    JSExtMediaPlayer.this.mediaPlayer.setDataSource(JSExtMediaPlayer.this.mContext, Uri.parse(playUrl));
                    JSExtMediaPlayer.this.isPrepared = false;
                    JSExtMediaPlayer.this.mediaPlayer.prepareAsync();
                }
            } catch (Exception e) {
                JSExtMediaPlayer.LOG.mo8825d("play error!");
                Toast.makeText(JSExtMediaPlayer.this.mContext, JSExtMediaPlayer.this.mContext.getResources().getString(C0395R.string.live_fail), 1).show();
                if (JSExtMediaPlayer.this.mPagePlayVideo != null) {
                    JSExtMediaPlayer.this.mPagePlayVideo.playError();
                }
                e.printStackTrace();
            }
            JSExtMediaPlayer.this.isSettingDatasource = false;
        }

        private synchronized void _seek(Message msg) {
            int curPos = Integer.valueOf(msg.arg1).intValue();
            if (JSExtMediaPlayer.this.mediaPlayer != null && JSExtMediaPlayer.this.isPlaying && !JSExtMediaPlayer.this.isPassStop && !JSExtMediaPlayer.this.isPressPause) {
                JSExtMediaPlayer.this.mediaPlayer.seekTo(curPos);
            }
        }

        private synchronized void _pause() {
            JSExtMediaPlayer.LOG.mo8825d("_pause");
            if (JSExtMediaPlayer.this.mediaPlayer != null && !JSExtMediaPlayer.this.isPassStop && JSExtMediaPlayer.this.isPlaying && !JSExtMediaPlayer.this.isPressPause) {
                JSExtMediaPlayer.this.isPlaying = false;
                JSExtMediaPlayer.this.currentPlayTimeTemp = JSExtMediaPlayer.this.mediaPlayer.getCurrentPosition();
                JSExtMediaPlayer.this.isPressPause = true;
                JSExtMediaPlayer.this.mediaPlayer.pause();
            }
        }

        private synchronized void _resume() {
            JSExtMediaPlayer.LOG.mo8825d("_resume");
            if (JSExtMediaPlayer.this.mediaPlayer != null && !JSExtMediaPlayer.this.isPassStop && JSExtMediaPlayer.this.isPressPause && !JSExtMediaPlayer.this.isPressResume) {
                JSExtMediaPlayer.this.isPlaying = true;
                JSExtMediaPlayer.this.isPressPause = false;
                JSExtMediaPlayer.this.mediaPlayer.start();
            }
        }

        private synchronized void _stop() {
            JSExtMediaPlayer.LOG.mo8825d("_stop");
            JSExtMediaPlayer.this.mPagePlayVideo.setPlaying(false);
            if (JSExtMediaPlayer.this.mediaPlayer != null) {
                try {
                    JSExtMediaPlayer.this.isPassStop = true;
                    JSExtMediaPlayer.this.mediaPlayer.stop();
                    JSExtMediaPlayer.this.mPagePlayVideo.notifyStopCheckPlayTime();
                    JSExtMediaPlayer.this.isPassStop = false;
                    Thread.sleep(1000);
                    JSExtMediaPlayer.this.mediaPlayer.release();
                    JSExtMediaPlayer.this.mediaPlayer = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    JSExtMediaPlayer.this.mediaPlayer.release();
                    JSExtMediaPlayer.this.mediaPlayer = null;
                } catch (Throwable th) {
                    JSExtMediaPlayer.this.mediaPlayer.release();
                    JSExtMediaPlayer.this.mediaPlayer = null;
                    throw th;
                }
            }
            return;
        }
    }

    private class MediaPlayerListeners implements OnBufferingUpdateListener, OnSeekCompleteListener, OnCompletionListener, OnPreparedListener, OnVideoSizeChangedListener, OnInfoListener, OnErrorListener, Callback {
        private MediaPlayerListeners() {
        }

        /* synthetic */ MediaPlayerListeners(JSExtMediaPlayer jSExtMediaPlayer, MediaPlayerListeners mediaPlayerListeners) {
            this();
        }

        public void onBufferingUpdate(MediaPlayer mp, int percent) {
            JSExtMediaPlayer.LOG.mo8825d("[onBufferingUpdate percent]" + percent);
        }

        public void onSeekComplete(MediaPlayer mp) {
            JSExtMediaPlayer.LOG.mo8825d("[seek complete]");
            if (JSExtMediaPlayer.this.mediaPlayer != null) {
                JSExtMediaPlayer.this.mediaPlayer.start();
            }
        }

        public void onCompletion(MediaPlayer mp) {
            JSExtMediaPlayer.LOG.mo8825d("[onCompletion called]");
            JSExtMediaPlayer.this.mPagePlayVideo.notifySeekEnd();
            JSExtMediaPlayer.this.mPagePlayVideo.setPlaying(false);
        }

        public void onPrepared(MediaPlayer mp) {
            JSExtMediaPlayer.LOG.mo8825d("[onPrepared called]");
            if (JSExtMediaPlayer.this.mPagePlayVideo == null || !JSExtMediaPlayer.this.mPagePlayVideo.isPlayFromSeek()) {
                mp.start();
            } else {
                mp.seekTo(JSExtMediaPlayer.this.mPagePlayVideo.getSeekPoint());
            }
            JSExtMediaPlayer.this.mPagePlayVideo.setPlaying(true);
            JSExtMediaPlayer.this.mPagePlayVideo.notifyPlayStatus();
            JSExtMediaPlayer.this.resumeMediaVol();
            JSExtMediaPlayer.this.isPlaying = true;
            JSExtMediaPlayer.this.isPrepared = true;
            JSExtMediaPlayer.this.isPressPause = false;
            JSExtMediaPlayer.this.isPressResume = false;
        }

        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
            JSExtMediaPlayer.this.refreshVideoDisplay();
        }

        public boolean onError(MediaPlayer mp, int what, int extra) {
            mp.stop();
            mp.release();
            return true;
        }

        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            return true;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (width != 0 && height != 0) {
                JSExtMediaPlayer.this.params.setDisplayArea_width(width);
                JSExtMediaPlayer.this.params.setDisplayArea_height(height);
            }
        }

        public void surfaceCreated(SurfaceHolder holder) {
            JSExtMediaPlayer.this.initMediaPlayer();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                if (JSExtMediaPlayer.this.mediaPlayer != null && JSExtMediaPlayer.this.mediaPlayer.isPlaying()) {
                    JSExtMediaPlayer.this.stop();
                }
            } catch (Exception e) {
                JSExtMediaPlayer.LOG.mo8825d(e.toString());
            }
        }
    }

    public JSExtMediaPlayer(Context context, SurfaceView surfaceView2, PagePlayVideo playPage) {
        LOG.mo8825d("JSExtMediaPlayer constructor");
        this.mContext = context;
        this.mPagePlayVideo = playPage;
        LOG.mo8825d("furface " + surfaceView2);
        this.surfaceView = surfaceView2;
        this.mHandlerThread = new HandlerThread("mediaplayer");
        this.mHandlerThread.start();
        this.mMediaCmdHandler = new MediaCmdHandler(this.mHandlerThread.getLooper());
        this.mDbProcessor = DBProcessor.getDBProcessor(this.mContext);
        this.audioManager = (AudioManager) this.mContext.getSystemService("audio");
        initSurfaceView(surfaceView2);
    }

    public void setSingleOrPlaylistMode(int mode) {
        LOG.mo8825d("use setSingleOrPlaylistMode(" + mode + ")");
        this.params.setPlayMode(mode);
    }

    public int getSingleOrPlaylistMode() {
        LOG.mo8825d("use getSingleOrPlaylistMode())");
        return this.params.getPlayMode();
    }

    public void setVideoDisplayMode(int videoDisplayMode) {
        LOG.mo8825d("called setVideoDisplayMode(" + videoDisplayMode + ")");
        this.params.setVideoDisplayMode(videoDisplayMode);
    }

    public int getVideoDisplayMode() {
        LOG.mo8825d("called getVideoDisplayMode()");
        return this.params.getVideoDisplayMode();
    }

    public void setVideoDisplayArea(int left, int top, int width, int height) {
        LOG.mo8825d("called setVideoDisplayArea(left=" + left + " top=" + top + " width=" + width + " height=" + height + ")");
        this.params.setDisplayArea_left(left);
        this.params.setDisplayArea_top(top);
        this.params.setDisplayArea_width(width);
        this.params.setDisplayArea_height(height);
    }

    public int getVideoDisplayLeft() {
        LOG.mo8825d("called getVideoDisplayLeft()");
        return this.params.getDisplayArea_left();
    }

    public int getVideoDisplayTop() {
        LOG.mo8825d("called getVideoDisplayTop()");
        return this.params.getDisplayArea_top();
    }

    public int getVideoDisplayWidth() {
        LOG.mo8825d("called getVideoDisplayWidth()");
        return this.params.getDisplayArea_width();
    }

    public int getVideoDisplayHeight() {
        LOG.mo8825d("called getVideoDisplayHeight()");
        return this.params.getDisplayArea_height();
    }

    public int getMediaDuration() {
        int duration = -1;
        if (this.mediaPlayer != null && !this.isPassStop && !this.isPressPause) {
            duration = this.mediaPlayer.getDuration();
        }
        LOG.mo8825d("called getMediaDuration()....duration=" + duration);
        return duration;
    }

    public int getCurrentPlayTime() {
        try {
            LOG.mo8826e("mediaPlayer.getCurrentPosition()=" + this.mediaPlayer.getCurrentPosition());
            if (this.mediaPlayer == null || this.isPassStop || this.isPressPause) {
                return this.currentPlayTimeTemp;
            }
            int curPos = this.mediaPlayer.getCurrentPosition();
            this.currentPlayTimeTemp = curPos;
            return curPos;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public boolean getPrepared() {
        return this.isPrepared;
    }

    public void initMediaPlayerParam(int instanceId, int playListFlag, int videoDisplayMode, int left, int top, int width, int height, int muteFlag, int useNativeUIFlag, int subtitleFlag, int videoAlpha, int cycleFlag, int randomFlag, int autoDelFlag) {
        LOG.mo8825d("called initMediaplayer(" + instanceId + "," + playListFlag + "," + videoDisplayMode + "," + height + "," + width + "," + left + "," + top + "," + muteFlag + "," + useNativeUIFlag + "," + subtitleFlag + "," + videoAlpha + "," + cycleFlag + "," + randomFlag + "," + autoDelFlag + ")");
        setSingleOrPlaylistMode(playListFlag);
        setVideoDisplayMode(videoDisplayMode);
        setVideoDisplayArea(left, top, width, height);
        refreshVideoDisplay();
    }

    public int releaseMediaPlayer(int instanceId) {
        LOG.mo8825d("called releaseMediaPlayer");
        if (this.mediaPlayer != null) {
            this.mediaPlayer.stop();
            this.mediaPlayer.release();
            this.mediaPlayer = null;
        }
        return 0;
    }

    public void setSingleMedia(String json) {
        LOG.mo8825d("use setSingleMedia(" + json + ")");
        try {
            JSONObject jo = new JSONArray(json).optJSONObject(0);
            this.params.setMediaUrl(jo.optString("mediaURL"));
            LOG.mo8825d("[mediaURL]" + this.params.getMediaUrl());
            this.params.setMediaCode(jo.optString("mediaCode"));
            this.params.setMediaType(jo.optInt("mediaType"));
            this.params.setAudioType(jo.optInt("audioType"));
            this.params.setVideoType(jo.optInt("videoType"));
            this.params.setStreamType(jo.optInt("streamType"));
            this.params.setDrmType(jo.optInt("drmType"));
            this.params.setFingerprint(jo.optInt("fingerPrint"));
            this.params.setCopyProtectio(jo.optInt("copyProtection"));
            this.params.setAllowTrickmodeFlag(jo.optInt("allowTrickmode"));
            this.params.setStartTime(jo.opt("startTime"));
            this.params.setEndTime(jo.opt("endTime"));
            this.params.setEntryID(jo.optString("entryID"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void playFromStart() {
        LOG.mo8825d("[width]" + this.surfaceView.getWidth());
        LOG.mo8825d("[height" + this.surfaceView.getHeight());
        initSurfaceView(this.surfaceView);
        initMediaPlayer();
        refreshVideoDisplay();
        if (this.mMediaCmdHandler.hasMessages(1)) {
            this.mMediaCmdHandler.removeMessages(1);
        }
        if (this.mPagePlayVideo == null || !this.mPagePlayVideo.isAtBufferPage()) {
            this.mMediaCmdHandler.sendEmptyMessage(1);
        } else {
            this.mMediaCmdHandler.sendEmptyMessage(1);
        }
    }

    public void playByTime(String timestamp) {
        if (this.mediaPlayer != null && isNumeric(timestamp)) {
            playByTime(0, Integer.parseInt(timestamp, 10));
        }
    }

    public void playByTime(int type, int timestamp) {
        playByTime(type, timestamp, 1);
    }

    public void playByTime(int type, int timestamp, int rate) {
        if (this.mMediaCmdHandler != null) {
            this.mMediaCmdHandler.sendMessage(this.mMediaCmdHandler.obtainMessage(5, timestamp, rate));
        }
    }

    public void pause() {
        if (this.mMediaCmdHandler != null) {
            this.mMediaCmdHandler.sendEmptyMessage(2);
        }
    }

    public void resume() {
        if (this.mMediaCmdHandler != null) {
            this.mMediaCmdHandler.sendEmptyMessage(3);
        }
    }

    public void gotoEnd() {
        LOG.mo8825d("called gotoEnd()");
        this.mediaPlayer.seekTo(getMediaDuration());
    }

    public void gotoStart() {
        LOG.mo8825d("called gotoStart()");
        this.mediaPlayer.seekTo(0);
    }

    public synchronized void stop() {
        if (this.mMediaCmdHandler != null) {
            this.mMediaCmdHandler.sendMessage(this.mMediaCmdHandler.obtainMessage(4));
        }
    }

    private void changeSurfaceSize() {
        setVideoDisplayMode(1);
        refreshVideoDisplay();
    }

    public void refreshVideoDisplay() {
        LOG.mo8825d("called refreshVideoDisplay()");
        int videoDisplayMode = this.params.getVideoDisplayMode();
        int width = 0;
        int height = 0;
        int top = 0;
        int left = 0;
        LOG.mo8825d("[videoDisplayMode]" + videoDisplayMode);
        switch (videoDisplayMode) {
            case 0:
                width = this.params.getDisplayArea_width();
                height = this.params.getDisplayArea_height();
                top = this.params.getDisplayArea_top();
                left = this.params.getDisplayArea_left();
                break;
            case 1:
                Display display = ((Activity) this.mContext).getWindowManager().getDefaultDisplay();
                width = display.getWidth();
                height = display.getHeight();
                top = 0;
                left = 0;
                setVideoDisplayArea(0, 0, width, height);
                break;
        }
        updateSurfaceView(width, height, top, left);
    }

    private void initVolume() {
        String track = this.mDbProcessor.getProp("track");
        if (track == null || track.equals(XmlPullParser.NO_NAMESPACE)) {
            track = new StringBuilder(String.valueOf(this.mediaTrackFlag)).toString();
        }
        this.mediaTrackFlag = Integer.parseInt(track, 10);
        String volume = this.mDbProcessor.getProp("volume");
        if (volume == null || volume.equals(XmlPullParser.NO_NAMESPACE)) {
            volume = new StringBuilder(String.valueOf(mediaVolume)).toString();
        }
        mediaVolume = Float.parseFloat(volume);
        setVolume(mediaVolume);
    }

    public void setTrackFlag(int mode) {
        this.mediaTrackFlag = mode;
    }

    public int getTrackFlag() {
        return this.mediaTrackFlag;
    }

    public void changeTrack() {
        switch (getTrack()) {
            case 1:
                this.mediaTrackFlag = 2;
                break;
            case 2:
                this.mediaTrackFlag = 3;
                break;
            case 3:
                this.mediaTrackFlag = 1;
                break;
            default:
                this.mediaTrackFlag = 1;
                break;
        }
        this.mDbProcessor.setProp("track", new StringBuilder(String.valueOf(this.mediaTrackFlag)).toString());
        setTrack(this.mediaTrackFlag);
    }

    public void setMuteFlag(boolean state) {
        this.audioManager.setStreamMute(3, state);
    }

    public void setChannal(int index) {
        if (this.mediaPlayer != null && index == 0) {
        }
    }

    public void mediaVol(String str) {
        this.audioManager.setStreamMute(3, false);
        if ("add".equals(str)) {
            this.audioManager.adjustStreamVolume(3, 1, 1);
        } else {
            this.audioManager.adjustStreamVolume(3, -1, 1);
        }
        mediaVolume = (float) this.audioManager.getStreamVolume(3);
        this.mDbProcessor.setProp("volume", String.valueOf(mediaVolume));
    }

    public void resumeMediaVol() {
    }

    public void setVolume(float volume) {
        mediaVolume = volume;
        this.mDbProcessor.setProp("volume", new StringBuilder(String.valueOf(mediaVolume)).toString());
        int imv = (int) mediaVolume;
        this.audioManager.setStreamVolume(3, imv, imv);
        if (this.mediaPlayer == null) {
            return;
        }
        if (this.mediaTrackFlag == 2) {
            this.mediaPlayer.setVolume(volume, 0.0f);
        } else if (this.mediaTrackFlag == 0) {
            this.mediaPlayer.setVolume(0.0f, volume);
        } else {
            this.mediaPlayer.setVolume(volume, volume);
        }
    }

    public int getMaxVolume() {
        return this.audioManager.getStreamMaxVolume(3);
    }

    public float getVolume() {
        return mediaVolume;
    }

    /* access modifiers changed from: private */
    public void initMediaPlayer() {
        LOG.mo8825d("initMediaPlayer");
        try {
            if (this.mediaPlayer == null) {
                this.mediaPlayer = new MediaPlayer();
                this.mediaPlayer.setDisplay(this.surfaceHolder);
                this.mediaPlayer.setOnBufferingUpdateListener(this.listeners);
                this.mediaPlayer.setOnCompletionListener(this.listeners);
                this.mediaPlayer.setOnPreparedListener(this.listeners);
                this.mediaPlayer.setOnVideoSizeChangedListener(this.listeners);
                this.mediaPlayer.setOnSeekCompleteListener(this.listeners);
                this.mediaPlayer.setOnErrorListener(this.listeners);
                this.mediaPlayer.setOnInfoListener(this.listeners);
                this.mediaPlayer.setAudioStreamType(3);
            }
            refreshVideoDisplay();
            this.isPlaying = false;
            this.isPrepared = false;
            this.isPressPause = false;
            this.isPressResume = false;
            this.isPassStop = false;
        } catch (Exception e) {
            LOG.mo8825d(e.toString());
        }
    }

    private void initSurfaceView(SurfaceView surfaceView2) {
        if (this.surfaceHolder == null) {
            this.surfaceHolder = this.surfaceView.getHolder();
            this.surfaceHolder.addCallback(this.listeners);
            this.surfaceHolder.setType(3);
            this.surfaceHolder.setFormat(3);
        }
    }

    private void updateSurfaceView(int width, int height, int top, int left) {
        if (this.surfaceView != null) {
            Map<String, Integer> area = getVideoArea(top, left, width, height);
            Message msg = new Message();
            if (this.params.getVideoDisplayMode() == 1) {
                msg.what = 0;
            } else if (this.params.getVideoDisplayMode() == 0) {
                msg.what = 1;
            }
            Bundle bun = new Bundle();
            bun.putInt("width", ((Integer) area.get("width")).intValue());
            bun.putInt("height", ((Integer) area.get("height")).intValue());
            bun.putInt("top", ((Integer) area.get("top")).intValue());
            bun.putInt("left", ((Integer) area.get("left")).intValue());
            msg.setData(bun);
            this.svHandler.sendMessage(msg);
            return;
        }
        LOG.mo8825d("SurfaceView NULL");
    }

    public SurfaceView getSurfaceView() {
        return this.surfaceView;
    }

    public void setSurfaceView(SurfaceView surfaceView2) {
        this.surfaceView = surfaceView2;
        initSurfaceView(surfaceView2);
    }

    public boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }

    public String getMediaStatus() {
        String str = "0";
        switch (this.params.getNowPlayMode()) {
            case 0:
                return "0";
            case 1:
                return "1";
            case 2:
                return "2";
            default:
                return "0";
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:7:0x0017  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Map<java.lang.String, java.lang.Integer> getVideoArea(int r10, int r11, int r12, int r13) {
        /*
            r9 = this;
            r0 = 0
            java.lang.String r2 = "0"
            java.util.HashMap r1 = new java.util.HashMap     // Catch:{ Exception -> 0x004f }
            r1.<init>()     // Catch:{ Exception -> 0x004f }
            android.content.Context r7 = r9.mContext     // Catch:{ Exception -> 0x0079 }
            android.content.ContentResolver r7 = r7.getContentResolver()     // Catch:{ Exception -> 0x0079 }
            java.lang.String r8 = "content_mode"
            java.lang.String r2 = android.provider.Settings.Secure.getString(r7, r8)     // Catch:{ Exception -> 0x0079 }
            r0 = r1
        L_0x0015:
            if (r2 == 0) goto L_0x0054
            java.lang.String r7 = "1"
            boolean r7 = r2.equals(r7)
            if (r7 == 0) goto L_0x0054
            int r7 = r13 * 4
            int r6 = r7 / 3
            r5 = r13
            int r7 = r12 - r6
            int r4 = r7 / 2
            java.lang.String r7 = "top"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r10)
            r0.put(r7, r8)
            java.lang.String r7 = "left"
            int r8 = r11 + r4
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)
            r0.put(r7, r8)
            java.lang.String r7 = "width"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r6)
            r0.put(r7, r8)
            java.lang.String r7 = "height"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r5)
            r0.put(r7, r8)
        L_0x004e:
            return r0
        L_0x004f:
            r3 = move-exception
        L_0x0050:
            r3.printStackTrace()
            goto L_0x0015
        L_0x0054:
            java.lang.String r7 = "top"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r10)
            r0.put(r7, r8)
            java.lang.String r7 = "left"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r11)
            r0.put(r7, r8)
            java.lang.String r7 = "width"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r12)
            r0.put(r7, r8)
            java.lang.String r7 = "height"
            java.lang.Integer r8 = java.lang.Integer.valueOf(r13)
            r0.put(r7, r8)
            goto L_0x004e
        L_0x0079:
            r3 = move-exception
            r0 = r1
            goto L_0x0050
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.nettv.media.JSExtMediaPlayer.getVideoArea(int, int, int, int):java.util.Map");
    }

    private int callNative(Parcel request, Parcel reply) {
        try {
            Integer num = (Integer) MediaPlayer.class.getMethod("invoke", new Class[]{Parcel.class, Parcel.class}).invoke(this.mediaPlayer, new Object[]{request, reply});
            return reply.readInt();
        } catch (Exception e) {
            LOG.mo8826e(e.toString());
            return -1;
        }
    }

    private int getTrack() {
        String track = this.mDbProcessor.getProp("track");
        if (!StringUtils.isNotEmpty(track) || !StringUtils.isNumberStr(track)) {
            return -1;
        }
        return Integer.valueOf(track).intValue();
    }

    private void setTrack(int track) {
        Parcel request = Parcel.obtain();
        request.writeInterfaceToken("android.media.IMediaPlayer");
        if (SystemProperties.get("ro.sw.chip.manufacturer", "Hisilicon").equals("Hisilicon")) {
            request.writeInt(5006);
            switch (track) {
                case 1:
                    request.writeInt(2);
                    break;
                case 2:
                    break;
                default:
                    request.writeInt(1);
                    break;
            }
            request.writeInt(3);
        } else {
            request.writeInt(128);
            switch (track) {
                case 1:
                    request.writeInt(2);
                    break;
                case 2:
                    break;
                default:
                    request.writeInt(3);
                    break;
            }
            request.writeInt(1);
        }
        Parcel reply = Parcel.obtain();
        callNative(request, reply);
        request.recycle();
        reply.recycle();
    }
}
