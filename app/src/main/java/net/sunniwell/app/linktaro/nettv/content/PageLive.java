package net.sunniwell.app.linktaro.nettv.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.sunniwell.aidl.bean.AdBean;
import net.sunniwell.aidl.bean.EPGBean;
import net.sunniwell.aidl.bean.MediaBean;
import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.Constants.C0412Constants;
import net.sunniwell.app.linktaro.nettv.entry.NettvActivity;
import net.sunniwell.app.linktaro.nettv.manager.DataManager;
import net.sunniwell.app.linktaro.nettv.processor.imp.DBProcessor;
import net.sunniwell.app.linktaro.tools.DateTimeUtil;
import net.sunniwell.app.linktaro.tools.JsonUtil;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import net.sunniwell.app.linktaro.tools.SharedPreUtil;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.common.tools.DateTime;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

public class PageLive extends PageBase implements OnFocusChangeListener, OnClickListener {
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(PageLive.class);
    private static final int VIEW_COUNT = 10;
    public static EPGBean mCurLiveProgram;
    /* access modifiers changed from: private */
    public PageLiveCallBackListener backListener;
    public int channelInx;
    /* access modifiers changed from: private */
    public boolean isShowingPlayPageList = false;
    /* access modifiers changed from: private */
    public int liveChannelNum;
    /* access modifiers changed from: private */
    public int livePosition;
    /* access modifiers changed from: private */
    public HashMap<String, AdBean> mAdBeanMap = new HashMap<>();
    /* access modifiers changed from: private */
    public ImageView mAdImg0;
    private FrameLayout mAdLayout;
    private HashMap<Integer, ArrayList<MediaBean>> mAllLiveChannelDataMap;
    private LinearLayout mChannelListlayout;
    private View mContentView;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public MediaBean mCurChannel;
    /* access modifiers changed from: private */
    public DBProcessor mDBProcessor;
    /* access modifiers changed from: private */
    public DataManager mDataManager;
    private HandlerThread mDownThread;
    /* access modifiers changed from: private */
    public ImageView mIvArrow;
    /* access modifiers changed from: private */
    public ImageView mIvCurQuality;
    /* access modifiers changed from: private */
    public ImageView mIvListLogo;
    /* access modifiers changed from: private */
    public ImageView mIvQulity;
    /* access modifiers changed from: private */
    public boolean mLastStatus;
    private TextView mLimitTimeTv;
    /* access modifiers changed from: private */
    public ListView mLiveListView;
    /* access modifiers changed from: private */
    public MoreLiveAdapter mLivePageAdapter;
    /* access modifiers changed from: private */
    public ArrayList<MediaBean> mLivePageChannelList;
    /* access modifiers changed from: private */
    public ImageView mLoadingIv;
    /* access modifiers changed from: private */
    public NettvActivity mMainApp;
    /* access modifiers changed from: private */
    public AnimationDrawable mPageLoaddingAnim;
    /* access modifiers changed from: private */
    public List<String> mPreImgUrlList;
    /* access modifiers changed from: private */
    public TextView mProgramChannelName;
    /* access modifiers changed from: private */
    public TextView mProgramDate;
    /* access modifiers changed from: private */
    public TextView mProgramInfo;
    /* access modifiers changed from: private */
    public LinearLayout mProgramInformationLayout;
    /* access modifiers changed from: private */
    public TextView mProgramTime;
    /* access modifiers changed from: private */
    public RelativeLayout mRl_live_root;
    private SharedPreUtil mSharedPreUtil;
    private Timer mTimer;
    /* access modifiers changed from: private */
    public TextView mTvChannelNum;
    /* access modifiers changed from: private */
    public TextView mTvCurtime;
    /* access modifiers changed from: private */
    public int mTvFormt;
    /* access modifiers changed from: private */
    public ImageView mTvIcon;
    /* access modifiers changed from: private */
    public UiHandler mUiHandler;
    /* access modifiers changed from: private */
    public WorkHandler mWorkHandler;
    /* access modifiers changed from: private */
    public int pageLiveCount;
    /* access modifiers changed from: private */
    public int pageLiveIndex;
    /* access modifiers changed from: private */
    public int preChannelInx;
    /* access modifiers changed from: private */
    public String timeStr;

    private class MoreLiveAdapter extends BaseAdapter implements OnItemSelectedListener, OnItemClickListener {
        private LayoutInflater inflater;

        public class ViewHolder {
            public TextView channelNum;
            public TextView newName;

            public ViewHolder() {
            }
        }

        public MoreLiveAdapter() {
            this.inflater = (LayoutInflater) PageLive.this.mMainApp.getSystemService("layout_inflater");
        }

        public int getCount() {
            int ori = PageLive.this.pageLiveIndex * 10;
            if (PageLive.this.mLivePageChannelList.size() - ori < 10) {
                return PageLive.this.mLivePageChannelList.size() - ori;
            }
            return 10;
        }

