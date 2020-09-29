package net.sunniwell.app.linktaro.nettv.content;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.format.Formatter;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import net.sunniwell.aidl.bean.EPGBean;
import net.sunniwell.aidl.bean.MediaBean;
import net.sunniwell.aidl.bean.UrlBean;
import net.sunniwell.app.linktaro.SWApplication;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.Constants.C0412Constants;
import net.sunniwell.app.linktaro.nettv.download.DownUtils;
import net.sunniwell.app.linktaro.nettv.download.DownloadTask;
import net.sunniwell.app.linktaro.nettv.entry.NettvActivity;
import net.sunniwell.app.linktaro.nettv.processor.imp.DBProcessor;
import net.sunniwell.app.linktaro.nettv.view.CustomInfoDialog;
import net.sunniwell.app.linktaro.nettv.view.CustomResDialog;
import net.sunniwell.app.linktaro.nettv.view.PromptDialogBox;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.common.tools.DateTime;
import org.codehaus.jackson.org.objectweb.asm.Opcodes;
import org.xmlpull.p019v1.XmlPullParser;

public class PageDownLoad extends PageBase {
    public static final int DATA_LIST_VIEW = 0;
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(PageDownLoad.class);
    private static final int PZQB_HIDE_TIME = 3000;
    private static final int VIEW_COUNT = 9;
    /* access modifiers changed from: private */
    public static MoreDownAdapter mDownPageAdapter;
    /* access modifiers changed from: private */
    public static List<DownloadTask> mDownPageList;
    private static Map<String, List<DownloadTask>> mDownPageMap;
    private PageDownLoadCallBackListener backListener;
    /* access modifiers changed from: private */
    public String category;
    private int categoryInx = 0;
    private List<String> categoryList;
    /* access modifiers changed from: private */
    public String dateStr;
    /* access modifiers changed from: private */
    public boolean isAtDeleteDownInfo;
    /* access modifiers changed from: private */
    public boolean isAtPzqb;
    /* access modifiers changed from: private */
    public boolean isAtframePage;
    private Map<String, List<Integer>> mBatchDeleteInfo;
    private Button mBtframeBack;
    /* access modifiers changed from: private */
    public Button mBtframePlay;
    private View mContentView;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public TextView mCurDate;
    /* access modifiers changed from: private */
    public DownloadTask mCurDownloadTask;
    /* access modifiers changed from: private */
    public TextView mCurTime;
    private CustomInfoDialog mCustomInfoDialog;
    private CustomResDialog mCustomResDialog;
    private PromptDialogBox mDialogBox;
    private RelativeLayout mDownLoadPageContent;
    /* access modifiers changed from: private */
    public String mDownLoadSpeed;
    /* access modifiers changed from: private */
    public ListView mDownPageListView;
    /* access modifiers changed from: private */
    public int mDownPos;
    /* access modifiers changed from: private */
    public int mDownSelect;
    /* access modifiers changed from: private */
    public TextView mDownSpeedView;
    /* access modifiers changed from: private */
    public ImageView mDownStateView;
    private HandlerThread mDownThread;
    /* access modifiers changed from: private */
    public DownUtils mDownUtils;
    private TextView mFileDateView;
    /* access modifiers changed from: private */
    public TextView mFileDesView;
    private ImageView mFileIconView;
    private TextView mFileIndexView;
    private TextView mFileNameView;
    /* access modifiers changed from: private */
    public boolean mIsAtBatchDelete = false;
    /* access modifiers changed from: private */
    public boolean mIsNothingSelected = false;
    /* access modifiers changed from: private */
    public ImageView mIvChannelIcon;
    private ImageView mIvframeIcon;
    private ImageView mIvframeShow;
    /* access modifiers changed from: private */
    public NettvActivity mMainApp;
    private TextView mNwNameView;
    /* access modifiers changed from: private */
    public int mPageDownCount;
    /* access modifiers changed from: private */
    public int mPageDownInx;
    /* access modifiers changed from: private */
    public TextView mProgramDate;
    /* access modifiers changed from: private */
    public TextView mProgramTime;
    /* access modifiers changed from: private */
    public TextView mProgramType;
    /* access modifiers changed from: private */
    public LinearLayout mPzqbPage;
    private RelativeLayout mRlDownpageRoot;
    private Timer mTimer;
    /* access modifiers changed from: private */
    public TextView mTvChannelName;
    /* access modifiers changed from: private */
    public TextView mTvChannelNum;
    /* access modifiers changed from: private */
    public TextView mTvProgramName;
    private TextView mTvPzqbTime;
    private TextView mTvframeDate;
    private TextView mTvframeDes;
    private TextView mTvframeFilename;
    private TextView mTvframeIndex;
    private TextView mTvframeName;
    private TextView mTvframeTime;
    /* access modifiers changed from: private */
    @SuppressLint({"HandlerLeak"})
    public Handler mUiHandler = new Handler() {
        public void handleMessage(Message msg) {
            PageDownLoad.LOG.mo8825d("...msg.waht===" + msg.what);
            switch (msg.what) {
                case 0:
                    PageDownLoad.LOG.mo8825d("********FRESH_DOWN_SPEED******");
                    if (PageDownLoad.this.mDownUtils.getDownLoadStatus() == 1 && PageDownLoad.this.mPageDownInx == 0 && PageDownLoad.this.mDownPageListView.getChildAt(0) != null) {
                        PageDownLoad.LOG.mo8825d("mDownPageListView.getChildAt(0) != null");
                        TextView speedView = (TextView) PageDownLoad.this.mDownPageListView.getChildAt(0).findViewById(C0395R.C0397id.filestate);
                        ImageView statusView = (ImageView) PageDownLoad.this.mDownPageListView.getChildAt(0).findViewById(C0395R.C0397id.down_icon);
                        speedView.setVisibility(0);
                        statusView.setVisibility(8);
                        PageDownLoad.this.mDownLoadSpeed = Formatter.formatFileSize(PageDownLoad.this.mContext, (long) PageDownLoad.this.mDownUtils.getStbDownLoadSpeed());
                        PageDownLoad.LOG.mo8825d("----mDownLoadSpeed----" + PageDownLoad.this.mDownLoadSpeed);
                        speedView.setText(PageDownLoad.this.mDownLoadSpeed);
                    }
                    PageDownLoad.this.mUiHandler.sendEmptyMessageDelayed(0, 1000);
                    return;
                case 1:
                    PageDownLoad.LOG.mo8825d("********PAUSE_DOWN_SPEED******");
                    if (PageDownLoad.this.mDownPageListView.getChildAt(0) != null) {
                        ImageView statusView2 = (ImageView) PageDownLoad.this.mDownPageListView.getChildAt(0).findViewById(C0395R.C0397id.down_icon);
                        ((TextView) PageDownLoad.this.mDownPageListView.getChildAt(0).findViewById(C0395R.C0397id.filestate)).setVisibility(8);
                        statusView2.setImageResource(C0395R.C0396drawable.stop);
                        statusView2.setVisibility(0);
                        return;
                    }
                    return;
                case 2:
                    PageDownLoad.this.initPzqbPage((DownloadTask) PageDownLoad.mDownPageList.get(PageDownLoad.this.mDownPos));
                    return;
                case 3:
                    PageDownLoad.LOG.mo8825d("SHOW_PAGE_PZQB");
                    PageDownLoad.this.isAtPzqb = true;
                    PageDownLoad.this.mPzqbPage.setVisibility(0);
                    PageDownLoad.this.mFileDesView.requestFocus();
                    return;
                case 4:
                    PageDownLoad.this.isAtPzqb = false;
                    PageDownLoad.this.mPzqbPage.setVisibility(8);
                    PageDownLoad.this.mDownPageListView.requestFocus();
                    return;
                case 5:
                    PageDownLoad.this.showDownTaskInfo();
                    return;
                case 6:
                    PageDownLoad.this.freshDownPage((List) msg.obj);
                    return;
                case 7:
                    PageDownLoad.this.invalidView();
                    return;
                case 8:
                    PageDownLoad.LOG.mo8825d("dateStr--->" + PageDownLoad.this.dateStr);
                    int wk = Calendar.getInstance().get(7);
                    PageDownLoad.this.mCurDate.setText(new StringBuilder(String.valueOf(PageDownLoad.this.dateStr)).append("(").append(PageDownLoad.this.mContext.getResources().getStringArray(C0395R.array.Week)[wk - 1]).append(")").toString());
                    PageDownLoad.this.mCurTime.setText(PageDownLoad.this.timeStr);
                    return;
                case 9:
                    PageDownLoad.LOG.mo8825d("CHANGE_CATEGORY_NAME---->" + PageDownLoad.this.category);
                    PageDownLoad.this.mProgramType.setText(PageDownLoad.this.category);
                    if (PageDownLoad.this.getVisibility() == 0) {
                        PageDownLoad.this.mDownPageListView.requestFocus();
                        return;
                    }
                    return;
                case 10:
                    PageDownLoad.this.clearUpEpgListData();
                    return;
                case 12:
                    if (PageDownLoad.this.mPageDownInx == 0 && PageDownLoad.this.mDownPageListView.getChildAt(0) != null && PageDownLoad.mDownPageList != null && PageDownLoad.mDownPageList.size() > 0 && 1 == ((DownloadTask) PageDownLoad.mDownPageList.get(0)).getStatus() && PageDownLoad.this.mDownUtils.getDownLoadStatus() == 2) {
                        ImageView statusView3 = (ImageView) PageDownLoad.this.mDownPageListView.getChildAt(0).findViewById(C0395R.C0397id.down_icon);
                        ((TextView) PageDownLoad.this.mDownPageListView.getChildAt(0).findViewById(C0395R.C0397id.filestate)).setVisibility(8);
                        statusView3.setVisibility(0);
                        statusView3.setImageResource(C0395R.C0396drawable.stop);
                        return;
                    }
                    return;
                case 13:
                    PageDownLoad.mDownPageAdapter.notifyDataSetChanged();
                    return;
                case 14:
                    PageDownLoad.this.showDownDialog(PageDownLoad.this.mContext.getResources().getString(C0395R.string.usb_lack_sapce));
                    return;
                case 15:
                    if (PageDownLoad.mDownPageAdapter != null) {
                        PageDownLoad.mDownPageAdapter.notifyDataSetChanged();
                        return;
                    }
                    return;
                case 16:
                    PageDownLoad.LOG.mo8825d("------mDownPageListView=" + PageDownLoad.this.mDownPageListView);
                    if (PageDownLoad.this.mDownPageListView != null) {
                        PageDownLoad.this.mDownPageListView.requestFocus();
                        return;
                    }
                    return;
                case 17:
                    PageDownLoad.this.isAtframePage = true;
                    PageDownLoad.this.mframePage.setVisibility(0);
                    PageDownLoad.this.mBtframePlay.requestFocus();
                    PageDownLoad.this.showFramePage();
                    return;
                case 18:
                    PageDownLoad.this.hideFramePage();
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public WorkHandler mWorkHandler;
    /* access modifiers changed from: private */
    public LinearLayout mframePage;
    /* access modifiers changed from: private */
    public String timeStr;

    private class MoreDownAdapter extends BaseAdapter implements OnItemSelectedListener, OnItemClickListener {
        private Context context;
        private int index;
        private List<DownloadTask> list;

        public MoreDownAdapter(Context context2, List<DownloadTask> list2) {
            this.list = list2;
            this.context = context2;
        }

        public List<DownloadTask> getList() {
            return this.list;
        }

        public void setList(List<DownloadTask> list2) {
            this.list = list2;
        }

        public int getCount() {
            int ori = PageDownLoad.this.mPageDownInx * 9;
            if (this.list.size() < 9) {
                PageDownLoad.LOG.mo8825d("list.size() --->" + this.list.size());
                return this.list.size();
            } else if (this.list.size() - ori < 9) {
                return this.list.size() - ori;
            } else {
                return 9;
            }
        }

        public Object getItem(int position) {
            return this.list.get(position);
        }

        public long getItemId(int position) {
            return (long) position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            PageDownLoad.LOG.mo8825d("getView()------------->" + position);
            LayoutInflater inflater = LayoutInflater.from(this.context);
            if (convertView == null) {
                v = inflater.inflate(C0395R.layout.downlist, null);
            } else {
                v = convertView;
            }
            TextView timeView = (TextView) v.findViewById(C0395R.C0397id.timeofdown);
            timeView.getPaint().setFakeBoldText(true);
            TextView fileView = (TextView) v.findViewById(C0395R.C0397id.fileofdown);
            fileView.getPaint().setFakeBoldText(true);
            PageDownLoad.this.mDownSpeedView = (TextView) v.findViewById(C0395R.C0397id.filestate);
            PageDownLoad.this.mDownSpeedView.getPaint().setFakeBoldText(true);
            PageDownLoad.this.mDownStateView = (ImageView) v.findViewById(C0395R.C0397id.down_icon);
            if (PageDownLoad.this.mPageDownInx >= 0 && PageDownLoad.this.mPageDownInx <= PageDownLoad.this.mPageDownCount - 1) {
                if (PageDownLoad.this.mPageDownInx > 0) {
                    this.index = (PageDownLoad.this.mPageDownInx * 9) + position;
                } else {
                    this.index = (PageDownLoad.this.mPageDownInx * 9) + position;
                }
            }
            DownloadTask downFile = (DownloadTask) this.list.get(this.index);
            PageDownLoad.LOG.mo8825d("getView()------------->" + downFile);
            if (downFile != null) {
                String[] str = downFile.getDate().split("-");
                String date = str[1] + "-" + str[2];
                timeView.setText(downFile.getStartTime() + "-" + downFile.getEndTime());
                fileView.setText(downFile.getProgramName());
                int downFlag = downFile.getStatus();
                PageDownLoad.LOG.mo8825d("---date=" + date + "&time=" + timeView.getText().toString() + "&downFlag=" + downFlag + "&fileName=" + downFile.getProgramName());
                PageDownLoad.this.checkDownIcon(downFlag, position);
            }
            return v;
        }

        public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
            this.index = (PageDownLoad.this.mPageDownInx * 9) + position;
            if (PageDownLoad.this.mIsAtBatchDelete) {
                PageDownLoad.this.mDownStateView = (ImageView) arg1.findViewById(C0395R.C0397id.down_icon);
                if (PageDownLoad.this.isBatContains(this.index)) {
                    PageDownLoad.this.batchDeleteUnsel(this.index);
                    PageDownLoad.this.mDownStateView.setImageResource(C0395R.C0396drawable.bat_delete_unsel);
                    return;
                }
                PageDownLoad.this.batchDeleteSelete(this.index);
                PageDownLoad.this.mDownStateView.setImageResource(C0395R.C0396drawable.bat_delete_sel);
            } else if (this.list != null && this.list.size() > 0) {
                DownloadTask downFile = (DownloadTask) this.list.get(this.index);
                if (downFile != null && downFile.getStatus() == 2) {
                    PageDownLoad.this.mCurDownloadTask = downFile;
                    PageDownLoad.this.mUiHandler.sendEmptyMessage(17);
                }
            }
        }

        public void onItemSelected(AdapterView<?> adapterView, View arg1, int position, long arg3) {
            PageDownLoad.this.mDownSelect = position;
            PageDownLoad.this.mDownPos = (PageDownLoad.this.mPageDownInx * 9) + position;
            PageDownLoad.LOG.mo8825d("..onItemSelected.mDownPos...====" + PageDownLoad.this.mDownPos + "&position=" + position);
            DownloadTask downFile = (DownloadTask) this.list.get(PageDownLoad.this.mDownPos);
            PageDownLoad.LOG.mo8825d("----downFile = " + downFile.toString());
            PageDownLoad.this.mTvChannelNum.setText(PageLive.getChannelNumStr(downFile.getChannelNum(), Integer.valueOf(downFile.getTvFormt()).intValue()));
            PageDownLoad.this.mTvChannelName.setText(downFile.getChannelName());
            PageDownLoad.this.mTvProgramName.setText(downFile.getProgramName());
            PageDownLoad.this.mProgramDate.setText(downFile.getDate());
            PageDownLoad.this.mProgramTime.setText(downFile.getStartTime() + "~" + downFile.getEndTime());
            Glide.with((Activity) PageDownLoad.this.mMainApp).load(downFile.getChannelIconUrl()).into(PageDownLoad.this.mIvChannelIcon);
        }

        public void onNothingSelected(AdapterView<?> adapterView) {
            PageDownLoad.LOG.mo8825d("...onNothingSelected...");
            PageDownLoad.this.mUiHandler.sendEmptyMessage(7);
            PageDownLoad.this.mIsNothingSelected = true;
        }
    }

    public interface PageDownLoadCallBackListener {
        void downLoadPageCallBack(int i, Message message);

        void showInfoDialog();
    }

    @SuppressLint({"HandlerLeak"})
    public class WorkHandler extends Handler {
        @SuppressLint({"HandlerLeak"})
        public WorkHandler(Looper mLooper) {
            super(mLooper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 3:
                    PageDownLoad.this.mDownUtils.reStartDownTask();
                    return;
                case 5:
                    PageDownLoad.this.mDownUtils.checkLivetask();
                    return;
                case 6:
                    if (PageDownLoad.this.mDownUtils.getDownLoadStatus() == 2) {
                        PageDownLoad.this.mUiHandler.sendEmptyMessage(0);
                        PageDownLoad.this.mDownUtils.resumeDownTask();
                        return;
                    } else if (PageDownLoad.this.mDownUtils.getDownLoadStatus() == 1) {
                        PageDownLoad.this.mDownUtils.pauseDownTask();
                        PageDownLoad.this.mUiHandler.sendEmptyMessage(1);
                        return;
                    } else {
                        return;
                    }
                case 7:
                    PageDownLoad.this.removeDownTask();
                    return;
                case 8:
                    PageDownLoad.this.batchDelete();
                    PageDownLoad.this.mUiHandler.sendEmptyMessage(15);
                    return;
                default:
                    return;
            }
        }
    }

    public PageDownLoad(Context context, PageDownLoadCallBackListener backListener2) {
        super(context);
        setPageDownLoadCallBackListener(backListener2);
    }

    public View getmContentView() {
        return this.mContentView;
    }

    public void setmContentView(View mContentView2) {
        this.mContentView = mContentView2;
    }

    public void onCreate(Context context) {
        LOG.mo8825d("*******onCreate******");
        this.mContentView = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0395R.layout.page_down_load, this);
        DBProcessor.getDBProcessor(context);
        this.mMainApp = (NettvActivity) context;
        this.mContext = context;
        SWSysProp.init(context);
        this.mDownUtils = DownUtils.getDownUtilsInstance(context);
        this.mDownUtils.setmMainApp(this.mMainApp);
        this.mDownThread = new HandlerThread("WorkThread");
        this.mDownThread.start();
        this.mWorkHandler = new WorkHandler(this.mDownThread.getLooper());
        this.mDownLoadSpeed = "0KB/S";
        this.category = XmlPullParser.NO_NAMESPACE;
        this.mTimer = new Timer();
        this.mCurDownloadTask = new DownloadTask();
        initUi();
        initDate();
    }

    public void onResume() {
        LOG.mo8825d("*******onResume******");
        if (this.mMainApp.isPlaying()) {
            this.backListener.downLoadPageCallBack(6, null);
        }
        loadDownPageData();
        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(9));
        this.mContentView.setVisibility(0);
        this.mDownLoadPageContent.setVisibility(0);
        if (this.mDownUtils != null && !this.mDownUtils.isUsbAvailable && this.mDownUtils.getDownLoadStatus() == 2) {
            this.mUiHandler.sendEmptyMessage(14);
        }
        this.mUiHandler.sendEmptyMessageDelayed(16, 1500);
        LOG.mo8825d("-----mMainApp=" + this.mMainApp);
        this.mDownUtils.setmMainApp(this.mMainApp);
        if (this.mDownUtils.getDownLoadStatus() == 1) {
            this.mUiHandler.removeMessages(0);
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(0));
        }
        if (!DownUtils.usbChecked()) {
            this.backListener.showInfoDialog();
        }
    }

    public void onStop() {
        LOG.mo8825d("*******onStop******");
        this.mPzqbPage.setVisibility(8);
        this.mContentView.setVisibility(8);
        this.mDownLoadPageContent.setVisibility(8);
        clearUpEpgListData();
    }

    public void onDestroy() {
        LOG.mo8825d("*******onDestroy******");
    }

    private void initUi() {
        LOG.mo8825d("*******initUi******");
        this.mRlDownpageRoot = (RelativeLayout) this.mContentView.findViewById(C0395R.C0397id.rl_download_root);
        this.mDownLoadPageContent = (RelativeLayout) this.mContentView.findViewById(C0395R.C0397id.downLoadPageContent);
        this.mIvChannelIcon = (ImageView) this.mContentView.findViewById(C0395R.C0397id.iv_download_channelicon);
        this.mTvChannelNum = (TextView) this.mContentView.findViewById(C0395R.C0397id.tv_download_channelnum);
        this.mTvChannelName = (TextView) this.mContentView.findViewById(C0395R.C0397id.tv_download_channelname);
        this.mTvProgramName = (TextView) this.mContentView.findViewById(C0395R.C0397id.tv_download_programname);
        this.mProgramDate = (TextView) this.mContentView.findViewById(C0395R.C0397id.tvProgramDate);
        this.mProgramTime = (TextView) this.mContentView.findViewById(C0395R.C0397id.tvProgramTime);
        this.mCurDate = (TextView) this.mContentView.findViewById(C0395R.C0397id.tvCurDate);
        this.mCurTime = (TextView) this.mContentView.findViewById(C0395R.C0397id.tvCurTime);
        this.mProgramType = (TextView) this.mContentView.findViewById(C0395R.C0397id.tvProgramType);
        this.mDownPageListView = (ListView) this.mContentView.findViewById(C0395R.C0397id.lvProgramList);
        this.mDownPageListView.setDivider(null);
        this.mPzqbPage = (LinearLayout) LayoutInflater.from(this.mContext).inflate(C0395R.layout.pzqb_layout_downpage, null);
        this.mRlDownpageRoot.addView(this.mPzqbPage);
        this.mFileNameView = (TextView) this.mPzqbPage.findViewById(C0395R.C0397id.filename);
        this.mFileNameView.getPaint().setFakeBoldText(true);
        this.mFileIndexView = (TextView) this.mPzqbPage.findViewById(C0395R.C0397id.paqb_index);
        this.mFileDateView = (TextView) this.mPzqbPage.findViewById(C0395R.C0397id.date_pzqb);
        this.mNwNameView = (TextView) this.mPzqbPage.findViewById(C0395R.C0397id.name_pzqb);
        this.mNwNameView.getPaint().setFakeBoldText(true);
        this.mFileIconView = (ImageView) this.mPzqbPage.findViewById(C0395R.C0397id.pzqbicon);
        this.mFileDesView = (TextView) this.mPzqbPage.findViewById(C0395R.C0397id.description);
        this.mTvPzqbTime = (TextView) this.mPzqbPage.findViewById(C0395R.C0397id.tv_pzqb_time);
        this.mFileDesView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PageDownLoad.this.isAtPzqb) {
                    PageDownLoad.this.mUiHandler.sendMessage(PageDownLoad.this.mUiHandler.obtainMessage(4));
                }
            }
        });
        this.mPzqbPage.setVisibility(8);
        initFramePage();
    }

    private void initFramePage() {
        this.mframePage = (LinearLayout) LayoutInflater.from(this.mContext).inflate(C0395R.layout.vodframe_detail_layout, null);
        this.mRlDownpageRoot.addView(this.mframePage);
        this.mTvframeFilename = (TextView) this.mframePage.findViewById(C0395R.C0397id.tv_vodframe_filename);
        this.mTvframeFilename.getPaint().setFakeBoldText(true);
        this.mTvframeDate = (TextView) this.mframePage.findViewById(C0395R.C0397id.tv_vodframe_date);
        this.mTvframeTime = (TextView) this.mframePage.findViewById(C0395R.C0397id.tv_vodframe_time);
        this.mIvframeIcon = (ImageView) this.mframePage.findViewById(C0395R.C0397id.iv_vodframe_icon);
        this.mTvframeIndex = (TextView) this.mframePage.findViewById(C0395R.C0397id.tv_vodframe_index);
        this.mTvframeName = (TextView) this.mframePage.findViewById(C0395R.C0397id.tv_vodframe_name);
        this.mTvframeName.getPaint().setFakeBoldText(true);
        this.mTvframeDes = (TextView) this.mframePage.findViewById(C0395R.C0397id.tv_vodframe_description);
        this.mIvframeShow = (ImageView) this.mframePage.findViewById(C0395R.C0397id.vodframe_show_icon);
        this.mBtframePlay = (Button) this.mframePage.findViewById(C0395R.C0397id.vodframe_play);
        this.mBtframeBack = (Button) this.mframePage.findViewById(C0395R.C0397id.vodframe_back);
        this.mBtframePlay.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PageDownLoad.this.isAtframePage) {
                    PageDownLoad.this.hideFrameToPlay();
                }
            }
        });
        this.mBtframeBack.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (PageDownLoad.this.isAtframePage) {
                    PageDownLoad.this.hideFramePage();
                }
            }
        });
        this.mframePage.setVisibility(8);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LOG.mo8825d("***onKeyDown******keyCode===*****" + keyCode);
        switch (keyCode) {
            case 4:
                if (this.isAtPzqb) {
                    this.mUiHandler.sendEmptyMessage(4);
                } else if (this.isAtDeleteDownInfo) {
                    hiddenDelteDialog();
                } else if (this.mIsAtBatchDelete) {
                    this.mIsAtBatchDelete = false;
                    if (mDownPageAdapter != null) {
                        mDownPageAdapter.notifyDataSetChanged();
                    }
                    return true;
                } else if (this.isAtframePage) {
                    this.mUiHandler.sendEmptyMessage(18);
                } else if (this.mMainApp.isPlaying()) {
                    this.mMainApp.stopMediaPlay();
                } else if ((this.mMainApp.getBackPageIndex() == 0 || this.mMainApp.getBackPageIndex() == 1) && SWApplication.INSTANCE.isHotKey()) {
                    SWApplication.INSTANCE.setHotKey(false);
                    this.mMainApp.goBackPage();
                } else {
                    this.mMainApp.exitNettv();
                }
                return true;
            case 8:
                return true;
            case 9:
                return true;
            case 19:
                if (this.isAtPzqb) {
                    return true;
                }
                if (this.mDownPageListView.getFirstVisiblePosition() == this.mDownSelect) {
                    if (this.mPageDownCount == 1) {
                        this.mDownPageListView.setSelection(0);
                        return true;
                    }
                    LOG.mo8825d("===mDownSelect===" + this.mDownSelect);
                    if (this.mPageDownInx > 0) {
                        this.mPageDownInx--;
                    } else if (this.mPageDownInx == 0) {
                        this.mPageDownInx = this.mPageDownCount - 1;
                    }
                    LOG.mo8825d("mPageDownInx ---->" + this.mPageDownInx + "mPageDownCount---->" + this.mPageDownCount);
                    if (mDownPageAdapter != null) {
                        mDownPageAdapter.notifyDataSetChanged();
                    }
                    this.mDownPageListView.setSelection((this.mPageDownInx + 1) * 8);
                    this.mDownPageListView.requestFocus();
                    LOG.mo8825d("mDownPageListView.getLastVisiblePosition()" + this.mDownPageListView.getLastVisiblePosition());
                }
                return true;
            case 20:
                if (this.isAtPzqb) {
                    return true;
                }
                if (this.mDownPageListView.getLastVisiblePosition() == this.mDownSelect) {
                    LOG.mo8825d("===mDownSelect===" + this.mDownSelect);
                    if (this.mPageDownInx < this.mPageDownCount - 1) {
                        this.mPageDownInx++;
                    } else if (this.mPageDownInx == this.mPageDownCount - 1) {
                        this.mPageDownInx = 0;
                    }
                    LOG.mo8825d("mPageDownInx ---->" + this.mPageDownInx + "mPageDownCount---->" + this.mPageDownCount);
                    this.mDownPageListView.setSelection(this.mDownPageListView.getFirstVisiblePosition());
                    mDownPageAdapter.notifyDataSetChanged();
                    this.mDownPageListView.requestFocus();
                    LOG.mo8825d("mDownPageListView.getFirstVisiblePosition()" + this.mDownPageListView.getFirstVisiblePosition());
                }
                return true;
            case 21:
                if (this.isAtPzqb) {
                    return true;
                }
                if (this.isAtDeleteDownInfo) {
                    return true;
                }
                if (this.isAtframePage) {
                    this.mBtframePlay.setNextFocusLeftId(C0395R.C0397id.vodframe_play);
                    this.mBtframeBack.setNextFocusLeftId(C0395R.C0397id.vodframe_play);
                    return true;
                }
                if (this.categoryList != null && this.categoryList.size() > 1) {
                    LOG.mo8825d("categoryInx---->" + this.categoryInx);
                    this.categoryInx--;
                    this.mPageDownInx = 0;
                    this.mPageDownCount = 0;
                    if (this.categoryInx < 0) {
                        this.categoryInx = this.categoryList.size() - 1;
                    }
                    this.category = (String) this.categoryList.get(this.categoryInx);
                    loadDownPageData(this.category);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(9));
                }
                return true;
            case 22:
                if (this.isAtPzqb) {
                    return true;
                }
                if (this.isAtDeleteDownInfo) {
                    return true;
                }
                if (this.isAtframePage) {
                    this.mBtframePlay.setNextFocusRightId(C0395R.C0397id.vodframe_back);
                    this.mBtframeBack.setNextFocusRightId(C0395R.C0397id.vodframe_back);
                    return true;
                }
                if (this.categoryList != null && this.categoryList.size() > 1) {
                    LOG.mo8825d("categoryInx---->" + this.categoryInx);
                    this.categoryInx++;
                    this.mPageDownInx = 0;
                    this.mPageDownCount = 0;
                    if (this.categoryInx > this.categoryList.size() - 1) {
                        this.categoryInx = 0;
                    }
                    this.category = (String) this.categoryList.get(this.categoryInx);
                    loadDownPageData(this.category);
                    this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(9));
                }
                return true;
            case 23:
                return true;
            case 69:
                this.mMainApp.setmTvFormt(3);
                this.backListener.downLoadPageCallBack(4, null);
                return true;
            case 84:
                if (this.isAtPzqb) {
                    return true;
                }
                if (this.mIsAtBatchDelete) {
                    return true;
                }
                this.mMainApp.setCateType(1);
                this.backListener.downLoadPageCallBack(3, null);
                onStop();
                return true;
            case Opcodes.SASTORE /*86*/:
                if (this.isAtPzqb) {
                    return true;
                }
                if (this.mMainApp.isPlaying()) {
                    this.backListener.downLoadPageCallBack(5, null);
                }
                return true;
            case Opcodes.DUP2 /*92*/:
                if (!this.isAtPzqb) {
                    if (this.mPageDownInx > 0) {
                        this.mPageDownInx--;
                    } else if (this.mPageDownInx == 0) {
                        this.mPageDownInx = this.mPageDownCount - 1;
                    }
                    this.mDownPageListView.setSelection(0);
                    mDownPageAdapter.notifyDataSetChanged();
                    this.mDownPageListView.requestFocus();
                    break;
                } else {
                    return true;
                }
            case Opcodes.DUP2_X1 /*93*/:
                if (!this.isAtPzqb) {
                    if (this.mPageDownInx < this.mPageDownCount - 1) {
                        this.mPageDownInx++;
                    } else if (this.mPageDownInx == this.mPageDownCount - 1) {
                        this.mPageDownInx = 0;
                    }
                    this.mDownPageListView.setSelection(0);
                    mDownPageAdapter.notifyDataSetChanged();
                    this.mDownPageListView.requestFocus();
                    break;
                } else {
                    return true;
                }
            case 140:
                this.mMainApp.setmTvFormt(2);
                this.backListener.downLoadPageCallBack(4, null);
                return true;
            case 142:
                this.mMainApp.setmTvFormt(1);
                this.backListener.downLoadPageCallBack(4, null);
                return true;
            case 174:
                if (this.isAtPzqb) {
                    this.mUiHandler.sendEmptyMessage(4);
                } else {
                    this.mUiHandler.removeMessages(4);
                    this.mUiHandler.sendEmptyMessage(3);
                    this.mUiHandler.sendEmptyMessage(2);
                    this.mUiHandler.sendMessageDelayed(this.mUiHandler.obtainMessage(4), 3000);
                }
                return true;
            case 183:
                if (this.isAtPzqb) {
                    return true;
                }
                if (this.mIsNothingSelected) {
                    return true;
                }
                if (this.mIsAtBatchDelete) {
                    showBatDeleteDialog();
                    return true;
                }
                if (this.isAtDeleteDownInfo) {
                    hiddenDelteDialog();
                } else {
                    showDelteDialog();
                }
                return true;
            case 184:
                if (this.isAtPzqb) {
                    return true;
                }
                this.mUiHandler.sendEmptyMessage(5);
                return true;
            case 185:
                if (this.isAtPzqb) {
                    return true;
                }
                controlDownload();
                return true;
            case 186:
                LOG.mo8825d("isAtPzqb--->" + this.isAtPzqb + "---isAtDeleteDownInfo-->" + this.isAtDeleteDownInfo + "--mIsNothingSelected-->" + this.mIsNothingSelected + "----mIsAtBatchDelete--->" + this.mIsAtBatchDelete);
                if (this.isAtPzqb) {
                    return true;
                }
                if (this.isAtDeleteDownInfo) {
                    return true;
                }
                if (this.mIsNothingSelected) {
                    return true;
                }
                if (this.mIsAtBatchDelete) {
                    this.mIsAtBatchDelete = false;
                    if (mDownPageAdapter != null) {
                        mDownPageAdapter.notifyDataSetChanged();
                    }
                    return true;
                }
                this.mIsAtBatchDelete = true;
                if (this.mBatchDeleteInfo != null) {
                    this.mBatchDeleteInfo.clear();
                } else {
                    this.mBatchDeleteInfo = new HashMap();
                }
                if (mDownPageAdapter != null) {
                    mDownPageAdapter.notifyDataSetChanged();
                }
                return true;
            case C0412Constants.KEY_RECORD /*1184*/:
                if (this.isAtPzqb) {
                    return true;
                }
                if (this.mIsAtBatchDelete) {
                    return true;
                }
                this.mMainApp.setCateType(2);
                this.backListener.downLoadPageCallBack(3, null);
                onStop();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void controlDownload() {
        int index = this.mDownSelect + (this.mPageDownInx * 11);
        LOG.mo8825d("---index=" + index);
        DownloadTask downFile = null;
        if (mDownPageAdapter != null) {
            downFile = (DownloadTask) mDownPageAdapter.getItem(index);
        }
        if (downFile != null && downFile.getStatus() == 1) {
            this.mWorkHandler.sendMessage(this.mWorkHandler.obtainMessage(6));
        }
    }

    public DownUtils getmDownUtils() {
        return this.mDownUtils;
    }

    public void setmDownUtils(DownUtils mDownUtils2) {
        this.mDownUtils = mDownUtils2;
    }

    public void pauseDownload() {
        this.mDownUtils.pauseDownTask();
    }

    /* access modifiers changed from: private */
    public void invalidView() {
        LOG.mo8825d("***invalidView***");
        this.mProgramType.setText(XmlPullParser.NO_NAMESPACE);
        this.mProgramDate.setText(XmlPullParser.NO_NAMESPACE);
        this.mProgramTime.setText(XmlPullParser.NO_NAMESPACE);
        this.mTvChannelNum.setText(XmlPullParser.NO_NAMESPACE);
        this.mTvChannelName.setText(XmlPullParser.NO_NAMESPACE);
        this.mTvProgramName.setText(XmlPullParser.NO_NAMESPACE);
        this.mIvChannelIcon.setImageDrawable(null);
    }

    public void doByMessage(Message data, int MessageFlag) {
        LOG.mo8825d("****doByMessage***");
    }

    /* access modifiers changed from: private */
    public void showDownTaskInfo() {
        float mPro;
        try {
            try {
                int flag = ((DownloadTask) mDownPageList.get(this.mDownPos)).getStatus();
                if (flag == 1) {
                    mPro = (float) this.mDownUtils.getDownProgess();
                } else if (flag == 0) {
                    mPro = 0.0f;
                } else {
                    mPro = 100.0f;
                }
                LOG.mo8825d("****mPro****--->" + mPro);
                String mPorgressVal = String.valueOf(mPro);
                showDownDialog(new StringBuilder(String.valueOf(getResources().getString(C0395R.string.ts_pro_begin))).append(mPorgressVal.substring(0, mPorgressVal.indexOf(".") + 2)).append(getResources().getString(C0395R.string.ts_pro_end)).toString());
            } catch (Exception e) {
                e = e;
                new DownloadTask();
                e.printStackTrace();
            }
        } catch (Exception e2) {
            e = e2;
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void showDownDialog(String msg) {
        LOG.mo8825d("..showDownDialog....." + msg);
        if (this.mDialogBox == null) {
            this.mDialogBox = PromptDialogBox.createDialog(getContext(), msg);
        }
        this.mDialogBox = this.mDialogBox.setMessage(msg);
        this.mDialogBox.show();
    }

    public static void refreshData() {
        LOG.mo8825d("****refreshData*****");
        if (mDownPageMap != null && mDownPageList != null && mDownPageAdapter != null) {
            mDownPageMap.clear();
            mDownPageList.clear();
            mDownPageAdapter.setList(mDownPageList);
            mDownPageAdapter.notifyDataSetChanged();
        }
    }

    private void loadDownPageData() {
        LOG.mo8825d("...loadDownPageData...");
        mDownPageMap = this.mDownUtils.getStbTaskList();
        LOG.mo8825d("mDownPageMap---->" + mDownPageMap);
        this.categoryList = new ArrayList();
        mDownPageList = new ArrayList();
        if (mDownPageMap == null || mDownPageMap.size() <= 0) {
            LOG.mo8825d("mDownPageMap == null !!!!!!!");
            this.mIsNothingSelected = true;
            this.mUiHandler.sendEmptyMessage(7);
            this.mUiHandler.sendEmptyMessage(10);
            return;
        }
        for (Entry<String, List<DownloadTask>> entry : mDownPageMap.entrySet()) {
            this.categoryList.add((String) entry.getKey());
            LOG.mo8825d("--loadDownPageData-category=" + this.category);
        }
        if (this.categoryList.size() > 0) {
            LOG.mo8825d("categoryList--==-->" + this.categoryList.size());
            LOG.mo8825d("categoryList--==-->" + this.categoryList);
            this.category = "全部";
            LOG.mo8825d("---category=" + this.category);
            mDownPageList = (List) mDownPageMap.get(this.category);
        }
        if (mDownPageList == null || mDownPageList.size() <= 0) {
            LOG.mo8825d("mDownPageList is null can't reach");
        } else {
            LOG.mo8825d("mDownPageList--==-->" + mDownPageList.size());
            LOG.mo8825d("mDownPageList--==-->" + mDownPageList);
            this.mIsNothingSelected = false;
        }
        Message msg = this.mUiHandler.obtainMessage(6);
        msg.obj = mDownPageList;
        this.mUiHandler.sendMessage(msg);
    }

    private void loadDownPageData(String category2) {
        LOG.mo8825d("...loadDownPageData...category..." + category2);
        mDownPageList = new ArrayList();
        if (mDownPageMap == null || mDownPageMap.size() <= 0) {
            LOG.mo8825d("mDownPageMap == null !!!!!!!");
            this.mIsNothingSelected = true;
            this.mUiHandler.sendEmptyMessage(7);
            this.mUiHandler.sendEmptyMessage(10);
            return;
        }
        mDownPageList = (List) mDownPageMap.get(category2);
        this.mIsNothingSelected = false;
        if (mDownPageList == null || mDownPageList.size() <= 0) {
            LOG.mo8825d("mDownPageList is null can't reach");
        } else {
            LOG.mo8825d("mDownPageList--==-->" + mDownPageList.size());
        }
        Message msg = this.mUiHandler.obtainMessage(6);
        msg.obj = mDownPageList;
        this.mUiHandler.sendMessage(msg);
    }

    /* access modifiers changed from: private */
    public void clearUpEpgListData() {
        LOG.mo8825d("...clearUpEpgListData...");
        if (mDownPageAdapter != null) {
            this.mDownPageListView.setAdapter(null);
            mDownPageAdapter.notifyDataSetChanged();
        }
    }

    private void goNextCategory() {
        if (this.categoryList != null) {
            LOG.mo8825d("goNextCategory+categoryList.size()----->" + this.categoryList.size());
        }
        if (this.categoryList != null && this.categoryList.size() > 0) {
            LOG.mo8825d("categoryInx---->" + this.categoryInx);
            this.categoryInx++;
            this.mPageDownInx = 0;
            this.mPageDownCount = 0;
            if (this.categoryInx > this.categoryList.size() - 1) {
                this.categoryInx = 0;
            }
            this.category = (String) this.categoryList.get(this.categoryInx);
            loadDownPageData(this.category);
            if (1 == ((DownloadTask) mDownPageList.get(0)).getStatus()) {
                LOG.mo8825d("need fresh speed");
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(0));
            }
            this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(9));
        }
    }

    /* access modifiers changed from: private */
    public void removeDownTask() {
        LOG.mo8825d("----removeDownTask------mDownPageList=" + mDownPageList);
        if (mDownPageList != null && mDownPageList.size() > 0) {
            String key = ((DownloadTask) mDownPageList.get(this.mDownPos)).getTaskName();
            String filePath = new StringBuilder(String.valueOf(((DownloadTask) mDownPageList.get(this.mDownPos)).getSavePath())).append(((DownloadTask) mDownPageList.get(this.mDownPos)).getLocalFile()).toString();
            LOG.mo8825d("**removeDownTask***key==*****" + key);
            LOG.mo8825d("**removeDownTask***filmPath==*****" + filePath);
            this.mDownUtils.deleteFile(filePath);
            this.mDownUtils.removeTaskInfo(key);
            this.mDownUtils.checkLivetask();
            loadDownPageData();
        }
    }

    /* access modifiers changed from: private */
    public void checkDownIcon(int flag, int position) {
        LOG.mo8825d("...checkDownIcon...flag==" + flag);
        if (this.mIsAtBatchDelete) {
            this.mDownSpeedView.setVisibility(8);
            if (isBatContains(position + (this.mPageDownInx * 9))) {
                this.mDownStateView.setImageResource(C0395R.C0396drawable.bat_delete_sel);
            } else {
                this.mDownStateView.setImageResource(C0395R.C0396drawable.bat_delete_unsel);
            }
            this.mDownStateView.setVisibility(0);
            return;
        }
        if (flag == 0) {
            this.mDownSpeedView.setVisibility(8);
            this.mDownStateView.setImageResource(C0395R.C0396drawable.wait);
            this.mDownStateView.setVisibility(0);
        }
        if (flag == 1) {
            LOG.mo8825d("DownUtils.DOWN_LOADING");
            if (mDownPageList == null || mDownPageList.size() <= 0 || ((DownloadTask) mDownPageList.get(0)).getStatus() != 1 || this.mDownUtils.getDownLoadStatus() != 2) {
                LOG.mo8825d("else----->mPauseDownTask != null && mPauseDownTask.equals('1')");
                this.mDownStateView.setVisibility(8);
                this.mDownSpeedView.setVisibility(0);
                this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(0));
            } else {
                LOG.mo8825d("mPauseDownTask != null && mPauseDownTask.equals('1')");
                this.mDownSpeedView.setVisibility(8);
                this.mDownStateView.setImageResource(C0395R.C0396drawable.stop);
                this.mDownStateView.setVisibility(0);
            }
        }
        if (flag == 2) {
            this.mDownSpeedView.setVisibility(8);
            this.mDownStateView.setImageResource(C0395R.C0396drawable.selected);
            this.mDownStateView.setVisibility(0);
        }
        if (flag == -1) {
            this.mDownSpeedView.setVisibility(8);
            this.mDownStateView.setImageResource(C0395R.C0396drawable.download_failed);
            this.mDownStateView.setVisibility(0);
        }
    }

    /* access modifiers changed from: private */
    public void freshDownPage(List<DownloadTask> singleCateList) {
        LOG.mo8825d("*******freshDownPage******");
        if (singleCateList != null) {
            float count = ((float) singleCateList.size()) / 9.0f;
            this.mPageDownCount = count % 1.0f == 0.0f ? (int) count : (int) (count + 1.0f);
            LOG.mo8825d("mPageDownCount" + this.mPageDownCount);
            mDownPageAdapter = new MoreDownAdapter(getContext(), singleCateList);
            this.mDownPageListView.setOnItemClickListener(mDownPageAdapter);
            this.mDownPageListView.setOnItemSelectedListener(mDownPageAdapter);
            mDownPageAdapter.setList(singleCateList);
            this.mDownPageListView.setAdapter(mDownPageAdapter);
            mDownPageAdapter.notifyDataSetChanged();
            this.mDownPageListView.requestFocus();
        }
    }

    private void showDelteDialog() {
        LOG.mo8825d("....showDelteDialog...");
        this.isAtDeleteDownInfo = true;
        this.mCustomResDialog = new CustomResDialog(this.mContext, C0395R.style.PromptDialog);
        this.mCustomResDialog.setDialogBg(C0395R.C0396drawable.ts_bg);
        this.mCustomResDialog.setOkBgResource(C0395R.C0396drawable.btn_check_ok_sel);
        this.mCustomResDialog.setCancelBgResource(C0395R.C0396drawable.btn_check_cancel_sel);
        this.mCustomResDialog.setTextContent(getResources().getText(C0395R.string.delete_reservation).toString());
        this.mCustomResDialog.setOkClickListener(new OnClickListener() {
            public void onClick(View v) {
                PageDownLoad.this.mWorkHandler.sendMessage(PageDownLoad.this.mWorkHandler.obtainMessage(7));
                PageDownLoad.this.hiddenDelteDialog();
            }
        });
        this.mCustomResDialog.setCancelClickListener(new OnClickListener() {
            public void onClick(View v) {
                PageDownLoad.this.hiddenDelteDialog();
            }
        });
        this.mCustomResDialog.setOnDismissListener(new OnDismissListener() {
            public void onDismiss(DialogInterface dialog) {
                PageDownLoad.this.isAtDeleteDownInfo = false;
            }
        });
        this.mCustomResDialog.show();
    }

    /* access modifiers changed from: private */
    public void hiddenDelteDialog() {
        LOG.mo8825d("....hiddenDelteDialog...");
        this.isAtDeleteDownInfo = false;
        this.mDownPageListView.requestFocus();
        if (this.mCustomResDialog != null) {
            this.mCustomResDialog.dismiss();
            this.mCustomResDialog = null;
        }
    }

    private void play() {
        if (this.mCurDownloadTask != null) {
            if (this.mMainApp.isPlaying()) {
                this.backListener.downLoadPageCallBack(5, null);
            }
            String url = new StringBuilder(String.valueOf(this.mCurDownloadTask.getSavePath())).append(this.mCurDownloadTask.getLocalFile()).toString();
            LOG.mo8825d("***DownLoad******url===****" + url);
            Message message = new Message();
            Bundle bundle = new Bundle();
            EPGBean epgBean = new EPGBean();
            MediaBean mediaBean = new MediaBean();
            Map<MediaBean, EPGBean> playDataMap = new HashMap<>();
            LOG.mo8825d("downFile.getFileStartTime()--->" + this.mCurDownloadTask.getStartTime());
            LOG.mo8825d("downFile.getFileEndTime()--->" + this.mCurDownloadTask.getEndTime());
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
            long startUtc = 0;
            long endUtc = 0;
            try {
                startUtc = sdf.parse(this.mCurDownloadTask.getStartTime()).getTime();
                endUtc = sdf.parse(this.mCurDownloadTask.getEndTime()).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            epgBean.setUtc(startUtc);
            epgBean.setEndUtc(endUtc);
            epgBean.setTitle(this.mCurDownloadTask.getProgramName());
            epgBean.setDescription(this.mCurDownloadTask.getDesc());
            epgBean.setType(this.mCurDownloadTask.getType());
            UrlBean urlBean = new UrlBean();
            urlBean.setUrl(url);
            ArrayList<UrlBean> urlList = new ArrayList<>();
            urlList.add(urlBean);
            mediaBean.setUrls(urlList);
            mediaBean.setTitle(this.mCurDownloadTask.getChannelName());
            mediaBean.setChannelNumber(this.mCurDownloadTask.getChannelNum());
            mediaBean.setId(this.mCurDownloadTask.getChannelMask());
            mediaBean.setImage(this.mCurDownloadTask.getChannelIconUrl());
            playDataMap.put(mediaBean, epgBean);
            message.obj = playDataMap;
            bundle.putString("channelMask", this.mCurDownloadTask.getChannelMask());
            bundle.putString(MailDbHelper.TYPE, "3");
            bundle.putString("playDate", this.mCurDownloadTask.getDate());
            message.setData(bundle);
            this.backListener.downLoadPageCallBack(1, message);
            return;
        }
        LOG.mo8825d("-----PageDownload---mCurDownloadTask is Empty----");
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
                    PageDownLoad.this.dateStr = new StringBuilder(String.valueOf(month)).append("月").append(day < 10 ? "0" + day : Integer.valueOf(day)).append("日").toString();
                    PageDownLoad.LOG.mo8825d("dateStr--->" + PageDownLoad.this.dateStr);
                    PageDownLoad.this.timeStr = (hour < 10 ? "0" + hour : Integer.valueOf(hour)) + ":" + (minute < 10 ? "0" + minute : Integer.valueOf(minute));
                    PageDownLoad.this.mUiHandler.sendEmptyMessage(8);
                }
            }, 100, DateTime.MILLIS_PER_MINUTE);
        }
    }

    /* access modifiers changed from: private */
    public void initPzqbPage(DownloadTask downFile) {
        LOG.mo8825d("******initPzqbPage*******");
        if (downFile != null) {
            try {
                String fileName = downFile.getProgramName();
                String fileDate = downFile.getDate();
                String descript = downFile.getDesc();
                String newsName = downFile.getChannelName();
                String fileNum = PageLive.getChannelNumStr(downFile.getChannelNum(), this.mMainApp.getmTvFormt());
                LOG.mo8825d("******initPzqbPage****fileName=***" + fileName);
                LOG.mo8825d("******initPzqbPage****fileNum=***" + fileNum);
                LOG.mo8825d("******initPzqbPage****fileDate=***" + fileDate);
                LOG.mo8825d("******initPzqbPage****newsName=***" + newsName);
                LOG.mo8825d("******initPzqbPage****descript=***" + descript);
                this.mFileNameView.setText(fileName);
                this.mFileIndexView.setText(fileNum);
                this.mFileDateView.setText(fileDate);
                this.mNwNameView.setText(newsName);
                this.mTvPzqbTime.setText(downFile.getStartTime() + "-" + downFile.getEndTime());
                this.mFileDesView.setText(descript);
                Glide.with(this.mContext).load(downFile.getChannelIconUrl()).centerCrop().into(this.mFileIconView);
            } catch (Exception e) {
                e.printStackTrace();
                LOG.mo8825d("...Exception...====" + e.toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public void showFramePage() {
        LOG.mo8825d("******initframePage*******");
        String str = XmlPullParser.NO_NAMESPACE;
        try {
            if (this.mCurDownloadTask != null) {
                String descript = this.mCurDownloadTask.getDesc();
                String fileName = this.mCurDownloadTask.getProgramName();
                String startTimeStr = this.mCurDownloadTask.getStartTime();
                String endTimeStr = this.mCurDownloadTask.getEndTime();
                String fileDate = this.mCurDownloadTask.getDate();
                String newsName = this.mCurDownloadTask.getChannelName();
                String mIconUrl = this.mCurDownloadTask.getChannelIconUrl();
                String fileIndex = PageLive.getChannelNumStr(this.mCurDownloadTask.getChannelNum(), this.mMainApp.getmTvFormt());
                this.mTvframeFilename.setText(fileName);
                this.mTvframeDate.setText(fileDate);
                this.mTvframeTime.setText(new StringBuilder(String.valueOf(startTimeStr)).append("-").append(endTimeStr).toString());
                this.mTvframeName.setText(newsName);
                this.mTvframeDes.setText(descript);
                this.mTvframeIndex.setText(fileIndex);
                Glide.with(this.mContext).load(mIconUrl).centerCrop().into(this.mIvframeIcon);
                String path = new StringBuilder(String.valueOf(this.mCurDownloadTask.getSavePath())).append(this.mCurDownloadTask.getLocalFile()).toString();
                Glide.with(this.mContext).load(path).into(this.mIvframeShow);
                LOG.mo8825d("---video path----" + path);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...====" + e.toString());
        }
    }

    /* access modifiers changed from: private */
    public void hideFramePage() {
        LOG.mo8825d("...hideFrame...");
        this.isAtframePage = false;
        this.mframePage.setVisibility(8);
        this.mDownLoadPageContent.setVisibility(0);
        this.mDownPageListView.requestFocus();
    }

    /* access modifiers changed from: private */
    public void hideFrameToPlay() {
        LOG.mo8825d("...hideFrameToPlay...");
        this.isAtframePage = false;
        this.mframePage.setVisibility(8);
        play();
    }

    public void callBackSpeedUpdate(String currentSpeed) {
        LOG.mo8825d("callBackSpeedUpdate------------->" + currentSpeed);
    }

    public void callBack2PauseIconUpdate() {
        LOG.mo8825d("callBack2PauseIconUpdate----->");
        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(12));
    }

    public void callBackCompleteUpdate() {
        LOG.mo8825d("callBackCompleteUpdate----->");
        this.mUiHandler.sendMessage(this.mUiHandler.obtainMessage(13));
    }

    /* access modifiers changed from: private */
    public void batchDeleteSelete(int index) {
        LOG.mo8825d("category---->" + this.category);
        if (this.mBatchDeleteInfo.containsKey(this.category)) {
            ((List) this.mBatchDeleteInfo.get(this.category)).add(Integer.valueOf(index));
        } else {
            List<Integer> deleteIndexList = new ArrayList<>();
            deleteIndexList.add(Integer.valueOf(index));
            this.mBatchDeleteInfo.put(this.category, deleteIndexList);
        }
        LOG.mo8825d("mBatchDeleteInfo----->" + this.mBatchDeleteInfo);
    }

    /* access modifiers changed from: private */
    public void batchDeleteUnsel(int index) {
        if (isBatContains(index)) {
            ((List) this.mBatchDeleteInfo.get(this.category)).remove(Integer.valueOf(index));
        }
        LOG.mo8825d("mBatchDeleteInfo----->" + this.mBatchDeleteInfo);
    }

    /* access modifiers changed from: private */
    public boolean isBatContains(int index) {
        LOG.mo8825d("category---->" + this.category);
        if (this.mBatchDeleteInfo == null || this.mBatchDeleteInfo.size() <= 0 || !this.mBatchDeleteInfo.containsKey(this.category)) {
            return false;
        }
        return ((List) this.mBatchDeleteInfo.get(this.category)).contains(Integer.valueOf(index));
    }

    /* access modifiers changed from: private */
    public void batchDelete() {
        LOG.mo8825d("mBatchDeleteInfo----->" + this.mBatchDeleteInfo);
        if (this.mBatchDeleteInfo != null && this.mBatchDeleteInfo.size() > 0) {
            for (Entry<String, List<Integer>> batchItem : this.mBatchDeleteInfo.entrySet()) {
                LOG.mo8825d("mBatchItem----key--->" + ((String) batchItem.getKey()));
                LOG.mo8825d("mBatchItem----value--->" + batchItem.getValue());
                List<Integer> batchList = (List) batchItem.getValue();
                List<DownloadTask> dataList = (List) mDownPageMap.get(batchItem.getKey());
                for (int i = 0; i < batchList.size(); i++) {
                    removeDownTask(dataList, ((Integer) batchList.get(i)).intValue());
                }
            }
            this.mDownUtils.checkLivetask();
            loadDownPageData();
            this.mBatchDeleteInfo.clear();
        }
    }

    private void removeDownTask(List<DownloadTask> downPageList, int downPos) {
        if (downPageList != null && downPageList.size() > 0) {
            String key = ((DownloadTask) downPageList.get(downPos)).getTaskName();
            String filePath = new StringBuilder(String.valueOf(((DownloadTask) downPageList.get(downPos)).getSavePath())).append(((DownloadTask) downPageList.get(downPos)).getLocalFile()).toString();
            LOG.mo8825d("---removeDownTask---key==---" + key);
            LOG.mo8825d("*---removeDownTask---filmPath==---" + filePath);
            this.mDownUtils.deleteFile(filePath);
            this.mDownUtils.removeTaskInfo(key);
        }
    }

    private void showBatDeleteDialog() {
        this.mCustomResDialog = new CustomResDialog(this.mContext, C0395R.style.PromptDialog);
        this.mCustomResDialog.setDialogBg(C0395R.C0396drawable.ts_bg);
        this.mCustomResDialog.setOkBgResource(C0395R.C0396drawable.btn_check_ok_sel);
        this.mCustomResDialog.setCancelBgResource(C0395R.C0396drawable.btn_check_cancel_sel);
        this.mCustomResDialog.setTextContent(getResources().getText(C0395R.string.delete_reservation).toString());
        this.mCustomResDialog.setOkClickListener(new OnClickListener() {
            public void onClick(View v) {
                PageDownLoad.this.hiddenDelteDialog();
                PageDownLoad.this.mIsAtBatchDelete = false;
                PageDownLoad.this.mWorkHandler.sendEmptyMessage(8);
            }
        });
        this.mCustomResDialog.setCancelClickListener(new OnClickListener() {
            public void onClick(View v) {
                PageDownLoad.this.hiddenDelteDialog();
            }
        });
        this.mCustomResDialog.show();
    }

    public void showLackSpaceDialog() {
        this.mCustomInfoDialog = new CustomInfoDialog(this.mContext, C0395R.style.PromptDialog);
        this.mCustomInfoDialog.setDialogBg(C0395R.C0396drawable.ts_bg);
        this.mCustomInfoDialog.setBtnCancelBgResource(C0395R.C0396drawable.cancel_cn);
        this.mCustomInfoDialog.setTextContent(getResources().getString(C0395R.string.usb_lack_sapce));
        this.mCustomInfoDialog.show();
    }

    public void setPageDownLoadCallBackListener(PageDownLoadCallBackListener backListener2) {
        this.backListener = backListener2;
    }
}
