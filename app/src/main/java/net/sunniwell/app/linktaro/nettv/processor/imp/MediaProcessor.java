package net.sunniwell.app.linktaro.nettv.processor.imp;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings.Secure;
import android.view.SurfaceView;
import java.util.regex.Pattern;
import net.sunniwell.app.linktaro.nettv.Constants.PagePlayVideoConstants;
import net.sunniwell.app.linktaro.nettv.content.PagePlayVideo;
import net.sunniwell.app.linktaro.nettv.media.JSExtMediaPlayer;
import net.sunniwell.app.linktaro.nettv.db.VodRecordDbHelper;
import net.sunniwell.app.linktaro.nettv.processor.PropProcessor;
import net.sunniwell.app.linktaro.tools.SWUtil;
import net.sunniwell.common.log.SWLogger;
import org.xmlpull.p019v1.XmlPullParser;

public class MediaProcessor implements PropProcessor {
    private static final SWLogger LOG = SWLogger.getLogger(MediaProcessor.class);
    private static final String[] PARAMS = {"media_cmd_play", "media_cmd_pause", "media_cmd_resume", "media_cmd_stop", "media_cmd_video_pig", PagePlayVideoConstants.FULL_SCREEN, "media_status", "media_data_time", VodRecordDbHelper.MEDIA_DURATION, "volume", "vol_inc", "vol_dec", "media_cmd_seek"};
    private Context mContext;
    private JSExtMediaPlayer media;

    public JSExtMediaPlayer getMedia() {
        return this.media;
    }

    public void setMedia(JSExtMediaPlayer media2) {
        this.media = media2;
    }

    public MediaProcessor(Context context, SurfaceView surfaceView, PagePlayVideo playPage) {
        this.mContext = context;
        this.media = new JSExtMediaPlayer(this.mContext, surfaceView, playPage);
    }

    public boolean isSupport(String prop) {
        return SWUtil.isContain(PARAMS, prop);
    }

    public String getProp(String name) {
        String ret = XmlPullParser.NO_NAMESPACE;
        if (name.equals("media_status")) {
            return this.media.getMediaStatus();
        }
        if (name.equals("currentplaytime")) {
            return new StringBuilder(String.valueOf(this.media.getCurrentPlayTime())).toString();
        }
        if (name.equals(VodRecordDbHelper.MEDIA_DURATION)) {
            return new StringBuilder(String.valueOf(this.media.getMediaDuration())).toString();
        }
        if (name.equals("volume")) {
            return new StringBuilder(String.valueOf(this.media.getVolume())).toString();
        }
        if (name.equals("prepared")) {
            return this.media.getPrepared() ? "true" : "false";
        }
        return ret;
    }

    public String setProp(String name, String value) {
        LOG.mo8825d("--setProp-----name=" + name);
        if (name.equals("media_cmd_play")) {
            mediaPlay(name, value);
        } else if (name.equals("media_cmd_pause")) {
            this.media.pause();
        } else if (name.equals("media_cmd_resume")) {
            this.media.resume();
        } else if (name.equals("media_cmd_stop")) {
            Secure.putInt(this.mContext.getContentResolver(), "pageInx", 2);
            Intent intent = new Intent("net.sunniwell.MarqueeTextView_PAGE_CHANGED");
            intent.putExtra("pageInx", 2);
            this.mContext.sendBroadcast(intent);
            if (this.media != null) {
                this.media.stop();
            }
        } else if (name.equals(PagePlayVideoConstants.LITTLE_SCREEN)) {
            Secure.putInt(this.mContext.getContentResolver(), "pageInx", 2);
            Intent intent2 = new Intent("net.sunniwell.MarqueeTextView_PAGE_CHANGED");
            intent2.putExtra("pageInx", 2);
            this.mContext.sendBroadcast(intent2);
            String[] strArray = value.split(",");
            int left = 0;
            int top = 0;
            int width = 0;
            int height = 0;
            if (strArray.length == 4) {
                if (isNumeric(strArray[0])) {
                    left = Integer.parseInt(strArray[0]);
                }
                if (isNumeric(strArray[1])) {
                    top = Integer.parseInt(strArray[1]);
                }
                if (isNumeric(strArray[2])) {
                    width = Integer.parseInt(strArray[2]);
                }
                if (isNumeric(strArray[3])) {
                    height = Integer.parseInt(strArray[3]);
                }
            }
            this.media.setVideoDisplayMode(0);
            this.media.setVideoDisplayArea(left, top, width, height);
            this.media.refreshVideoDisplay();
        } else if (name.equals(PagePlayVideoConstants.FULL_SCREEN)) {
            Secure.putInt(this.mContext.getContentResolver(), "pageInx", 1);
            Intent intent3 = new Intent("net.sunniwell.MarqueeTextView_PAGE_CHANGED");
            intent3.putExtra("pageInx", 1);
            this.mContext.sendBroadcast(intent3);
            if (this.media.getVideoDisplayMode() == 0) {
                this.media.setVideoDisplayMode(1);
                this.media.refreshVideoDisplay();
            }
        } else if (name.equals("media.track")) {
            this.media.changeTrack();
        } else if (name.equals("vol_inc")) {
            this.media.mediaVol("add");
        } else if (name.equals("vol_dec")) {
            this.media.mediaVol("dec");
        } else if (name.equals("media_cmd_seek")) {
            this.media.playByTime(value);
        }
        return null;
    }

    private void mediaPlay(String name, String value) {
        String pUrl = value;
        LOG.mo8825d(".....mediaPlay()..URL=" + pUrl);
        int mode = this.media.getVideoDisplayMode();
        int left = this.media.getVideoDisplayLeft();
        int top = this.media.getVideoDisplayTop();
        int width = this.media.getVideoDisplayWidth();
        LOG.mo8825d(".....mediaPlay()...mode=" + mode + "..left.." + left + "...top=" + top + "..width=" + width + "...height=" + this.media.getVideoDisplayHeight());
        this.media.setSingleMedia("[{mediaURL:'" + pUrl + "',mediaCode:'code1',mediaType:2,entryID:'entry1'}]");
        this.media.playFromStart();
    }

    public boolean isNumeric(String str) {
        return Pattern.compile("[0-9]*").matcher(str).matches();
    }
}