        public Object getItem(int position) {
            return PageLive.this.mLivePageChannelList.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = this.inflater.inflate(R.layout.livelist, null);
                holder = new ViewHolder();
                holder.channelNum = (TextView) convertView.findViewById(R.id.tv_live_channelnum);
                holder.newName = (TextView) convertView.findViewById(R.id.newname);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (PageLive.this.pageLiveIndex >= 0 && PageLive.this.pageLiveIndex <= PageLive.this.pageLiveCount - 1) {
                int index = position + (PageLive.this.pageLiveIndex * 10);
                holder.channelNum.setText(PageLive.getChannelNumStr(((MediaBean) PageLive.this.mLivePageChannelList.get(index)).getChannelNumber(), PageLive.this.mTvFormt));
                holder.newName.setText(((MediaBean) PageLive.this.mLivePageChannelList.get(index)).getTitle());
            }
            PageLive.LOG.mo8825d("--------!!!!!!!!!!!!!!!!!!!!!mTvFormt=" + PageLive.this.mTvFormt);
            switch (PageLive.this.mTvFormt) {
                case 1:
                    convertView.setBackgroundResource(R.drawable.live_bs_list_sel);
                    break;
                case 2:
                    convertView.setBackgroundResource(R.drawable.live_cs_list_sel);
                    break;
                case 3:
                    convertView.setBackgroundResource(R.drawable.live_ds_list_sel);
                    break;
            }
            return convertView;
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long arg3) {
            PageLive.this.livePosition = position;
            PageLive.this.channelInx = (PageLive.this.pageLiveIndex * 10) + position;
            PageLive.this.mCurChannel = (MediaBean) PageLive.this.mLivePageChannelList.get(PageLive.this.channelInx);
            PageLive.this.mWorkHandler.removeMessages(0);
            if (PageLive.this.mCurChannel != null) {
                Message msg = new Message();
                msg.what = 0;
                msg.arg1 = PageLive.this.channelInx;
                msg.obj = PageLive.this.mCurChannel;
                PageLive.this.mWorkHandler.sendMessageDelayed(msg, 300);
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
            PageLive.LOG.mo8825d("[channelInx]" + PageLive.this.channelInx);
            PageLive.this.setListViewSelection(PageLive.this.channelInx);
            PageLive.this.channelInx = (PageLive.this.pageLiveIndex * 10) + position;
            PageLive.this.preChannelInx = PageLive.this.channelInx;
            PageLive.this.mCurChannel = (MediaBean) PageLive.this.mLivePageChannelList.get(PageLive.this.channelInx);
            if (PageLive.this.mLivePageChannelList != null && PageLive.this.mLivePageChannelList.size() > 0) {
                HashMap<MediaBean, EPGBean> playDataMap = new HashMap<>();
                playDataMap.put(PageLive.this.mCurChannel, PageLive.mCurLiveProgram);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = playDataMap;
                Bundle mBundle = new Bundle();
                mBundle.putString(MailDbHelper.TYPE, "1");
                mBundle.putInt("clickPage", 0);
                msg.setData(mBundle);
                PageLive.this.mDataManager.setPlayingChannel(PageLive.this.mCurChannel);
                PageLive.this.backListener.livePageCallBack(1, msg);
                PageLive.this.mDBProcessor.setProp("TvFormt", PageLive.this.mTvFormt);
                PageLive.this.onStop();
            }
        }
    }

    public interface PageLiveCallBackListener {
        void livePageCallBack(int i, Message message);
    }

    @SuppressLint({"HandlerLeak"})
    public class UiHandler extends Handler {
        public UiHandler() {
        }

