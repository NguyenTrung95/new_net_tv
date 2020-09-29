package net.sunniwell.app.linktaro.nettv.content;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.provider.Settings.Secure;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import net.sunniwell.aidl.SDKRemoteManager;
import net.sunniwell.aidl.bean.EPGBean;
import net.sunniwell.aidl.bean.MediaBean;
import net.sunniwell.aidl.bean.UrlBean;
import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.Constants.PagePlayVideoConstants;
import net.sunniwell.app.linktaro.nettv.bean.PagePlayVideoBean;
import net.sunniwell.app.linktaro.nettv.db.VodRecordDbHelper;
import net.sunniwell.app.linktaro.nettv.download.DownUtils;
import net.sunniwell.app.linktaro.nettv.download.DownloadTask;
import net.sunniwell.app.linktaro.nettv.entry.ErrorPageActivity;
import net.sunniwell.app.linktaro.nettv.entry.NettvActivity;
import net.sunniwell.app.linktaro.nettv.manager.DataManager;
import net.sunniwell.app.linktaro.nettv.processor.imp.DBProcessor;
import net.sunniwell.app.linktaro.nettv.processor.imp.MediaProcessor;
import net.sunniwell.app.linktaro.nettv.view.CustomResDialog;
import net.sunniwell.app.linktaro.nettv.view.ProgressHintDelegate.SeekBarHintAdapter;
import net.sunniwell.app.linktaro.nettv.view.ProgressHintDelegate.SeekBarHintDelegateHolder;
import net.sunniwell.app.linktaro.tools.DateTimeUtil;
import net.sunniwell.app.linktaro.tools.JsonUtil;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.app.linktaro.tools.SharedPreUtil;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.common.tools.DateTime;

import org.apache.http.HttpStatus;
import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

