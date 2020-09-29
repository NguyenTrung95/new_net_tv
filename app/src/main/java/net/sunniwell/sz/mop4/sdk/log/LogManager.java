package net.sunniwell.sz.mop4.sdk.log;

import android.content.Context;
import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.sunniwell.sz.mop4.sdk.log.LogBean.action_extend;
import net.sunniwell.sz.mop4.sdk.log.LogBean.exception_extend;
import net.sunniwell.sz.mop4.sdk.log.LogBean.statistics_extend;
import net.sunniwell.sz.mop4.sdk.media.MediaBean;
import net.sunniwell.sz.mop4.sdk.ad.AdBean;
import net.sunniwell.sz.mop4.sdk.param.Parameter;
import net.sunniwell.sz.mop4.sdk.util.Tools;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.LogManager */
public class LogManager {
    private static final boolean BAIDU_ENABLE = true;
    private static final boolean MOP_ENABLE = true;
    private static ExecutorService executor;
    private static LogManager manager = new LogManager();
    private Context appContext = null;

    public static LogManager getInstance() {
        executor = Executors.newSingleThreadExecutor();
        return manager;
    }

    public void init(Context appContext2, boolean forSTB) {
        this.appContext = appContext2.getApplicationContext();
        initBaiduStat(forSTB);
    }

    private void initBaiduStat(boolean forTv) {
        StatService.setAppChannel(this.appContext, Parameter.get(LogBean1.TERMINAL_STATISTICS_CHANNEL), true);
        StatService.setSendLogStrategy(this.appContext, SendStrategyEnum.APP_START, 2, false);
        StatService.setForTv(this.appContext, forTv);
    }

    public void sendLog(LogBean logBean) {
        if (logBean != null) {
            sendBaiDuLog(logBean);
            sendMopLog(logBean);
        }
    }

    private void sendMopLog(LogBean logBean) {
        if (logBean != null) {
            final String body = LogUtil.splitMessageBody(logBean);
            executor.execute(new Runnable() {
                public void run() {
                    LogUtil.sendlog(body);
                }
            });
        }
    }

    private void sendBaiDuLog(LogBean logBean) {
        int i = BaiduStatLable.MEDIABEAN_META_EPG;
        if (logBean != null) {
            switch (logBean.getSubtype()) {
                case 2:
                    if (exception_extend.play.toString().equals(logBean.getExtend())) {
                        StatService.onEvent(this.appContext, BaiduStatEventId.MEDIAPLAY_ERROR, logBean.getName());
                        return;
                    }
                    return;
                case 4:
                    if (action_extend.play.toString().equals(logBean.getExtend())) {
                        Context context = this.appContext;
                        String str = BaiduStatEventId.MEDIAPLAY_META_COUNT;
                        if (!logBean.isEpg()) {
                            i = logBean.getMeta();
                        }
                        StatService.onEvent(context, str, BaiduStatLable.getMediaMeta(i));
                        StatService.onEvent(this.appContext, BaiduStatEventId.MEDIAPLAY_COUNT, logBean.getName() + (logBean.isEpg() ? BaiduStatLable.EPG_SUFFIX : XmlPullParser.NO_NAMESPACE));
                        return;
                    }
                    return;
                case 7:
                    if (statistics_extend.ad.toString().equals(logBean.getExtend())) {
                        StatService.onEvent(this.appContext, BaiduStatEventId.AD_PLAY_COUNT, logBean.getName());
                        return;
                    }
                    StatService.onEventDuration(this.appContext, BaiduStatEventId.MEDIAPLAY_TIME, logBean.getName() + (logBean.isEpg() ? BaiduStatLable.EPG_SUFFIX : XmlPullParser.NO_NAMESPACE), logBean.getEnd_utc() - logBean.getStart_utc());
                    Context context2 = this.appContext;
                    String str2 = BaiduStatEventId.MEDIAPLAY_META_TIME;
                    if (!logBean.isEpg()) {
                        i = logBean.getMeta();
                    }
                    StatService.onEventDuration(context2, str2, BaiduStatLable.getMediaMeta(i), logBean.getEnd_utc() - logBean.getStart_utc());
                    return;
                default:
                    return;
            }
        }
    }