        public void handleMessage(Message msg) {
            String sb;
            PageLive.LOG.mo8825d("[msg.waht]" + msg.what);
            switch (msg.what) {
                case 0:
                    PageLive.this.mProgramInformationLayout.setVisibility(4);
                    PageLive.this.mIvArrow.setVisibility(8);
                    break;
                case 1:
                    PageLive.this.mProgramInformationLayout.setVisibility(0);
                    PageLive.this.mProgramInformationLayout.bringToFront();
                    PageLive.this.mIvArrow.setVisibility(0);
                    if (PageLive.this.mLiveListView != null && !PageLive.this.mLiveListView.isFocused() && PageLive.this.getVisibility() == 0) {
                        PageLive.this.mLiveListView.setFocusable(true);
                        PageLive.this.mLiveListView.requestFocus();
                        break;
                    }
                case 2:
                    PageLive.this.mCurChannel = (MediaBean) msg.obj;
                    if (PageLive.this.mCurChannel != null) {
                        String chinnalName = PageLive.this.mCurChannel.getTitle();
                        String iconUrl = PageLive.this.mCurChannel.getImage();
                        PageLive.this.mProgramChannelName.setText(chinnalName);
                        PageLive.this.mProgramChannelName.getPaint().setFakeBoldText(true);
                        try {
                            Glide.with(PageLive.this.mContext).load(iconUrl).into(PageLive.this.mTvIcon);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        PageLive.this.mTvChannelNum.setText(PageLive.getChannelNumStr(PageLive.this.mCurChannel.getChannelNumber(), PageLive.this.mTvFormt));
                        String[] WEEK = PageLive.this.mContext.getResources().getStringArray(R.array.Week);
                        int month = Calendar.getInstance().get(2) + 1;
                        int day = Calendar.getInstance().get(5);
                        int wk = Calendar.getInstance().get(7);
                        StringBuilder append = new StringBuilder(String.valueOf(month)).append("月");
                        if (day < 10) {
                            StringBuilder sb2 = new StringBuilder("0");
                            sb = sb2.append(day).toString();
                        } else {
                            StringBuilder sb3 = new StringBuilder(String.valueOf(day));
                            sb = sb3.append("日").toString();
                        }
                        PageLive.this.mProgramDate.setText(append.append(sb).append("(").append(WEEK[wk - 1]).append(")").toString());
                        PageLive.this.mIvCurQuality.setImageResource(PageLive.this.getCurQualityRes());
                        PageLive.this.mTvCurtime.setText(PageLive.this.timeStr);
                        if (PageLive.mCurLiveProgram == null) {
                            PageLive.this.mProgramTime.setText(XmlPullParser.NO_NAMESPACE);
                            PageLive.this.mProgramInfo.setText(XmlPullParser.NO_NAMESPACE);
                            break;
                        } else {
                            PageLive.this.mProgramTime.setText(new StringBuilder(String.valueOf(DateTimeUtil.UtcToDate(PageLive.mCurLiveProgram.getUtc()))).append("-").append(DateTimeUtil.UtcToDate(PageLive.mCurLiveProgram.getEndUtc())).toString());
                            PageLive.this.mProgramInfo.setText(PageLive.mCurLiveProgram.getTitle());
                            break;
                        }
                    }
                    break;
                case 4:
                    if (PageLive.this.mLivePageChannelList != null && PageLive.this.mLivePageChannelList.size() > 0) {
                        MediaBean media2 = (MediaBean) msg.obj;
                        int mediaIndex = 0;
                        float count = ((float) PageLive.this.mLivePageChannelList.size()) / 10.0f;
                        PageLive.this.pageLiveCount = count % 1.0f == 0.0f ? (int) count : (int) (1.0f + count);
                        if (media2 == null) {
                            PageLive.this.pageLiveIndex = 0;
                        } else {
                            mediaIndex = PageLive.this.mLivePageChannelList.indexOf(media2);
                            if (mediaIndex == -1) {
                                mediaIndex = PageLive.this.mLivePageChannelList.indexOf(media2);
                            }
                            if (mediaIndex == -1) {
                                PageLive.this.pageLiveIndex = 0;
                            } else {
                                PageLive.this.pageLiveIndex = mediaIndex / 10;
                            }
                        }
                        PageLive.this.mLivePageAdapter = new MoreLiveAdapter();
                        PageLive.this.mLiveListView.setAdapter(PageLive.this.mLivePageAdapter);
                        PageLive.this.mLiveListView.setOnItemSelectedListener(PageLive.this.mLivePageAdapter);
                        PageLive.this.mLiveListView.setOnItemClickListener(PageLive.this.mLivePageAdapter);
                        PageLive.this.mLivePageAdapter.notifyDataSetChanged();
                        if (media2 != null) {
                            if (mediaIndex != -1) {
                                PageLive.this.setListViewSelection(mediaIndex);
                                break;
                            } else {
                                PageLive.this.setListViewSelection(0);
                                break;
                            }
                        } else {
                            PageLive.this.setListViewSelection(PageLive.this.channelInx);
                            break;
                        }
                    } else {
                        return;
                    }
                    break;
                case 5:
                    PageLive.this.mIvListLogo.setImageResource(R.drawable.dsicon);
                    break;
                case 6:
                    PageLive.this.mIvListLogo.setImageResource(R.drawable.csicon);
                    break;
                case 7:
                    PageLive.this.mIvListLogo.setImageResource(R.drawable.bsicon);
                    break;
                case 9:
                    PageLive.this.hiddenListTable();
                    break;
                case 10:
                    PageLive.this.showListTable(false);
                    break;
                case 11:
                    if (PageLive.this.mLivePageAdapter == null) {
                        PageLive.this.mUiHandler.sendEmptyMessage(4);
                        break;
                    } else {
                        PageLive.this.mLivePageAdapter.notifyDataSetChanged();
                        break;
                    }
                case 12:
                    PageLive.this.isShowingPlayPageList = true;
                    PageLive.this.showPlayPageListTable((MediaBean) msg.obj);
                    break;
                case 15:
                    if (PageLive.this.mIvQulity != null) {
                        PageLive.this.mIvQulity.setVisibility(8);
                        PageLive.this.mRl_live_root.removeView(PageLive.this.mIvQulity);
                        PageLive.this.mIvQulity = null;
                        break;
                    }
                    break;
                case 16:
                    if (PageLive.this.mTvCurtime != null && PageLive.this.mTvCurtime.isShown()) {
                        PageLive.this.mTvCurtime.setText(PageLive.this.timeStr);
                        break;
                    }
                case 17:
                    if (PageLive.this.mPreImgUrlList != null && PageLive.this.mPreImgUrlList.size() > 0) {
                        try {
                            if (PageLive.this.mTvFormt == 3) {
                                Glide.with(PageLive.this.mContext).load(Integer.valueOf(R.drawable.dsicon)).into(PageLive.this.mIvListLogo);
                            } else if (PageLive.this.mTvFormt == 1 && PageLive.this.mPreImgUrlList.size() > 1) {
                                Glide.with(PageLive.this.mContext).load(Integer.valueOf(R.drawable.csicon)).into(PageLive.this.mIvListLogo);
                            } else if (PageLive.this.mTvFormt == 2 && PageLive.this.mPreImgUrlList.size() > 2) {
                                Glide.with(PageLive.this.mContext).load(Integer.valueOf(R.drawable.bsicon)).into(PageLive.this.mIvListLogo);
                            }
                        } catch (IllegalArgumentException e2) {
                            e2.printStackTrace();
                        }
                    }
                    AdBean mAdBean = null;
                    if (PageLive.this.mTvFormt == 3) {
                        mAdBean = (AdBean) PageLive.this.mAdBeanMap.get(C0412Constants.TV_AD_DS);
                    } else if (PageLive.this.mTvFormt == 1) {
                        mAdBean = (AdBean) PageLive.this.mAdBeanMap.get(C0412Constants.TV_AD_BS);
                    } else if (PageLive.this.mTvFormt == 2) {
                        mAdBean = (AdBean) PageLive.this.mAdBeanMap.get(C0412Constants.TV_AD_CS);
                    }
                    if (mAdBean != null && !TextUtils.isEmpty(mAdBean.getContent())) {
                        PageLive.this.mAdImg0.setVisibility(0);
                        try {
                            Glide.with(PageLive.this.mContext).load(mAdBean.getContent()).into(PageLive.this.mAdImg0);
                            break;
                        } catch (Exception e3) {
                            break;
                        }
                    } else {
                        PageLive.this.mAdImg0.setImageResource(R.drawable.page_live_default_bg);
                        PageLive.this.mAdImg0.setVisibility(0);
                        break;
                    }
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
            switch (msg.what) {
                case 0:
                    PageLive.mCurLiveProgram = PageLive.this.mDataManager.getTextEPGBean((MediaBean) msg.obj, msg.arg1);
                    Message uimsg = new Message();
                    uimsg.what = 2;
                    uimsg.obj = msg.obj;
                    PageLive.this.mUiHandler.sendMessage(uimsg);
                    return;
                case 1:
                    PageLive.LOG.mo8825d("[RELOAD_DATA]");
                    PageLive.this.mDataManager.initLiveDataFromNet();
                    boolean isHasData = PageLive.this.mDataManager.isLivePageHasData();
                    PageLive.this.onShowLoading(!isHasData);
                    if (isHasData) {
                        PageLive.this.mLivePageChannelList = PageLive.this.mDataManager.getLiveData(PageLive.this.mTvFormt);
                        PageLive.this.liveChannelNum = PageLive.this.mLivePageChannelList.size();
                    } else {
                        PageLive.this.mWorkHandler.sendEmptyMessage(1);
                    }
                    PageLive.this.mUiHandler.removeMessages(4);
                    PageLive.this.mUiHandler.sendMessage(PageLive.this.mUiHandler.obtainMessage(4));
                    return;
                case 3:
                    for (Entry<MediaBean, EPGBean> entry : PageLive.this.mDataManager.getFirstChannelProgramMapFromNet(PageLive.this.mTvFormt, PageLive.this.channelInx).entrySet()) {
                        PageLive.this.mCurChannel = (MediaBean) entry.getKey();
                        PageLive.mCurLiveProgram = (EPGBean) entry.getValue();
                    }
                    PageLive.LOG.mo8825d("[mCurChannel]" + PageLive.this.mCurChannel + "[mCurLiveProgram]" + PageLive.mCurLiveProgram);
                    if (PageLive.this.mCurChannel != null) {
                        HashMap<MediaBean, EPGBean> playDataMap = new HashMap<>();
                        playDataMap.put(PageLive.this.mCurChannel, PageLive.mCurLiveProgram);
                        Message msg2 = new Message();
                        msg2.what = 1;
                        msg2.obj = playDataMap;
                        Bundle mBundle = new Bundle();
                        mBundle.putString(MailDbHelper.TYPE, "1");
                        mBundle.putInt("clickPage", 0);
                        msg2.setData(mBundle);
                        PageLive.this.mDataManager.setPlayingChannel(PageLive.this.mCurChannel);
                        PageLive.this.backListener.livePageCallBack(1, msg2);
                        PageLive.this.mDBProcessor.setProp("TvFormt", PageLive.this.mTvFormt);
                        return;
                    }
                    return;
                case 4:
                    PageLive.this.mAdBeanMap = PageLive.this.mDataManager.downAdData();
                    PageLive.this.mUiHandler.sendEmptyMessage(17);
                    return;
                default:
                    return;
            }
        }
    }

    public int getLiveChannelNum() {
        return this.liveChannelNum;
    }

    public void setLiveChannelNum(int liveChannelNum2) {
        this.liveChannelNum = liveChannelNum2;
    }

    public int getChannelInx() {
        return this.channelInx;
    }

    public void setChannelInx(int channelInx2) {
        this.channelInx = channelInx2;
    }

    public HashMap<Integer, ArrayList<MediaBean>> getmAllLiveChannelDataMap() {
        return this.mAllLiveChannelDataMap;
    }

    public void setmAllLiveChannelDataMap(HashMap<Integer, ArrayList<MediaBean>> mAllLiveChannelDataMap2) {
        this.mAllLiveChannelDataMap = mAllLiveChannelDataMap2;
    }

    public View getmContentView() {
        return this.mContentView;
    }

    public void setmContentView(View mContentView2) {
        this.mContentView = mContentView2;
    }

    public PageLive(Context context, PageLiveCallBackListener backListener2) {
        super(context);
        setPageLiveCallBackListener(backListener2);
    }

    public void onCreate(Context context) {
        LOG.mo8825d("[onCreate]");
        this.mContentView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.page_live, this);
        this.mDownThread = new HandlerThread("WorkThread");
        this.mDownThread.start();
        this.mWorkHandler = new WorkHandler(this.mDownThread.getLooper());
        this.mUiHandler = new UiHandler();
        this.mContext = context;
        this.mMainApp = (NettvActivity) context;
        this.mSharedPreUtil = SharedPreUtil.getSharedPreUtil(context);
        this.mDBProcessor = DBProcessor.getDBProcessor(context);
        this.mAllLiveChannelDataMap = new HashMap<>();
        this.mDataManager = DataManager.getInstance(context);
        this.mTimer = new Timer();
        initUi();
        initData();
        RefreshDate();
    }

