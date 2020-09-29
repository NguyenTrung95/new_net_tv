package net.sunniwell.app.linktaro.launcher.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import net.sunniwell.aidl.SDKRemoteManager;
import net.sunniwell.aidl.SDKRemoteManager.SDKRemoteCallBack;
import net.sunniwell.aidl.bean.AdBean;
import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.SWApplication;
import net.sunniwell.app.linktaro.launcher.adapter.MyViewPagerAdapter;
import net.sunniwell.app.linktaro.launcher.constans.Constans;
import net.sunniwell.app.linktaro.launcher.constans.Constans.SWAction;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.launcher.fragment.TvFragment;
import net.sunniwell.app.linktaro.launcher.view.MarqueeTextView;
import net.sunniwell.app.linktaro.launcher.view.SystemBar;
import net.sunniwell.app.linktaro.launcher.widgets.CustomInfoDialog;
import net.sunniwell.app.linktaro.nettv.service.SWUpdateStbDataService;
import net.sunniwell.app.linktaro.tools.DateTimeUtil;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import net.sunniwell.app.linktaro.tools.PagerScroller;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.sz.p2pproxy.P2pProxyManager;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

public class HomeActivity extends FragmentActivity implements OnFocusChangeListener {
    private static final int CALL_FRAGMENT_SHOWAD = 10;
    private static final int CALL_REFRESH_MAIL = 9;
    private static final int CHANGE_NOMAL_MAIL = 14;
    private static final int CHANGE_RED_MAIL = 13;
    private static final int GET_ALL_AD_DATA = 12;
    private static final int GET_SUBSCRIBE = 13;
    private static final int GET_USENAME = 11;
    private static final int MSG_EXIT = 15;
    private final int MGS_ACCOUNT_EXPIRED = 4;
    private final int MSG_ICON_FOCUSED = 0;
    private final int MSG_ICON_UNFOCUSED = 1;
    private final int MSG_ITEM_FOCUSED = 2;
    private final int MSG_ITEM_UNFOCUSED = 3;
    protected final int MSG_NET_CONNECTED = 1;
    protected final int MSG_NET_DISCONNECTED = 0;
    protected final int MSG_SHOW_VIEWS = 2;
    private final String NETTV_LIVE = "3";
    private final String NETTV_RECORD = "6";
    private final String NETTV_VOD = "1";
    int checkedviewId = 0;
    private ArrayList<Fragment> fragments;
    private HandlerThread mAdDataHandlerThread;
    private List<AdBean> mAllAdList;
    /* access modifiers changed from: private */
    public CheckUserStatusHandler mCheckUserStatusHandler;
    private HandlerThread mCheckUserStatusThread;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public CustomInfoDialog mDialog;
    /* access modifiers changed from: private */
    public RadioButton mEmail_item;
    /* access modifiers changed from: private */
    public GetAdDataHandler mGetAdDataHandler;
    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (msg.obj == null) {
                        LogcatUtils.m321d("unknown tag");
                        return;
                    }
                    return;
                case 1:
                    if (msg.obj == null) {
                        LogcatUtils.m321d("unknown tag");
                        return;
                    }
                    return;
                case 2:
                    if (((View) msg.obj) == null) {
                        LogcatUtils.m321d("unknown tag");
                        return;
                    } else if (((View) msg.obj).getId() == R.id.service_item) {
                        ((View) msg.obj).setBackgroundResource(R.drawable.my_checked);
                        return;
                    } else if (((View) msg.obj).getId() == R.id.media_item) {
                        ((View) msg.obj).setBackgroundResource(R.drawable.media_checked);
                        return;
                    } else if (((View) msg.obj).getId() == R.id.email_item) {
                        ((View) msg.obj).setBackgroundResource(R.drawable.email_checked);
                        HomeActivity.this.cancelMailNotify();
                        return;
                    } else {
                        return;
                    }
                case 3:
                    if (msg.obj == null) {
                        LogcatUtils.m321d("unknown tag");
                        return;
                    } else if (((View) msg.obj).getId() == R.id.service_item) {
                        ((View) msg.obj).setBackgroundResource(R.drawable.my_unchecked);
                        return;
                    } else if (((View) msg.obj).getId() == R.id.media_item) {
                        ((View) msg.obj).setBackgroundResource(R.drawable.media_unchecked);
                        return;
                    } else if (((View) msg.obj).getId() == R.id.email_item) {
                        HomeActivity.this.MailNotify();
                        ((View) msg.obj).setBackgroundResource(R.drawable.email_unchecked);
                        return;
                    } else {
                        return;
                    }
                case 4:
                    if (HomeActivity.this.mDialog == null) {
                        HomeActivity.this.mDialog = new CustomInfoDialog(HomeActivity.this.mContext, R.style.PromptDialog, true, 4);
                        HomeActivity.this.mDialog.setTextContent(HomeActivity.this.getString(R.string.account_expired));
                        HomeActivity.this.mDialog.show();
                        return;
                    }
                    return;
                case 9:
                    HomeActivity.this.MailNotify();
                    return;
                case 10:
                    LogcatUtils.m321d("CALL_FRAGMENT_SHOWAD---->");
                    HomeActivity.this.mTvFragment.beginShowAd(HomeActivity.this.mTvAdList);
                    return;
                case 13:
                    HomeActivity.this.mEmail_item.setBackgroundResource(R.drawable.email_red);
                    HomeActivity.this.mHandler.sendEmptyMessageDelayed(14, 500);
                    return;
                case 14:
                    HomeActivity.this.mEmail_item.setBackgroundResource(R.drawable.email_icon);
                    HomeActivity.this.mHandler.sendEmptyMessageDelayed(13, 500);
                    return;
                default:
                    return;
            }
        }
    };
    /* access modifiers changed from: private */
    public boolean mLogined = false;
    /* access modifiers changed from: private */
    public MarqueeTextView mMarqueeTextView;
    /* access modifiers changed from: private */
    public P2pProxyManager mP2pProxyManager;
    /* access modifiers changed from: private */
    public RadioGroup mRadioGroup;
    private BroadcastReceiver mSDKCallBackBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogcatUtils.m321d("action/" + action);
            if (action.equals(Constans.LOGIN_SUCEESS)) {
                String user = intent.getStringExtra("user");
                String password = intent.getStringExtra("password");
                SWSysProp.setStringParam("user_name", user);
                SWSysProp.setStringParam("password", password);
                if (!HomeActivity.this.mLogined) {
                    LogcatUtils.m321d("user " + user + "/password " + password);
                    SystemProperties.get("sys.is.login", "true");
                    long oisUtc = intent.getLongExtra("oisUtc", 0);
                    StringBuilder sb = new StringBuilder("[Ois Time]");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
                    Date date = new Date(oisUtc);
                    LogcatUtils.m321d(sb.append(simpleDateFormat.format(date)).toString());
                    SystemClock.setCurrentTimeMillis(oisUtc);
                    HomeActivity.this.startService(new Intent(HomeActivity.this, SWUpdateStbDataService.class));
                    HomeActivity.this.mLogined = true;
                    HomeActivity.this.mCheckUserStatusHandler.sendMessage(HomeActivity.this.mCheckUserStatusHandler.obtainMessage(11));
                    HomeActivity.this.mCheckUserStatusHandler.sendMessage(HomeActivity.this.mCheckUserStatusHandler.obtainMessage(13));
                    HomeActivity.this.mGetAdDataHandler.sendMessage(HomeActivity.this.mGetAdDataHandler.obtainMessage(12));
                    HomeActivity.this.mP2pProxyManager = new P2pProxyManager(HomeActivity.this.mContext);
                    Thread r0 = new Thread() {
                        public void run() {
                            HomeActivity.this.mP2pProxyManager.initP2pProxy();
                        }
                    };
                    r0.start();
                }
            } else if (action.equals(Constans.LOGIN_FAILED)) {
                int errorCode = intent.getIntExtra("error_code", -1);
                LogcatUtils.m321d("error_code " + errorCode);
                if (errorCode == 404 || errorCode == 401 || errorCode == 405) {
                    LogcatUtils.m321d("[Invalid user]changed to default user");
                    SWSysProp.setStringParam("user_name", "sunniwell");
                    SWSysProp.setStringParam("password", "888888");
                }
                if (errorCode == 408) {
                    HomeActivity.this.mHandler.sendEmptyMessage(4);
                }
            } else if (action.equals(Constans.ASSIGN_USER)) {
                String user2 = intent.getStringExtra("user");
                String password2 = intent.getStringExtra("password");
                LogcatUtils.m321d("Assign user " + user2);
                SWSysProp.setStringParam("user_name", user2);
                SWSysProp.setStringParam("password", password2);
            } else if (action.equals(Constans.INCOMING_MESSAGE)) {
                String title = intent.getStringExtra("title");
                String content = intent.getStringExtra(MailDbHelper.CONTENT);
                if (!MailDbHelper.isOpen()) {
                    SWApplication.mMailHelper.openDb();
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("name", title);
                contentValues.put(MailDbHelper.DATE, DateTimeUtil.getCurDate());
                contentValues.put(MailDbHelper.DATE_DETAILS, DateTimeUtil.getCurDateDetails());
                contentValues.put(MailDbHelper.FLAG, "0");
                contentValues.put(MailDbHelper.SUB_NAME, "0");
                contentValues.put(MailDbHelper.TYPE, "0");
                contentValues.put(MailDbHelper.CONTENT, content);
                SWApplication.mMailHelper.insertDataByContentValue(contentValues);
                SWApplication.mMailHelper.close();
            } else if (!action.equals(Constans.UPGRADE_SOFTWARE) && action.equals(Constans.TEXT_MESSAGE)) {
                String text = intent.getStringExtra("text");
                LogcatUtils.m321d("showMarqueeTv message " + text);
                if (StringUtils.isNotEmpty(text)) {
                    int speed = intent.getIntExtra("speed", 3);
                    int loopTime = intent.getIntExtra("loop_time", 3);
                    String fontColor = intent.getStringExtra("font_color");
                    LogcatUtils.m321d("speed " + speed + "/loopTime " + loopTime + "/fontColor " + fontColor);
                    HomeActivity.this.showMarqueeTv(speed, loopTime, text, fontColor);
                }
            }
        }
    };
    private SDKRemoteCallBack mSDKRemoteCallBack = new SDKRemoteCallBack() {
        public void onServiceDisconnected(ComponentName name) {
            LogcatUtils.m321d("mop service disconnected");
        }

        public void onServiceConnected(ComponentName name, IBinder binder) {
            LogcatUtils.m321d("mop service connected");
        }
    };
    private SDKRemoteManager mSdkRemoteManager;
    private SystemBar mSystemBar;
    /* access modifiers changed from: private */
    public List<AdBean> mTvAdList;
    /* access modifiers changed from: private */
    public TvFragment mTvFragment;
    private ViewPager mViewPager;
    private MyViewPagerAdapter mViewPagerAdapter;
    private int[] tagBtns;

    public class CheckUserStatusHandler extends Handler {
        public CheckUserStatusHandler(Looper mLooper) {
            super(mLooper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 11:
                    HomeActivity.this.getUserName();
                    break;
                case 13:
                    HomeActivity.this.getUserSubscribe();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public class GetAdDataHandler extends Handler {
        public GetAdDataHandler(Looper mLooper) {
            super(mLooper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 12:
                    HomeActivity.this.downAllAdData();
                    break;
            }
            super.handleMessage(msg);
        }
    }

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    private void regist() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constans.LOGIN_SUCEESS);
        filter.addAction(Constans.INCOMING_MESSAGE);
        filter.addAction(Constans.ASSIGN_USER);
        filter.addAction(Constans.UPGRADE_SOFTWARE);
        filter.addAction(Constans.LOGIN_FAILED);
        filter.addAction(Constans.TEXT_MESSAGE);
        registerReceiver(this.mSDKCallBackBroadcastReceiver, filter);
    }

    private void unregist() {
        unregisterReceiver(this.mSDKCallBackBroadcastReceiver);
    }

    /* access modifiers changed from: private */
    public void showMarqueeTv(int speed, int loopTime, String text, String fontColor) {
        int interval;
        if (this.mMarqueeTextView == null) {
            this.mMarqueeTextView = (MarqueeTextView) this.mSystemBar.findViewById(R.id.marquee);
        }
        if (this.mMarqueeTextView != null) {
            LayoutParams layoutParams = (LayoutParams) this.mMarqueeTextView.getLayoutParams();
            this.mMarqueeTextView.pauseScroll();
            this.mMarqueeTextView.setLayoutParams(layoutParams);
            this.mMarqueeTextView.setText(text);
            this.mMarqueeTextView.getContentWidth();
            if (speed < 10 && speed > 0) {
                interval = 100 - (speed * 10);
            } else if (speed == 10) {
                interval = 1;
            } else {
                interval = 50;
            }
            this.mMarqueeTextView.setVisibility(0);
            this.mMarqueeTextView.setmMoveSpeed(interval);
            this.mMarqueeTextView.setTextSize(35.0f);
            this.mMarqueeTextView.setmAllLoopTimes(loopTime);
            String colorString = fontColor;
            if (TextUtils.isEmpty(colorString)) {
                colorString = "#FFFFFF";
            }
            this.mMarqueeTextView.setTextColor(Color.parseColor(colorString));
            this.mMarqueeTextView.setmDelayedTime(1);
            this.mMarqueeTextView.setmCurLoopTimes(0);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    HomeActivity.this.mMarqueeTextView.startScroll();
                }
            }, 100);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LogcatUtils.m321d("Create");
        this.mContext = this;
        SWSysProp.init(this);
        regist();
        initView();
        initData();
        this.mSdkRemoteManager = SDKRemoteManager.getInstance(this, this.mSDKRemoteCallBack);
    }

    private void initView() {
        this.mSystemBar = (SystemBar) findViewById(R.id.system_bar);
        this.mRadioGroup = (RadioGroup) findViewById(R.id.radiogroup_tag);
        this.mEmail_item = (RadioButton) findViewById(R.id.email_item);
        this.tagBtns = new int[this.mRadioGroup.getChildCount()];
        for (int i = 0; i < this.tagBtns.length; i++) {
            this.tagBtns[i] = this.mRadioGroup.getChildAt(i).getId();
        }
        this.mViewPager = (ViewPager) findViewById(R.id.ll_homepage);
        setViewPagerScrollDuration(this.mViewPager, 800);
        this.mViewPager.setOffscreenPageLimit(this.mRadioGroup.getChildCount());
        this.fragments = new ArrayList<>();
    }

    private void initData() {
        this.mAllAdList = new ArrayList();
        this.mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager(), this.fragments);
        this.mTvFragment = new TvFragment();
        this.fragments.add(this.mTvFragment);
        this.mViewPager.setAdapter(this.mViewPagerAdapter);
        this.mViewPager.setVisibility(0);
        this.mCheckUserStatusThread = new HandlerThread("CheckUserStatusThread");
        this.mCheckUserStatusThread.start();
        this.mCheckUserStatusHandler = new CheckUserStatusHandler(this.mCheckUserStatusThread.getLooper());
        this.mAdDataHandlerThread = new HandlerThread("GetAdDataThread");
        this.mAdDataHandlerThread.start();
        this.mGetAdDataHandler = new GetAdDataHandler(this.mAdDataHandlerThread.getLooper());
    }

    private void initListener() {
        for (int i = 0; i < this.mRadioGroup.getChildCount(); i++) {
            this.mRadioGroup.getChildAt(i).setOnFocusChangeListener(this);
            this.mRadioGroup.getChildAt(i).setTag(Integer.valueOf(i));
        }
        this.mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                for (int i = 0; i < HomeActivity.this.mRadioGroup.getChildCount(); i++) {
                    View v = HomeActivity.this.mRadioGroup.getChildAt(position);
                    if (i == position) {
                        v.setSelected(true);
                        v.requestFocus();
                    } else {
                        v.setSelected(false);
                    }
                }
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
        this.mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("TAGG", group.getChildCount() + "----" + checkedId);
                HomeActivity.this.checkedviewId = checkedId;
                RadioButton viewGroup = (RadioButton) HomeActivity.this.findViewById(checkedId);
                switch (checkedId) {
                    case R.id.media_item /*2131361809*/:
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, ConfigActivity.class));
                        viewGroup.setChecked(false);
                        return;
                    case R.id.email_item /*2131361811*/:
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, Email.class));
                        viewGroup.setChecked(false);
                        return;
                    case R.id.service_item /*2131361813*/:
                        HomeActivity.this.startActivity(new Intent(HomeActivity.this, InfoActivity.class));
                        viewGroup.setChecked(false);
                        return;
                    default:
                        Toast.makeText(HomeActivity.this, "secret", 0).show();
                        return;
                }
            }
        });
    }

    private void setViewPagerScrollDuration(ViewPager viewPager, int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            PagerScroller scroller = new PagerScroller(this, new DecelerateInterpolator());
            scroller.setmDuration(duration);
            field.set(viewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        LogcatUtils.m321d("onStart");
        super.onStart();
        initListener();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        LogcatUtils.m321d("onStop");
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        LogcatUtils.m321d("onResume");
        super.onResume();
        this.mHandler.sendEmptyMessage(10);
        this.mHandler.sendEmptyMessage(9);
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LogcatUtils.m321d("onDestroy");
        unregist();
        if (this.mP2pProxyManager != null) {
            this.mP2pProxyManager.exitP2pProxy();
        }
        this.mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void MailNotify() {
        int unreadMail = getMailCount();
        LogcatUtils.m321d("unreadMail " + unreadMail);
        if (unreadMail != 0) {
            sendMailNotify();
        } else {
            cancelMailNotify();
        }
        if (this.mHandler.hasMessages(9)) {
            this.mHandler.removeMessages(9);
        }
        this.mHandler.sendEmptyMessageDelayed(9, 10000);
    }

    private int getMailCount() {
        int readount = 0;
        if (!MailDbHelper.isOpen()) {
            SWApplication.mMailHelper.openDb();
        }
        Cursor allDataCursor = SWApplication.mMailHelper.queryData();
        while (allDataCursor.moveToNext()) {
            if ("0".equals(allDataCursor.getString(allDataCursor.getColumnIndex(MailDbHelper.FLAG)))) {
                readount++;
            }
        }
        SWApplication.mMailHelper.close();
        LogcatUtils.m321d("getMailCount " + readount);
        return readount;
    }

    private void sendMailNotify() {
        LogcatUtils.m321d("sendMailNotify");
        this.mHandler.sendEmptyMessage(13);
    }

    /* access modifiers changed from: private */
    public void cancelMailNotify() {
        LogcatUtils.m321d("cancelMailNotify");
        if (this.mHandler.hasMessages(13)) {
            this.mHandler.removeMessages(13);
        }
        if (this.mHandler.hasMessages(14)) {
            this.mHandler.removeMessages(14);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        super.onPause();
        LogcatUtils.m321d("onPause");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (this.mHandler.hasMessages(15)) {
                    return super.onKeyDown(keyCode, event);
                }
                Toast.makeText(this.mContext, "また一回押してAPPから退出します", 0).show();
                this.mHandler.sendEmptyMessageDelayed(15, 2000);
                return true;
            case 183:
                startNetTV("1");
                return true;
            case 184:
                startNetTV("3");
                return true;
            case 186:
                startNetTV("6");
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus && v.getTag() != null) {
            this.mViewPager.setCurrentItem(((Integer) v.getTag()).intValue());
        }
        Message msg = Message.obtain();
        msg.obj = v;
        msg.what = hasFocus ? 2 : 3;
        this.mHandler.sendMessage(msg);
    }

    /* access modifiers changed from: private */
    public void getUserName() {
        if (this.mSdkRemoteManager != null) {
            String user = SWSysProp.getStringParam("user_name", XmlPullParser.NO_NAMESPACE);
            String mUserName = this.mSdkRemoteManager.getUserName(user);
            String validtime = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date(this.mSdkRemoteManager.getValidtoUtc(user)));
            LogcatUtils.m321d("mUserName " + mUserName + "/validtime " + validtime);
            SWSysProp.setStringParam("account_name", mUserName);
            SWSysProp.setStringParam("account_time", validtime);
        }
    }

    /* access modifiers changed from: private */
    public void getUserSubscribe() {
        try {
            if (this.mSdkRemoteManager != null) {
                Properties prop = new Properties();
                InputStream is = getAssets().open("subscribe.properties");
                if (is != null) {
                    prop.load(is);
                    String sid = this.mSdkRemoteManager.getUserSubscribe(SWSysProp.getStringParam("user_name", XmlPullParser.NO_NAMESPACE));
                    LogcatUtils.m321d("subscribe id " + sid);
                    LogcatUtils.m321d("subscribe name " + prop.getProperty(sid));
                    SWSysProp.setStringParam("account_subscribe", prop.getProperty(sid));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void downAllAdData() {
        if (this.mSdkRemoteManager != null) {
            this.mAllAdList = this.mSdkRemoteManager.getAdData(0, "launcher");
            this.mTvAdList = new ArrayList();
            if (this.mAllAdList != null && this.mAllAdList.size() > 0) {
                for (AdBean ad : this.mAllAdList) {
                    if (ad != null && ad.getExtend().substring(0, 1).equals("1")) {
                        this.mTvAdList.add(ad);
                    }
                }
                Collections.sort(this.mTvAdList, new Comparator<AdBean>() {
                    public int compare(AdBean lhsAd, AdBean rhsAd) {
                        if (!StringUtils.isNumberStr(lhsAd.getExtend()) || !StringUtils.isNumberStr(rhsAd.getExtend())) {
                            return 0;
                        }
                        return Integer.valueOf(lhsAd.getExtend()).intValue() - Integer.valueOf(rhsAd.getExtend()).intValue();
                    }
                });
                this.mHandler.sendEmptyMessage(10);
            }
        }
    }

    private void startNetTV(String type) {
        Intent intent = new Intent();
        intent.setAction(SWAction.NETTV);
        intent.putExtra(MailDbHelper.FLAG, type);
        startActivity(intent);
    }
}
