package net.sunniwell.app.linktaro.nettv.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.sunniwell.aidl.bean.EPGBean;
import net.sunniwell.aidl.bean.MediaBean;
import net.sunniwell.aidl.bean.UrlBean;
import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.Constants.C0412Constants;
import net.sunniwell.app.linktaro.nettv.download.DownUtils;
import net.sunniwell.app.linktaro.nettv.download.DownloadTask;
import net.sunniwell.app.linktaro.nettv.entry.NettvActivity;
import net.sunniwell.app.linktaro.nettv.manager.DataManager;
import net.sunniwell.app.linktaro.nettv.view.CustomResDialog;
import net.sunniwell.app.linktaro.tools.DateTimeUtil;
import net.sunniwell.app.linktaro.tools.SharedPreUtil;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.common.tools.DateTime;

import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.objectweb.asm.Opcodes;
import org.xmlpull.v1.XmlPullParser;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PageCategory extends PageBase {
    /* access modifiers changed from: private */
    public static String[] DATES = new String[7];
    private static String[] DATE_STR = new String[7];
    /* access modifiers changed from: private */
    public static final int[] DAYS = {R.id.day01, R.id.day02, R.id.day03, R.id.day04, R.id.day05, R.id.day06, R.id.day07};
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(PageEPG.class);
    private static final int VIEW_COUNT = 10;
    private static String[] WEEK_STR = new String[7];
    private static long intervalofTime = 0;
    /* access modifiers changed from: private */
    public PageCategoryCallBackListener backListener;
    /* access modifiers changed from: private */
    public String dateStr;
    private int delayTime2 = 60;
    /* access modifiers changed from: private */
    public boolean isAtEpgPage;
    /* access modifiers changed from: private */
    public boolean isAtPzqbPage;
    /* access modifiers changed from: private */
    public boolean isAtReservationHintPage;
    /* access modifiers changed from: private */
    public boolean isAtVodPage;
    private boolean isDelay = true;
    private ArrayList<EPGBean> mCategoryDataList;
    private int mCategoryInx = 0;
    private List<String> mCategoryNameList;
    /* access modifiers changed from: private */
    public EPGBean mCategoryProgram;
    private TextView mChannelNameView;
    private View mContentView;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public int mCurPosition;
    /* access modifiers changed from: private */
    public CustomResDialog mCustomResDialog;
    /* access modifiers changed from: private */
    public DataManager mDataManager;
    /* access modifiers changed from: private */
    public int mDateInx;
    private ImageView mDownIcon;
    private HandlerThread mDownThread;
    /* access modifiers changed from: private */
    public DownUtils mDownUtils;
    /* access modifiers changed from: private */
    public TextView mEpgDateView;
    private RelativeLayout mEpgLayout;
    /* access modifiers changed from: private */
    public ListView mEpgListView;
    /* access modifiers changed from: private */
    public MoreVodAdapter mEpgPageAdapter;
    /* access modifiers changed from: private */
    public TextView mEpgTimeView;
    private ImageView mEpgTitleIcon;
    private TextView mFileDateView;
    /* access modifiers changed from: private */
    public TextView mFileDesView;
    private ImageView mFileIconView;
    private TextView mFileIndexView;
    private TextView mFileNameView;
    private ImageView mIvQulity;
    /* access modifiers changed from: private */
    public NettvActivity mMainApp;
    private TextView mNwNameView;
    /* access modifiers changed from: private */
    public LinearLayout mPzqbPage;
    private RelativeLayout mRlCategoryRoot;
    private SharedPreUtil mSharedPreUtil;
    private Timer mTimer;
    /* access modifiers changed from: private */
    public int mTvFormt;
    private TextView mTvPzqbTime;
    /* access modifiers changed from: private */
    public UiHandler mUiHandler;
    /* access modifiers changed from: private */
    public WorkHandler mWorkHandler;
    /* access modifiers changed from: private */
    public int pageVodCount;
    /* access modifiers changed from: private */
    public int pageVodIndex;
    private int preEpgPageChannelInx;
    private int predateInx;
    /* access modifiers changed from: private */
    public int recordInx;
    /* access modifiers changed from: private */
    public String timeStr;
    /* access modifiers changed from: private */
    public int vodPosition;
    private LinearLayout weekLabel;

    private class MoreVodAdapter extends BaseAdapter implements OnItemSelectedListener, OnItemClickListener {
        private Context context;
        private int index;
        private ArrayList<EPGBean> list;
        private String mClickCategoryMark = XmlPullParser.NO_NAMESPACE;
        private TextView mTvRecord;

        public MoreVodAdapter(Context context2, ArrayList<EPGBean> list2) {
            this.context = context2;
            this.list = list2;
        }

        public ArrayList<EPGBean> getList() {
            return this.list;
        }

        public void setList(ArrayList<EPGBean> list2) {
            this.list = list2;
        }

        public int getCount() {
            int ori = PageCategory.this.pageVodIndex * 10;
            if (this.list == null || this.list.size() < 1) {
                return 0;
            }
            if (this.list.size() <= 10) {
                return this.list.size();
            }
            if (this.list.size() - ori < 10) {
                return this.list.size() - ori;
            }
            return 10;
        }

        public Object getItem(int position) {
            return this.list.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            PageCategory.LOG.mo8825d("********getView***position===***" + position + "****pageVodIndex===****" + PageCategory.this.pageVodIndex);
            if (convertView == null) {
                view = View.inflate(this.context, R.layout.categorylistitem, null);
            } else {
                view = convertView;
                if (((Integer) view.getTag()).intValue() == PageCategory.this.pageVodIndex) {
                    return view;
                }
            }
            ImageView channelIcon = (ImageView) view.findViewById(R.id.channel_icon);
            TextView date = (TextView) view.findViewById(R.id.channelnum);
            TextView newName = (TextView) view.findViewById(R.id.newname);
            if (position == 0) {
                PageCategory.LOG.mo8825d("position == 0");
                this.mTvRecord = newName;
            }
            ImageView mDownView = (ImageView) view.findViewById(R.id.down_icon);
            date.getPaint().setFakeBoldText(true);
            newName.getPaint().setFakeBoldText(true);
            if (PageCategory.this.pageVodIndex >= 0 && PageCategory.this.pageVodIndex <= PageCategory.this.pageVodCount) {
                PageCategory.LOG.mo8825d("pageVodIndex >= 0");
                this.index = (PageCategory.this.pageVodIndex * 10) + position;
                if (this.index < this.list.size()) {
                    EPGBean epgBean = (EPGBean) this.list.get(this.index);
                    newName.setText(epgBean.getTitle());
                    String startTimeStr = DateTimeUtil.UtcToDate(epgBean.getUtc());
                    date.setText(new StringBuilder(String.valueOf(startTimeStr)).append("~").append(DateTimeUtil.UtcToDate(epgBean.getEndUtc())).toString());
                    String channelId = epgBean.getChannelId();
                    PageCategory.LOG.mo8825d("........channelId===......." + channelId);
                    MediaBean mediaBean = null;
                    if (PageCategory.this.mDataManager.isLivePageHasData()) {
                        mediaBean = PageCategory.this.mDataManager.getChannelById(channelId, PageCategory.this.mTvFormt);
                        PageCategory.LOG.mo8825d("mediaBean------>" + mediaBean);
                    }
                    if (mediaBean != null && StringUtils.isNotEmpty(mediaBean.getImage())) {
                        Glide.with(PageCategory.this.mContext).load(mediaBean.getImage()).centerCrop().into(channelIcon);
                        channelIcon.setVisibility(0);
                    }
                    mDownView.setVisibility(8);
                    String fileMask = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(PageCategory.DATES[PageCategory.this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(DateTimeUtil.UtcToDate(epgBean.getUtc())).append(":00").toString()))).toString())).append(channelId).toString();
                    if (PageCategory.this.isAtVodPage) {
                        PageCategory.LOG.mo8825d("isAtVodPage");
                        if (DownUtils.usbChecked() && PageCategory.this.mDownUtils.isContainsKey(fileMask)) {
                            if (PageCategory.this.mDownUtils.getDownFlagByKey(fileMask) == 2) {
                                mDownView.setImageResource(R.drawable.selected);
                            } else {
                                mDownView.setImageResource(R.drawable.wait);
                            }
                            mDownView.setVisibility(0);
                        } else if (!PageCategory.this.mMainApp.isPlaying() || !fileMask.equals(this.mClickCategoryMark)) {
                            mDownView.setVisibility(8);
                        } else {
                            mDownView.setVisibility(0);
                            mDownView.setImageResource(R.drawable.state_play_no_focus);
                        }
                    } else if (PageCategory.this.isAtEpgPage) {
                        PageCategory.LOG.mo8825d("isAtEpgPage");
                        if (DownUtils.usbChecked() && PageCategory.this.mDownUtils.isContainsKey(fileMask)) {
                            if (PageCategory.this.mDownUtils.getDownFlagByKey(fileMask) == 2) {
                                mDownView.setImageResource(R.drawable.selected);
                            } else {
                                mDownView.setImageResource(R.drawable.wait);
                            }
                            mDownView.setVisibility(0);
                        } else if (!PageCategory.this.mMainApp.isPlaying() || !fileMask.equals(this.mClickCategoryMark)) {
                            mDownView.setVisibility(8);
                        } else {
                            mDownView.setVisibility(0);
                            mDownView.setImageResource(R.drawable.state_play_no_focus);
                        }
                    }
                }
            }
            view.setTag(Integer.valueOf(PageCategory.this.pageVodIndex));
            return view;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            PageCategory.this.recordInx = (PageCategory.this.pageVodIndex * 10) + position;
            PageCategory.LOG.mo8825d("recordInx----->" + PageCategory.this.recordInx);
            PageCategory.this.mCurPosition = position;
            PageCategory.this.mCategoryProgram = (EPGBean) this.list.get(PageCategory.this.recordInx);
            String mClickdaysToUtc = new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(PageCategory.DATES[PageCategory.this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(DateTimeUtil.UtcToDate(PageCategory.this.mCategoryProgram.getUtc())).append(":00").toString()))).toString();
            this.mClickCategoryMark = new StringBuilder(String.valueOf(mClickdaysToUtc)).append(PageCategory.this.mCategoryProgram.getChannelId()).toString();
            PageCategory.this.play(position);
        }

        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
            PageCategory.this.vodPosition = position;
            PageCategory.this.recordInx = (PageCategory.this.pageVodIndex * 10) + position;
            PageCategory.LOG.mo8825d("...localPosition...====" + PageCategory.this.vodPosition);
            if (this.mTvRecord != null) {
                PageCategory.LOG.mo8825d("mTvRecord-------------->" + this.mTvRecord.isSelected());
                this.mTvRecord.setSingleLine(true);
                this.mTvRecord.setEllipsize(TruncateAt.MARQUEE);
                this.mTvRecord.setMarqueeRepeatLimit(-1);
                try {
                    Method method = this.mTvRecord.getClass().getDeclaredMethod("startMarquee", null);
                    method.setAccessible(true);
                    method.invoke(this.mTvRecord, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
        }
    }

    public interface PageCategoryCallBackListener {
        void categoryPageCallBack(int i, Message message);
    }

    @SuppressLint({"HandlerLeak"})
    public class UiHandler extends Handler {
        public UiHandler() {
        }

        public void handleMessage(Message msg) {
            PageCategory.LOG.mo8825d("...msg.waht===" + msg.what);
            switch (msg.what) {
                case 1:
                    if (PageCategory.this.mEpgPageAdapter != null) {
                        PageCategory.this.mEpgPageAdapter.notifyDataSetChanged();
                        PageCategory.this.mEpgListView.setAdapter(PageCategory.this.mEpgPageAdapter);
                        PageCategory.this.mEpgListView.requestFocus();
                        return;
                    }
                    return;
                case 2:
                    PageCategory.this.clearUpEpgListData();
                    return;
                case 3:
                    PageCategory.this.play(PageCategory.this.mCurPosition);
                    return;
                case 4:
                    PageCategory.this.showChannelView();
                    return;
                case 5:
                    PageCategory.LOG.mo8825d("******RESUME_DATE******");
                    for (int i = 1; i < 7; i++) {
                        ((Button) PageCategory.this.findViewById(PageCategory.DAYS[i])).setBackgroundResource(R.drawable.epg_daytext_sel);
                    }
                    ((Button) PageCategory.this.findViewById(PageCategory.DAYS[0])).setBackgroundResource(R.drawable.epg_date_checked);
                    return;
                case 6:
                    PageCategory.this.initPzqbPage();
                    return;
                case 7:
                    PageCategory.this.isAtPzqbPage = true;
                    PageCategory.this.mPzqbPage.setVisibility(0);
                    PageCategory.this.mFileDesView.requestFocus();
                    return;
                case 11:
                    PageCategory.this.freshDatePage();
                    return;
                case 13:
                    PageCategory.this.initDownIcon((LinearLayout) msg.obj);
                    return;
                case 14:
                    int wk = Calendar.getInstance().get(7);
                    PageCategory.this.mEpgDateView.setText(new StringBuilder(String.valueOf(PageCategory.this.dateStr)).append("(").append(PageCategory.this.mContext.getResources().getStringArray(R.array.Week)[wk - 1]).append(")").toString());
                    PageCategory.this.mEpgTimeView.setText(PageCategory.this.timeStr);
                    return;
                case 15:
                    PageCategory.this.hidePzqbPage();
                    return;
                case 17:
                    PageCategory.this.loadEpgPage();
                    return;
                case 18:
                    PageCategory.this.loadVodPage();
                    return;
                case 21:
                    PageCategory.LOG.mo8825d("....show_reservation_dialog...");
                    if (PageCategory.this.mCustomResDialog == null || !PageCategory.this.mCustomResDialog.isShowing()) {
                        PageCategory.this.mCustomResDialog = new CustomResDialog(PageCategory.this.mContext, R.style.PromptDialog);
                        PageCategory.this.mCustomResDialog.setDialogBg(R.drawable.ts_bg);
                        PageCategory.this.mCustomResDialog.setOkBgResource(R.drawable.btn_check_ok_sel);
                        PageCategory.this.mCustomResDialog.setCancelBgResource(R.drawable.btn_check_cancel_sel);
                        PageCategory.this.mCustomResDialog.setTextContent(PageCategory.this.getResources().getText(R.string.first_reservation).toString());
                        PageCategory.this.mCustomResDialog.setOkClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                boolean z = false;
                                PageCategory.this.mCustomResDialog.dismiss();
                                PageCategory.this.isAtReservationHintPage = false;
                                if (PageCategory.this.mDownUtils.isDownTaskMAX()) {
                                    PageCategory.this.backListener.categoryPageCallBack(13, null);
                                    return;
                                }
                                PageCategory.LOG.mo8825d("cy!mDownUtils.isDownTaskMAX()");
                                LinearLayout layout = null;
                                if (PageCategory.this.mEpgListView != null) {
                                    layout = (LinearLayout) PageCategory.this.mEpgListView.getSelectedView();
                                    PageCategory.LOG.mo8825d("mEpgListView.getSelectedItemPosition());" + PageCategory.this.mEpgListView.getSelectedItemPosition());
                                    SWLogger log = PageCategory.LOG;
                                    StringBuilder sb = new StringBuilder("*******initDownIcon****layout != null**");
                                    if (layout != null) {
                                        z = true;
                                    }
                                    log.mo8825d(sb.append(z).toString());
                                }
                                Message iconMsg = Message.obtain();
                                iconMsg.what = 13;
                                iconMsg.obj = layout;
                                PageCategory.this.mUiHandler.sendMessage(iconMsg);
                                PageCategory.this.mWorkHandler.sendEmptyMessage(7);
                                PageCategory.this.mEpgListView.requestFocus();
                            }
                        });
                        PageCategory.this.mCustomResDialog.setCancelClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                PageCategory.this.mCustomResDialog.dismiss();
                            }
                        });
                        PageCategory.this.mCustomResDialog.setOnDismissListener(new OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                                PageCategory.this.isAtReservationHintPage = false;
                            }
                        });
                        PageCategory.this.mCustomResDialog.show();
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public class WorkHandler extends Handler {
        @SuppressLint({"HandlerLeak"})
        public WorkHandler(Looper mLooper) {
            super(mLooper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    PageCategory.this.loadEpgData();
                    return;
                case 2:
                    PageCategory.this.loadVodData();
                    return;
                case 3:
                    PageCategory.this.decideLoadData();
                    return;
                case 4:
                    PageCategory.this.checkDownTask();
                    return;
                case 7:
                    PageCategory.this.addDownTaskToList();
                    return;
                case 8:
                    PageCategory.this.initEpgPageListAdapter();
                    return;
                default:
                    return;
            }
        }
    }

    public View getmContentView() {
        return this.mContentView;
    }

    public PageCategory(Context context, PageCategoryCallBackListener backListener2) {
        super(context);
        setPageCategoryCallBackListener(backListener2);
    }

    public void onCreate(Context context) {
        LOG.mo8825d("*******onCreate******");
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mDownThread = new HandlerThread("WorkThread");
        this.mDownThread.start();
        this.mWorkHandler = new WorkHandler(this.mDownThread.getLooper());
        this.mUiHandler = new UiHandler();
        this.mMainApp = (NettvActivity) context;
        this.mContext = context;
        this.mDownUtils = DownUtils.getDownUtilsInstance(context);
        this.mSharedPreUtil = SharedPreUtil.getSharedPreUtil(context);
        this.mTimer = new Timer();
        this.mDataManager = DataManager.getInstance(context);
        this.mContentView = layoutInflater.inflate(R.layout.page_category, this);
        initUi();
        initDate();
        initCategoryName();
        initChannelDate();
    }

    public void onResume() {
        LOG.mo8825d("onResume");
        this.mEpgListView.setFocusable(true);
        this.mTvFormt = this.mMainApp.getmTvFormt();
        LOG.mo8825d("******zhaohangqi***epgPageChannelInx===***" + this.mCategoryInx + "****mTvFormt==****" + this.mTvFormt);
        if (this.mMainApp.getCateType() == 5) {
            this.isAtEpgPage = true;
            this.isAtVodPage = false;
            if (this.mMainApp.isPlaying()) {
                this.backListener.categoryPageCallBack(7, null);
            }
            showPage();
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(17));
        } else if (this.mMainApp.getCateType() == 6) {
            this.isAtEpgPage = false;
            this.isAtVodPage = true;
            if (this.mMainApp.isPlaying()) {
                this.backListener.categoryPageCallBack(7, null);
            }
            showPage();
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(18));
        }
        this.mEpgListView.requestFocus();
    }

    public void onStop() {
        LOG.mo8825d("*****onStop******");
        this.mContentView.setVisibility(8);
        this.mEpgListView.setFocusable(false);
    }

    public void onDestroy() {
    }

    public void doByMessage(Message data, int MessageFlag) {
        switch (MessageFlag) {
            case 1:
                clearUpEpgListData();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: private */
    public void play(int position) {
        String startTime = XmlPullParser.NO_NAMESPACE;
        String endTime = XmlPullParser.NO_NAMESPACE;
        String type = XmlPullParser.NO_NAMESPACE;
        if (this.isAtVodPage) {
            type = "0";
            String startTimeStr = DateTimeUtil.UtcToDate(this.mCategoryProgram.getUtc());
            String endTimeStr = DateTimeUtil.UtcToDate(this.mCategoryProgram.getEndUtc());
            startTime = new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTimeStr).append(":00").toString();
            endTime = new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(endTimeStr).append(":00").toString();
        } else if (this.isAtEpgPage) {
            long currentUtc = System.currentTimeMillis();
            LOG.mo8825d("mEpgProgram------>" + this.mCategoryProgram.getUtc() + "------endUtc-------->" + this.mCategoryProgram.getEndUtc());
            LOG.mo8825d("now------->" + currentUtc);
            if (currentUtc <= this.mCategoryProgram.getUtc() || currentUtc >= this.mCategoryProgram.getEndUtc() || this.mDateInx != 0) {
                this.isAtPzqbPage = true;
                this.mUiHandler.removeMessages(15);
                this.mUiHandler.sendEmptyMessage(6);
                this.mUiHandler.sendEmptyMessage(7);
                this.mUiHandler.sendMessageDelayed(this.mUiHandler.obtainMessage(15), 3000);
                return;
            }
            type = "1";
        }
        MediaBean curChannel = null;
        if (this.mCategoryProgram != null) {
            String channelId = this.mCategoryProgram.getChannelId();
            if (this.mDataManager.isLivePageHasData()) {
                curChannel = this.mDataManager.getChannelById(channelId, this.mTvFormt);
            }
            if (curChannel == null) {
                LOG.mo8825d("mediaBean == null");
            } else if (curChannel.getUrls() == null || curChannel.getUrls().get(0) == null || StringUtils.isEmpty(((UrlBean) curChannel.getUrls().get(0)).getUrl())) {
                LOG.mo8825d(".......url为空........");
            } else {
                if ("1".equals(type)) {
                    LOG.mo8825d("mLastPlayChannel------->" + this.mDataManager.getPlayingChannel() + "-----mCurChannel---->" + curChannel + "------mMainApp.isPlaying()---" + this.mMainApp.isPlaying());
                    MediaBean lastPlayChannel = this.mDataManager.getPlayingChannel();
                    if (this.mMainApp.isPlaying() && lastPlayChannel != null && lastPlayChannel.equals(curChannel)) {
                        this.mMainApp.setFullPlay(true);
                        this.mCategoryInx = this.preEpgPageChannelInx;
                        this.mUiHandler.sendEmptyMessage(6);
                        this.backListener.categoryPageCallBack(3, null);
                        this.mUiHandler.removeMessages(2);
                        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                        onStop();
                        return;
                    }
                }
                HashMap<MediaBean, EPGBean> playDataMap = new HashMap<>();
                playDataMap.put(curChannel, this.mCategoryProgram);
                Message msg = new Message();
                msg.what = 2;
                msg.obj = playDataMap;
                Bundle mBundle = new Bundle();
                mBundle.putString(MailDbHelper.TYPE, type);
                if ("1".equals(type)) {
                    mBundle.putInt("clickPage", 5);
                } else {
                    mBundle.putInt("clickPage", 6);
                }
                String playDate = DATES[this.mDateInx];
                mBundle.putString("startTime", startTime);
                mBundle.putString("endTime", endTime);
                mBundle.putString("playDate", playDate);
                msg.setData(mBundle);
                if ("1".equals(type)) {
                    this.mDataManager.setPlayingChannel(curChannel);
                }
                this.backListener.categoryPageCallBack(2, msg);
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                onStop();
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LOG.mo8825d("********onKeyDown*****keyCode====***" + keyCode);
        LOG.mo8825d("********isAtPzqbPage====***" + this.isAtPzqbPage);
        switch (keyCode) {
            case 4:
                if (this.isAtPzqbPage) {
                    this.mUiHandler.sendEmptyMessage(15);
                    return true;
                } else if (this.mMainApp.isPlaying()) {
                    this.mMainApp.setFullPlay(true);
                    this.mCategoryInx = this.preEpgPageChannelInx;
                    this.mUiHandler.sendEmptyMessage(6);
                    this.backListener.categoryPageCallBack(3, null);
                    this.mUiHandler.removeMessages(2);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                    onStop();
                    return true;
                } else {
                    this.mUiHandler.removeMessages(2);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                    this.backListener.categoryPageCallBack(1, null);
                    onStop();
                    return true;
                }
            case 19:
                LOG.mo8825d("***KEYCODE_DPAD_DOWN***mEpgListView.getFirstVisiblePosition()==******" + this.mEpgListView.getFirstVisiblePosition() + "******vodPosition*****" + this.vodPosition);
                if (!this.isAtReservationHintPage) {
                    if (this.isAtPzqbPage || this.mEpgListView.getFirstVisiblePosition() != this.vodPosition) {
                        return true;
                    }
                    if (this.pageVodIndex > 0) {
                        this.pageVodIndex--;
                        this.mEpgPageAdapter.notifyDataSetChanged();
                        this.mEpgListView.setSelection(9);
                        LOG.mo8825d("***KEYCODE_DPAD_UP***pageVodIndex====*******" + this.pageVodIndex);
                        return true;
                    } else if (this.pageVodIndex != 0) {
                        return true;
                    } else {
                        if (this.isAtEpgPage) {
                            this.mDateInx--;
                            if (this.mDateInx < 0) {
                                this.mDateInx = 0;
                                return true;
                            }
                            this.isDelay = true;
                            this.mUiHandler.sendEmptyMessage(2);
                            this.mUiHandler.sendEmptyMessage(11);
                            this.mWorkHandler.sendEmptyMessage(3);
                            return true;
                        } else if (!this.isAtVodPage) {
                            return true;
                        } else {
                            this.mDateInx--;
                            if (this.mDateInx < 0) {
                                this.mDateInx = 0;
                                return true;
                            }
                            this.isDelay = true;
                            this.mUiHandler.sendEmptyMessage(2);
                            this.mUiHandler.sendEmptyMessage(11);
                            this.mWorkHandler.sendEmptyMessage(3);
                            return true;
                        }
                    }
                }
                break;
            case 20:
                LOG.mo8825d("***KEYCODE_DPAD_DOWN***mEpgListView.getLastVisiblePosition()==******" + this.mEpgListView.getLastVisiblePosition() + "******vodPosition*****" + this.vodPosition);
                if (!this.isAtReservationHintPage) {
                    if (!this.isAtPzqbPage) {
                        if (this.mEpgListView.getLastVisiblePosition() == this.vodPosition) {
                            if (this.pageVodIndex >= this.pageVodCount - 1) {
                                if (this.pageVodIndex == this.pageVodCount - 1) {
                                    LOG.mo8825d("***KEYCODE_DPAD_DOWN2***pageVodIndex==******" + this.pageVodIndex + "******pageVodCount*****" + this.pageVodCount);
                                    if (!this.isAtEpgPage) {
                                        if (this.isAtVodPage) {
                                            this.mDateInx++;
                                            if (this.mDateInx <= DATES.length - 1) {
                                                this.mUiHandler.sendEmptyMessage(2);
                                                this.mUiHandler.sendEmptyMessage(11);
                                                this.mWorkHandler.sendEmptyMessage(2);
                                                break;
                                            } else {
                                                this.mDateInx = DATES.length - 1;
                                                return true;
                                            }
                                        }
                                    } else {
                                        this.mDateInx++;
                                        if (this.mDateInx <= DATES.length - 1) {
                                            this.mUiHandler.sendEmptyMessage(2);
                                            this.mUiHandler.sendEmptyMessage(11);
                                            this.mWorkHandler.sendEmptyMessage(1);
                                            break;
                                        } else {
                                            this.mDateInx = DATES.length - 1;
                                            return true;
                                        }
                                    }
                                }
                            } else {
                                this.pageVodIndex++;
                                this.mEpgListView.setSelection(0);
                                this.mEpgPageAdapter.notifyDataSetChanged();
                                LOG.mo8825d("***KEYCODE_DPAD_DOWN1***pageVodIndex==******" + this.pageVodIndex);
                                break;
                            }
                        }
                    } else {
                        return true;
                    }
                }
                break;
            case 21:
                if (!this.isAtReservationHintPage) {
                    if (!this.isAtPzqbPage) {
                        this.mCategoryInx--;
                        if (this.mCategoryInx < 0) {
                            this.mCategoryInx = this.mCategoryNameList.size() - 1;
                        }
                        this.preEpgPageChannelInx = this.mCategoryInx;
                        this.isDelay = true;
                        this.mUiHandler.removeMessages(4);
                        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(4));
                        this.mUiHandler.removeMessages(2);
                        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                        this.mWorkHandler.removeMessages(3);
                        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(3));
                        break;
                    } else {
                        return true;
                    }
                }
                break;
            case 22:
                if (!this.isAtReservationHintPage) {
                    if (!this.isAtPzqbPage) {
                        this.mCategoryInx++;
                        if (this.mCategoryInx > this.mCategoryNameList.size() - 1) {
                            this.mCategoryInx = 0;
                        }
                        this.preEpgPageChannelInx = this.mCategoryInx;
                        this.isDelay = true;
                        this.mUiHandler.removeMessages(4);
                        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(4));
                        this.mUiHandler.removeMessages(2);
                        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                        this.mWorkHandler.removeMessages(3);
                        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(3));
                        break;
                    } else {
                        return true;
                    }
                }
                break;
            case 23:
                if (this.isAtPzqbPage) {
                    return true;
                }
                break;
            case 27:
                if (this.isAtPzqbPage) {
                    return true;
                }
                if (DownUtils.usbChecked()) {
                    this.backListener.categoryPageCallBack(6, null);
                    onStop();
                    return true;
                }
                this.backListener.categoryPageCallBack(10, null);
                return true;
            case 69:
                LOG.mo8825d("********mTvFormt===********" + this.mTvFormt);
                if (this.mTvFormt != 3) {
                    this.mMainApp.setChannelInx(0);
                }
                this.mMainApp.setmTvFormt(3);
                this.backListener.categoryPageCallBack(14, null);
                return true;
            case 84:
                LOG.mo8825d("番组表 + redKey");
                if (!this.isAtReservationHintPage) {
                    if (this.isAtPzqbPage) {
                        return true;
                    }
                    this.mMainApp.setCateType(1);
                    this.backListener.categoryPageCallBack(11, null);
                    this.mUiHandler.removeMessages(2);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                    return true;
                }
                break;
            case Opcodes.SASTORE /*86*/:
                if (!this.mMainApp.isPlaying()) {
                    return true;
                }
                this.backListener.categoryPageCallBack(9, null);
                return true;
            case Opcodes.DUP2 /*92*/:
                if (!this.isAtReservationHintPage) {
                    if (this.isAtPzqbPage) {
                        return true;
                    }
                    if (this.isAtEpgPage) {
                        LOG.mo8825d("******到这了******");
                        this.mDateInx--;
                        if (this.mDateInx < 0) {
                            this.mDateInx = 0;
                            return true;
                        }
                        this.isDelay = true;
                        this.mUiHandler.sendEmptyMessage(2);
                        this.mUiHandler.sendEmptyMessage(11);
                        this.mWorkHandler.sendEmptyMessage(1);
                        return true;
                    } else if (!this.isAtVodPage) {
                        return true;
                    } else {
                        this.mDateInx--;
                        if (this.mDateInx < 0) {
                            this.mDateInx = 0;
                            return true;
                        }
                        this.isDelay = true;
                        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                        this.mUiHandler.sendEmptyMessage(11);
                        this.mWorkHandler.sendEmptyMessage(2);
                        return true;
                    }
                }
                break;
            case Opcodes.DUP2_X1 /*93*/:
                if (!this.isAtReservationHintPage) {
                    if (this.isAtPzqbPage) {
                        return true;
                    }
                    if (this.isAtEpgPage) {
                        LOG.mo8825d("******到这了******");
                        this.mDateInx++;
                        if (this.mDateInx > DATES.length - 1) {
                            this.mDateInx = DATES.length - 1;
                            return true;
                        }
                        this.isDelay = true;
                        this.mUiHandler.sendEmptyMessage(2);
                        this.mUiHandler.sendEmptyMessage(11);
                        this.mWorkHandler.sendEmptyMessage(1);
                        return true;
                    } else if (!this.isAtVodPage) {
                        return true;
                    } else {
                        this.mDateInx++;
                        if (this.mDateInx > DATES.length - 1) {
                            this.mDateInx = DATES.length - 1;
                            return true;
                        }
                        this.isDelay = true;
                        this.mUiHandler.sendEmptyMessage(2);
                        this.mUiHandler.sendEmptyMessage(11);
                        this.mWorkHandler.sendEmptyMessage(2);
                        return true;
                    }
                }
                break;
            case 140:
                LOG.mo8825d("********mTvFormt===********" + this.mTvFormt);
                if (this.mTvFormt != 2) {
                    this.mMainApp.setChannelInx(0);
                }
                this.mMainApp.setmTvFormt(2);
                this.backListener.categoryPageCallBack(14, null);
                return true;
            case 142:
                LOG.mo8825d("********mTvFormt===********" + this.mTvFormt);
                if (this.mTvFormt != 1) {
                    this.mMainApp.setChannelInx(0);
                }
                this.mMainApp.setmTvFormt(1);
                this.backListener.categoryPageCallBack(14, null);
                return true;
            case 174:
                if (!this.isAtReservationHintPage) {
                    if (this.isAtPzqbPage) {
                        this.mUiHandler.sendEmptyMessage(15);
                        return true;
                    }
                    this.isAtPzqbPage = true;
                    this.mUiHandler.removeMessages(15);
                    this.mUiHandler.sendEmptyMessage(6);
                    this.mUiHandler.sendEmptyMessage(7);
                    this.mUiHandler.sendMessageDelayed(this.mUiHandler.obtainMessage(15), 3000);
                    return true;
                }
                break;
            case 185:
                if (!this.isAtReservationHintPage) {
                    if (DownUtils.usbChecked()) {
                        LOG.mo8825d("DownUtils.usbChecked()");
                        if (this.mCategoryDataList == null || this.mCategoryDataList.size() <= 0) {
                            LOG.mo8825d("no data!!!");
                            return true;
                        }
                        LOG.mo8825d("change surface!!!");
                        if (this.isAtPzqbPage) {
                            this.mUiHandler.sendEmptyMessage(15);
                        }
                        this.mWorkHandler.removeMessages(4);
                        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(4));
                        return true;
                    }
                    this.backListener.categoryPageCallBack(10, null);
                    return true;
                }
                break;
            case 186:
                return true;
            case C0412Constants.KEY_RECORD /*1184*/:
                LOG.mo8825d("********回看键********isAtVodPage==***" + this.isAtVodPage + "*****isAtEpgPage=****" + this.isAtEpgPage);
                if (!this.isAtReservationHintPage) {
                    if (this.isAtPzqbPage) {
                        return true;
                    }
                    this.mMainApp.setCateType(2);
                    this.backListener.categoryPageCallBack(11, null);
                    this.mUiHandler.removeMessages(2);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initUi() {
        this.mRlCategoryRoot = (RelativeLayout) this.mContentView.findViewById(R.id.rl_category_root);
        this.mChannelNameView = (TextView) this.mContentView.findViewById(R.id.newname);
        this.mEpgDateView = (TextView) this.mContentView.findViewById(R.id.date_pzb);
        this.mEpgTimeView = (TextView) this.mContentView.findViewById(R.id.time_pzb);
        this.mEpgLayout = (RelativeLayout) this.mContentView.findViewById(R.id.pzb_bg);
        this.weekLabel = (LinearLayout) this.mContentView.findViewById(R.id.ll_category_week);
        this.mEpgTitleIcon = (ImageView) this.mContentView.findViewById(R.id.epg_title_icon);
        this.mEpgListView = (ListView) this.mContentView.findViewById(R.id.vodlist);
        this.mPzqbPage = (LinearLayout) LayoutInflater.from(this.mContext).inflate(R.layout.pzqb_layout_epgpage, null);
        this.mRlCategoryRoot.addView(this.mPzqbPage);
        this.mFileNameView = (TextView) this.mPzqbPage.findViewById(R.id.filename);
        this.mFileNameView.getPaint().setFakeBoldText(true);
        this.mFileIndexView = (TextView) this.mPzqbPage.findViewById(R.id.paqb_index);
        this.mFileDateView = (TextView) this.mPzqbPage.findViewById(R.id.date_pzqb);
        this.mNwNameView = (TextView) this.mPzqbPage.findViewById(R.id.name_pzqb);
        this.mNwNameView.getPaint().setFakeBoldText(true);
        this.mFileIconView = (ImageView) this.mPzqbPage.findViewById(R.id.pzqbicon);
        this.mFileDesView = (TextView) this.mPzqbPage.findViewById(R.id.description);
        this.mTvPzqbTime = (TextView) this.mPzqbPage.findViewById(R.id.tv_pzqb_time);
        this.mFileDesView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PageCategory.this.isAtPzqbPage) {
                    PageCategory.this.mUiHandler.sendMessage(PageCategory.this.mUiHandler.obtainMessage(15));
                }
            }
        });
        this.mPzqbPage.setVisibility(8);
    }

    private void initCategoryName() {
        LOG.mo8825d("initCategoryName-------------->");
        if (this.mDataManager.isCategoryHasData()) {
            this.mCategoryNameList = this.mDataManager.getAllCategory();
        }
        LOG.mo8825d("mCategoryNameList---->" + this.mCategoryNameList);
        if (this.mCategoryNameList == null || this.mCategoryNameList.size() <= 0) {
            this.mCategoryNameList = new ArrayList();
            this.mCategoryNameList.add("ドラマ");
            this.mCategoryNameList.add("情報/ワイドショー");
            this.mCategoryNameList.add("ドキュメンタリー/教養");
            this.mCategoryNameList.add("ニュース/報道");
            this.mCategoryNameList.add("スポーツ");
            this.mCategoryNameList.add("趣味/教育");
            this.mCategoryNameList.add("アニメ/特撮");
            this.mCategoryNameList.add("福祉");
        }
    }

    private void initChannelDate() {
    }

    private void initDate() {
        if (this.mTimer != null) {
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    Calendar calendar = Calendar.getInstance();
                    int month = calendar.get(2) + 1;
                    int day = calendar.get(5);
                    int i = calendar.get(7);
                    int hour = calendar.get(11);
                    int minute = calendar.get(12);
                    PageCategory.this.dateStr = new StringBuilder(String.valueOf(month)).append("月").append(day < 10 ? "0" + day : Integer.valueOf(day)).append("日").toString();
                    PageCategory.this.timeStr = (hour < 10 ? "0" + hour : Integer.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : Integer.valueOf(minute));
                    PageCategory.this.mUiHandler.sendEmptyMessage(14);
                }
            }, 100, DateTime.MILLIS_PER_MINUTE);
        }
    }

    /* access modifiers changed from: private */
    public void showChannelView() {
        LOG.mo8825d("************showChannelView()************");
        String str = XmlPullParser.NO_NAMESPACE;
        try {
            if (this.mCategoryNameList != null && this.mCategoryNameList.size() > 0) {
                this.mChannelNameView.setText((String) this.mCategoryNameList.get(this.mCategoryInx));
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
    }

    /* access modifiers changed from: private */
    public void loadEpgPage() {
        LOG.mo8825d("********loadEpgPage*******");
        this.mMainApp.setCateType(5);
        this.mUiHandler.sendEmptyMessage(4);
        this.mMainApp.setFullPlay(false);
        this.mWorkHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: private */
    public void loadVodPage() {
        LOG.mo8825d("********loadVodPage*******");
        this.mMainApp.setCateType(6);
        showChannelView();
        this.mMainApp.setFullPlay(false);
        this.mWorkHandler.sendEmptyMessage(2);
    }

    private void showPage() {
        LOG.mo8825d("...showPage...");
        if (!this.isAtVodPage) {
            this.mDateInx = 0;
            this.predateInx = 0;
            this.mUiHandler.sendEmptyMessage(5);
        }
        initPreDate();
        initWeekView();
        this.mMainApp.setFullPlay(false);
        this.isAtPzqbPage = false;
        setEpgPageBackground();
        this.mContentView.setVisibility(0);
    }

    private void setEpgPageBackground() {
        LOG.mo8825d("*********setEpgPageBackground********isAtEpgPage--->" + this.isAtEpgPage + "---isAtVodPage--->" + this.isAtVodPage);
        if (this.isAtEpgPage) {
            this.weekLabel.setVisibility(0);
            this.mEpgTitleIcon.setBackgroundResource(R.drawable.epg_title_icon);
        } else if (this.isAtVodPage) {
            this.weekLabel.setVisibility(0);
            this.mEpgTitleIcon.setBackgroundResource(R.drawable.vod_title_icon);
        }
    }

    /* access modifiers changed from: private */
    public void clearUpEpgListData() {
        LOG.mo8825d("...clearUpEpgListData...");
        if (this.mEpgPageAdapter != null) {
            this.mEpgListView.setAdapter(null);
            this.mEpgPageAdapter.notifyDataSetChanged();
        }
    }

    private void initPreDate() {
        for (int i = 0; i < 7; i++) {
            Calendar calendar = Calendar.getInstance();
            if (this.isAtVodPage) {
                calendar.add(5, -i);
            } else {
                calendar.add(5, i);
            }
            int year = calendar.get(1);
            int month = calendar.get(2) + 1;
            int day = calendar.get(5);
            int wk = calendar.get(7);
            String[] WEEK = this.mContext.getResources().getStringArray(R.array.Week);
            WEEK_STR[i] = (day < 10 ? "0" + day : Integer.valueOf(day)) + WEEK[wk - 1];
            DATES[i] = new StringBuilder(String.valueOf(year)).append("-").append(month < 10 ? "0" + month : new StringBuilder(String.valueOf(month)).toString()).append("-").append(day < 10 ? "0" + day : new StringBuilder(String.valueOf(day)).toString()).toString();
            DATE_STR[i] = new StringBuilder(String.valueOf(month)).append("月").append(day < 10 ? "0" + day : new StringBuilder(String.valueOf(day)).append("日").toString()).append("(").append(WEEK[wk - 1]).append(")").toString();
        }
    }

    private void initWeekView() {
        for (int i = 0; i < 7; i++) {
            ((Button) findViewById(DAYS[i])).setText(WEEK_STR[i]);
        }
        ((Button) findViewById(DAYS[0])).requestFocus();
    }

    /* access modifiers changed from: private */
    public void loadEpgData() {
        long startUtcTime;
        long endUtcTime;
        LOG.mo8825d("....loadEpgData....");
        if (this.isDelay) {
            if (this.mCategoryNameList != null && this.mCategoryNameList.size() > 0) {
                String categoryName = (String) this.mCategoryNameList.get(this.mCategoryInx);
                if (this.mDateInx == 0) {
                    startUtcTime = System.currentTimeMillis();
                } else {
                    startUtcTime = DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(" 00:00:00").toString());
                }
                if (this.mDateInx < DATES.length - 1) {
                    endUtcTime = DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx + 1])).append(" 00:00:00").toString());
                } else {
                    endUtcTime = startUtcTime + DateTime.MILLIS_PER_DAY;
                }
                this.mCategoryDataList = this.mDataManager.getCategoryProgramByColumn(this.mTvFormt, startUtcTime, endUtcTime, categoryName, new Comparator<EPGBean>() {
                    public int compare(EPGBean lhs, EPGBean rhs) {
                        return (int) (lhs.getUtc() - rhs.getUtc());
                    }
                });
                LOG.mo8825d("mCategoryDataList----->" + this.mCategoryDataList);
            }
            this.mWorkHandler.removeMessages(8);
            this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(8));
        }
    }

    /* access modifiers changed from: private */
    public void initEpgPageListAdapter() {
        if (this.mCategoryDataList == null || this.mCategoryDataList.size() <= 0) {
            LOG.mo8825d("mEpgPageList == null");
        } else {
            LOG.mo8825d("mCategoryDataList != null && mCategoryDataList.size() > 0");
            this.recordInx = 0;
            this.pageVodIndex = 0;
            float count = ((float) this.mCategoryDataList.size()) / 10.0f;
            this.pageVodCount = count % 1.0f == 0.0f ? (int) count : (int) (count + 1.0f);
            if (this.mEpgPageAdapter == null) {
                this.mEpgPageAdapter = new MoreVodAdapter(this.mContext, this.mCategoryDataList);
                this.mEpgListView.setOnItemSelectedListener(this.mEpgPageAdapter);
                this.mEpgListView.setOnItemClickListener(this.mEpgPageAdapter);
            } else {
                this.mEpgPageAdapter.getList().clear();
                this.mEpgPageAdapter.setList(this.mCategoryDataList);
            }
        }
        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(1));
    }

    /* access modifiers changed from: private */
    public void initPzqbPage() {
        LOG.mo8825d(".......initPzqbPage....");
        if (this.mCategoryDataList != null && this.mCategoryDataList.size() > 0) {
            try {
                EPGBean epgBean = (EPGBean) this.mCategoryDataList.get(this.recordInx);
                if (epgBean != null) {
                    MediaBean channel = this.mDataManager.getChannelById(epgBean.getChannelId(), this.mTvFormt);
                    String fileIndex = String.valueOf(channel.getChannelNumber());
                    String descript = epgBean.getDescription();
                    String fileName = epgBean.getTitle();
                    String startTime = DateTimeUtil.UtcToDate(epgBean.getUtc());
                    String endTime = DateTimeUtil.UtcToDate(epgBean.getEndUtc());
                    String fileDate = DATE_STR[this.mDateInx];
                    String newsName = channel.getTitle();
                    if (this.mDataManager.isLivePageHasData()) {
                        String mIconUrl = channel.getImage();
                        LOG.mo8825d("*******mIconUrl******" + mIconUrl);
                        Glide.with(this.mContext).load(mIconUrl).centerCrop().into(this.mFileIconView);
                    }
                    this.mFileNameView.setText(fileName);
                    this.mFileIndexView.setText(fileIndex);
                    this.mFileDateView.setText(fileDate);
                    this.mTvPzqbTime.setText(new StringBuilder(String.valueOf(startTime)).append("-").append(endTime).toString());
                    this.mNwNameView.setText(newsName);
                    this.mFileDesView.setText(descript);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LOG.mo8825d("...Exception...====" + e.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public void freshDatePage() {
        LOG.mo8825d("...freshDatePage...");
        Button tvView2 = (Button) findViewById(DAYS[this.predateInx]);
        ((Button) findViewById(DAYS[this.mDateInx])).setBackgroundResource(R.drawable.epg_date_checked);
        if (this.predateInx != 0 || this.mDateInx != 0) {
            LOG.mo8825d("========================================!(predateInx == 0 && mDateInx == 0) predateInx---->" + this.predateInx + "mDateInx---->" + this.mDateInx);
            tvView2.setBackgroundResource(R.drawable.epg_daytext_sel);
            this.predateInx = this.mDateInx;
        }
    }

    /* access modifiers changed from: private */
    public void hidePzqbPage() {
        LOG.mo8825d("...hiddenPzqb...");
        this.isAtPzqbPage = false;
        this.mPzqbPage.setVisibility(View.GONE);
        this.mEpgLayout.setVisibility(View.VISIBLE);
        this.mEpgListView.requestFocus();
    }

    /* access modifiers changed from: private */
    public void initDownIcon(LinearLayout layout) {
        LOG.mo8825d("*******initDownIcon******");
        if (layout != null) {
            LOG.mo8825d("layout != null");
            this.mDownIcon = (ImageView) layout.findViewById(R.id.down_icon);
            this.mDownIcon.setVisibility(0);
            this.mDownIcon.setImageResource(R.drawable.wait);
        }
    }

    /* access modifiers changed from: private */
    public void decideLoadData() {
        LOG.mo8825d("========decideLoadData========");
        this.isDelay = false;
        intervalofTime = 0;
        this.delayTime2 += 10;
        if (this.delayTime2 > 120) {
            this.delayTime2 = 120;
        }
        while (!this.isDelay) {
            try {
                LOG.mo8825d("=============" + intervalofTime);
                intervalofTime++;
                Thread.sleep(1);
                if (intervalofTime == ((long) this.delayTime2)) {
                    this.isDelay = true;
                    if (this.isAtVodPage) {
                        loadVodData();
                    } else if (this.isAtEpgPage) {
                        loadEpgData();
                    }
                    this.delayTime2 = 60;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                LOG.mo8825d("...Exception...===" + e.toString());
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void loadVodData() {
        long endUtcTime;
        LOG.mo8825d("....loadVodData...");
        if (this.isDelay) {
            LOG.mo8825d("mCategoryNameList----" + this.mCategoryNameList);
            if (this.mCategoryNameList != null && this.mCategoryNameList.size() > 0) {
                String categoryName = (String) this.mCategoryNameList.get(this.mCategoryInx);
                long startUtcTime = DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(" 00:00:00").toString());
                if (this.mDateInx == 0) {
                    endUtcTime = System.currentTimeMillis();
                } else {
                    endUtcTime = DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx - 1])).append(" 00:00:00").toString());
                }
                this.mCategoryDataList = this.mDataManager.getCategoryProgramByColumn(this.mTvFormt, startUtcTime, endUtcTime, categoryName, new Comparator<EPGBean>() {
                    public int compare(EPGBean lhs, EPGBean rhs) {
                        return (int) (rhs.getUtc() - lhs.getUtc());
                    }
                });
                LOG.mo8825d("mCategoryDataList---->" + this.mCategoryDataList);
            }
        }
        this.mWorkHandler.removeMessages(8);
        this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(8));
    }

    /* access modifiers changed from: private */
    public void checkDownTask() {
        LOG.mo8825d("*******checkDownTask*******");
        if (DownUtils.usbChecked()) {
            new EPGBean();
            if (this.mCategoryDataList != null && this.mCategoryDataList.size() != 0 && this.mCategoryNameList != null) {
                EPGBean epgBean = (EPGBean) this.mCategoryDataList.get(this.recordInx);
                String fileMask = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(DateTimeUtil.UtcToDate(epgBean.getUtc())).append(":00").toString()))).toString())).append(epgBean.getChannelId()).toString();
                LOG.mo8825d("fileMask--->" + fileMask);
                if (!this.mDownUtils.isContainsKey(fileMask)) {
                    this.isAtReservationHintPage = true;
                    this.mUiHandler.removeMessages(21);
                    this.mUiHandler.sendEmptyMessage(21);
                    return;
                }
                this.backListener.categoryPageCallBack(15, null);
                return;
            }
            return;
        }
        this.backListener.categoryPageCallBack(10, null);
    }

    /* access modifiers changed from: private */
    public void addDownTaskToList() {
        LOG.mo8825d("....addDownTaskToList......");
        MediaBean channelParams = null;
        try {
            EPGBean epgBean = (EPGBean) this.mCategoryDataList.get(this.recordInx);
            if (this.mDataManager.isLivePageHasData()) {
                channelParams = this.mDataManager.getChannelById(epgBean.getChannelId(), this.mTvFormt);
            }
            if (channelParams == null) {
                LOG.mo8825d("map == null");
            } else if (channelParams.getUrls() == null || channelParams.getUrls().get(0) == null) {
                LOG.mo8825d(".......url为空........");
            } else {
                ArrayList<UrlBean> urlList = channelParams.getUrls();
                String vodUrl = XmlPullParser.NO_NAMESPACE;
                int quality = 3;
                while (quality >= 0) {
                    LOG.mo8825d("quality---->" + quality);
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
                    LOG.mo8825d(".......no such quality........" + quality);
                    quality--;
                    this.mDataManager.setQuality(quality);
                }
                if (StringUtils.isEmpty(vodUrl)) {
                    LOG.mo8825d("vodUrl is null,cannot download!!!");
                    return;
                }
                String channelMask = channelParams.getId();
                String programName = epgBean.getTitle();
                String startTimeStr = DateTimeUtil.UtcToDate(epgBean.getUtc());
                String endTimeStr = DateTimeUtil.UtcToDate(epgBean.getEndUtc());
                String description = epgBean.getDescription();
                String channelName = channelParams.getTitle();
                int channelNum = channelParams.getChannelNumber();
                String channelIconUrl = channelParams.getImage();
                String fileMask = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTimeStr).append(":00").toString()))).toString())).append(channelMask).toString();
                String programType = epgBean.getType();
                DownloadTask downFile = new DownloadTask();
                downFile.setChannelMask(channelMask);
                downFile.setDate(DATES[this.mDateInx]);
                downFile.setDesc(description);
                downFile.setVodUrl(vodUrl);
                downFile.setTaskName(fileMask);
                downFile.setStartTime(startTimeStr);
                downFile.setEndTime(endTimeStr);
                downFile.setProgramName(programName);
                downFile.setChannelName(channelName);
                downFile.setChannelIconUrl(channelIconUrl);
                downFile.setChannelNum(channelNum);
                downFile.setTvFormt(this.mTvFormt);
                downFile.setCategory(programType);
                downFile.setStatus(0);
                downFile.setLocalFile(new StringBuilder(String.valueOf(fileMask)).append(".ts").toString());
                if (this.isAtVodPage) {
                    downFile.setType(DownloadTask.TYPE_VOD);
                    this.mDownUtils.addDownTask(downFile);
                } else if (this.isAtEpgPage) {
                    downFile.setType(DownloadTask.TYPE_EPG);
                    this.mDownUtils.addDownTask(downFile);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPageCategoryCallBackListener(PageCategoryCallBackListener backListener2) {
        this.backListener = backListener2;
    }
}