    public void onResume() {
        LOG.mo8825d("[onResume]" + getTvFormt());
        boolean livePageHasData = this.mDataManager.isLivePageHasData();
        LOG.mo8825d("[livePageHasData]" + livePageHasData);
        if (!livePageHasData) {
            this.mWorkHandler.sendEmptyMessage(1);
        }
        this.mMainApp.stopMediaPlay();
        this.mContentView.setVisibility(0);
        this.mAdLayout.setVisibility(0);
        this.mLiveListView.setVisibility(0);
        this.mChannelListlayout.setVisibility(0);
        this.mProgramInformationLayout.setVisibility(0);
        this.mLiveListView.setSelection(this.channelInx);
        this.mTvFormt = getTvFormt();
        this.mMainApp.setmTvFormt(this.mTvFormt);
        loadLiveTable();
        this.mLiveListView.setFocusable(true);
    }

    public void onStop() {
        LOG.mo8825d("[onStop]");
        this.mUiHandler.sendEmptyMessage(15);
        this.mContentView.setVisibility(8);
        this.mAdLayout.setVisibility(8);
        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(9));
    }

    public void onDestroy() {
        try {
            this.mUiHandler.removeCallbacksAndMessages(null);
            this.mWorkHandler = null;
            this.mDownThread.interrupt();
        } catch (Exception e) {
            LogcatUtils.m321d(e.toString());
        }
    }

