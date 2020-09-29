package net.sunniwell.app.linktaro.nettv.content;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
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

import net.sunniwell.aidl.SDKRemoteManager;
import net.sunniwell.aidl.bean.EPGBean;
import net.sunniwell.aidl.bean.MediaBean;
import net.sunniwell.aidl.bean.UrlBean;
import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.SWApplication;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.download.DownUtils;
import net.sunniwell.app.linktaro.nettv.download.DownloadTask;
import net.sunniwell.app.linktaro.nettv.entry.NettvActivity;
import net.sunniwell.app.linktaro.nettv.manager.DataManager;
import net.sunniwell.app.linktaro.nettv.processor.imp.DBProcessor;
import net.sunniwell.app.linktaro.nettv.view.CustomResDialog;
import net.sunniwell.app.linktaro.tools.DateTimeUtil;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.app.linktaro.tools.SharedPreUtil;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.tools.DateTime;

import org.codehaus.jackson.util.MinimalPrettyPrinter;
import org.xmlpull.v1.XmlPullParser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;

public class PageEPG extends PageBase {
    /* access modifiers changed from: private */
    public static String[] DATES = new String[7];
    private static String[] DATE_STR = new String[7];
    /* access modifiers changed from: private */
    public static final int[] DAYS = {R.id.day01, R.id.day02, R.id.day03, R.id.day04, R.id.day05, R.id.day06, R.id.day07};
    private static final int DAY_COUNT = 7;
    private static final int VIEW_COUNT = 10;
    private static String[] WEEK_STR = new String[7];
    private static long intervalofTime = 0;
    private static long mLastClickTime;
    /* access modifiers changed from: private */
    public static int mVodWeek = 0;
    private PageEPGCallBackListener backListener;
    private int curProgramType = 0;
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
    /* access modifiers changed from: private */
    public boolean isDelay = true;
    private Button[] mBtnList = new Button[7];
    private ImageView mChanelIconView;
    private TextView mChannelNameView;
    /* access modifiers changed from: private */
    public int mClickIndex;
    /* access modifiers changed from: private */
    public int mClickPage;
    private View mContentView;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public int mCurPosition;
    /* access modifiers changed from: private */
    public int mCurVodPosition;
    /* access modifiers changed from: private */
    public CustomResDialog mCustomResDialog;
    private DBProcessor mDBProcessor;
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
    public int mEpgPageChannelInx;
    /* access modifiers changed from: private */
    public List<EPGBean> mEpgPageList;
    /* access modifiers changed from: private */
    public EPGBean mEpgProgram;
    /* access modifiers changed from: private */
    public TextView mEpgTimeView;
    private ImageView mEpgTitleIcon;
    private TextView mFileDateView;
    /* access modifiers changed from: private */
    public TextView mFileDesView;
    private ImageView mFileIconView;
    private TextView mFileIndexView;
    private TextView mFileNameView;
    /* access modifiers changed from: private */
    public int mGetCount;
    /* access modifiers changed from: private */
    public ImageView mIvChangeMark;
    /* access modifiers changed from: private */
    public TextView mIvPzqbTip;
    /* access modifiers changed from: private */
    public boolean mLastStatus;
    /* access modifiers changed from: private */
    public ArrayList<MediaBean> mLivePageChannelList;
    /* access modifiers changed from: private */
    public ImageView mLoadingIv;
    /* access modifiers changed from: private */
    public NettvActivity mMainApp;
    private TextView mNwNameView;
    /* access modifiers changed from: private */
    public AnimationDrawable mPageLoaddingAnim;
    /* access modifiers changed from: private */
    public LinearLayout mPzqbPage;
    /* access modifiers changed from: private */
    public int mRecordInx;
    private RelativeLayout mRlEpgRoot;
    private Timer mTimer;
    private TextView mTvChannelNum;
    /* access modifiers changed from: private */
    public int mTvFormt;
    private TextView mTvPzqbTime;
    /* access modifiers changed from: private */
    public UiHandler mUiHandler;
    /* access modifiers changed from: private */
    public int mVodPageIndex;
    /* access modifiers changed from: private */
    public WorkHandler mWorkHandler;
    /* access modifiers changed from: private */
    public int pageVodCount;
    /* access modifiers changed from: private */
    public String timeStr;

    private class MoreVodAdapter extends BaseAdapter implements OnItemSelectedListener, OnItemClickListener, OnKeyListener {
        private Context context;
        private int index;
        private List<EPGBean> list;
        private String mClickMark = XmlPullParser.NO_NAMESPACE;
        private int programType;

        public void setProgramType(int programType2) {
            this.programType = programType2;
        }

        public MoreVodAdapter(Context context2, List<EPGBean> list2, int type) {
            this.context = context2;
            this.list = list2;
            this.programType = type;
        }

        public List<EPGBean> getList() {
            return this.list;
        }

        public void setList(List<EPGBean> list2) {
            this.list = list2;
        }