public class PagePlayVideo extends PageBase {
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(PagePlayVideo.class);
    public static boolean isShowingLiveList = false;
    public static VodRecordDbHelper mVodDBHelper;
    /* access modifiers changed from: private */
    public PagePlayCallBackListener backListener;
    /* access modifiers changed from: private */
    public int curPos = 0;
    private String dateStr;
    /* access modifiers changed from: private */
    public long duration;
    private int inerval;
    /* access modifiers changed from: private */
    public boolean isAtBufferPage;
    private boolean isAtPlaySeekPage;
    /* access modifiers changed from: private */
    public boolean isAtPzqbPage;
    /* access modifiers changed from: private */
    public boolean isFullPlay;
    private boolean isPassed;
    private boolean isPlayFromSeek;
    /* access modifiers changed from: private */
    public boolean isPlaying;
    /* access modifiers changed from: private */
    public boolean isResume = true;
    /* access modifiers changed from: private */
    public boolean isSeeked;
    /* access modifiers changed from: private */
    public boolean isStopSeek = true;
    private ImageView mAdBufferView;
    private List<String> mBufferImgUrlList;
    private FrameLayout mBufferPageLayout;
    private Calendar mCalendar;
    private ImageView mChanIconView;
    private TextView mChanNumView;
    private int mClickPage;
    /* access modifiers changed from: private */
    public View mContentView;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public MediaBean mCurChannel;
    /* access modifiers changed from: private */
    public EPGBean mCurLiveProgram;
    /* access modifiers changed from: private */
    public long mCurPlayTime;
    private String mCurPlayType;
    /* access modifiers changed from: private */
    public CustomResDialog mCustomResDialog;
    private DBProcessor mDBProcessor;
    private DataManager mDataManager;
    private TextView mDateofTsView;
    private HandlerThread mDownThread;
    /* access modifiers changed from: private */
    public DownUtils mDownUtils;
    private TextView mFNewNameView;
    private TextView mFilNameView;
    private TextView mFileDateView;
    /* access modifiers changed from: private */
    public TextView mFileDesView;
    private ImageView mFileIconView;
    private TextView mFileIndexView;
    private TextView mFileNameView;
    private TextView mFilmNameView;
    private FrameLayout mFlPlayPageRoot;
    Runnable mFreshProgressBar = new Runnable() {
        public void run() {
            while (PagePlayVideo.this.isStopSeek) {
                try {
                    PagePlayVideo pagePlayVideo = PagePlayVideo.this;
                    pagePlayVideo.timeTemp = pagePlayVideo.timeTemp + 1;
                    PagePlayVideo.LOG.mo8825d("[curPos]" + PagePlayVideo.this.curPos + "[timeTemp]" + PagePlayVideo.this.timeTemp);
                    Thread.sleep(1000);
                    if (PagePlayVideo.this.timeTemp > 5) {
                        PagePlayVideo.this.isStopSeek = false;
                        PagePlayVideo.this.mUiHandler.sendEmptyMessage(13);
                    } else if (!PagePlayVideo.this.isSeeked) {
                        PagePlayVideo.this.mUiHandler.sendEmptyMessage(12);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    private LinearLayout mInfoLayout;
    private boolean mIsOversea;
    private ImageView mIvDialog;
    private ImageView mIvPageQuality;
    private ImageView mIvPzqbTip;
    private ImageView mIvQualityInfo;
    private ImageView mIvQulity;
    /* access modifiers changed from: private */
    public ImageView mIvReviewPrompt;
    /* access modifiers changed from: private */
    public NettvActivity mMainApp;
    /* access modifiers changed from: private */
    public MediaProcessor mMediaProcessor;
    private TextView mNwNameView;
    private TextView mNwsNameView;
    private LinearLayout mPlayLayout;
    /* access modifiers changed from: private */
    public long mPlayTime;
    private String mPlayUrl;
    private PagePlayVideoBean mPlayVideoBean;
    private SurfaceView mPreview;
    /* access modifiers changed from: private */
    public LinearLayout mPzqbPage;
    private SDKRemoteManager mSdkRemoteManager;
    private SeekBarHintDelegateHolder mSeekBar;
    private SharedPreUtil mSharedPreUtil;
    private long mStartTime = -1;
    private String mTerminalid;
    private TextView mTimeofTsView;
    private TextView mTotoalTimeView;
    private ImageView mTrackView;
    private TextView mTvCurtimeInfo;
    private TextView mTvNowTime;
    private TextView mTvPzqbTime;
    /* access modifiers changed from: private */
    public UiHandler mUiHandler;
    /* access modifiers changed from: private */
    public WorkHandler mWorkHandler;
    private Random mrRandom;
    private int seekPoint;
    /* access modifiers changed from: private */
    public int timeTemp;

    public interface PagePlayCallBackListener {
        void playPageCallBack(int i, Message message);
    }

    @SuppressLint({"HandlerLeak"})
    public class UiHandler extends Handler {
        public UiHandler() {
        }

        public void handleMessage(Message msg) {
            PagePlayVideo.LOG.mo8825d("[msg]" + msg.what);
            switch (msg.what) {
                case 0:
                    PagePlayVideo.this.showSurfacePage();
                    break;
                case 2:
                    PagePlayVideo.LOG.mo8825d("[PlayTime]" + PagePlayVideo.this.mPlayTime);
                    PagePlayVideo.this.mUiHandler.sendMessageDelayed(PagePlayVideo.this.mUiHandler.obtainMessage(3), DateTime.MILLIS_PER_MINUTE);
                    break;
                case 3:
                    if (!PagePlayVideo.this.isPlaying) {
                        PagePlayVideo.this.mPlayTime = 0;
                        break;
                    } else {
                        PagePlayVideo pagePlayVideo = PagePlayVideo.this;
                        pagePlayVideo.mPlayTime = pagePlayVideo.mPlayTime + DateTime.MILLIS_PER_MINUTE;
                        if (PagePlayVideo.this.mPlayTime >= PagePlayVideoConstants.PLAY_MAX_TIME) {
                            if (PagePlayVideo.this.mMediaProcessor != null && (PagePlayVideo.this.isPlaying || PagePlayVideo.this.isAtBufferPage)) {
                                PagePlayVideo.LOG.mo8825d("[stopMediaPlay]");
                                PagePlayVideo.this.stopMediaPlay();
                                break;
                            }
                        } else {
                            PagePlayVideo.this.mUiHandler.sendMessage(PagePlayVideo.this.mUiHandler.obtainMessage(2));
                            break;
                        }
                    }
                case 4:
                    PagePlayVideo.this.isAtPzqbPage = true;
                    PagePlayVideo.this.mPzqbPage.setVisibility(0);
                    PagePlayVideo.this.mFileDesView.requestFocus();
                    break;
                case 5:
                    PagePlayVideo.this.hidePzqbPage();
                    break;
                case 6:
                    PagePlayVideo.this.initPzqbPage();
                    break;
                case 8:
                    PagePlayVideo.this.hideBufferAdPage();
                    break;
                case 9:
                    PagePlayVideo.this.showInfoPage();
                    break;
                case 10:
                    PagePlayVideo.this.hideInfoPage();
                    break;
                case 12:
                    PagePlayVideo.this.freshPlaySeekPage();
                    break;
                case 13:
                    PagePlayVideo.this.hidePlaySeekPage();
                    if (PagePlayVideo.this.isResume) {
                        PagePlayVideo.this.mIvReviewPrompt.setVisibility(4);
                        break;
                    }
                    break;
                case 14:
                    PagePlayVideo.this.showPlaySeekPage();
                    break;
                case 15:
                    PagePlayVideo.this.hideTrackView();
                    break;
                case 16:
                    PagePlayVideo.this.showTrackView();
                    if (PagePlayVideo.this.mUiHandler.hasMessages(15)) {
                        PagePlayVideo.this.mUiHandler.removeMessages(15);
                    }
                    PagePlayVideo.this.mUiHandler.sendEmptyMessageDelayed(15, 1500);
                    break;
                case 17:
                    PagePlayVideo.this.initInfoPage();
                    break;
                case 18:
                    PagePlayVideo.LOG.mo8825d("[CHANGE_SURFACE_TO_EPG_SCREEN_SIZE]");
                    PagePlayVideo.this.mUiHandler.removeMessages(10);
                    PagePlayVideo.this.mUiHandler.sendEmptyMessage(10);
                    PagePlayVideo.this.mMediaProcessor.setProp(PagePlayVideoConstants.LITTLE_SCREEN, "0,132,760,405");
                    break;
                case 19:
                    PagePlayVideo.LOG.mo8825d("[NOTIC_VEDIO_TO_THE_END]");
                    PagePlayVideo.this.stopMediaPlay();
                    PagePlayVideo.this.onStop();
                    if (PagePlayVideo.this.mMainApp.isFullPlay()) {
                        PagePlayVideo.this.backListener.playPageCallBack(1, null);
                    }
                    PagePlayVideo.this.deleteData();
                    break;
                case 20:
                    PagePlayVideo.LOG.mo8825d("CHANGE_SURFACE_TO_RESERVATION_HINT_SCREEN_SIZE--------->");
                    PagePlayVideo.this.mMediaProcessor.setProp(PagePlayVideoConstants.LITTLE_SCREEN, PagePlayVideoConstants.RESERVATION_HINT_PAGE_SCREEN_LETTLE_SIZE);
                    break;
                case 21:
                    if (PagePlayVideo.this.mContentView != null) {
                        PagePlayVideo.this.mContentView.requestFocus();
                        break;
                    }
                    break;
                case 23:
                    PagePlayVideo.this.gotoPlayPage(msg);
                    break;
                case 24:
                    PagePlayVideo.LOG.mo8825d("[show reservation dialog]");
                    if (PagePlayVideo.this.mCustomResDialog == null || !PagePlayVideo.this.mCustomResDialog.isShowing()) {
                        PagePlayVideo.this.mCustomResDialog = new CustomResDialog(PagePlayVideo.this.mContext, R.style.PromptDialog);
                        PagePlayVideo.this.mCustomResDialog.setDialogBg(R.drawable.ts_bg_trans);
                        PagePlayVideo.this.mCustomResDialog.setOkBgResource(R.drawable.btn_check_ok_sel);
                        PagePlayVideo.this.mCustomResDialog.setCancelBgResource(R.drawable.btn_check_cancel_sel);
                        PagePlayVideo.this.mCustomResDialog.setTextContent(PagePlayVideo.this.getResources().getText(R.string.first_reservation).toString());
                        PagePlayVideo.this.mCustomResDialog.setOkClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                PagePlayVideo.this.mCustomResDialog.dismiss();
                                if (PagePlayVideo.this.mDownUtils.isDownTaskMAX()) {
                                    PagePlayVideo.this.backListener.playPageCallBack(13, null);
                                } else {
                                    PagePlayVideo.this.mWorkHandler.sendEmptyMessage(6);
                                }
                            }
                        });
                        PagePlayVideo.this.mCustomResDialog.setCancelClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                PagePlayVideo.this.mCustomResDialog.dismiss();
                            }
                        });
                        PagePlayVideo.this.mCustomResDialog.show();
                        break;
                    } else {
                        return;
                    }
                case 25:
                    PagePlayVideo.this.onStop();
                    PagePlayVideo.this.isFullPlay = false;
                    PagePlayVideo.this.isAtBufferPage = false;
                    PagePlayVideo.this.mWorkHandler.removeMessages(2);
                    PagePlayVideo.this.backListener.playPageCallBack(1, null);
                    break;
                case 26:
                    String currentPlaytime = (String) msg.obj;
                    PagePlayVideo.LOG.mo8825d("[currentPlaytime]" + currentPlaytime);
                    PagePlayVideo.this.mMediaProcessor.setProp("media_cmd_seek", currentPlaytime);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    @SuppressLint({"HandlerLeak"})
    public class WorkHandler extends Handler {
        @SuppressLint({"HandlerLeak"})
        public WorkHandler(Looper mLooper) {
            super(mLooper);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 2:
                    PagePlayVideo.this.checkConnetTime();
                    return;
                case 4:
                    PagePlayVideo.LOG.mo8825d("[CMD_STOP_MEDIA]");
                    PagePlayVideo.this.stopMediaPlay();
                    return;
                case 5:
                    PagePlayVideo.this.authen((String) msg.obj);
                    return;
                case 6:
                    PagePlayVideo.this.addDownTaskToList(PagePlayVideo.this.mCurChannel, PagePlayVideo.this.mCurLiveProgram);
                    return;
                case 7:
                    PagePlayVideo.this.checkDownTask(PagePlayVideo.this.mCurChannel, PagePlayVideo.this.mCurLiveProgram);
                    return;
                case 9:
                    PagePlayVideo.LOG.mo8825d("[RECORD_PLAY_TIME]");
                    PagePlayVideo.this.recordPlayTime();
                    return;
                case 10:
                    String playTime = (String) msg.obj;
                    Message seekMsg = Message.obtain();
                    seekMsg.what = 26;
                    seekMsg.obj = playTime;
                    PagePlayVideo.this.mUiHandler.sendMessage(seekMsg);
                    return;
                default:
                    return;
            }
        }
    }

    public View getmContentView() {
        return this.mContentView;
    }

    public void setmContentView(View mContentView2) {
        this.mContentView = mContentView2;
    }

    public boolean isFullPlay() {
        return this.isFullPlay;
    }

    public void setFullPlay(boolean isFullPlay2) {
        this.isFullPlay = isFullPlay2;
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void setPlaying(boolean isPlaying2) {
        this.isPlaying = isPlaying2;
    }

    public SurfaceView getSurfaceView() {
        LOG.mo8825d("[getSurfaceView]" + this.mPreview);
        if (this.mPreview == null) {
            this.mPreview = (SurfaceView) this.mContentView.findViewById(R.id.surface);
        }
        return this.mPreview;
    }

    public boolean isPlayFromSeek() {
        return this.isPlayFromSeek;
    }

    public int getSeekPoint() {
        return this.seekPoint;
    }

    public PagePlayVideo(Context context, PagePlayCallBackListener backListener2) {
        super(context);
        setPagePlayCallBackListener(backListener2);
    }

    public void onCreate(Context context) {
        LOG.mo8825d("[onCreate]");
        this.mContentView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.page_play_vedio, this);
        this.mUiHandler = new UiHandler();
        this.mSdkRemoteManager = SDKRemoteManager.getInstance(context, null);
        this.mContext = context;
        this.mDownUtils = DownUtils.getDownUtilsInstance(context);
        this.mDownUtils.registReceiver();
        initUi();
        this.mMainApp = (NettvActivity) context;
        this.mrRandom = new Random();
        this.mSharedPreUtil = SharedPreUtil.getSharedPreUtil(context);
        this.mDBProcessor = DBProcessor.getDBProcessor(context);
        this.mDownThread = new HandlerThread("WorkThread");
        this.mDownThread.start();
        this.mWorkHandler = new WorkHandler(this.mDownThread.getLooper());
        this.mMediaProcessor = new MediaProcessor(context, this.mPreview, this);
        this.mCalendar = Calendar.getInstance();
        this.mDataManager = DataManager.getInstance(context);
        this.mTerminalid = SystemProperties.get("ro.serialno");
        mVodDBHelper = NettvActivity.vodDBHelper;
        this.mPlayVideoBean = new PagePlayVideoBean();
    }

    public void onResume() {
        LOG.mo8825d("[onResume]");
        this.mContentView.setVisibility(0);
        this.mWorkHandler.removeMessages(4);
    }

    public void onStop() {
        LOG.mo8825d("[onStop]isAtPzqbPage " + this.isAtPzqbPage);
        LOG.mo8825d("[onStop]isAtPlaySeekPage " + this.isAtPlaySeekPage);
        LOG.mo8825d("[onStop]isAtBufferPage " + this.isAtBufferPage);
        this.mUiHandler.sendEmptyMessage(22);
        this.mContentView.setVisibility(4);
        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(15));
        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(10));
        if (this.isAtPzqbPage) {
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(5));
        }
        if (this.isAtPlaySeekPage) {
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(13));
        }
        if (this.isAtBufferPage) {
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(8));
        }
    }

    public void onDestroy() {
        LOG.mo8825d("[onDestory]");
        if (this.mDownUtils != null) {
            this.mDownUtils.unregisterReceiver();
        }
    }

    public void doByMessage(Message data, int MessageFlag) {
        LOG.mo8825d("[doByMessage]" + MessageFlag);
        switch (MessageFlag) {
            case 0:
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(21));
                this.mUiHandler.sendEmptyMessage(22);
                notifyShowBufferPage();
                this.mCurPlayType = data.getData().getString(MailDbHelper.TYPE);
                this.mPlayVideoBean.setCurPlayType(this.mCurPlayType);
                LOG.mo8825d(".......curPlayType......" + this.mCurPlayType);
                this.mClickPage = data.getData().getInt("clickPage");
                if (this.mCurPlayType.equals("0")) {
                    String playDate = data.getData().getString("playDate");
                    LOG.mo8825d("playDate----->" + playDate);
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(playDate);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    this.mCalendar.setTime(date);
                } else if (this.mCurPlayType.equals("1")) {
                    this.mCalendar.setTimeInMillis(System.currentTimeMillis());
                    if (this.mClickPage != 1) {
                    }
                } else if (this.mCurPlayType.equals("3")) {
                    String playDate2 = data.getData().getString("playDate");
                    LOG.mo8825d("playDate----->" + playDate2);
                    Date date2 = null;
                    try {
                        date2 = new SimpleDateFormat("yyyy-MM-dd").parse(playDate2);
                    } catch (ParseException e2) {
                        e2.printStackTrace();
                    }
                    this.mCalendar.setTime(date2);
                }
                for (Entry<MediaBean, EPGBean> entry : ((HashMap) data.obj).entrySet()) {
                    this.mCurChannel = (MediaBean) entry.getKey();
                    this.mCurLiveProgram = (EPGBean) entry.getValue();
                }
                LOG.mo8825d("[mCurChannel]" + this.mCurChannel);
                LOG.mo8825d("[mCurLiveProgram]" + this.mCurLiveProgram);
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(17));
                data.what = 23;
                this.mUiHandler.sendMessage(data);
                LOG.mo8825d("[mCurChannel.getId]" + this.mCurChannel.getId());
                Message msg = Message.obtain();
                msg.what = 5;
                msg.obj = this.mCurChannel.getId();
                this.mWorkHandler.removeMessages(5);
                this.mWorkHandler.sendMessage(msg);
                return;
            case 1:
                recordPlayTime();
                this.mWorkHandler.removeMessages(4);
                this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(4));
                return;
            case 2:
                this.isFullPlay = true;
                this.mMediaProcessor.setProp(PagePlayVideoConstants.FULL_SCREEN, "XXX");
                this.mContentView.requestFocus();
                return;
            case 3:
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(18));
                return;
            case 4:
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(20));
                return;
            case 5:
                this.mMediaProcessor.setProp(PagePlayVideoConstants.LITTLE_SCREEN, "0,132,760,405");
                return;
            default:
                return;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LOG.mo8825d("[keycode]" + keyCode);
        this.mPlayTime = 0;
        switch (keyCode) {
            case 4:
                if (this.isAtPzqbPage) {
                    this.mUiHandler.sendEmptyMessage(5);
                    return true;
                } else if (this.isSeeked) {
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(13));
                    return true;
                } else if (this.isAtBufferPage) {
                    this.isAtBufferPage = false;
                    stopMediaPlay();
                    this.isFullPlay = false;
                    this.mWorkHandler.removeMessages(2);
                    this.backListener.playPageCallBack(1, null);
                    onStop();
                    return true;
                } else if (this.mMainApp.getBackPageIndex() == 0) {
                    this.isPlaying = false;
                    this.isFullPlay = false;
                    stopMediaPlay();
                    this.backListener.playPageCallBack(4, null);
                    onStop();
                    return true;
                } else if (this.mMainApp.getBackPageIndex() != 3) {
                    this.backListener.playPageCallBack(1, null);
                    return true;
                } else if (!DownUtils.usbChecked()) {
                    return true;
                } else {
                    this.isFullPlay = false;
                    this.backListener.playPageCallBack(7, null);
                    return true;
                }
            case 19:
            case Opcodes.IF_ACMPNE /*166*/:
                if (this.isAtPzqbPage) {
                    return true;
                }
                if (this.mCurPlayType.equals("1")) {
                    if (!this.mMediaProcessor.getProp("prepared").equals("true")) {
                        return true;
                    }
                    this.backListener.playPageCallBack(6, null);
                    return true;
                } else if (this.isAtPlaySeekPage) {
                    return true;
                } else {
                    this.timeTemp = 0;
                    this.isSeeked = true;
                    this.mUiHandler.sendEmptyMessage(14);
                    return true;
                }
            case 20:
            case Opcodes.GOTO /*167*/:
                if (this.isAtPzqbPage) {
                    return true;
                }
                if (this.mCurPlayType.equals("1")) {
                    if (!this.mMediaProcessor.getProp("prepared").equals("true")) {
                        return true;
                    }
                    this.backListener.playPageCallBack(5, null);
                    return true;
                } else if (this.isAtPlaySeekPage) {
                    return true;
                } else {
                    this.timeTemp = 0;
                    this.isSeeked = true;
                    this.mUiHandler.sendEmptyMessage(14);
                    return true;
                }
            case 21:
                if (!this.isPlaying || !this.isFullPlay || this.isAtBufferPage || !this.isFullPlay) {
                    return true;
                }
                if (!this.mCurPlayType.equals("0") && !this.mCurPlayType.equals("3")) {
                    return true;
                }
                this.timeTemp = 0;
                this.isSeeked = true;
                this.inerval = (int) (((long) this.inerval) - ((1 * this.duration) / 100));
                if (!this.isAtPlaySeekPage) {
                    this.mUiHandler.sendEmptyMessage(14);
                    return true;
                }
                this.mUiHandler.sendEmptyMessage(12);
                return true;
            case 22:
                if (!this.isPlaying || !this.isFullPlay || this.isAtBufferPage || !this.isFullPlay) {
                    return true;
                }
                if (!this.mCurPlayType.equals("0") && !this.mCurPlayType.equals("3")) {
                    return true;
                }
                this.timeTemp = 0;
                this.isSeeked = true;
                this.inerval = (int) (((long) this.inerval) + ((1 * this.duration) / 100));
                if (!this.isAtPlaySeekPage) {
                    this.mUiHandler.sendEmptyMessage(14);
                    return true;
                }
                this.mUiHandler.sendEmptyMessage(12);
                return true;
            case 23:
                break;
            case 85:
                LOG.mo8826e("[isPlaying]" + this.isPlaying + "[isFullPlay]" + this.isFullPlay + "[isPassed]" + this.isPassed + "[isResume]" + this.isResume + "[isSeeked]" + this.isSeeked + "[curPlayType]" + this.mCurPlayType);
                if (this.mCurPlayType.equals("1")) {
                    return true;
                }
                if (this.isSeeked) {
                    if (!this.isPlaying || !this.isFullPlay || this.isAtBufferPage || !this.isFullPlay) {
                        return true;
                    }
                    if ((!this.mCurPlayType.equals("0") && !this.mCurPlayType.equals("3")) || !this.isSeeked) {
                        return true;
                    }
                    this.isSeeked = false;
                    this.isStopSeek = false;
                    if (this.isPassed) {
                        this.isPassed = false;
                        this.isResume = true;
                        this.mMediaProcessor.setProp("media_cmd_resume", "XXX");
                    }
                    this.mUiHandler.sendEmptyMessage(13);
                    LOG.mo8826e("[curPlayTime]" + this.mCurPlayTime);
                    this.mMediaProcessor.setProp("media_cmd_seek", String.valueOf(this.mCurPlayTime));
                    return true;
                } else if (this.isPlaying && this.isFullPlay && this.isPassed && !this.isResume && (this.mCurPlayType.equals("0") || this.mCurPlayType.equals("3"))) {
                    this.isPassed = false;
                    this.isResume = true;
                    this.mIvReviewPrompt.setBackgroundResource(R.drawable.review_play);
                    this.mUiHandler.sendEmptyMessageDelayed(13, 1000);
                    this.mMediaProcessor.setProp("media_cmd_resume", "XXX");
                    return true;
                } else if (this.isPlaying && this.isFullPlay && !this.isPassed && this.isResume && (this.mCurPlayType.equals("0") || this.mCurPlayType.equals("3"))) {
                    this.isPassed = true;
                    this.isResume = false;
                    this.inerval = 0;
                    showPlaySeekPage();
                    this.mMediaProcessor.setProp("media_cmd_pause", "XXX");
                    this.mIvReviewPrompt.setVisibility(0);
                    this.mIvReviewPrompt.setBackgroundResource(R.drawable.review_pause);
                    return true;
                }
                break;
            case 183:
            case 184:
            case 186:
                return true;
            case 185:
                if (!this.isAtPzqbPage) {
                    return true;
                }
                LOG.mo8825d("[mClickPage]" + this.mClickPage + "[mCurType]" + this.mCurPlayType);
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(5));
                this.mWorkHandler.sendEmptyMessage(7);
                return true;
            case HttpStatus.SC_MULTIPLE_CHOICES /*300*/:
                this.mMediaProcessor.setProp("media.track", null);
                if (this.mUiHandler.hasMessages(16)) {
                    this.mUiHandler.removeMessages(16);
                }
                this.mUiHandler.sendEmptyMessage(16);
                break;
        }
        LOG.mo8825d("[mCurPlayType]" + this.mCurPlayType);
        if (this.isAtPzqbPage) {
            return true;
        }
        if (this.isSeeked) {
            if (!this.isPlaying || !this.isFullPlay || this.isAtBufferPage || !this.isFullPlay) {
                return true;
            }
            if ((!this.mCurPlayType.equals("0") && !this.mCurPlayType.equals("3")) || !this.isSeeked) {
                return true;
            }
            this.isSeeked = false;
            this.isStopSeek = false;
            if (this.isPassed) {
                this.isPassed = false;
                this.isResume = true;
                this.mMediaProcessor.setProp("media_cmd_resume", "XXX");
            }
            this.mUiHandler.sendEmptyMessage(13);
            LOG.mo8826e("[curPlayTime]" + this.mCurPlayTime);
            this.mMediaProcessor.setProp("media_cmd_seek", String.valueOf(this.mCurPlayTime));
            return true;
        } else if (this.isPlaying && this.isFullPlay && this.isPassed && !this.isResume && (this.mCurPlayType.equals("0") || this.mCurPlayType.equals("3"))) {
            this.isPassed = false;
            this.isResume = true;
            this.mIvReviewPrompt.setBackgroundResource(R.drawable.review_play);
            this.mUiHandler.sendEmptyMessageDelayed(13, 1000);
            this.mMediaProcessor.setProp("media_cmd_resume", "XXX");
            return true;
        } else if (!this.isPlaying || !this.isFullPlay || this.isPassed || !this.isResume || (!this.mCurPlayType.equals("0") && !this.mCurPlayType.equals("3"))) {
            if ("0".equals(this.mCurPlayType) && !this.isAtPlaySeekPage) {
                this.timeTemp = 0;
                this.isSeeked = true;
                this.mUiHandler.sendEmptyMessage(14);
            }
            if (!this.mCurPlayType.equals("1")) {
                return true;
            }
            if (!this.isAtBufferPage) {
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(13));
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(10));
                Message msg = Message.obtain();
                msg.arg1 = 10;
                isShowingLiveList = true;
                this.backListener.playPageCallBack(2, msg);
                this.mContentView.clearFocus();
            }
            return super.onKeyDown(keyCode, event);
        } else {
            this.isPassed = true;
            this.isResume = false;
            this.inerval = 0;
            showPlaySeekPage();
            this.mMediaProcessor.setProp("media_cmd_pause", "XXX");
            this.mIvReviewPrompt.setVisibility(0);
            this.mIvReviewPrompt.setBackgroundResource(R.drawable.review_pause);
            return true;
        }
    }

    private void initUi() {
        LOG.mo8825d("[initUI]");
        this.mIvPageQuality = (ImageView) this.mContentView.findViewById(R.id.iv_page_quality);
        this.mTvNowTime = (TextView) this.mContentView.findViewById(R.id.tv_now_time);
        this.mFlPlayPageRoot = (FrameLayout) this.mContentView.findViewById(R.id.fl_play_page);
        this.mPlayLayout = (LinearLayout) this.mContentView.findViewById(R.id.play);
        this.mSeekBar = (SeekBarHintDelegateHolder) this.mContentView.findViewById(R.id.tsb_seek);
        this.mTotoalTimeView = (TextView) this.mContentView.findViewById(R.id.total_time);
        this.mFilmNameView = (TextView) this.mContentView.findViewById(R.id.flname);
        this.mFNewNameView = (TextView) this.mContentView.findViewById(R.id.nwname);
        this.mTrackView = (ImageView) this.mContentView.findViewById(R.id.tracks);
        this.mIvReviewPrompt = (ImageView) this.mContentView.findViewById(R.id.iv_review_prompt);
        this.mAdBufferView = (ImageView) this.mContentView.findViewById(R.id.ad);
        this.mIvDialog = (ImageView) this.mContentView.findViewById(R.id.iv_dialog);
        this.mBufferPageLayout = (FrameLayout) this.mContentView.findViewById(R.id.buffer_layout);
        this.mPreview = (SurfaceView) this.mContentView.findViewById(R.id.surface);
        this.mPreview.setEnabled(false);
        SurfaceHolder mHolder = this.mPreview.getHolder();
        mHolder.setType(0);
        mHolder.setFormat(1);
        mHolder.setKeepScreenOn(true);
        ((AnimationDrawable) this.mIvDialog.getBackground()).start();
        this.mInfoLayout = (LinearLayout) this.mContentView.findViewById(R.id.info_layout);
        this.mNwsNameView = (TextView) this.mInfoLayout.findViewById(R.id.nwsname);
        this.mNwsNameView.getPaint().setFakeBoldText(true);
        this.mDateofTsView = (TextView) this.mInfoLayout.findViewById(R.id.date_ts);
        this.mTimeofTsView = (TextView) this.mInfoLayout.findViewById(R.id.time_ts);
        this.mFilNameView = (TextView) this.mInfoLayout.findViewById(R.id.flmname);
        this.mChanNumView = (TextView) this.mInfoLayout.findViewById(R.id.channum);
        this.mIvQualityInfo = (ImageView) this.mInfoLayout.findViewById(R.id.iv_play_quality);
        this.mTvCurtimeInfo = (TextView) this.mInfoLayout.findViewById(R.id.tv_play_curtime);
        this.mChanIconView = (ImageView) this.mInfoLayout.findViewById(R.id.chanicon);
        this.mPzqbPage = (LinearLayout) LayoutInflater.from(this.mContext).inflate(R.layout.pzqb_layout_playpage, null);
        this.mFlPlayPageRoot.addView(this.mPzqbPage);
        this.mFileNameView = (TextView) this.mPzqbPage.findViewById(R.id.filename);
        this.mFileNameView.getPaint().setFakeBoldText(true);
        this.mFileIndexView = (TextView) this.mPzqbPage.findViewById(R.id.paqb_index);
        this.mFileDateView = (TextView) this.mPzqbPage.findViewById(R.id.date_pzqb);
        this.mNwNameView = (TextView) this.mPzqbPage.findViewById(R.id.name_pzqb);
        this.mNwNameView.getPaint().setFakeBoldText(true);
        this.mFileIconView = (ImageView) this.mPzqbPage.findViewById(R.id.pzqbicon);
        this.mFileDesView = (TextView) this.mPzqbPage.findViewById(R.id.description);
        this.mTvPzqbTime = (TextView) this.mPzqbPage.findViewById(R.id.tv_pzqb_time);
        this.mIvPzqbTip = (ImageView) this.mPzqbPage.findViewById(R.id.pzqb_tip);
        this.mPzqbPage.setVisibility(8);
        this.mFileDesView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PagePlayVideo.this.isAtPzqbPage) {
                    PagePlayVideo.this.mUiHandler.sendMessage(PagePlayVideo.this.mUiHandler.obtainMessage(5));
                }
            }
        });
    }

    public void notifyShowBufferPage() {
        LOG.mo8825d("[notifyShowBufferPage]");
        this.mUiHandler.sendEmptyMessage(0);
    }

    /* access modifiers changed from: private */
    public void showSurfacePage() {
        LOG.mo8825d("[showSurfacePage]");
        initBufferImgs();
        this.isAtBufferPage = true;
        hidePlaySeekPage();
        this.mBufferPageLayout.setVisibility(0);
        this.mInfoLayout.setVisibility(8);
        this.mTrackView.setVisibility(8);
        this.mPzqbPage.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void hidePlaySeekPage() {
        LOG.mo8825d("[hidePlaySeekPage]");
        this.isAtPlaySeekPage = false;
        this.inerval = 0;
        this.isSeeked = false;
        this.mSeekBar.getHintDelegate().hidePopup();
        this.mPlayLayout.setVisibility(8);
    }

    private void initBufferImgs() {
        if (this.mBufferImgUrlList == null) {
            String json = this.mSharedPreUtil.getStbImgs("StbBufferImgs");
            LOG.mo8825d("[buffer images]" + json);
            if (StringUtils.isNotEmpty(json)) {
                this.mBufferImgUrlList = JsonUtil.getList(json);
            } else {
                this.mBufferImgUrlList = new ArrayList();
                this.mBufferImgUrlList.add(0, XmlPullParser.NO_NAMESPACE);
            }
        }
        if (this.mBufferImgUrlList.size() != 0) {
            int inx = Math.abs(this.mrRandom.nextInt() % this.mBufferImgUrlList.size());
            LOG.mo8825d("[show index]" + inx);
            try {
                Glide.with((Activity) this.mMainApp).load((String) this.mBufferImgUrlList.get(inx)).into(this.mAdBufferView);
            } catch (Exception e) {
            }
        }
    }

    /* access modifiers changed from: private */
    public void hidePzqbPage() {
        LOG.mo8825d("[hiddenPzqb]");
        this.isAtPzqbPage = false;
        this.mPzqbPage.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void initPzqbPage() {
        initDateStr();
        LOG.mo8825d("[current live program]" + this.mCurLiveProgram);
        if (this.mCurLiveProgram == null) {
            this.mFileNameView.setText(XmlPullParser.NO_NAMESPACE);
            this.mFileDateView.setText(XmlPullParser.NO_NAMESPACE);
            this.mFileDesView.setText(XmlPullParser.NO_NAMESPACE);
        } else {
            String startUtcStr = DateTimeUtil.UtcToDate(this.mCurLiveProgram.getUtc());
            String endtUtcStr = DateTimeUtil.UtcToDate(this.mCurLiveProgram.getEndUtc());
            String programDate = this.dateStr;
            String programdescript = this.mCurLiveProgram.getDescription();
            this.mFileNameView.setText(this.mCurLiveProgram.getTitle());
            this.mFileDateView.setText(programDate);
            this.mTvPzqbTime.setText(new StringBuilder(String.valueOf(startUtcStr)).append("~").append(endtUtcStr).toString());
            this.mFileDesView.setText(programdescript);
        }
        if (this.mCurPlayType.equals("0")) {
            this.mIvPzqbTip.setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.pzqb_appointment));
        } else if (this.mCurPlayType.equals("1")) {
            this.mIvPzqbTip.setImageDrawable(this.mContext.getResources().getDrawable(R.drawable.pzqb_reserve_new));
        }
        this.mFileIndexView.setText(PageLive.getChannelNumStr(this.mCurChannel.getChannelNumber(), this.mMainApp.getmTvFormt()));
        this.mNwNameView.setText(this.mCurChannel.getTitle());
        LOG.mo8825d("[channel image]" + this.mCurChannel.getImage());
        Glide.with((Activity) this.mMainApp).load(this.mCurChannel.getImage()).into(this.mFileIconView);
    }

    private void initDateStr() {
        String[] WEEK = this.mContext.getResources().getStringArray(R.array.Week);
        int month = this.mCalendar.get(2) + 1;
        int day = this.mCalendar.get(5);
        this.dateStr = new StringBuilder(String.valueOf(month)).append("月").append(day < 10 ? "0" + day : new StringBuilder(String.valueOf(day)).append("日").toString()).append("(").append(WEEK[this.mCalendar.get(7) - 1]).append(")").toString();
    }

    public void notifyStopCheckPlayTime() {
        this.mUiHandler.removeMessages(2);
        this.mUiHandler.removeMessages(3);
    }

    public void stopMediaPlay() {
        if (this.mMediaProcessor != null) {
            this.mMediaProcessor.setProp("media_cmd_stop", "XXX");
        }
        this.mPlayTime = 0;
    }

    public void notifyPlayStatus() {
        LOG.mo8825d("[notifyPlayStatus]");
        this.mUiHandler.removeMessages(8);
        this.mUiHandler.removeMessages(9);
        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(8));
        this.mUiHandler.sendMessageDelayed(this.mUiHandler.obtainMessage(9), 500);
        this.mWorkHandler.removeMessages(2);
    }

    public void notifySeekEnd() {
        LOG.mo8825d("[notifySeekEnd]");
        this.mUiHandler.sendEmptyMessage(19);
    }

    /* access modifiers changed from: private */
    public void checkConnetTime() {
        LOG.mo8825d("[isPlaying]" + this.isPlaying + "[isAtBufferPage]" + this.isAtBufferPage);
        if (!this.isPlaying && this.isAtBufferPage) {
            Toast.makeText(getContext(), getResources().getString(R.string.live_fail), 1).show();
            stopMediaPlay();
            this.backListener.playPageCallBack(1, null);
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(15));
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(10));
            if (this.isAtPzqbPage) {
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(5));
            }
            if (this.isAtPlaySeekPage) {
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(13));
            }
            if (this.isAtBufferPage) {
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(8));
            }
        }
    }

    /* access modifiers changed from: private */
    public void hideBufferAdPage() {
        LOG.mo8825d("[hiddenAD]");
        this.isAtBufferPage = false;
        this.mBufferPageLayout.setVisibility(8);
        this.mAdBufferView.setImageDrawable(null);
    }

    /* access modifiers changed from: private */
    public void showInfoPage() {
        LOG.mo8825d("[showInfoPage]");
        LOG.mo8825d("[ChanIconView]" + this.mChanIconView.getX() + "  " + this.mChanIconView.getY());
        this.mUiHandler.removeMessages(10);
        this.mInfoLayout.setVisibility(0);
        this.mUiHandler.sendEmptyMessageDelayed(10, 3000);
        if (this.mCurPlayType.equals("0") && !this.isAtPlaySeekPage) {
            this.timeTemp = 0;
            this.isSeeked = true;
            this.mUiHandler.sendEmptyMessage(14);
        }
    }

    /* access modifiers changed from: private */
    public void hideInfoPage() {
        LOG.mo8825d("[hideInfoPage]");
        this.mInfoLayout.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void showPlaySeekPage() {
        LOG.mo8825d("[showPlaySeekPage]");
        if (this.isPlaying) {
            try {
                LOG.mo8825d("[mCurLiveProgram]" + this.mCurLiveProgram);
                this.isAtPlaySeekPage = true;
                this.isStopSeek = true;
                String time = this.mMediaProcessor.getProp(VodRecordDbHelper.MEDIA_DURATION);
                LogcatUtils.m321d("duration is " + this.duration + " time is " + time);
                if (StringUtils.isNumberStr(time) && this.duration <= 0 && Integer.parseInt(time) > 0) {
                    this.duration = (long) Integer.parseInt(time);
                }
                LOG.mo8826e("[showPlaySeekPage.duration]" + this.duration + "[TimeStr]" + StringUtils.getTimeStr(this.duration / 1000));
                this.mTotoalTimeView.setText(StringUtils.getTimeStr(this.duration / 1000));
                freshPlaySeekPage();
                this.mFilmNameView.setText(this.mCurLiveProgram.getTitle());
                this.mFNewNameView.setText(this.mCurChannel.getTitle());
                this.mIvPageQuality.setImageResource(getCurQualityRes());
                this.mTvNowTime.setText(initDate());
                this.mPlayLayout.setVisibility(0);
                this.mTrackView.setVisibility(8);
                if (!this.isPassed) {
                    new Thread(this.mFreshProgressBar).start();
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.mo8825d("...Exception..." + e.toString());
            }
        }
    }

    private String initDate() {
        Calendar calendar = Calendar.getInstance();
        String[] WEEK = this.mContext.getResources().getStringArray(R.array.Week);
        int month = calendar.get(2) + 1;
        int day = calendar.get(5);
        String dateStr2 = new StringBuilder(String.valueOf(month)).append("月").append(day < 10 ? "0" + day : new StringBuilder(String.valueOf(day)).append("日").toString()).append("(").append(WEEK[calendar.get(7) - 1]).append(")").toString();
        int hour = calendar.get(11);
        int minute = calendar.get(12);
        return new StringBuilder(String.valueOf(dateStr2)).append("           ").append((hour < 10 ? "0" + hour : Integer.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : Integer.valueOf(minute))).toString();
    }

    /* access modifiers changed from: private */
    public void freshPlaySeekPage() {
        LOG.mo8826e("[freshPlaySeekPage]");
        if (this.isSeeked) {
            this.mSeekBar.getHintDelegate().showPopup();
        }
        String time = this.mMediaProcessor.getProp("currentplaytime");
        LOG.mo8826e("[time]" + time);
        if (StringUtils.isNumberStr(time) && Integer.parseInt(time) > 0) {
            this.mCurPlayTime = Long.parseLong(time);
        }
        LOG.mo8826e("[curPlayTime]" + this.mCurPlayTime + "[startTime]" + this.mStartTime + "[duration]" + this.duration + "[inerval]" + this.inerval);
        if (this.mCurPlayTime + ((long) this.inerval) > this.duration) {
            this.mCurPlayTime = this.duration;
        } else if (this.mCurPlayTime + ((long) this.inerval) < 0) {
            this.mCurPlayTime = 0;
        } else {
            this.mCurPlayTime += (long) this.inerval;
        }
        LOG.mo8826e("[curPlayTime]" + this.mCurPlayTime + "[startTime]" + this.mStartTime + "[duration]" + this.duration);
        if (this.mCurPlayTime > 0) {
            this.curPos = (int) ((this.mCurPlayTime * 100) / this.duration);
        }
        LOG.mo8826e("[curPos]" + this.curPos);
        setProgressPos(this.curPos);
    }

    private void setProgressPos(int pos) {
        if (pos == 0) {
            pos = 1;
        }
        this.mSeekBar.getHintDelegate().setProgress(pos);
        this.mSeekBar.getHintDelegate().setHintAdapter(new SeekBarHintAdapter() {
            public String getHint(SeekBar seekBar, int progress) {
                return new StringBuilder(String.valueOf(StringUtils.getTimeStr(PagePlayVideo.this.mCurPlayTime / 1000))).append(" / ").append(StringUtils.getTimeStr(PagePlayVideo.this.duration / 1000)).toString();
            }
        });
    }

    /* access modifiers changed from: private */
    public void showTrackView() {
        LOG.mo8825d("[showTrackView]");
        checkTrackView();
        this.mTrackView.setVisibility(0);
    }

    private void checkTrackView() {
        String track = this.mDBProcessor.getProp("track");
        if (track.equals("1")) {
            this.mTrackView.setImageResource(R.drawable.leftk);
        } else if (track.equals("2")) {
            this.mTrackView.setImageResource(R.drawable.rightk);
        }
        if (track.equals("3")) {
            this.mTrackView.setImageResource(R.drawable.body);
        }
    }

    /* access modifiers changed from: private */
    public void hideTrackView() {
        LOG.mo8825d("[hideTrackView]");
        this.mTrackView.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void gotoPlayPage(Message msg) {
        LOG.mo8825d("[gotoPlayPage]");
        ArrayList<UrlBean> urlList = this.mCurChannel.getUrls();
        String playUrl = null;
        if (urlList != null && urlList.size() > 0) {
            int quality = this.mDataManager.getQuality();
            this.mDataManager.setAutoQuality(false);
            while (quality >= 0) {
                LOG.mo8825d("[quality]" + quality);
                Iterator it = urlList.iterator();
                while (it.hasNext()) {
                    UrlBean urlBean = (UrlBean) it.next();
                    if (quality == urlBean.getQuality()) {
                        playUrl = urlBean.getUrl();
                    }
                }
                if (!StringUtils.isEmpty(playUrl)) {
                    break;
                }
                quality--;
                this.mDataManager.setAutoQuality(quality);
                this.mDataManager.setAutoQuality(true);
            }
            LOG.mo8825d("[quality]" + quality);
            LOG.mo8825d("[mCurPlayType]" + this.mCurPlayType);
            if (StringUtils.isEmpty(playUrl)) {
                if (StringUtils.isNotEmpty(this.mCurPlayType) && this.mCurPlayType.equals("3")) {
                    playUrl = ((UrlBean) urlList.get(0)).getUrl();
                }
                LOG.mo8825d("[playUrl]" + playUrl);
                if (StringUtils.isEmpty(playUrl)) {
                    return;
                }
            }
            LOG.mo8825d("[playUrl]" + playUrl);
            if (playUrl.startsWith("mop://")) {
                playUrl = mopUrl2RealUrl(playUrl);
            }
            if (StringUtils.isNotEmpty(this.mCurPlayType)) {
                LOG.mo8826e("[playtype]" + this.mCurPlayType);
                if (this.mCurPlayType.equals("0")) {
                    String startTime = msg.getData().getString("startTime");
                    String endTime = msg.getData().getString("endTime");
                    if (StringUtils.isNotEmpty(startTime)) {
                        long startT = DateTimeUtil.getUtcTime(startTime);
                        long endT = DateTimeUtil.getUtcTime(endTime);
                        if (startT != 0) {
                            this.mStartTime = startT;
                            this.duration = endT - startT;
                            playUrl = new StringBuilder(String.valueOf(playUrl)).append("&playseek=").append(startT / 1000).append("-").append(endT / 1000).toString();
                        }
                    }
                }
                this.mPlayUrl = playUrl;
                LOG.mo8825d("[playUrl]" + this.mPlayUrl);
                if (this.mMediaProcessor != null) {
                    this.mMediaProcessor.setProp(PagePlayVideoConstants.FULL_SCREEN, "XXX");
                    play(this.mPlayUrl);
                }
            }
            this.mUiHandler.sendEmptyMessage(6);
            this.mUiHandler.sendEmptyMessage(17);
        }
    }

    public synchronized void play(String url) {
        this.mWorkHandler.removeMessages(2);
        this.mWorkHandler.sendEmptyMessageDelayed(2, 30000);
        notifyStopCheckPlayTime();
        this.mUiHandler.sendMessageDelayed(this.mUiHandler.obtainMessage(2), DateTime.MILLIS_PER_MINUTE);
        if (isPlaying()) {
            recordPlayTime();
        }
        if ("0".equals(this.mCurPlayType)) {
            if (!VodRecordDbHelper.isOpen()) {
                mVodDBHelper.openDb();
            }
            EPGBean epgBean = this.mCurLiveProgram;
            String programMask = epgBean.getUtc() + epgBean.getChannelId();
            LOG.mo8825d("[PROGRAM_MASK]" + programMask);
            Cursor cursor = mVodDBHelper.queryOneData(programMask);
            if (cursor == null || !cursor.moveToNext()) {
                this.isPlayFromSeek = false;
            } else {
                LOG.mo8825d("[has play time!!!]");
                String currentPlaytime = cursor.getString(cursor.getColumnIndex(VodRecordDbHelper.CURRENT_PLAYTIME));
                LOG.mo8825d("[currentPlaytime]" + currentPlaytime);
                this.isPlayFromSeek = true;
                try {
                    this.seekPoint = Integer.valueOf(currentPlaytime).intValue();
                } catch (Exception e) {
                    this.seekPoint = 0;
                    e.printStackTrace();
                }
            }
            mVodDBHelper.close();
            cursor.close();
        } else {
            this.isPlayFromSeek = false;
        }
        this.mMediaProcessor.setProp("media_cmd_play", url);
        return;
    }

    /* access modifiers changed from: private */
    public void initInfoPage() {
        String sb;
        Object valueOf;
        Object valueOf2;
        LOG.mo8825d("[initInfoPage]");
        try {
            String[] WEEK = this.mContext.getResources().getStringArray(R.array.Week);
            int month = this.mCalendar.get(2) + 1;
            int day = this.mCalendar.get(5);
            int wk = this.mCalendar.get(7);
            StringBuilder append = new StringBuilder(String.valueOf(month)).append("月");
            if (day < 10) {
                StringBuilder sb2 = new StringBuilder("0");
                sb = sb2.append(day).toString();
            } else {
                StringBuilder sb3 = new StringBuilder(String.valueOf(day));
                sb = sb3.append("日").toString();
            }
            String dateStr1 = append.append(sb).append("(").append(WEEK[wk - 1]).append(")").toString();
            if (this.mCurLiveProgram == null) {
                this.mTimeofTsView.setText(XmlPullParser.NO_NAMESPACE);
                this.mNwsNameView.setText(XmlPullParser.NO_NAMESPACE);
            } else {
                String fileName = this.mCurLiveProgram.getTitle();
                String startUtcStr = DateTimeUtil.UtcToDate(this.mCurLiveProgram.getUtc());
                this.mTimeofTsView.setText(new StringBuilder(String.valueOf(startUtcStr)).append("~").append(DateTimeUtil.UtcToDate(this.mCurLiveProgram.getEndUtc())).toString());
                this.mNwsNameView.setText(fileName);
            }
            this.mFilNameView.setText(this.mCurChannel.getTitle());
            this.mChanNumView.setText(PageLive.getChannelNumStr(this.mCurChannel.getChannelNumber(), this.mMainApp.getmTvFormt()));
            this.mDateofTsView.setText(dateStr1);
            Glide.with((Activity) this.mMainApp).load(this.mCurChannel.getImage()).into(this.mChanIconView);
            Glide.with((Activity) this.mMainApp).load(Integer.valueOf(getCurQualityRes())).into(this.mIvQualityInfo);
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(11);
            int minute = calendar.get(12);
            StringBuilder sb4 = new StringBuilder();
            if (hour < 10) {
                StringBuilder sb5 = new StringBuilder("0");
                valueOf = sb5.append(hour).toString();
            } else {
                valueOf = Integer.valueOf(hour);
            }
            StringBuilder append2 = sb4.append(valueOf).append(":");
            if (minute < 10) {
                StringBuilder sb6 = new StringBuilder("0");
                valueOf2 = sb6.append(minute).toString();
            } else {
                valueOf2 = Integer.valueOf(minute);
            }
            this.mTvCurtimeInfo.setText(append2.append(valueOf2).toString());
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...====" + e.toString());
        }
    }

    private String mopUrl2RealUrl(String url) {
        String ret;
        String protocol = Secure.getString(this.mContext.getContentResolver(), "transport_way");
        LOG.mo8825d("[protocol]" + protocol);
        LOG.mo8825d("[url]" + url);
        String ret2 = url.substring(6);
        if (StringUtils.isEmpty(this.mTerminalid)) {
            this.mTerminalid = SystemProperties.get("ro.serialno");
        }
        String user = SWSysProp.getStringParam("user_name", XmlPullParser.NO_NAMESPACE);
        LOG.mo8825d("[user]" + user);
        String curOisUrl = this.mSdkRemoteManager.getCurOisUrl();
        LOG.mo8825d("[curOisUrl]" + curOisUrl);
        if ("transport_hls".equalsIgnoreCase(protocol)) {
            ret = "http://" + curOisUrl.replace("5001", "5000/") + ret2 + ".m3u8?protocal=hls&user=" + user + "&tid=" + this.mTerminalid + "&sid=" + this.mCurChannel.getId() + "&type=stb&token=" + this.mSdkRemoteManager.getOisToken();
        } else {
            ret = "http://127.0.0.1:5000/" + ret2 + ".m3u8?ois=" + curOisUrl.split("\\:")[0] + "&port=5000&cid=" + ret2 + "&user=" + user + "&tid=" + this.mTerminalid + "&sid=" + this.mCurChannel.getId() + "&type=stb&ois_token=" + this.mSdkRemoteManager.getOisToken();
        }
        LOG.mo8825d("[mopUrl2RealUrl]" + ret);
        return ret;
    }

    /* access modifiers changed from: private */
    public void authen(String mediaId) {
        if (StringUtils.isEmpty(this.mTerminalid)) {
            this.mTerminalid = SystemProperties.get("ro.serialno");
        }
        String user = SWSysProp.getStringParam("user_name", XmlPullParser.NO_NAMESPACE);
        LOG.mo8825d("[user]" + user);
        int authenResult = this.mSdkRemoteManager.authen(user, this.mTerminalid, mediaId);
        LOG.mo8825d("[authen]" + authenResult);
        switch (authenResult) {
            case -1:
                LOG.mo8825d("[net work error!!!]");
                return;
            case 200:
                LOG.mo8825d("[authen_success!!!]");
                return;
            case HttpStatus.SC_UNAUTHORIZED /*401*/:
                LOG.mo8825d("[illegal_user!!!]");
                return;
            case HttpStatus.SC_PAYMENT_REQUIRED /*402*/:
                LOG.mo8825d("[forbidden_user!!!]");
                return;
            case HttpStatus.SC_FORBIDDEN /*403*/:
                LOG.mo8825d("[invalid_token!!!]");
                new Thread() {
                    public void run() {
                        PagePlayVideo.this.stopMediaPlay();
                    }
                }.start();
                HashMap<MediaBean, EPGBean> playDataMap = new HashMap<>();
                playDataMap.put(this.mCurChannel, this.mCurLiveProgram);
                Message msg = new Message();
                msg.obj = playDataMap;
                Bundle mBundle = new Bundle();
                mBundle.putString(MailDbHelper.TYPE, this.mCurPlayType);
                mBundle.putInt("clickPage", this.mClickPage);
                msg.setData(mBundle);
                doByMessage(msg, 0);
                return;
            case HttpStatus.SC_NOT_FOUND /*404*/:
                LOG.mo8825d("[invalid_mediaId!!!]");
                return;
            case HttpStatus.SC_NOT_ACCEPTABLE /*406*/:
                LOG.mo8825d("[user_unordered!!!]");
                new Thread() {
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        PagePlayVideo.LOG.mo8825d("user_unordered!!!---stopMediaPlay-");
                        PagePlayVideo.this.stopMediaPlay();
                        SWSysProp.setIntParam("error_code", 3);
                        PagePlayVideo.this.mContext.startActivity(new Intent(PagePlayVideo.this.mContext, ErrorPageActivity.class));
                    }
                }.start();
                return;
            case HttpStatus.SC_REQUEST_TIMEOUT /*408*/:
                LOG.mo8825d("[ordered_time_unreached!!!]");
                return;
            case HttpStatus.SC_CONFLICT /*409*/:
                LOG.mo8825d("[invalid_stb!!!]");
                return;
            default:
                return;
        }
    }

    private int getCurQualityRes() {
        int quality;
        if (this.mDataManager.isAutoQuality()) {
            quality = this.mDataManager.getAutoQuality();
        } else {
            quality = this.mDataManager.getQuality();
        }
        switch (quality) {
            case 2:
                return R.drawable.sd;
            case 4:
                return R.drawable.fhd;
            default:
                return R.drawable.hd;
        }
    }

    /* access modifiers changed from: private */
    public void checkDownTask(MediaBean channelParams, EPGBean epgBean) {
        try {
            if (!DownUtils.usbChecked()) {
                this.backListener.playPageCallBack(8, null);
            } else if (channelParams != null && epgBean != null) {
                String channelMask = channelParams.getId();
                String startTime = DateTimeUtil.UtcToDate(epgBean.getUtc());
                int year = this.mCalendar.get(1);
                int month = this.mCalendar.get(2) + 1;
                int day = this.mCalendar.get(5);
                String fileMask = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(year)).append("-").append(month < 10 ? "0" + month : new StringBuilder(String.valueOf(month)).toString()).append("-").append(day < 10 ? "0" + day : new StringBuilder(String.valueOf(day)).toString()).toString())).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime).append(":00").toString()))).toString())).append(channelMask).toString();
                LOG.mo8825d("[fileMask]" + fileMask);
                if (!this.mDownUtils.isContainsKey(fileMask)) {
                    this.mUiHandler.removeMessages(24);
                    this.mUiHandler.sendEmptyMessage(24);
                    return;
                }
                this.backListener.playPageCallBack(12, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void addDownTaskToList(MediaBean channelParams, EPGBean program) {
        String sb;
        String sb2;
        LOG.mo8825d("[channelParams]" + channelParams.getId());
        try {
            if (channelParams.getUrls() != null && channelParams.getUrls().get(0) != null) {
                ArrayList<UrlBean> urlList = channelParams.getUrls();
                String vodUrl = XmlPullParser.NO_NAMESPACE;
                int quality = 3;
                while (quality >= 0) {
                    LOG.mo8825d("[quality]" + quality);
                    Iterator it = urlList.iterator();
                    while (it.hasNext()) {
                        UrlBean urlBean = (UrlBean) it.next();
                        if (quality == urlBean.getQuality()) {
                            vodUrl = urlBean.getUrl();
                        }
                    }
                    if (!StringUtils.isEmpty(vodUrl)) {
                        break;
                    }
                    quality--;
                    this.mDataManager.setQuality(quality);
                }
                LOG.mo8825d("[vodUrl]" + vodUrl);
                if (!StringUtils.isEmpty(vodUrl)) {
                    String programName = program.getTitle();
                    String startTime = DateTimeUtil.UtcToDate(program.getUtc());
                    String endTime = DateTimeUtil.UtcToDate(program.getEndUtc());
                    String description = program.getDescription();
                    String programType = program.getType();
                    String channelName = channelParams.getTitle();
                    int channelNum = channelParams.getChannelNumber();
                    String channelMask = channelParams.getId();
                    String channelIconUrl = channelParams.getImage();
                    int year = this.mCalendar.get(1);
                    int month = this.mCalendar.get(2) + 1;
                    int day = this.mCalendar.get(5);
                    StringBuilder append = new StringBuilder(String.valueOf(year)).append("-");
                    if (month < 10) {
                        StringBuilder sb3 = new StringBuilder("0");
                        sb = sb3.append(month).toString();
                    } else {
                        StringBuilder sb4 = new StringBuilder(String.valueOf(month));
                        sb = sb4.toString();
                    }
                    StringBuilder append2 = append.append(sb).append("-");
                    if (day < 10) {
                        StringBuilder sb5 = new StringBuilder("0");
                        sb2 = sb5.append(day).toString();
                    } else {
                        StringBuilder sb6 = new StringBuilder(String.valueOf(day));
                        sb2 = sb6.toString();
                    }
                    String dateStr2 = append2.append(sb2).toString();
                    String fileMask = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(dateStr2)).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime).append(":00").toString()))).toString())).append(channelMask).toString();
                    DownloadTask downFile = new DownloadTask();
                    downFile.setChannelMask(channelMask);
                    LOG.mo8825d("[channelMask]" + channelMask);
                    downFile.setCategory(programType);
                    downFile.setTaskName(fileMask);
                    downFile.setChannelIconUrl(channelIconUrl);
                    downFile.setChannelName(channelName);
                    downFile.setChannelNum(channelNum);
                    downFile.setDate(dateStr2);
                    downFile.setDesc(description);
                    downFile.setLocalFile(new StringBuilder(String.valueOf(fileMask)).append(".ts").toString());
                    downFile.setStartTime(startTime);
                    downFile.setEndTime(endTime);
                    downFile.setProgramName(programName);
                    downFile.setTvFormt(this.mMainApp.getmTvFormt());
                    downFile.setStatus(0);
                    downFile.setVodUrl(vodUrl);
                    if (this.mCurPlayType == "1") {
                        downFile.setType(DownloadTask.TYPE_EPG);
                    } else {
                        downFile.setType(DownloadTask.TYPE_VOD);
                    }
                    this.mDownUtils.addDownTask(downFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playError() {
        this.mUiHandler.sendEmptyMessage(25);
    }

    public void playStop() {
        LOG.mo8825d("[playStop]");
        recordPlayTime();
    }

    /* access modifiers changed from: private */
    public synchronized void recordPlayTime() {
        LOG.mo8825d("[recordPlayTime]");
        if ("0".equals(this.mCurPlayType)) {
            if (!VodRecordDbHelper.isOpen()) {
                mVodDBHelper.openDb();
            }
            EPGBean epgBean = this.mCurLiveProgram;
            String channelId = epgBean.getChannelId();
            long utc = epgBean.getUtc();
            String currentPlaytime = this.mMediaProcessor.getProp("currentplaytime");
            String programMask = new StringBuilder(String.valueOf(utc)).append(channelId).toString();
            LOG.mo8825d("[PROGRAM_MASK]" + programMask);
            LOG.mo8825d("[utc]" + utc);
            LOG.mo8825d("[currentPlaytime]" + currentPlaytime);
            Cursor cursor = mVodDBHelper.queryOneData(programMask);
            LOG.mo8825d("[cursor]" + cursor);
            if (cursor == null || !cursor.moveToNext()) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(VodRecordDbHelper.PROGRAM_MASK, programMask);
                contentValues.put(VodRecordDbHelper.PROGRAM_DATE, String.valueOf(utc));
                contentValues.put(VodRecordDbHelper.CURRENT_PLAYTIME, currentPlaytime);
                mVodDBHelper.insertDataByContentValue(contentValues);
            } else {
                LOG.mo8825d("[has record]" + cursor.getCount());
                String id = cursor.getString(cursor.getColumnIndex("_id"));
                ContentValues contentValues2 = new ContentValues();
                contentValues2.put(VodRecordDbHelper.PROGRAM_MASK, programMask);
                contentValues2.put(VodRecordDbHelper.PROGRAM_DATE, String.valueOf(utc));
                contentValues2.put(VodRecordDbHelper.CURRENT_PLAYTIME, currentPlaytime);
                mVodDBHelper.updateData(contentValues2, id);
            }
            cursor.close();
            mVodDBHelper.close();
        }
    }

    /* access modifiers changed from: private */
    public synchronized void deleteData() {
        LOG.mo8825d("[deleteData]");
        if (!VodRecordDbHelper.isOpen()) {
            mVodDBHelper.openDb();
        }
        EPGBean epgBean = this.mCurLiveProgram;
        String channelId = epgBean.getChannelId();
        long utc = epgBean.getUtc();
        String programMask = new StringBuilder(String.valueOf(utc)).append(channelId).toString();
        LOG.mo8825d("[PROGRAM_MASK]" + programMask);
        LOG.mo8825d("[utc]" + utc);
        Cursor cursor = mVodDBHelper.queryOneData(programMask);
        LOG.mo8825d("-[cursor]" + cursor);
        LOG.mo8825d("[cur]" + mVodDBHelper.query(null, null, null, null));
        mVodDBHelper.deleteOneDataByMask(programMask);
        mVodDBHelper.close();
        cursor.close();
    }

    private void setPagePlayCallBackListener(PagePlayCallBackListener backListener2) {
        this.backListener = backListener2;
    }

    public boolean isAtBufferPage() {
        return this.isAtBufferPage;
    }
}