    public void doByMessage(Message data, int MessageFlag) {
        boolean isHide = false;
        LOG.mo8825d("[doByMessage]" + MessageFlag);
        switch (MessageFlag) {
            case 0:
                if (data.arg1 == 10) {
                    isHide = true;
                }
                showListTable(isHide);
                this.mTvFormt = getTvFormt();
                loadLiveTable();
                return;
            case 1:
                int channelCount1 = this.mLivePageChannelList.size();
                LOG.mo8825d("[channelInx]" + this.channelInx + "[livePosition]" + this.livePosition + "[channelCount1]" + channelCount1 + "[preChannelInx]" + this.preChannelInx);
                if (this.mLivePageChannelList != null && this.mLivePageChannelList.size() > 0) {
                    this.channelInx = this.preChannelInx;
                    if (this.channelInx == 0) {
                        this.channelInx = channelCount1 - 1;
                    } else {
                        this.channelInx--;
                    }
                    setListViewSelection(this.channelInx);
                    this.preChannelInx = this.channelInx;
                    this.mWorkHandler.removeMessages(3);
                    this.mWorkHandler.sendEmptyMessageDelayed(3, 500);
                    return;
                }
                return;
            case 2:
                int channelCount2 = this.mLivePageChannelList.size();
                LOG.mo8825d("[channelInx]" + this.channelInx + "[livePosition]" + this.livePosition + "[channelCount1]" + channelCount2 + "[preChannelInx]" + this.preChannelInx);
                if (this.mLivePageChannelList != null && this.mLivePageChannelList.size() > 0) {
                    this.channelInx = this.preChannelInx;
                    if (this.channelInx == channelCount2 - 1) {
                        this.channelInx = 0;
                    } else {
                        this.channelInx++;
                    }
                    setListViewSelection(this.channelInx);
                    this.preChannelInx = this.channelInx;
                    this.mWorkHandler.removeMessages(3);
                    this.mWorkHandler.sendEmptyMessageDelayed(3, 500);
                    return;
                }
                return;
            case 3:
                setListViewSelection(this.channelInx);
                this.mWorkHandler.sendEmptyMessage(3);
                return;
            default:
                return;
        }
    }