        public int getCount() {
            int ori = PageEPG.this.mVodPageIndex * 10;
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
            View v;
            LogcatUtils.m321d("********getView***position===***" + position + "****pageVodIndex===****" + PageEPG.this.mVodPageIndex);
            if (convertView == null) {
                v = LayoutInflater.from(this.context).inflate(R.layout.vodlistitem, null);
            } else {
                v = convertView;
                if (((Integer) v.getTag()).intValue() == PageEPG.this.mVodPageIndex) {
                    LogcatUtils.m321d("****到这了****");
                    return v;
                }
            }
            ImageView channelIcon = (ImageView) v.findViewById(R.id.channel_icon);
            TextView date = (TextView) v.findViewById(R.id.channelnum);
            date.getPaint().setFakeBoldText(true);
            TextView newName = (TextView) v.findViewById(R.id.newname);
            if (position == 0) {
                LogcatUtils.m321d("position == 0");
            }
            newName.getPaint().setFakeBoldText(true);
            ImageView mDownView = (ImageView) v.findViewById(R.id.down_icon);
            if (PageEPG.this.mVodPageIndex >= 0 && PageEPG.this.mVodPageIndex <= PageEPG.this.pageVodCount) {
                this.index = (PageEPG.this.mVodPageIndex * 10) + position;
                if (this.index < this.list.size()) {
                    EPGBean epgBean = (EPGBean) this.list.get(this.index);
                    if (epgBean == null) {
                        return v;
                    }
                    newName.setText(epgBean.getTitle());
                    String startTimeStr = DateTimeUtil.UtcToDate(epgBean.getUtc());
                    String endTimeStr = DateTimeUtil.UtcToDate(epgBean.getEndUtc());
                    if (!TextUtils.isEmpty(epgBean.getTitle())) {
                        date.setText(new StringBuilder(String.valueOf(startTimeStr)).append("~").append(endTimeStr).toString());
                    }
                    if (this.programType == 1) {
                        channelIcon.setImageResource(R.drawable.bs_01);
                        channelIcon.setVisibility(0);
                    }
                    mDownView.setVisibility(8);
                    if (PageEPG.this.mLivePageChannelList != null && PageEPG.this.mLivePageChannelList.size() > PageEPG.this.mEpgPageChannelInx) {
                        MediaBean curChannel = (MediaBean) PageEPG.this.mLivePageChannelList.get(PageEPG.this.mEpgPageChannelInx);
                        if (curChannel != null) {
                            String fileMask = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(PageEPG.DATES[PageEPG.this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTimeStr).append(":00").toString()))).toString())).append(curChannel.getId()).toString();
                            LogcatUtils.m321d("----fileMask=" + fileMask);
                            if (PageEPG.this.isAtVodPage) {
                                if (DownUtils.usbChecked() && PageEPG.this.mDownUtils.isContainsKey(fileMask)) {
                                    if (PageEPG.this.mDownUtils.getDownFlagByKey(fileMask) == 2) {
                                        mDownView.setImageResource(R.drawable.selected);
                                    } else {
                                        mDownView.setImageResource(R.drawable.wait);
                                    }
                                    mDownView.setVisibility(0);
                                } else if (!PageEPG.this.mMainApp.isPlaying() || !fileMask.equals(this.mClickMark)) {
                                    mDownView.setVisibility(8);
                                } else {
                                    mDownView.setVisibility(0);
                                    mDownView.setImageResource(R.drawable.state_play_no_focus);
                                }
                            } else if (PageEPG.this.isAtEpgPage) {
                                if (DownUtils.usbChecked() && PageEPG.this.mDownUtils.isContainsKey(fileMask)) {
                                    if (PageEPG.this.mDownUtils.getDownFlagByKey(fileMask) == 2) {
                                        mDownView.setImageResource(R.drawable.selected);
                                    } else {
                                        mDownView.setImageResource(R.drawable.wait);
                                    }
                                    mDownView.setVisibility(0);
                                } else if (!PageEPG.this.mMainApp.isPlaying() || !fileMask.equals(this.mClickMark)) {
                                    mDownView.setVisibility(8);
                                } else {
                                    mDownView.setVisibility(0);
                                    mDownView.setImageResource(R.drawable.state_play_no_focus);
                                }
                            }
                        }
                    }
                }
            }
            v.setTag(Integer.valueOf(PageEPG.this.mVodPageIndex));
            return v;
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long arg3) {
            PageEPG.this.mCurVodPosition = position;
            PageEPG.this.mRecordInx = (PageEPG.this.mVodPageIndex * 10) + position;
            LogcatUtils.m321d("...localPosition...====" + PageEPG.this.mCurVodPosition);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            LogcatUtils.m321d("...onNothingSelected...");
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
            PageEPG.this.mRecordInx = (PageEPG.this.mVodPageIndex * 10) + position;
            PageEPG.this.mCurPosition = position;
            PageEPG.this.mEpgProgram = (EPGBean) this.list.get(PageEPG.this.mRecordInx);
            if (!TextUtils.isEmpty(PageEPG.this.mEpgProgram.getTitle())) {
                String mClickdaysToUtc = new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(PageEPG.DATES[PageEPG.this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(DateTimeUtil.UtcToDate(PageEPG.this.mEpgProgram.getUtc())).append(":00").toString()))).toString();
                this.mClickMark = new StringBuilder(String.valueOf(mClickdaysToUtc)).append(PageEPG.this.mEpgProgram.getChannelId()).toString();
                LogcatUtils.m321d("----list--Click--mClickMark = " + this.mClickMark);
                PageEPG.this.play(position);
            }
        }

        public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
            int action = keyEvent.getAction();
            LogcatUtils.m321d("action is " + action);
            if (action == 0 && keyCode == 92) {
                if (PageEPG.this.isAtReservationHintPage || PageEPG.this.isAtPzqbPage) {
                    return true;
                }
                LogcatUtils.m321d("mDateInx " + PageEPG.this.mDateInx);
                if (PageEPG.this.mDateInx == 0) {
                    LogcatUtils.m321d("isAtVodPage------>" + PageEPG.this.isAtVodPage);
                    if (PageEPG.this.isAtVodPage) {
                        PageEPG.this.initList(7);
                        PageEPG.mVodWeek = 0;
                        PageEPG.this.isAtEpgPage = true;
                        PageEPG.this.isAtVodPage = false;
                        PageEPG.this.mMainApp.setBackPageIndex(1);
                        PageEPG.this.showPage();
                        PageEPG.this.clearUpEpgListData();
                        PageEPG.this.loadEpgPage();
                        return true;
                    } else if (PageEPG.this.isAtEpgPage) {
                        PageEPG.mVodWeek = 0;
                        PageEPG.this.initList(28);
                        PageEPG.this.isAtEpgPage = false;
                        PageEPG.this.isAtVodPage = true;
                        PageEPG.this.mMainApp.setBackPageIndex(2);
                        PageEPG.this.showPage();
                        PageEPG.this.clearUpEpgListData();
                        PageEPG.this.loadVodPage();
                        return true;
                    }
                }
                if (PageEPG.this.isAtEpgPage) {
                    PageEPG pageEPG = PageEPG.this;
                    pageEPG.mDateInx = pageEPG.mDateInx - 1;
                    if (PageEPG.this.mDateInx < 0) {
                        PageEPG.this.mDateInx = 0;
                        return true;
                    }
                    PageEPG.this.isDelay = true;
                    PageEPG.this.mUiHandler.sendEmptyMessage(2);
                    PageEPG.this.mUiHandler.sendEmptyMessage(11);
                    PageEPG.this.mWorkHandler.sendEmptyMessage(1);
                    return true;
                } else if (!PageEPG.this.isAtVodPage) {
                    return true;
                } else {
                    PageEPG pageEPG2 = PageEPG.this;
                    pageEPG2.mDateInx = pageEPG2.mDateInx - 1;
                    if (PageEPG.this.mDateInx < 0) {
                        PageEPG.this.mDateInx = 0;
                        return true;
                    }
                    LogcatUtils.m321d("mDateInx is " + PageEPG.this.mDateInx + "  mDateInx % 7 is " + ((PageEPG.this.mDateInx + 1) % 7));
                    if ((PageEPG.this.mDateInx + 1) % 7 == 0) {
                        LogcatUtils.m321d("refresh date and mVodWeek is " + PageEPG.mVodWeek);
                        PageEPG.this.initWeekView(PageEPG.mVodWeek);
                    }
                    LogcatUtils.m321d("refresh date and mVodWeek is " + PageEPG.mVodWeek);
                    PageEPG.this.isDelay = true;
                    PageEPG.this.mUiHandler.sendMessage(PageEPG.this.mUiHandler.obtainMessage(2));
                    PageEPG.this.mUiHandler.sendEmptyMessage(11);
                    PageEPG.this.mWorkHandler.sendEmptyMessage(2);
                    return true;
                }
            } else if (action != 0 || keyCode != 93) {
                return false;
            } else {
                LogcatUtils.m321d("isAtReservationHintPage is " + PageEPG.this.isAtReservationHintPage + " isAtPzqbPage is " + PageEPG.this.isAtPzqbPage);
                if (PageEPG.this.isAtReservationHintPage || PageEPG.this.isAtPzqbPage) {
                    return true;
                }
                if (PageEPG.this.isAtEpgPage) {
                    PageEPG pageEPG3 = PageEPG.this;
                    pageEPG3.mDateInx = pageEPG3.mDateInx + 1;
                    if (PageEPG.this.mDateInx > PageEPG.DATES.length - 1) {
                        PageEPG.this.mDateInx = PageEPG.DATES.length - 1;
                        return true;
                    }
                    PageEPG.this.isDelay = true;
                    PageEPG.this.mUiHandler.sendEmptyMessage(2);
                    PageEPG.this.mUiHandler.sendEmptyMessage(11);
                    PageEPG.this.mWorkHandler.sendEmptyMessage(1);
                    return true;
                } else if (!PageEPG.this.isAtVodPage) {
                    return true;
                } else {
                    PageEPG pageEPG4 = PageEPG.this;
                    pageEPG4.mDateInx = pageEPG4.mDateInx + 1;
                    if (PageEPG.this.mDateInx > PageEPG.DATES.length - 1) {
                        PageEPG.this.mDateInx = PageEPG.DATES.length - 1;
                        return true;
                    }
                    if (PageEPG.this.mDateInx % 7 == 0) {
                        LogcatUtils.m321d("refresh date and mVodWeek is " + PageEPG.mVodWeek);
                        PageEPG.this.initWeekView(PageEPG.mVodWeek);
                    }
                    LogcatUtils.m321d("mDateInx is " + PageEPG.this.mDateInx + "  DATES is " + PageEPG.DATES.length + "  mVodWeek is " + PageEPG.mVodWeek);
                    PageEPG.this.isDelay = true;
                    PageEPG.this.mUiHandler.sendEmptyMessage(2);
                    PageEPG.this.mUiHandler.sendEmptyMessage(11);
                    PageEPG.this.mWorkHandler.sendEmptyMessage(2);
                    return true;
                }
            }
        }
    }

    public interface PageEPGCallBackListener {
        void epgPageCallBack(int i, Message message);
    }

    @SuppressLint({"HandlerLeak"})
    public class UiHandler extends Handler {
        public UiHandler() {
        }

        public void handleMessage(Message msg) {
            LogcatUtils.m321d("...msg.waht===" + msg.what);
            switch (msg.what) {
                case 1:
                    PageEPG.this.mEpgListView.setAdapter(PageEPG.this.mEpgPageAdapter);
                    LogcatUtils.m321d("mClickIndex------>" + PageEPG.this.mClickIndex + "------mClickPage----->" + PageEPG.this.mClickPage);
                    if (!(PageEPG.this.mClickIndex == 0 && PageEPG.this.mClickPage == 0) && PageEPG.this.isAtVodPage) {
                        PageEPG.this.mVodPageIndex = PageEPG.this.mClickPage;
                        PageEPG.this.mEpgListView.setSelection(PageEPG.this.mClickIndex);
                        PageEPG.this.mClickIndex = 0;
                        PageEPG.this.mClickPage = 0;
                    }
                    PageEPG.this.mEpgPageAdapter.notifyDataSetChanged();
                    PageEPG.this.mEpgListView.requestFocus();
                    break;
                case 2:
                    PageEPG.this.clearUpEpgListData();
                    break;
                case 3:
                    PageEPG.this.showPage();
                    break;
                case 4:
                    PageEPG.this.mLivePageChannelList = PageEPG.this.mDataManager.getLiveData(PageEPG.this.mTvFormt);
                    LogcatUtils.m321d("----mLivePageChannelList=" + PageEPG.this.mLivePageChannelList);
                    LogcatUtils.m321d("----mEpgPageChannelInx=" + PageEPG.this.mEpgPageChannelInx);
                    if (PageEPG.this.mLivePageChannelList != null && PageEPG.this.mLivePageChannelList.size() > PageEPG.this.mEpgPageChannelInx) {
                        PageEPG.this.showChannelView((MediaBean) PageEPG.this.mLivePageChannelList.get(PageEPG.this.mEpgPageChannelInx));
                    }
                    switch (PageEPG.this.mTvFormt) {
                        case 1:
                            PageEPG.this.mIvChangeMark.setBackgroundResource(R.drawable.epg_table_bsarrow);
                            break;
                        case 2:
                            PageEPG.this.mIvChangeMark.setBackgroundResource(R.drawable.epg_table_csarrow);
                            break;
                        case 3:
                            PageEPG.this.mIvChangeMark.setBackgroundResource(R.drawable.epg_table_dsarrow);
                            break;
                    }
                case 5:
                    LogcatUtils.m321d("******RESUME_DATE******");
                    for (int i = 1; i < 7; i++) {
                        ((Button) PageEPG.this.findViewById(PageEPG.DAYS[i])).setBackgroundResource(R.drawable.epg_daytext_sel);
                    }
                    ((Button) PageEPG.this.findViewById(PageEPG.DAYS[0])).setBackgroundResource(R.drawable.epg_date_checked);
                    break;
                case 6:
                    EPGBean epgBean = null;
                    if (PageEPG.this.mEpgPageList != null && PageEPG.this.mEpgPageList.size() > 0) {
                        epgBean = (EPGBean) PageEPG.this.mEpgPageList.get(PageEPG.this.mRecordInx);
                    }
                    if (PageEPG.this.mLivePageChannelList == null) {
                        PageEPG.this.mLivePageChannelList = PageEPG.this.mDataManager.getLiveData(PageEPG.this.mTvFormt);
                    }
                    if (PageEPG.this.mLivePageChannelList != null) {
                        PageEPG.this.initPzqbPage((MediaBean) PageEPG.this.mLivePageChannelList.get(PageEPG.this.mEpgPageChannelInx), epgBean);
                        if (!PageEPG.this.isAtVodPage) {
                            if (PageEPG.this.isAtEpgPage) {
                                PageEPG.this.mIvPzqbTip.setText(R.string.order_project);
                                break;
                            }
                        } else {
                            PageEPG.this.mIvPzqbTip.setText(R.string.order_group);
                            break;
                        }
                    } else {
                        return;
                    }
                    break;
                case 7:
                    PageEPG.this.isAtPzqbPage = true;
                    PageEPG.this.mPzqbPage.setVisibility(0);
                    PageEPG.this.mFileDesView.requestFocus();
                    break;
                case 11:
                    PageEPG.this.freshDatePage();
                    break;
                case 13:
                    PageEPG.this.initDownIcon((LinearLayout) msg.obj);
                    break;
                case 14:
                    int wk = Calendar.getInstance().get(7);
                    PageEPG.this.mEpgDateView.setText(new StringBuilder(String.valueOf(PageEPG.this.dateStr)).append("(").append(PageEPG.this.mContext.getResources().getStringArray(R.array.Week)[wk - 1]).append(")").toString());
                    PageEPG.this.mEpgTimeView.setText(PageEPG.this.timeStr);
                    break;
                case 15:
                    PageEPG.this.hidePzqbPage();
                    break;
                case 17:
                    PageEPG.this.loadEpgPage();
                    break;
                case 18:
                    PageEPG.this.loadVodPage();
                    break;
                case 20:
                    LogcatUtils.m321d("....show_reservation_dialog...");
                    if (PageEPG.this.mCustomResDialog == null || !PageEPG.this.mCustomResDialog.isShowing()) {
                        PageEPG.this.mCustomResDialog = new CustomResDialog(PageEPG.this.mContext, R.style.PromptDialog);
                        PageEPG.this.mCustomResDialog.setDialogBg(R.drawable.ts_bg);
                        PageEPG.this.mCustomResDialog.setOkBgResource(R.drawable.btn_check_ok_sel);
                        PageEPG.this.mCustomResDialog.setCancelBgResource(R.drawable.btn_check_cancel_sel);
                        PageEPG.this.mCustomResDialog.setTextContent(PageEPG.this.getResources().getText(R.string.first_reservation).toString());
                        PageEPG.this.mCustomResDialog.setOkClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                PageEPG.this.addProgramToTask();
                            }
                        });
                        PageEPG.this.mCustomResDialog.setCancelClickListener(new OnClickListener() {
                            public void onClick(View v) {
                                PageEPG.this.mCustomResDialog.dismiss();
                            }
                        });
                        PageEPG.this.mCustomResDialog.setOnDismissListener(new OnDismissListener() {
                            public void onDismiss(DialogInterface dialog) {
                                PageEPG.this.isAtReservationHintPage = false;
                            }
                        });
                        PageEPG.this.mCustomResDialog.show();
                        break;
                    } else {
                        return;
                    }
                case 21:
                    PageEPG.this.mCustomResDialog = new CustomResDialog(PageEPG.this.mContext, R.style.PromptDialog);
                    PageEPG.this.mCustomResDialog.setDialogBg(R.drawable.ts_bg);
                    PageEPG.this.mCustomResDialog.setOkBgResource(R.drawable.btn_check_ok_sel);
                    PageEPG.this.mCustomResDialog.setCancelBgResource(R.drawable.btn_check_cancel_sel);
                    PageEPG.this.mCustomResDialog.setTextContent(PageEPG.this.getResources().getString(R.string.ts_no_usb));
                    PageEPG.this.mCustomResDialog.setOkClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            PageEPG.this.mCustomResDialog.dismiss();
                        }
                    });
                    PageEPG.this.mCustomResDialog.setCancelClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            PageEPG.this.mCustomResDialog.dismiss();
                        }
                    });
                    PageEPG.this.mCustomResDialog.show();
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
                case 1:
                    PageEPG.this.loadEpgData();
                    return;
                case 2:
                    PageEPG.this.loadVodData();
                    return;
                case 3:
                    PageEPG.this.decideLoadData();
                    return;
                case 4:
                    LogcatUtils.m321d("---CMD_CHECK_DOWN_TASK----");
                    EPGBean epgBean = null;
                    MediaBean channelParams2 = null;
                    Map<MediaBean, EPGBean> downData = (Map) msg.obj;
                    LogcatUtils.m321d("---CMD_CHECK_DOWN_TASK----downData=" + downData);
                    if (downData == null || downData.size() <= 0) {
                        LogcatUtils.m321d("----mEpgPageList=" + PageEPG.this.mEpgPageList);
                        LogcatUtils.m321d("----mLivePageChannelList=" + PageEPG.this.mLivePageChannelList);
                        if (PageEPG.this.mLivePageChannelList == null) {
                            LogcatUtils.m321d("---mDataManager=" + PageEPG.this.mDataManager + "&mTvFormt=" + PageEPG.this.mTvFormt);
                            if (PageEPG.this.mDataManager != null && PageEPG.this.mDataManager.isLivePageHasData()) {
                                PageEPG.this.mLivePageChannelList = PageEPG.this.mDataManager.getLiveData(PageEPG.this.mTvFormt);
                            }
                        }
                        LogcatUtils.m321d("--3--mEpgPageList=" + PageEPG.this.mEpgPageList);
                        LogcatUtils.m321d("--3--mLivePageChannelList=" + PageEPG.this.mLivePageChannelList);
                        if (PageEPG.this.mEpgPageList == null || PageEPG.this.mEpgPageList.size() == 0 || PageEPG.this.mLivePageChannelList == null) {
                            LogcatUtils.m321d("--CMD_CHECK_DOWN_TASK---return-");
                            return;
                        } else {
                            epgBean = (EPGBean) PageEPG.this.mEpgPageList.get(PageEPG.this.mRecordInx);
                            channelParams2 = (MediaBean) PageEPG.this.mLivePageChannelList.get(PageEPG.this.mEpgPageChannelInx);
                        }
                    } else {
                        for (Entry<MediaBean, EPGBean> entry : downData.entrySet()) {
                            epgBean = (EPGBean) entry.getValue();
                            channelParams2 = (MediaBean) entry.getKey();
                            LogcatUtils.m321d("channelParams2------->" + channelParams2 + "------epgBean----->" + epgBean);
                        }
                    }
                    PageEPG.this.checkDownTask(channelParams2, epgBean);
                    return;
                case 7:
                    PageEPG.this.addDownTaskToList((MediaBean) PageEPG.this.mLivePageChannelList.get(PageEPG.this.mEpgPageChannelInx), (EPGBean) PageEPG.this.mEpgPageList.get(PageEPG.this.mRecordInx));
                    return;
                case 8:
                    PageEPG.this.initEpgPageListAdapter();
                    return;
                case 9:
                    PageEPG.this.mDataManager.initLiveDataFromNet();
                    if (PageEPG.this.mDataManager.isLivePageHasData()) {
                        PageEPG.this.mLivePageChannelList = PageEPG.this.mDataManager.getLiveData(PageEPG.this.mTvFormt);
                        if (PageEPG.this.mMainApp.getCateType() == 1) {
                            PageEPG.this.mUiHandler.sendMessage(PageEPG.this.mUiHandler.obtainMessage(17));
                            return;
                        } else if (PageEPG.this.mMainApp.getCateType() == 2) {
                            PageEPG.this.mUiHandler.sendMessage(PageEPG.this.mUiHandler.obtainMessage(18));
                            return;
                        } else {
                            return;
                        }
                    } else {
                        PageEPG.this.mWorkHandler.sendMessage(PageEPG.this.mWorkHandler.obtainMessage(9));
                        return;
                    }
                case 10:
                    PageEPG.this.startLoadData();
                    LogcatUtils.m321d("failed time is " + PageEPG.this.mGetCount);
                    if (PageEPG.this.mGetCount == 5 && ((PageEPG.this.mEpgPageList == null || PageEPG.this.mEpgPageList.isEmpty()) && PageEPG.this.mEpgPageList != null)) {
                        PageEPG.this.mEpgPageList.add(new EPGBean());
                    }
                    if (PageEPG.this.mEpgPageList == null || PageEPG.this.mEpgPageList.isEmpty()) {
                        PageEPG pageEPG = PageEPG.this;
                        pageEPG.mGetCount = pageEPG.mGetCount + 1;
                        PageEPG.this.onShowLoading(true);
                        PageEPG.this.mWorkHandler.removeMessages(10);
                        PageEPG.this.mWorkHandler.sendEmptyMessageDelayed(10, 2000);
                        return;
                    }
                    PageEPG.this.mGetCount = 0;
                    PageEPG.this.onShowLoading(false);
                    PageEPG.this.mWorkHandler.removeMessages(8);
                    PageEPG.this.mWorkHandler.sendMessage(PageEPG.this.mWorkHandler.obtainMessage(8));
                    return;
                case 11:
                    PageEPG.this.startLoadVodData();
                    LogcatUtils.m321d("failed time is " + PageEPG.this.mGetCount);
                    if (PageEPG.this.mGetCount == 5 && ((PageEPG.this.mEpgPageList == null || PageEPG.this.mEpgPageList.isEmpty()) && PageEPG.this.mEpgPageList != null)) {
                        PageEPG.this.mEpgPageList.add(new EPGBean());
                    }
                    if (PageEPG.this.mEpgPageList == null || PageEPG.this.mEpgPageList.isEmpty()) {
                        PageEPG pageEPG2 = PageEPG.this;
                        pageEPG2.mGetCount = pageEPG2.mGetCount + 1;
                        PageEPG.this.onShowLoading(true);
                        PageEPG.this.mWorkHandler.removeMessages(11);
                        PageEPG.this.mWorkHandler.sendEmptyMessageDelayed(11, 2000);
                        return;
                    }
                    PageEPG.this.mGetCount = 0;
                    PageEPG.this.onShowLoading(false);
                    PageEPG.this.mWorkHandler.removeMessages(8);
                    PageEPG.this.mWorkHandler.sendMessage(PageEPG.this.mWorkHandler.obtainMessage(8));
                    return;
                default:
                    return;
            }
        }
    }

    public int getmDateInx() {
        return this.mDateInx;
    }

    public void setmDateInx(int mDateInx2) {
        this.mDateInx = mDateInx2;
    }

    public View getmContentView() {
        return this.mContentView;
    }

    public void setmContentView(View mContentView2) {
        this.mContentView = mContentView2;
    }

    public PageEPG(Context context, PageEPGCallBackListener backListener2) {
        super(context);
        this.backListener = backListener2;
    }

    public void onCreate(Context context) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService("layout_inflater");
        this.mDownThread = new HandlerThread("WorkThread");
        this.mDownThread.start();
        this.mWorkHandler = new WorkHandler(this.mDownThread.getLooper());
        this.mUiHandler = new UiHandler();
        this.mMainApp = (NettvActivity) context;
        this.mContext = context;
        this.mDownUtils = DownUtils.getDownUtilsInstance(context);
        SWSysProp.init(context);
        this.mDBProcessor = DBProcessor.getDBProcessor(context);
        SharedPreUtil.getSharedPreUtil(context);
        this.mTimer = new Timer();
        this.mDataManager = DataManager.getInstance(context);
        SDKRemoteManager.getInstance(context, null);
        this.mContentView = layoutInflater.inflate(R.layout.page_epg, this);
        initUi();
        initDate();
    }

    public void onResume() {
        LogcatUtils.m321d("******onResume******");
        this.mEpgListView.setFocusable(true);
        this.mTvFormt = this.mMainApp.getmTvFormt();
        this.mEpgPageChannelInx = this.mMainApp.getChannelInx();
        LogcatUtils.m321d("epgPageChannelInx=" + this.mEpgPageChannelInx + " mTvFormt=" + this.mTvFormt);
        if (this.mDataManager.isLivePageHasData()) {
            this.mLivePageChannelList = this.mDataManager.getLiveData(this.mTvFormt);
        } else {
            this.mWorkHandler.sendEmptyMessage(9);
        }
        LogcatUtils.m321d("mMainApp.getCateType()------------>" + this.mMainApp.getCateType());
        if (this.mMainApp.isPlaying()) {
            this.backListener.epgPageCallBack(7, null);
        }
        if (this.mMainApp.getCateType() == 1) {
            this.isAtEpgPage = true;
            this.isAtVodPage = false;
            initList(7);
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(17));
        } else if (this.mMainApp.getCateType() == 2) {
            this.isAtEpgPage = false;
            this.isAtVodPage = true;
            initList(7);
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(18));
        }
        showPage();
        this.mEpgListView.requestFocus();
    }

    public void onStop() {
        LogcatUtils.m321d("*****onStop******");
        this.mContentView.setVisibility(8);
        this.mEpgListView.setFocusable(false);
    }

    public void onDestroy() {
    }

    public void doByMessage(Message data, int MessageFlag) {
        switch (MessageFlag) {
            case 0:
                this.mDownIcon.setVisibility(0);
                return;
            case 1:
                clearUpEpgListData();
                return;
            case 2:
                LogcatUtils.m321d("SHOW_RESERVATION----------->");
                if (DownUtils.usbChecked()) {
                    LogcatUtils.m321d("change surface!!!");
                    Message msg = Message.obtain();
                    msg.what = 4;
                    msg.obj = data.obj;
                    this.mWorkHandler.sendMessage(msg);
                    this.backListener.epgPageCallBack(8, null);
                    return;
                }
                this.backListener.epgPageCallBack(10, null);
                return;
            case 3:
                play(this.mCurPosition);
                return;
            default:
                return;
        }
    }

    private void initUi() {
        this.mRlEpgRoot = (RelativeLayout) this.mContentView.findViewById(R.id.rl_epg_root);
        this.mIvChangeMark = (ImageView) this.mContentView.findViewById(R.id.iv_change_mark);
        this.mChannelNameView = (TextView) this.mContentView.findViewById(R.id.newname);
        this.mTvChannelNum = (TextView) this.mContentView.findViewById(R.id.tv_epg_channelnum);
        this.mTvChannelNum.getPaint().setFakeBoldText(true);
        this.mEpgDateView = (TextView) this.mContentView.findViewById(R.id.date_pzb);
        this.mEpgTimeView = (TextView) this.mContentView.findViewById(R.id.time_pzb);
        this.mChanelIconView = (ImageView) this.mContentView.findViewById(R.id.channelicon);
        this.mEpgLayout = (RelativeLayout) this.mContentView.findViewById(R.id.pzb_bg);
        this.mEpgTitleIcon = (ImageView) this.mContentView.findViewById(R.id.epg_title_icon);
        this.mEpgListView = (ListView) this.mContentView.findViewById(R.id.vodlist);
        this.mLoadingIv = (ImageView) this.mContentView.findViewById(R.id.loading_iv);
        this.mPzqbPage = (LinearLayout) LayoutInflater.from(this.mContext).inflate(R.layout.pzqb_layout_epgpage, null);
        this.mRlEpgRoot.addView(this.mPzqbPage);
        this.mFileNameView = (TextView) this.mPzqbPage.findViewById(R.id.filename);
        this.mFileNameView.getPaint().setFakeBoldText(true);
        this.mFileIndexView = (TextView) this.mPzqbPage.findViewById(R.id.paqb_index);
        this.mFileDateView = (TextView) this.mPzqbPage.findViewById(R.id.date_pzqb);
        this.mNwNameView = (TextView) this.mPzqbPage.findViewById(R.id.name_pzqb);
        this.mNwNameView.getPaint().setFakeBoldText(true);
        this.mFileIconView = (ImageView) this.mPzqbPage.findViewById(R.id.pzqbicon);
        this.mFileDesView = (TextView) this.mPzqbPage.findViewById(R.id.description);
        this.mTvPzqbTime = (TextView) this.mPzqbPage.findViewById(R.id.tv_pzqb_time);
        this.mIvPzqbTip = (TextView) this.mPzqbPage.findViewById(R.id.pzqb_epg_ok);
        this.mFileDesView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PageEPG.this.isAtPzqbPage) {
                    PageEPG.this.mUiHandler.sendMessage(PageEPG.this.mUiHandler.obtainMessage(15));
                }
            }
        });
        this.mPzqbPage.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void play(int position) {
        LogcatUtils.m321d("play position " + position);
        String startTime = XmlPullParser.NO_NAMESPACE;
        String endTime = XmlPullParser.NO_NAMESPACE;
        String type = XmlPullParser.NO_NAMESPACE;
        if (this.isAtVodPage) {
            type = "0";
            long sTime = this.mEpgProgram.getUtc();
            long eTime = this.mEpgProgram.getEndUtc();
            String startTime2 = DateTimeUtil.UtcToDate(sTime);
            String endTime2 = DateTimeUtil.UtcToDate(eTime);
            LogcatUtils.m322e("-abc-startTime=" + startTime2 + "&endTime=" + endTime2);
            LogcatUtils.m322e("----abc--DATES=" + DATES[this.mDateInx]);
            DateFormat df = new SimpleDateFormat("mm:ss");
            try {
                if (df.parse(startTime2).getTime() - df.parse(endTime2).getTime() <= 0) {
                    endTime = new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(endTime2).append(":00").toString();
                } else if (this.mDateInx - 1 < 0) {
                    endTime = new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(endTime2).append(":00").toString();
                } else {
                    endTime = new StringBuilder(String.valueOf(DATES[this.mDateInx - 1])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(endTime2).append(":00").toString();
                }
                startTime = new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime2).append(":00").toString();
            } catch (Exception e) {
                startTime = new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime2).append(":00").toString();
                endTime = new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(endTime2).append(":00").toString();
                e.printStackTrace();
            }
        } else if (this.isAtEpgPage) {
            if (this.mRecordInx == 0 && this.mDateInx == 0) {
                type = "1";
            } else {
                this.isAtPzqbPage = true;
                this.mUiHandler.removeMessages(15);
                this.mUiHandler.sendEmptyMessage(6);
                this.mUiHandler.sendEmptyMessage(7);
                this.mUiHandler.sendMessageDelayed(this.mUiHandler.obtainMessage(15), 10000);
                return;
            }
        }
        this.mMainApp.setChannelInx(this.mEpgPageChannelInx);
        LogcatUtils.m321d("----mLivePageChannelList=" + this.mLivePageChannelList);
        LogcatUtils.m321d("---mEpgPageChannelInx=" + this.mEpgPageChannelInx);
        if (this.mLivePageChannelList == null) {
            LogcatUtils.m321d("---mDataManager=" + this.mDataManager);
            LogcatUtils.m321d("---mTvFormt=" + this.mTvFormt);
            if (this.mDataManager != null && this.mDataManager.isLivePageHasData()) {
                this.mLivePageChannelList = this.mDataManager.getLiveData(this.mTvFormt);
            }
        }
        LogcatUtils.m321d("--1--mLivePageChannelList=" + this.mLivePageChannelList);
        LogcatUtils.m321d("--1--mEpgPageChannelInx=" + this.mEpgPageChannelInx);
        if (this.mLivePageChannelList != null) {
            MediaBean curChannel = (MediaBean) this.mLivePageChannelList.get(this.mEpgPageChannelInx);
            HashMap<MediaBean, EPGBean> playDataMap = new HashMap<>();
            playDataMap.put(curChannel, this.mEpgProgram);
            Message msg = new Message();
            msg.what = 2;
            msg.obj = playDataMap;
            Bundle mBundle = new Bundle();
            mBundle.putString(MailDbHelper.TYPE, type);
            if ("1".equals(type)) {
                mBundle.putInt("clickPage", 1);
            } else {
                mBundle.putInt("clickPage", 2);
            }
            String playDate = DATES[this.mDateInx];
            mBundle.putString("startTime", startTime);
            mBundle.putString("endTime", endTime);
            mBundle.putString("playDate", playDate);
            msg.setData(mBundle);
            if ("1".equals(type)) {
                this.mDataManager.setPlayingChannel(curChannel);
            }
            if ("0".equals(type)) {
                this.mClickIndex = position;
                this.mClickPage = this.mVodPageIndex;
            }
            this.backListener.epgPageCallBack(2, msg);
            this.mDBProcessor.setProp("TvFormt", this.mTvFormt);
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
            onStop();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LogcatUtils.m321d("onKeyDown keyCode " + keyCode);
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastClickTime < 500 && keyCode != 4) {
            return true;
        }
        mLastClickTime = currentTime;
        switch (keyCode) {
            case 4:
                if (this.isAtPzqbPage) {
                    if (this.mUiHandler.hasMessages(15)) {
                        this.mUiHandler.removeMessages(15);
                    }
                    this.mUiHandler.sendEmptyMessage(15);
                } else if (this.mMainApp.isPlaying()) {
                    this.mMainApp.stopMediaPlay();
                } else if ((this.mMainApp.getBackPageIndex() == 3 || this.mMainApp.getBackPageIndex() == 0) && SWApplication.INSTANCE.isHotKey()) {
                    SWApplication.INSTANCE.setHotKey(false);
                    this.mMainApp.goBackPage();
                } else {
                    this.mMainApp.exitNettv();
                }
                mVodWeek = 0;
                return true;
            case 19:
                if (this.mEpgListView.getFirstVisiblePosition() != this.mCurVodPosition) {
                    return true;
                }
                if (this.mVodPageIndex > 0) {
                    this.mVodPageIndex--;
                    this.mEpgPageAdapter.notifyDataSetChanged();
                    this.mEpgListView.setSelection(9);
                    LogcatUtils.m321d("***KEYCODE_DPAD_UP***pageVodIndex====*******" + this.mVodPageIndex);
                    return true;
                } else if (this.mVodPageIndex != 0) {
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
            case 20:
                if (this.mEpgListView.getLastVisiblePosition() == this.mCurVodPosition) {
                    if (this.mVodPageIndex >= this.pageVodCount - 1) {
                        if (this.mVodPageIndex == this.pageVodCount - 1) {
                            LogcatUtils.m321d("***KEYCODE_DPAD_DOWN2***pageVodIndex==******" + this.mVodPageIndex + "******pageVodCount*****" + this.pageVodCount);
                            if (!this.isAtEpgPage) {
                                if (this.isAtVodPage) {
                                    LogcatUtils.m321d("mDateInx is " + this.mDateInx);
                                    this.mDateInx++;
                                    if (this.mDateInx <= DATES.length - 1) {
                                        LogcatUtils.m321d("mDateInx is " + this.mDateInx);
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
                        this.mVodPageIndex++;
                        this.mEpgListView.setSelection(0);
                        this.mEpgPageAdapter.notifyDataSetChanged();
                        LogcatUtils.m321d("***KEYCODE_DPAD_DOWN1***pageVodIndex==******" + this.mVodPageIndex);
                        break;
                    }
                }
                break;
            case 21:
                if (!this.isAtReservationHintPage && !this.isAtPzqbPage && this.mLivePageChannelList != null && this.mLivePageChannelList.size() > 1) {
                    this.mEpgPageChannelInx--;
                    if (this.mEpgPageChannelInx < 0) {
                        this.mEpgPageChannelInx = this.mLivePageChannelList.size() - 1;
                    }
                    this.mMainApp.setChannelInx(this.mEpgPageChannelInx);
                    this.isDelay = true;
                    this.mUiHandler.removeMessages(4);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(4));
                    this.mUiHandler.removeMessages(2);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                    this.mWorkHandler.removeMessages(3);
                    this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(3));
                    break;
                }
            case 22:
                if (!this.isAtReservationHintPage && !this.isAtPzqbPage && this.mLivePageChannelList != null && this.mLivePageChannelList.size() > 1) {
                    this.mEpgPageChannelInx++;
                    LogcatUtils.m321d("epgPageChannelInx---->" + this.mEpgPageChannelInx + "------mLivePageChannelList.size()----->" + this.mLivePageChannelList.size());
                    if (this.mEpgPageChannelInx > this.mLivePageChannelList.size() - 1) {
                        this.mEpgPageChannelInx = 0;
                    }
                    this.mMainApp.setChannelInx(this.mEpgPageChannelInx);
                    this.isDelay = true;
                    this.mUiHandler.removeMessages(4);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(4));
                    this.mUiHandler.removeMessages(2);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                    this.mWorkHandler.removeMessages(3);
                    this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(3));
                    break;
                }
            case 23:
                LogcatUtils.m321d("center--->");
                if (!this.isAtPzqbPage) {
                    return true;
                }
                LogcatUtils.m321d("isAtPzqbPage--->");
                return true;
            case 184:
                LogcatUtils.m321d("mTvFormt---->" + this.mTvFormt + " isAtReservationHintPage is " + this.isAtReservationHintPage + " isAtPzqbPage is " + this.isAtPzqbPage + " mTvFormt is " + this.mTvFormt);
                if (!this.isAtReservationHintPage && !this.isAtPzqbPage) {
                    switch (this.mTvFormt) {
                        case 1:
                            this.mTvFormt = 2;
                            this.mEpgPageChannelInx = 0;
                            this.mMainApp.setmTvFormt(2);
                            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(4));
                            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                            this.isDelay = true;
                            this.mWorkHandler.sendMessage(this.mUiHandler.obtainMessage(3));
                            return true;
                        case 2:
                            this.mTvFormt = 3;
                            this.mEpgPageChannelInx = 0;
                            this.mMainApp.setmTvFormt(3);
                            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(4));
                            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                            this.isDelay = true;
                            this.mWorkHandler.sendMessage(this.mUiHandler.obtainMessage(3));
                            return true;
                        case 3:
                            this.mTvFormt = 1;
                            this.mEpgPageChannelInx = 0;
                            this.mMainApp.setmTvFormt(1);
                            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(4));
                            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(2));
                            this.isDelay = true;
                            this.mWorkHandler.sendMessage(this.mUiHandler.obtainMessage(3));
                            return true;
                        default:
                            return true;
                    }
                }
            case 185:
                if (!this.isAtReservationHintPage) {
                    if (this.isAtPzqbPage) {
                        this.mUiHandler.sendEmptyMessage(15);
                        return true;
                    }
                    this.isAtPzqbPage = true;
                    this.mUiHandler.removeMessages(15);
                    this.mUiHandler.sendEmptyMessage(6);
                    this.mUiHandler.sendEmptyMessage(7);
                    this.mUiHandler.sendMessageDelayed(this.mUiHandler.obtainMessage(15), 10000);
                    return true;
                }
                break;
            case 186:
                if (!DownUtils.usbChecked()) {
                    this.mUiHandler.sendEmptyMessage(21);
                    return true;
                } else if (this.isAtPzqbPage) {
                    if (this.mUiHandler.hasMessages(15)) {
                        this.mUiHandler.removeMessages(15);
                    }
                    this.mUiHandler.sendEmptyMessage(15);
                    addProgramToTask();
                    return true;
                } else {
                    LogcatUtils.m321d("DownUtils.usbChecked()");
                    if (this.mEpgPageList == null || this.mEpgPageList.size() <= 0) {
                        LogcatUtils.m321d("no data!!!");
                        return true;
                    }
                    if (this.isAtPzqbPage) {
                        this.mUiHandler.sendEmptyMessage(15);
                    }
                    this.mWorkHandler.removeMessages(4);
                    this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(4));
                    return true;
                }
        }
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public void onShowLoading(final boolean isVisible) {
        this.mMainApp.runOnUiThread(new Runnable() {
            public void run() {
                if (PageEPG.this.mLastStatus != isVisible) {
                    PageEPG.this.mLoadingIv.setVisibility(isVisible ? 0 : 8);
                    PageEPG.this.mLoadingIv.setBackgroundResource(R.anim.page_loading);
                    PageEPG.this.mPageLoaddingAnim = (AnimationDrawable) PageEPG.this.mLoadingIv.getBackground();
                    if (isVisible) {
                        PageEPG.this.mPageLoaddingAnim.start();
                    } else {
                        PageEPG.this.mPageLoaddingAnim.stop();
                    }
                    PageEPG.this.mLastStatus = isVisible;
                }
            }
        });
    }

    public void addProgramToTask() {
        boolean z = false;
        if (this.mCustomResDialog != null && this.mCustomResDialog.isShowing()) {
            this.mCustomResDialog.dismiss();
        }
        this.isAtReservationHintPage = false;
        if (this.mDownUtils.isDownTaskMAX()) {
            this.backListener.epgPageCallBack(13, null);
            return;
        }
        LogcatUtils.m321d("cy!mDownUtils.isDownTaskMAX()");
        LinearLayout layout = null;
        if (this.mEpgListView != null) {
            layout = (LinearLayout) this.mEpgListView.getSelectedView();
            LogcatUtils.m321d("mEpgListView.getSelectedItemPosition());" + this.mEpgListView.getSelectedItemPosition());
            StringBuilder sb = new StringBuilder("*******initDownIcon****layout != null**");
            if (layout != null) {
                z = true;
            }
            LogcatUtils.m321d(sb.append(z).toString());
        }
        Message iconMsg = Message.obtain();
        iconMsg.what = 13;
        iconMsg.obj = layout;
        this.mUiHandler.sendMessage(iconMsg);
        this.mWorkHandler.sendEmptyMessage(7);
        this.mEpgListView.requestFocus();
    }

    /* access modifiers changed from: private */
    public void initEpgPageListAdapter() {
        try {
            LogcatUtils.m321d("***********mEpgPageList.size********" + this.mEpgPageList.size());
            if (this.mEpgPageList != null && this.mEpgPageList.size() > 0) {
                this.mRecordInx = 0;
                this.mVodPageIndex = 0;
                float count = ((float) this.mEpgPageList.size()) / 10.0f;
                this.pageVodCount = count % 1.0f == 0.0f ? (int) count : (int) (count + 1.0f);
                if (this.mEpgPageAdapter == null) {
                    LogcatUtils.m321d("start set adapter mEpgPageList is " + this.mEpgPageList + "  curProgramType is " + this.curProgramType);
                    this.mEpgPageAdapter = new MoreVodAdapter(this.mContext, this.mEpgPageList, this.curProgramType);
                    this.mEpgListView.setOnItemSelectedListener(this.mEpgPageAdapter);
                    this.mEpgListView.setOnItemClickListener(this.mEpgPageAdapter);
                    this.mEpgListView.setOnKeyListener(this.mEpgPageAdapter);
                } else {
                    this.mEpgPageAdapter.getList().clear();
                    this.mEpgPageAdapter.setList(this.mEpgPageList);
                    this.mEpgPageAdapter.setProgramType(this.curProgramType);
                }
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void loadEpgData() {
        LogcatUtils.m321d("....loadEpgData....");
        this.mWorkHandler.sendEmptyMessage(10);
    }

    public void startLoadData() {
        long startUtcTime;
        long endUtcTime;
        if (this.isDelay) {
            LogcatUtils.m321d("mDateInx----->" + this.mDateInx);
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
            LogcatUtils.m321d("....startUtcTime===...." + startUtcTime);
            if (this.mDateInx == 0) {
                LogcatUtils.m321d("-------mTvFormt=" + this.mTvFormt + "&mMainApp=" + this.mMainApp.getChannelInx() + "&startUtcTime=" + startUtcTime + "&endUtcTime=" + (endUtcTime - 1));
                this.mEpgPageList = this.mDataManager.getProgramListByChannel(this.mTvFormt, this.mMainApp.getChannelInx(), startUtcTime, endUtcTime - 1, true);
            } else {
                this.mEpgPageList = this.mDataManager.getProgramListByChannel(this.mTvFormt, this.mMainApp.getChannelInx(), startUtcTime, endUtcTime - 1, false);
            }
            LogcatUtils.m321d("mEpgPageList----->" + this.mEpgPageList);
        }
    }

    /* access modifiers changed from: private */
    public void loadVodData() {
        LogcatUtils.m321d("....loadVodData...");
        this.mWorkHandler.sendEmptyMessage(11);
    }

    public void startLoadVodData() {
        long endUtcTime;
        if (this.isDelay) {
            long startUtcTime = DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(" 00:00:00").toString());
            if (this.mDateInx == 0) {
                endUtcTime = System.currentTimeMillis();
            } else {
                endUtcTime = DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(" 23:59:59").toString());
            }
            LogcatUtils.m321d("....DATES[mDateInx]===...." + DATES[this.mDateInx]);
            this.mEpgPageList = this.mDataManager.getProgramListByChannel(this.mTvFormt, this.mMainApp.getChannelInx(), startUtcTime, endUtcTime, false);
            if (this.mEpgPageList != null && this.mEpgPageList.size() > 0) {
                Collections.reverse(this.mEpgPageList);
                if (this.mDateInx == 0) {
                    this.mEpgPageList.remove(0);
                }
            }
            LogcatUtils.m321d("mEpgPageList----->" + this.mEpgPageList);
        }
    }

    /* access modifiers changed from: private */
    public void decideLoadData() {
        LogcatUtils.m321d("========decideLoadData========");
        this.isDelay = false;
        intervalofTime = 0;
        this.delayTime2 += 10;
        if (this.delayTime2 > 120) {
            this.delayTime2 = 120;
        }
        while (!this.isDelay) {
            try {
                LogcatUtils.m321d("=============" + intervalofTime);
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
                LogcatUtils.m321d("...Exception...===" + e.toString());
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public void clearUpEpgListData() {
        LogcatUtils.m321d("...clearUpEpgListData...");
        if (this.mEpgPageAdapter != null) {
            this.mEpgListView.setAdapter(null);
            this.mEpgPageAdapter.notifyDataSetChanged();
        }
    }

    /* access modifiers changed from: private */
    public void loadVodPage() {
        LogcatUtils.m321d("********loadVodPage*******");
        this.mMainApp.setCateType(2);
        this.mUiHandler.sendEmptyMessage(4);
        this.mMainApp.setFullPlay(false);
        this.mWorkHandler.sendEmptyMessage(2);
    }

    /* access modifiers changed from: private */
    public void loadEpgPage() {
        LogcatUtils.m321d("********loadEpgPage*******");
        this.mMainApp.setCateType(1);
        this.mUiHandler.sendEmptyMessage(4);
        this.mMainApp.setFullPlay(false);
        this.mWorkHandler.sendEmptyMessage(1);
    }

    /* access modifiers changed from: private */
    public void showPage() {
        LogcatUtils.m321d("...showPage...");
        if (!this.isAtVodPage) {
            this.mDateInx = 0;
            this.mUiHandler.sendEmptyMessage(5);
        }
        initPreDate();
        initWeekView(mVodWeek);
        this.mMainApp.setFullPlay(false);
        this.isAtPzqbPage = false;
        setEpgPageBackground();
        this.mContentView.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void initList(int count) {
        WEEK_STR = new String[count];
        DATE_STR = new String[count];
        DATES = new String[count];
    }

    /* access modifiers changed from: private */
    public void showChannelView(MediaBean channel) {
        MediaBean curChannel = channel;
        if (curChannel != null) {
            try {
                String newName = curChannel.getTitle();
                String channelIconUrl = curChannel.getImage();
                LogcatUtils.m321d("[channelIconUrl]" + channelIconUrl);
                this.mTvChannelNum.setText(PageLive.getChannelNumStr(curChannel.getChannelNumber(), this.mMainApp.getmTvFormt()));
                this.mChannelNameView.setText(newName);
                Glide.with(this.mContext).load(channelIconUrl).centerCrop().into(this.mChanelIconView);
            } catch (Exception e) {
                e.printStackTrace();
                LogcatUtils.m321d("...Exception...===" + e.toString());
            }
        }
    }

    private void setEpgPageBackground() {
        LogcatUtils.m321d("*********setEpgPageBackground*****isAtEpgPage==********isAtVodPage****" + this.isAtVodPage);
        if (this.isAtEpgPage) {
            this.mEpgTitleIcon.setBackgroundResource(R.drawable.epg_title_icon);
        } else if (this.isAtVodPage) {
            this.mEpgTitleIcon.setBackgroundResource(R.drawable.vod_title_icon2);
        }
    }

    /* access modifiers changed from: private */
    public void initPzqbPage(MediaBean channel, EPGBean epgBean) {
        LogcatUtils.m321d(".......initPzqbPage....");
        String fileIndex = PageLive.getChannelNumStr(channel.getChannelNumber(), this.mMainApp.getmTvFormt());
        LogcatUtils.m321d(".......recordInx==...." + this.mRecordInx);
        if (epgBean != null) {
            try {
                String descript = epgBean.getDescription();
                String fileName = epgBean.getTitle();
                String startTimeStr = DateTimeUtil.UtcToDate(epgBean.getUtc());
                String endTimeStr = DateTimeUtil.UtcToDate(epgBean.getEndUtc());
                String fileDate = DATE_STR[this.mDateInx];
                String newsName = channel.getTitle();
                String mIconUrl = channel.getImage();
                LogcatUtils.m321d("*******mIconUrl******" + mIconUrl);
                this.mFileNameView.setText(fileName);
                this.mFileIndexView.setText(fileIndex);
                this.mFileDateView.setText(fileDate);
                this.mTvPzqbTime.setText(new StringBuilder(String.valueOf(startTimeStr)).append("-").append(endTimeStr).toString());
                this.mNwNameView.setText(newsName);
                this.mFileDesView.setText(descript);
                Glide.with(this.mContext).load(mIconUrl).fitCenter().into(this.mFileIconView);
            } catch (Exception e) {
                e.printStackTrace();
                LogcatUtils.m321d("...Exception...====" + e.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public void freshDatePage() {
        LogcatUtils.m321d("...freshDatePage..mDateInx==." + this.mDateInx + "  mVodWeek is " + mVodWeek);
        for (int findViewById : DAYS) {
            ((Button) findViewById(findViewById)).setBackgroundResource(R.drawable.epg_date_unchecked);
        }
        ((Button) findViewById(DAYS[this.mDateInx - (mVodWeek * 7)])).setBackgroundResource(R.drawable.epg_date_checked);
    }

    /* access modifiers changed from: private */
    public void initDownIcon(LinearLayout layout) {
        LogcatUtils.m321d("*******initDownIcon******");
        if (layout != null) {
            LogcatUtils.m321d("layout != null");
            this.mDownIcon = (ImageView) layout.findViewById(R.id.down_icon);
            this.mDownIcon.setVisibility(0);
            this.mDownIcon.setImageResource(R.drawable.wait);
        }
    }

    private void initDate() {
        if (this.mTimer != null) {
            this.mTimer.schedule(new TimerTask() {
                public void run() {
                    Calendar calendar = Calendar.getInstance();
                    int month = calendar.get(2) + 1;
                    int day = calendar.get(5);
                    calendar.get(7);
                    int hour = calendar.get(11);
                    int minute = calendar.get(12);
                    PageEPG.this.dateStr = new StringBuilder(String.valueOf(month)).append("月").append(day < 10 ? "0" + day : Integer.valueOf(day)).append("日").toString();
                    PageEPG.this.timeStr = (hour < 10 ? "0" + hour : Integer.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : Integer.valueOf(minute));
                    PageEPG.this.mUiHandler.sendEmptyMessage(14);
                }
            }, 100, DateTime.MILLIS_PER_MINUTE);
        }
    }

    /* access modifiers changed from: private */
    public void initWeekView(int page) {
        for (int i = 0; i < 7; i++) {
            Button btView = (Button) findViewById(DAYS[i]);
            String text = WEEK_STR[(page * 7) + i];
            LogcatUtils.m321d("WEEK_STR size is " + WEEK_STR.length + " dayinfo is " + text);
            btView.setText(text);
        }
        int position = this.mDateInx - (mVodWeek * 7);
        int[] iArr = DAYS;
        if (position <= 0) {
            position = 0;
        }
        Button bt1 = (Button) findViewById(iArr[position]);
        bt1.setBackgroundResource(R.drawable.epg_date_checked);
        bt1.requestFocus();
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
        logArr(WEEK_STR);
        logArr(DATES);
        logArr(DATE_STR);
    }

    private void logArr(Object[] arr) {
        for (int i = 0; i < arr.length; i++) {
            LogcatUtils.m321d("arr---" + i + "----->" + arr[i]);
        }
    }

    /* access modifiers changed from: private */
    public void hidePzqbPage() {
        LogcatUtils.m321d("...hiddenPzqb...");
        this.isAtPzqbPage = false;
        this.mPzqbPage.setVisibility(8);
        this.mEpgLayout.setVisibility(0);
        this.mEpgListView.requestFocus();
    }

    /* access modifiers changed from: private */
    public void checkDownTask(MediaBean channelParams, EPGBean epgBean) {
        LogcatUtils.m321d("*******checkDownTask*******");
        if (DownUtils.usbChecked()) {
            String fileMask = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(DateTimeUtil.UtcToDate(epgBean.getUtc())).append(":00").toString()))).toString())).append(channelParams.getId()).toString();
            LogcatUtils.m321d("filemask---->" + fileMask);
            if (!this.mDownUtils.isContainsKey(fileMask)) {
                this.isAtReservationHintPage = true;
                this.mUiHandler.removeMessages(20);
                this.mUiHandler.sendEmptyMessage(20);
                return;
            }
            this.backListener.epgPageCallBack(14, null);
            return;
        }
        this.backListener.epgPageCallBack(10, null);
    }

    /* access modifiers changed from: private */
    public void addDownTaskToList(MediaBean channelParams, EPGBean program) {
        LogcatUtils.m321d("....addDownTaskToList......");
        LogcatUtils.m321d("channelParams----->" + channelParams.getId());
        try {
            if (channelParams.getUrls() == null || channelParams.getUrls().get(0) == null) {
                LogcatUtils.m321d(".......url为空........");
                return;
            }
            ArrayList<UrlBean> urlList = channelParams.getUrls();
            String vodUrl = XmlPullParser.NO_NAMESPACE;
            int quality = 3;
            while (quality >= 0) {
                LogcatUtils.m321d("quality---->" + quality);
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
                LogcatUtils.m321d(".......no such quality........" + quality);
                quality--;
                this.mDataManager.setQuality(quality);
            }
            LogcatUtils.m321d("vodUrl------>" + vodUrl);
            if (StringUtils.isEmpty(vodUrl)) {
                LogcatUtils.m321d("vodUrl is null,cannot download!!!");
                return;
            }
            String channelMask = channelParams.getId();
            String programName = program.getTitle();
            String startTime = DateTimeUtil.UtcToDate(program.getUtc());
            String endTime = DateTimeUtil.UtcToDate(program.getEndUtc());
            String description = program.getDescription();
            String channelName = channelParams.getTitle();
            int channelNum = channelParams.getChannelNumber();
            String channelIconUrl = channelParams.getImage();
            String fileMask = new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(DateTimeUtil.getUtcTime(new StringBuilder(String.valueOf(DATES[this.mDateInx])).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(startTime).append(":00").toString()))).toString())).append(channelMask).toString();
            String programType = program.getType();
            DownloadTask downFile = new DownloadTask();
            downFile.setChannelMask(channelMask);
            LogcatUtils.m321d("channelMask------>" + channelMask);
            downFile.setDate(DATES[this.mDateInx]);
            downFile.setDesc(description);
            downFile.setVodUrl(vodUrl);
            downFile.setTaskName(fileMask);
            downFile.setStartTime(startTime);
            downFile.setEndTime(endTime);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPageEPGCallBackListener(PageEPGCallBackListener backListener2) {
        this.backListener = backListener2;
    }
}