    private LogBean getLogBeanByMediaBean(MediaBean mediaBean) {
        LogBean logBean = new LogBean();
        logBean.setName(Tools.parseNull(mediaBean.getTitle()));
        logBean.setMedia_id(mediaBean.getId());
        logBean.setMeta(mediaBean.getMeta());
        return logBean;
    }

    public void sendLogPlayStart(MediaBean mediaBean, String url, boolean isEpg) {
        if (mediaBean != null && mediaBean.getMeta() >= 0) {
            LogBean logBean = getLogBeanByMediaBean(mediaBean);
            logBean.setSubtype(4);
            logBean.setExtend(action_extend.play.toString());
            logBean.setUrl(url);
            logBean.setEpg(isEpg);
            sendLog(logBean);
        }
    }

    public void sendLogPlayStop(MediaBean mediaBean, String url) {
        if (mediaBean != null && mediaBean.getMeta() >= 0) {
            LogBean logBean = getLogBeanByMediaBean(mediaBean);
            logBean.setSubtype(4);
            logBean.setExtend(action_extend.stop.toString());
            logBean.setUrl(url);
            sendLog(logBean);
        }
    }

    public void sendLogPlayPauseResume(MediaBean mediaBean, String url, boolean isPause) {
        if (mediaBean != null && mediaBean.getMeta() >= 0) {
            LogBean logBean = getLogBeanByMediaBean(mediaBean);
            logBean.setSubtype(4);
            if (isPause) {
                logBean.setExtend(action_extend.pause.toString());
            } else {
                logBean.setExtend(action_extend.resume.toString());
            }
            logBean.setUrl(url);
            sendLog(logBean);
        }
    }

    public void sendLogPlayError(MediaBean mediaBean, String url, long serial) {
        if (mediaBean != null && mediaBean.getMeta() >= 0) {
            LogBean logBean = getLogBeanByMediaBean(mediaBean);
            logBean.setSubtype(2);
            logBean.setExtend(exception_extend.play.toString());
            logBean.setUrl(url);
            switch (mediaBean.getMeta()) {
                case 2:
                case 4:
                    logBean.setSerial(serial);
                    break;
            }
            sendLog(logBean);
        }
    }

    public void sendLogPlayStatistics(MediaBean mediaBean, String url, long utcMsStart, long durationMs, long curPosMs, long serial) {
        if (mediaBean != null && mediaBean.getMeta() >= 0) {
            LogBean logBean = getLogBeanByMediaBean(mediaBean);
            logBean.setUrl(url);
            logBean.setStart_utc(utcMsStart);
            logBean.setEnd_utc(System.currentTimeMillis());
            logBean.setSubtype(7);
            switch (mediaBean.getMeta()) {
                case 0:
                case 5:
                    logBean.setExtend(statistics_extend.channel.toString());
                    break;
                case 1:
                case 3:
                case 6:
                    break;
                case 2:
                case 4:
                    logBean.setSerial(serial);
                    break;
                case 7:
                    logBean.setExtend(statistics_extend.lunbo.toString());
                    break;
            }
            logBean.setDuration_total((int) (durationMs / 1000));
            logBean.setDuration_watch((int) (curPosMs / 1000));
            if (logBean.getDuration_total() > 0) {
                logBean.setDuration_percent((logBean.getDuration_watch() * 100) / logBean.getDuration_total());
            }
            logBean.setExtend(statistics_extend.vod.toString());
            sendLog(logBean);
        }
    }

    public void sendLogAd(AdBean adBean) {
        if (adBean != null) {
            LogBean logBean = new LogBean();
            logBean.setSubtype(7);
            logBean.setExtend(statistics_extend.ad.toString());
            logBean.setName(Tools.parseNull(adBean.getTitle()));
            logBean.setMeta(adBean.getMeta());
            sendLog(logBean);
        }
    }
}