    private void initUi() {
        this.mRl_live_root = (RelativeLayout) this.mContentView.findViewById(R.id.live_layout);
        this.mAdImg0 = (ImageView) this.mContentView.findViewById(R.id.imgt0);
        this.mIvArrow = (ImageView) this.mContentView.findViewById(R.id.iv_live_arrow);
        this.mAdImg0.setOnFocusChangeListener(this);
        this.mAdImg0.setOnClickListener(this);
        this.mAdLayout = (FrameLayout) this.mContentView.findViewById(R.id.ad_layout);
        this.mChannelListlayout = (LinearLayout) this.mContentView.findViewById(R.id.li_live_list_layout);
        this.mLiveListView = (ListView) this.mContentView.findViewById(R.id.channellist);
        this.mIvListLogo = (ImageView) this.mContentView.findViewById(R.id.iv_list_logo_icon);
        this.mLiveListView.setDivider(null);
        this.mLiveListView.requestFocus();
        this.mProgramInformationLayout = (LinearLayout) findViewById(R.id.program_information_layout);
        this.mTvCurtime = (TextView) findViewById(R.id.tv_live_curtime);
        this.mIvCurQuality = (ImageView) findViewById(R.id.iv_live_quality);
        this.mTvChannelNum = (TextView) findViewById(R.id.tv_live_programnum);
        this.mProgramChannelName = (TextView) findViewById(R.id.program_chinnal_name);
        this.mProgramDate = (TextView) findViewById(R.id.program_date);
        this.mProgramTime = (TextView) findViewById(R.id.program_time);
        this.mProgramInfo = (TextView) findViewById(R.id.program_info);
        this.mLimitTimeTv = (TextView) findViewById(R.id.tv_valid_day);
        this.mTvIcon = (ImageView) findViewById(R.id.program_channel_icon);
        this.mLoadingIv = (ImageView) this.mContentView.findViewById(R.id.loading_iv);
    }

    private void initData() {
        LOG.mo8825d("*****initData*****");
        initAdUrlList();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        LOG.mo8825d("[Visibility]" + getVisibility());
        if (getVisibility() != 0) {
            return super.dispatchKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        LOG.mo8825d("[keycode]" + keyCode);
        LOG.mo8825d("[KeyEvent..Action]" + event.getAction());
        if (event.getAction() == 1) {
            switch (keyCode) {
                case 183:
                case 184:
                case 186:
                    return true;
            }
        } else if (event.getAction() == 0) {
            switch (keyCode) {
                case 4:
                    this.mMainApp.exitNettv();
                    return true;
                case 19:
                    if (this.mLiveListView.getFirstVisiblePosition() == this.livePosition) {
                        LOG.mo8825d("[livePosition]" + this.livePosition);
                        if (this.pageLiveIndex > 0) {
                            this.pageLiveIndex--;
                            this.mLiveListView.setSelection(9);
                        } else if (this.pageLiveIndex == 0 && this.isShowingPlayPageList) {
                            int tvFormt = this.mMainApp.getmTvFormt();
                            if (tvFormt == 2) {
                                this.channelInx = 0;
                                this.mTvFormt = 3;
                                this.mMainApp.setmTvFormt(3);
                                loadLiveTable();
                            } else if (tvFormt == 1) {
                                this.channelInx = 0;
                                this.mTvFormt = 2;
                                this.mMainApp.setmTvFormt(2);
                                loadLiveTable();
                            } else if (tvFormt == 3) {
                                this.channelInx = 0;
                                this.mTvFormt = 1;
                                this.mMainApp.setmTvFormt(1);
                                loadLiveTable();
                            }
                        }
                        LOG.mo8825d("[mLiveListView.getLastVisiblePosition]" + this.mLiveListView.getLastVisiblePosition());
                        this.mLivePageAdapter.notifyDataSetChanged();
                        return true;
                    }
                    break;
                case 20:
                    if (this.mLiveListView.getLastVisiblePosition() == this.livePosition) {
                        LOG.mo8825d("[ivePosition]" + this.livePosition);
                        LOG.mo8825d("[pageLiveIndex]" + this.pageLiveIndex + "[pageLiveCount]" + this.pageLiveCount);
                        if (this.pageLiveIndex < this.pageLiveCount - 1) {
                            this.pageLiveIndex++;
                        } else if (this.pageLiveIndex == this.pageLiveCount - 1) {
                            if (this.mTvFormt == 2) {
                                this.channelInx = 0;
                                this.mTvFormt = 3;
                                this.mMainApp.setmTvFormt(3);
                                loadLiveTable();
                                return true;
                            } else if (this.mTvFormt == 1) {
                                this.channelInx = 0;
                                this.mTvFormt = 2;
                                this.mMainApp.setmTvFormt(2);
                                loadLiveTable();
                                return true;
                            } else if (this.mTvFormt != 3) {
                                return true;
                            } else {
                                this.channelInx = 0;
                                this.mTvFormt = 1;
                                this.mMainApp.setmTvFormt(1);
                                loadLiveTable();
                                return true;
                            }
                        }
                        this.mLivePageAdapter.notifyDataSetChanged();
                        this.mLiveListView.setSelection(this.mLiveListView.getFirstVisiblePosition());
                        return true;
                    }
                    break;
                case 21:
                    if (this.mTvFormt == 2) {
                        this.channelInx = 0;
                        this.mTvFormt = 1;
                        this.mMainApp.setmTvFormt(1);
                        loadLiveTable();
                    } else if (this.mTvFormt == 1) {
                        this.channelInx = 0;
                        this.mTvFormt = 3;
                        this.mMainApp.setmTvFormt(3);
                        loadLiveTable();
                    } else if (this.mTvFormt == 3) {
                        this.channelInx = 0;
                        this.mTvFormt = 2;
                        this.mMainApp.setmTvFormt(2);
                        loadLiveTable();
                    }
                    this.mIvCurQuality.setImageResource(getCurQualityRes());
                    return true;
                case 22:
                case 184:
                    if (this.mTvFormt == 2) {
                        this.channelInx = 0;
                        this.mTvFormt = 3;
                        this.mMainApp.setmTvFormt(3);
                        loadLiveTable();
                    } else if (this.mTvFormt == 1) {
                        this.channelInx = 0;
                        this.mTvFormt = 2;
                        this.mMainApp.setmTvFormt(2);
                        loadLiveTable();
                    } else if (this.mTvFormt == 3) {
                        this.channelInx = 0;
                        this.mTvFormt = 1;
                        this.mMainApp.setmTvFormt(1);
                        loadLiveTable();
                    }
                    this.mIvCurQuality.setImageResource(getCurQualityRes());
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    /* access modifiers changed from: private */
    public void setListViewSelection(int position) {
        if (getVisibility() == 0) {
            LOG.mo8825d("[position]" + position);
            if (position + 1 > this.mLivePageChannelList.size()) {
                position = 0;
            }
            this.pageLiveIndex = position / 10;
            this.mLivePageAdapter.notifyDataSetChanged();
            this.mLiveListView.setFocusable(true);
            boolean requestFocus = this.mLiveListView.requestFocus();
            this.mLiveListView.setSelection(position - (this.pageLiveIndex * 10));
        }
    }

    /* access modifiers changed from: private */
    public void showListTable(boolean isHide) {
        LOG.mo8825d("[showListTable]");
        if (isHide) {
            this.mProgramInformationLayout.setVisibility(8);
        } else {
            this.mProgramInformationLayout.setVisibility(0);
        }
        this.mContentView.setVisibility(0);
        this.mLiveListView.setVisibility(0);
        this.mChannelListlayout.setVisibility(0);
        this.mAdLayout.setVisibility(4);
        this.mLiveListView.setFocusable(true);
        this.mLiveListView.requestFocus();
    }

    /* access modifiers changed from: private */
    public void hiddenListTable() {
        LOG.mo8825d("[hiddenListTable]");
        PagePlayVideo.isShowingLiveList = false;
        this.mLiveListView.setFocusable(false);
        this.mLiveListView.setVisibility(4);
        this.mChannelListlayout.setVisibility(4);
    }

    /* access modifiers changed from: private */
    public void showPlayPageListTable(MediaBean media) {
        LOG.mo8825d("[showPlayPageListTable]");
        this.mTvFormt = getTvFormt();
        loadAllLiveChannelData(media);
        checkView();
        this.mIvArrow.setVisibility(0);
        this.mProgramInformationLayout.setVisibility(0);
        this.mContentView.setVisibility(0);
        this.mLiveListView.setVisibility(0);
        this.mChannelListlayout.setVisibility(0);
        this.mAdLayout.setVisibility(4);
        this.mLiveListView.setFocusable(true);
        this.mLiveListView.requestFocus();
    }

    private void initAdUrlList() {
        String adUrl = this.mSharedPreUtil.getStbImgs("StbLivePageAdImgs");
        LOG.mo8825d("[adUrl]" + adUrl);
        if (StringUtils.isNotEmpty(adUrl)) {
            this.mPreImgUrlList = JsonUtil.getList(adUrl);
        } else {
            this.mPreImgUrlList = new ArrayList();
            this.mPreImgUrlList.add(0, XmlPullParser.NO_NAMESPACE);
        }
        this.mLimitTimeTv.setText(DateTimeUtil.getLimitDay());
        this.mWorkHandler.sendEmptyMessage(4);
    }

    private void loadAllLiveChannelData(MediaBean media) {
        if (this.mDataManager.isLivePageHasData()) {
            this.mLivePageChannelList = this.mDataManager.getLiveData(this.mTvFormt);
        }
        LOG.mo8825d("[mLivePageChannelList]" + this.mLivePageChannelList + "[mTvFormt]" + this.mTvFormt);
        if (this.mLivePageChannelList != null && this.mLivePageChannelList.size() > 0) {
            this.liveChannelNum = this.mLivePageChannelList.size();
            this.mCurChannel = (MediaBean) this.mLivePageChannelList.get(0);
            if (media == null) {
                this.mUiHandler.sendEmptyMessage(4);
                return;
            }
            Message msg = Message.obtain();
            msg.what = 4;
            msg.obj = media;
            this.mUiHandler.sendMessage(msg);
        }
    }

    /* access modifiers changed from: private */
    public void onShowLoading(final boolean isVisible) {
        this.mMainApp.runOnUiThread(new Runnable() {
            public void run() {
                if (PageLive.this.mLastStatus != isVisible) {
                    PageLive.this.mLoadingIv.setVisibility(isVisible ? 0 : 8);
                    PageLive.this.mLoadingIv.setBackgroundResource(R.drawable.page_loading);
                    PageLive.this.mPageLoaddingAnim = (AnimationDrawable) PageLive.this.mLoadingIv.getBackground();
                    if (isVisible) {
                        PageLive.this.mPageLoaddingAnim.start();
                    } else {
                        PageLive.this.mPageLoaddingAnim.stop();
                    }
                    PageLive.this.mLastStatus = isVisible;
                }
            }
        });
    }

    private int checkView() {
        LOG.mo8825d("----mTvFormt = " + this.mTvFormt);
        if (this.mTvFormt == 3) {
            this.mUiHandler.sendEmptyMessage(5);
        } else if (this.mTvFormt == 1) {
            this.mUiHandler.sendEmptyMessage(7);
        } else if (this.mTvFormt == 2) {
            this.mUiHandler.sendEmptyMessage(6);
        }
        return 0;
    }

    private int getTvFormt() {
        String str = this.mDBProcessor.getProp("TvFormt");
        LOG.mo8825d("[getTvFormt]" + str);
        if (StringUtils.isNotEmpty(str)) {
            return Integer.parseInt(str, 10);
        }
        return 0;
    }

    public void livePageAdOnClick(View view) {
        new Message().what = 8;
        view.getId();
    }

    private void initLiveTable() {
        loadAllLiveChannelData(null);
    }

    private void loadLiveTable() {
        LOG.mo8825d("[loadLiveTable]");
        this.mUiHandler.removeMessages(17);
        this.mUiHandler.sendEmptyMessage(17);
        initLiveTable();
        checkView();
    }

    public ArrayList<MediaBean> getmLivePageChannelList() {
        return this.mLivePageChannelList;
    }

    private void RefreshDate() {
        if (this.mTimer != null) {
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    Calendar calendar = Calendar.getInstance();
                    int hour = calendar.get(11);
                    int minute = calendar.get(12);
                    PageLive.this.timeStr = (hour < 10 ? "0" + hour : Integer.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : Integer.valueOf(minute));
                    PageLive.this.mUiHandler.sendEmptyMessage(16);
                }
            }, 100, DateTime.MILLIS_PER_MINUTE);
        }
    }

    /* access modifiers changed from: private */
    public int getCurQualityRes() {
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

    public static String getChannelNumStr(int channelNum, int tvformt) {
        LOG.mo8825d("[tvformt]" + tvformt);
        String str = XmlPullParser.NO_NAMESPACE;
        if (tvformt != 3) {
            return String.valueOf(channelNum);
        }
        if (channelNum / 10 < 1) {
            return "00" + channelNum;
        }
        return "0" + channelNum;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        LOG.mo8825d("[view]" + v.getId() + "[hasFocus]" + hasFocus);
    }

    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    public void setPageLiveCallBackListener(PageLiveCallBackListener backListener2) {
        this.backListener = backListener2;
    }
}
