package net.sunniwell.app.linktaro.nettv.entry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sunniwell.app.linktaro.SWApplication;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.app.linktaro.nettv.Constants.MainAppConstants;
import net.sunniwell.app.linktaro.nettv.bean.PagePlayVideoBean;
import net.sunniwell.app.linktaro.nettv.content.PageCategory;
import net.sunniwell.app.linktaro.nettv.content.PageCategory.PageCategoryCallBackListener;
import net.sunniwell.app.linktaro.nettv.content.PageDownLoad;
import net.sunniwell.app.linktaro.nettv.content.PageDownLoad.PageDownLoadCallBackListener;
import net.sunniwell.app.linktaro.nettv.content.PageEPG;
import net.sunniwell.app.linktaro.nettv.content.PageEPG.PageEPGCallBackListener;
import net.sunniwell.app.linktaro.nettv.content.PageLive;
import net.sunniwell.app.linktaro.nettv.content.PageLive.PageLiveCallBackListener;
import net.sunniwell.app.linktaro.nettv.content.PagePlayVideo;
import net.sunniwell.app.linktaro.nettv.content.PagePlayVideo.PagePlayCallBackListener;
import net.sunniwell.app.linktaro.nettv.download.DownUtils;
import net.sunniwell.app.linktaro.nettv.db.VodRecordDbHelper;
import net.sunniwell.app.linktaro.nettv.processor.imp.DBProcessor;
import net.sunniwell.app.linktaro.nettv.view.CustomInfoDialog;
import net.sunniwell.app.linktaro.nettv.view.CustomResDialog;
import net.sunniwell.app.linktaro.tools.LogcatUtils;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import org.xmlpull.p019v1.XmlPullParser;

public class NettvActivity extends Activity implements PageDownLoadCallBackListener, PageLiveCallBackListener, PagePlayCallBackListener, PageEPGCallBackListener, PageCategoryCallBackListener {
    private static final List<Integer> HISTORY = new ArrayList();
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(NettvActivity.class);
    private static final int SHOW_INFO_DIALOG = 1;
    private static final int SHOW_INFO_DIALOG_TRANS = 2;
    private static final int SHOW_RES_DIALOG = 3;
    private static final int SHOW_RES_DIALOG_TRANS = 4;
    public static VodRecordDbHelper vodDBHelper;
    @SuppressLint({"HandlerLeak"})
    private Handler UiHandler = new Handler() {
        public void handleMessage(Message msg) {
            NettvActivity.LOG.mo8825d("...msg.waht===" + msg.what);
            switch (msg.what) {
                case MainAppConstants.SHOW_PAGE_EPG /*1006*/:
                    NettvActivity.this.showEpgPage();
                    break;
                case MainAppConstants.SHOW_PAGE_LIVE /*1008*/:
                    NettvActivity.this.showLivePage();
                    break;
                case MainAppConstants.SHOW_PAGE_CATEGORY /*1009*/:
                    NettvActivity.this.showCategoryPage();
                    break;
                case MainAppConstants.SHOW_PAGE_DOWN /*1010*/:
                    NettvActivity.this.showDownPage();
                    break;
                case MainAppConstants.SHOW_PAGE_PLAY /*1011*/:
                    NettvActivity.this.showPlayPage();
                    break;
                case MainAppConstants.HIDE_PAGE_EPG /*1013*/:
                    NettvActivity.this.hideVodEpgPage();
                    break;
            }
            super.handleMessage(msg);
        }
    };
    private int cateType;
    private int channelInx;
    private boolean isAtCategoryPage;
    private boolean isAtCategoryVodPage;
    public boolean isAtDownPage;
    private boolean isAtEpgPage;
    private boolean isAtLivePage;
    private boolean isAtPlayPage;
    private boolean isAtVodPage;
    private PageCategory mCategoryPage;
    private FrameLayout mContentLayout;
    /* access modifiers changed from: private */
    public CustomInfoDialog mCustomInfoDialog;
    /* access modifiers changed from: private */
    public CustomResDialog mCustomResDialog;
    private DBProcessor mDBProcessor;
    private PageDownLoad mDownLoadPage;
    private PageEPG mEPGPage;
    private PageLive mLivePage;
    private PagePlayVideo mPlayPage;
    private PagePlayVideoBean mPlayVideoBean;
    public int mTvFormt;
    private Handler mUiHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    NettvActivity.this.mCustomInfoDialog = new CustomInfoDialog(NettvActivity.this, C0395R.style.PromptDialog);
                    NettvActivity.this.mCustomInfoDialog.setDialogBg(C0395R.C0396drawable.ts_bg);
                    NettvActivity.this.mCustomInfoDialog.setBtnCancelBgResource(C0395R.C0396drawable.cancel_focus_img);
                    NettvActivity.this.mCustomInfoDialog.setTextContent((String) msg.obj);
                    NettvActivity.this.mCustomInfoDialog.show();
                    return;
                case 2:
                    NettvActivity.this.mCustomInfoDialog = new CustomInfoDialog(NettvActivity.this, C0395R.style.PromptDialog);
                    NettvActivity.this.mCustomInfoDialog.setDialogBg(C0395R.C0396drawable.ts_bg_trans);
                    NettvActivity.this.mCustomInfoDialog.setBtnCancelBgResource(C0395R.C0396drawable.cancel_trans);
                    NettvActivity.this.mCustomInfoDialog.setTextContent((String) msg.obj);
                    NettvActivity.this.mCustomInfoDialog.show();
                    return;
                case 3:
                    NettvActivity.this.mCustomResDialog = new CustomResDialog(NettvActivity.this, C0395R.style.PromptDialog);
                    NettvActivity.this.mCustomResDialog.setDialogBg(C0395R.C0396drawable.ts_bg);
                    NettvActivity.this.mCustomResDialog.setOkBgResource(C0395R.C0396drawable.btn_check_ok_sel);
                    NettvActivity.this.mCustomResDialog.setCancelBgResource(C0395R.C0396drawable.btn_check_cancel_sel);
                    NettvActivity.this.mCustomResDialog.setTextContent((String) msg.obj);
                    NettvActivity.this.mCustomResDialog.setOkClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            NettvActivity.this.mCustomResDialog.dismiss();
                        }
                    });
                    NettvActivity.this.mCustomResDialog.setCancelClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            NettvActivity.this.mCustomResDialog.dismiss();
                        }
                    });
                    NettvActivity.this.mCustomResDialog.show();
                    return;
                case 4:
                    NettvActivity.this.mCustomResDialog = new CustomResDialog(NettvActivity.this, C0395R.style.PromptDialog);
                    NettvActivity.this.mCustomResDialog.setDialogBg(C0395R.C0396drawable.ts_bg_trans);
                    NettvActivity.this.mCustomResDialog.setOkBgResource(C0395R.C0396drawable.btn_check_ok_sel);
                    NettvActivity.this.mCustomResDialog.setCancelBgResource(C0395R.C0396drawable.btn_check_cancel_sel);
                    NettvActivity.this.mCustomResDialog.setTextContent((String) msg.obj);
                    NettvActivity.this.mCustomResDialog.setOkClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            NettvActivity.this.mCustomResDialog.dismiss();
                        }
                    });
                    NettvActivity.this.mCustomResDialog.setCancelClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            NettvActivity.this.mCustomResDialog.dismiss();
                        }
                    });
                    NettvActivity.this.mCustomResDialog.show();
                    return;
                default:
                    return;
            }
        }
    };

    public int getmTvFormt() {
        return this.mTvFormt;
    }

    public void setmTvFormt(int mTvFormt2) {
        this.mDBProcessor.setProp("TvFormt", mTvFormt2);
        this.mTvFormt = mTvFormt2;
    }

    public int getCateType() {
        return this.cateType;
    }

    public void setCateType(int cateType2) {
        this.cateType = cateType2;
    }

    public int getChannelInx() {
        if (this.mLivePage != null) {
            return this.mLivePage.getChannelInx();
        }
        return this.channelInx;
    }

    public void setChannelInx(int channelInx2) {
        if (this.mLivePage != null) {
            this.mLivePage.setChannelInx(channelInx2);
        }
    }

    public boolean isPlaying() {
        if (this.mPlayPage != null) {
            return this.mPlayPage.isPlaying();
        }
        return false;
    }

    public void setPlaying(boolean isPlaying) {
        if (this.mPlayPage != null) {
            this.mPlayPage.setPlaying(isPlaying);
        }
    }

    public boolean isFullPlay() {
        if (this.mPlayPage != null) {
            return this.mPlayPage.isFullPlay();
        }
        return false;
    }

    public void setFullPlay(boolean isFullPlay) {
        if (this.mPlayPage != null) {
            this.mPlayPage.setFullPlay(isFullPlay);
        }
    }

    public int getLiveChannelNum() {
        if (this.mLivePage != null) {
            return this.mLivePage.getLiveChannelNum();
        }
        return 0;
    }

    public void setLiveChannelNum(int liveChannelNum) {
        if (this.mLivePage != null) {
            this.mLivePage.setLiveChannelNum(liveChannelNum);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LOG.mo8825d("...onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(C0395R.layout.p2p);
        this.mDBProcessor = DBProcessor.getDBProcessor(this);
        this.mPlayVideoBean = new PagePlayVideoBean();
        initPage();
        vodDBHelper = new VodRecordDbHelper(this, null, null, 1);
        new Thread() {
            public void run() {
                if (!VodRecordDbHelper.isOpen()) {
                    NettvActivity.vodDBHelper.openDb();
                }
                NettvActivity.vodDBHelper.deleteData("program_date<" + (System.currentTimeMillis() - 604800000), null);
                NettvActivity.vodDBHelper.close();
            }
        }.start();
        initCateType(getIntent());
    }

    /* access modifiers changed from: protected */
    public void onRestart() {
        LOG.mo8825d("...onRestart...");
        super.onRestart();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        LOG.mo8825d("...onStart.....");
        setPlaying(false);
        initStbPage();
        super.onStart();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        LOG.mo8825d("...onResume...");
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LOG.mo8825d("...onPause...");
        setmTvFormt(3);
        LOG.mo8825d("------getmTvFormt=" + this.mTvFormt);
        Secure.putInt(getContentResolver(), "pageInx", 2);
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        LOG.mo8825d("...onStop...");
        super.onStop();
        LOG.mo8825d("***zhaohangqi******isPlaying===***" + isPlaying());
        if (isPlaying()) {
            this.mPlayPage.doByMessage(null, 1);
        }
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LOG.mo8825d("...onDestroy...");
        if (HISTORY != null) {
            HISTORY.removeAll(HISTORY);
        }
        this.mPlayPage.onDestroy();
        if (!(this.mDownLoadPage == null || this.mDownLoadPage.getmDownUtils() == null)) {
            this.mDownLoadPage.getmDownUtils().pauseDownTask();
        }
        super.onDestroy();
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        LOG.mo8825d("flag----->" + intent.getStringExtra(MailDbHelper.FLAG));
        initCateType(intent);
        super.onNewIntent(intent);
    }

    private void initPage() {
        LOG.mo8825d("...initPage...");
        loadLivePage();
        loadPlayPage();
    }

    private void initCateType(Intent intent) {
        if (intent != null && intent.getExtras() != null) {
            String flag = intent.getExtras().getString(MailDbHelper.FLAG);
            int intExtra = 0;
            if (StringUtils.isNumberStr(flag)) {
                intExtra = Integer.parseInt(flag);
            }
            LOG.mo8825d("intExtra----->" + intExtra);
            if (intExtra != 0) {
                switch (intExtra) {
                    case 1:
                        LOG.mo8825d("vodvodod");
                        this.mDBProcessor.setProp("isFromLauncher", "1");
                        this.mDBProcessor.setProp("cateType", 2);
                        return;
                    case 2:
                        LOG.mo8825d("epgepg");
                        this.mDBProcessor.setProp("isFromLauncher", "1");
                        this.mDBProcessor.setProp("cateType", 1);
                        return;
                    case 3:
                        LOG.mo8825d("dsdsdsd");
                        this.mDBProcessor.setProp("cateType", 0);
                        this.mDBProcessor.setProp("TvFormt", 3);
                        return;
                    case 4:
                        LOG.mo8825d("bsbsbs");
                        this.mDBProcessor.setProp("cateType", 0);
                        this.mDBProcessor.setProp("TvFormt", 1);
                        return;
                    case 5:
                        LOG.mo8825d("cscscscs");
                        this.mDBProcessor.setProp("cateType", 0);
                        this.mDBProcessor.setProp("TvFormt", 2);
                        return;
                    case 6:
                        LOG.mo8825d("downpage");
                        this.mDBProcessor.setProp("isFromLauncher", "1");
                        this.mDBProcessor.setProp("cateType", 3);
                        return;
                    default:
                        return;
                }
            }
        }
    }

    private void initStbPage() {
        LOG.mo8825d("******initStbPage*****");
        initData();
        initLayout();
        this.cateType = getTvCateType();
        checkPage();
    }

    private void initData() {
        this.mDBProcessor.setProp("stbPageInx", "1");
        this.mTvFormt = getTvFormt();
    }

    private void initLayout() {
        this.mContentLayout = (FrameLayout) findViewById(C0395R.C0397id.contentLayout);
    }

    private void checkPage() {
        LOG.mo8825d("**cateType*****" + this.cateType);
        for (int i = 0; i < HISTORY.size(); i++) {
            if (((Integer) HISTORY.get(i)).intValue() == this.cateType) {
                HISTORY.remove(i);
            }
        }
        LOG.mo8825d("HISTORY---->" + HISTORY);
        if (this.cateType == 1 || this.cateType == 2) {
            LOG.mo8825d("goto epg/vod");
            if (HISTORY.contains(Integer.valueOf(1))) {
                LOG.mo8825d("has epg, remove");
                if (HISTORY.size() > 0) {
                    HISTORY.remove(Integer.valueOf(1));
                }
            } else if (HISTORY.contains(Integer.valueOf(2))) {
                LOG.mo8825d("has vod, remove");
                if (HISTORY.size() > 0) {
                    HISTORY.remove(Integer.valueOf(2));
                }
            }
        }
        if (this.cateType == 5 || this.cateType == 6) {
            LOG.mo8825d("goto category/vod");
            if (HISTORY.contains(Integer.valueOf(5))) {
                LOG.mo8825d("has category, remove");
                if (HISTORY.size() > 0) {
                    HISTORY.remove(Integer.valueOf(5));
                }
            } else if (HISTORY.contains(Integer.valueOf(6))) {
                LOG.mo8825d("has category_vod, remove");
                if (HISTORY.size() > 0) {
                    HISTORY.remove(Integer.valueOf(6));
                }
            }
        }
        HISTORY.add(Integer.valueOf(this.cateType));
        LOG.mo8825d("HISTORY---->" + HISTORY);
        initPageview();
    }

    public int getBackPageIndex() {
        int inx = HISTORY.size() - 2;
        if (inx >= 0) {
            return ((Integer) HISTORY.get(inx)).intValue();
        }
        return 0;
    }

    public void setBackPageIndex(int catey) {
        int inx = HISTORY.size() - 1;
        if (inx >= 0) {
            HISTORY.remove(inx);
            HISTORY.add(Integer.valueOf(catey));
        }
    }

    private void backPage() {
        if (this.mDBProcessor != null) {
            this.mDBProcessor.setProp("cateType", new StringBuilder(String.valueOf(this.cateType)).toString());
        }
        initPageview();
    }

    private void initPageview() {
        LOG.mo8825d("*********initPageview**********");
        switch (this.cateType) {
            case 0:
                loadLivePage();
                return;
            case 1:
                loadEpgPage();
                return;
            case 2:
                loadVodPage();
                return;
            case 3:
                loadDownPage();
                return;
            case 4:
                loadPlayPage();
                return;
            case 5:
                LOG.mo8825d("initPageview() + PAGE_CATEGORY");
                loadCategoryPage();
                return;
            case 6:
                LOG.mo8825d("initPageview() + PAGE_CATEGORY_VOD");
                loadCategoryVodPage();
                return;
            default:
                return;
        }
    }

    private void loadCategoryPage() {
        LOG.mo8825d("loadCategoryPage");
        this.isAtLivePage = false;
        this.isAtVodPage = false;
        this.isAtEpgPage = false;
        this.isAtDownPage = false;
        this.isAtPlayPage = false;
        this.isAtCategoryPage = true;
        this.isAtCategoryVodPage = false;
        this.UiHandler.sendEmptyMessage(MainAppConstants.SHOW_PAGE_CATEGORY);
    }

    private void loadCategoryVodPage() {
        this.isAtLivePage = false;
        this.isAtVodPage = false;
        this.isAtEpgPage = false;
        this.isAtDownPage = false;
        this.isAtPlayPage = false;
        this.isAtCategoryPage = false;
        this.isAtCategoryVodPage = true;
        this.UiHandler.sendEmptyMessage(MainAppConstants.SHOW_PAGE_CATEGORY);
    }

    private void loadLivePage() {
        LOG.mo8825d("...loadLivePage...");
        this.isAtLivePage = true;
        this.isAtVodPage = false;
        this.isAtEpgPage = false;
        this.isAtDownPage = false;
        this.isAtPlayPage = false;
        this.isAtCategoryPage = false;
        this.isAtCategoryVodPage = false;
        this.UiHandler.sendEmptyMessage(MainAppConstants.SHOW_PAGE_LIVE);
    }

    private void loadVodPage() {
        this.isAtLivePage = false;
        this.isAtVodPage = true;
        this.isAtEpgPage = false;
        this.isAtDownPage = false;
        this.isAtPlayPage = false;
        this.isAtCategoryPage = false;
        this.isAtCategoryVodPage = false;
        this.UiHandler.sendEmptyMessage(MainAppConstants.SHOW_PAGE_EPG);
    }

    private void loadEpgPage() {
        LOG.mo8825d("********loadEpgPage*******");
        this.isAtLivePage = false;
        this.isAtVodPage = false;
        this.isAtEpgPage = true;
        this.isAtDownPage = false;
        this.isAtPlayPage = false;
        this.isAtCategoryPage = false;
        this.isAtCategoryVodPage = false;
        this.UiHandler.sendEmptyMessage(MainAppConstants.SHOW_PAGE_EPG);
    }

    private void loadDownPage() {
        LOG.mo8825d("...loadDownPage...");
        this.isAtLivePage = false;
        this.isAtVodPage = false;
        this.isAtEpgPage = false;
        this.isAtDownPage = true;
        this.isAtPlayPage = false;
        this.isAtCategoryPage = false;
        this.isAtCategoryVodPage = false;
        this.UiHandler.sendEmptyMessage(MainAppConstants.SHOW_PAGE_DOWN);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        LogcatUtils.m321d("keycode is " + event.getKeyCode());
        if (event.getAction() == 0 && (this.isAtVodPage || this.isAtEpgPage)) {
            LOG.mo8825d("isAtEpgPage");
            this.mEPGPage.onKeyDown(event.getKeyCode(), event);
        }
        return super.dispatchKeyEvent(event);
    }

    private void loadPlayPage() {
        LOG.mo8825d("...loadPlayPage...");
        this.isAtLivePage = false;
        this.isAtVodPage = false;
        this.isAtEpgPage = false;
        this.isAtDownPage = false;
        this.isAtPlayPage = true;
        this.isAtCategoryPage = false;
        this.isAtCategoryVodPage = false;
        this.UiHandler.sendEmptyMessage(MainAppConstants.SHOW_PAGE_PLAY);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LOG.mo8825d("..onKeyDown...keycode..." + keyCode);
        if (this.isAtLivePage) {
            LOG.mo8825d("isAtLivePage");
            if (186 == keyCode) {
                SWApplication.INSTANCE.setHotKey(true);
                loadDownPage();
            } else if (183 == keyCode) {
                SWApplication.INSTANCE.setHotKey(true);
                this.mDBProcessor.setProp("isFromLauncher", "1");
                this.mDBProcessor.setProp("cateType", 2);
                setCateType(2);
                loadVodPage();
            } else {
                this.mLivePage.onKeyDown(keyCode, event);
            }
        } else if (this.isAtVodPage || this.isAtEpgPage) {
            LOG.mo8825d("isAtEpgPage");
            this.mEPGPage.onKeyDown(keyCode, event);
        } else if (this.isAtDownPage) {
            LOG.mo8825d("isAtDownPage");
            this.mDownLoadPage.onKeyDown(keyCode, event);
        } else if (this.isAtPlayPage) {
            LOG.mo8825d("isAtPlayPage");
            if (184 == keyCode) {
                loadLivePage();
            } else if (186 == keyCode) {
                loadDownPage();
            } else if (183 == keyCode) {
                this.mDBProcessor.setProp("isFromLauncher", "1");
                this.mDBProcessor.setProp("cateType", 2);
                setCateType(2);
                loadVodPage();
            } else {
                this.mPlayPage.onKeyDown(keyCode, event);
            }
        } else if (this.isAtCategoryPage || this.isAtCategoryVodPage) {
            LOG.mo8825d("isAtCategoryPage");
            this.mCategoryPage.onKeyDown(keyCode, event);
        }
        return false;
    }

    public void stopMediaPlay() {
        if (this.mPlayPage != null) {
            this.mPlayPage.doByMessage(null, 1);
        }
    }

    public void exitNettv() {
        finish();
    }

    public void showDownIcon() {
        LOG.mo8825d("[showDownIcon]");
        try {
            if (this.isAtEpgPage && this.mEPGPage != null) {
                this.mEPGPage.doByMessage(null, 0);
            }
        } catch (Exception e) {
            LOG.mo8825d("...Exception...===" + e.toString());
        }
    }

    private int getTvFormt() {
        String str = this.mDBProcessor.getProp("TvFormt");
        if (StringUtils.isNotEmpty(str)) {
            return Integer.parseInt(str, 10);
        }
        return 0;
    }

    private int getTvCateType() {
        String str = this.mDBProcessor.getProp("cateType");
        if (StringUtils.isNotEmpty(str)) {
            return Integer.parseInt(str, 10);
        }
        return 0;
    }

    public void callBackUpdateDownPage() {
        this.cateType = 3;
        checkPage();
    }

    public String getServerDomain() {
        String serverDomain = "203.85.72.180";
        if (!this.mDBProcessor.getProp("serverAddress").equals(XmlPullParser.NO_NAMESPACE)) {
            serverDomain = this.mDBProcessor.getProp("serverAddress");
        } else {
            this.mDBProcessor.setProp("serverAddress", "203.85.72.180");
        }
        LOG.mo8825d("serverDomain====" + serverDomain);
        return serverDomain;
    }

    /* access modifiers changed from: private */
    public void showDownPage() {
        LOG.mo8825d("...showDownPage...");
        if (this.mDownLoadPage == null) {
            this.mDownLoadPage = new PageDownLoad(this, this);
            this.mContentLayout.addView(this.mDownLoadPage.getmContentView());
        }
        Secure.putInt(getContentResolver(), "pageInx", 2);
        Intent intent = new Intent("net.sunniwell.MarqueeTextView_PAGE_CHANGED");
        intent.putExtra("pageInx", 2);
        sendBroadcast(intent);
        if (this.mEPGPage != null) {
            LOG.mo8825d("mEPGPage---->INVISIBLE");
            this.mEPGPage.setVisibility(4);
        }
        if (this.mCategoryPage != null) {
            LOG.mo8825d("mCategoryPage---->INVISIBLE");
            this.mCategoryPage.setVisibility(4);
        }
        if (this.mLivePage != null) {
            LOG.mo8825d("mLivePage---->INVISIBLE");
            this.mLivePage.setVisibility(4);
        }
        this.mDownLoadPage.bringToFront();
        this.mDownLoadPage.onResume();
    }

    public PageDownLoad getPageDownLoad() {
        return this.mDownLoadPage;
    }

    /* access modifiers changed from: private */
    public void showLivePage() {
        LOG.mo8825d("...showLivePage...");
        if (this.mLivePage == null) {
            LOG.mo8825d("...MainApp -------->>>>>mLivePage == null...");
            this.mLivePage = new PageLive(this, this);
            this.mContentLayout.addView(this.mLivePage.getmContentView());
        }
        Secure.putInt(getContentResolver(), "pageInx", 2);
        Intent intent = new Intent("net.sunniwell.MarqueeTextView_PAGE_CHANGED");
        intent.putExtra("pageInx", 2);
        sendBroadcast(intent);
        if (this.mEPGPage != null) {
            LOG.mo8825d("mEPGPage---->INVISIBLE");
            this.mEPGPage.onStop();
        }
        if (this.mCategoryPage != null) {
            LOG.mo8825d("mCategoryPage---->INVISIBLE");
            this.mCategoryPage.onStop();
        }
        if (this.mDownLoadPage != null) {
            LOG.mo8825d("mDownLoadPage---->INVISIBLE");
            this.mDownLoadPage.onStop();
        }
        if (this.mPlayPage != null) {
            LOG.mo8825d("mPlayPage---->INVISIBLE");
            this.mPlayPage.onStop();
        }
        this.mLivePage.bringToFront();
        this.mLivePage.onResume();
    }

    /* access modifiers changed from: private */
    public void hideVodEpgPage() {
        LOG.mo8825d("...hideVodPage...");
        this.isAtVodPage = false;
        this.isAtEpgPage = false;
    }

    /* access modifiers changed from: private */
    public void showEpgPage() {
        LOG.mo8825d("...showEpgPage...");
        if (this.mEPGPage == null) {
            this.mEPGPage = new PageEPG(this, this);
            this.mContentLayout.addView(this.mEPGPage.getmContentView());
        }
        Secure.putInt(getContentResolver(), "pageInx", 2);
        Intent intent = new Intent("net.sunniwell.MarqueeTextView_PAGE_CHANGED");
        intent.putExtra("pageInx", 2);
        sendBroadcast(intent);
        LOG.mo8825d("isFromLauncher------>" + this.mDBProcessor.getProp("isFromLauncher"));
        if ("1".equals(this.mDBProcessor.getProp("isFromLauncher"))) {
            LOG.mo8825d("getBackPageIndex()---->" + getBackPageIndex());
            if (getBackPageIndex() == 0) {
                if (this.mLivePage != null) {
                    this.mLivePage.onStop();
                }
            } else if (getBackPageIndex() == 3 && this.mDownLoadPage != null) {
                this.mDownLoadPage.onStop();
            }
        }
        if (this.mLivePage != null) {
            LOG.mo8825d("mLivePage---->INVISIBLE");
            this.mLivePage.setVisibility(4);
        }
        if (this.mDownLoadPage != null) {
            LOG.mo8825d("mDownLoadPage---->INVISIBLE");
            this.mDownLoadPage.setVisibility(4);
        }
        this.mDBProcessor.setProp("isFromLauncher", "0");
        this.mEPGPage.bringToFront();
        this.mEPGPage.onResume();
    }

    /* access modifiers changed from: private */
    public void showPlayPage() {
        LOG.mo8825d("...showPlayPage...");
        if (this.mPlayPage == null) {
            this.mPlayPage = new PagePlayVideo(this, this);
            this.mContentLayout.addView(this.mPlayPage.getmContentView());
        }
        if (this.mLivePage != null) {
            this.mLivePage.setVisibility(8);
        }
        if (this.mEPGPage != null) {
            this.mEPGPage.setVisibility(8);
        }
        if (this.mCategoryPage != null) {
            this.mCategoryPage.setVisibility(8);
        }
        if (this.mDownLoadPage != null) {
            this.mDownLoadPage.setVisibility(8);
        }
        this.mPlayPage.bringToFront();
        this.mPlayPage.onResume();
    }

    /* access modifiers changed from: private */
    public void showCategoryPage() {
        LOG.mo8825d("showCategoryPage");
        if (this.mCategoryPage == null) {
            this.mCategoryPage = new PageCategory(this, this);
            this.mContentLayout.addView(this.mCategoryPage.getmContentView());
        }
        Secure.putInt(getContentResolver(), "pageInx", 2);
        Intent intent = new Intent("net.sunniwell.MarqueeTextView_PAGE_CHANGED");
        intent.putExtra("pageInx", 2);
        sendBroadcast(intent);
        this.mCategoryPage.bringToFront();
        this.mCategoryPage.onResume();
    }

    public void gotoSettingPage() {
        LOG.mo8825d("gotoSettingPage--------->");
        Intent settins = new Intent();
        ComponentName component = new ComponentName("net.sunniwell.app.swsetting_zhongying", "net.sunniwell.app.settings.activities.MainActivity");
        settins.setFlags(268435456);
        settins.setComponent(component);
        startActivity(settins);
    }

    public void forceStopPackages(String packagename) {
        ActivityManager am = (ActivityManager) getSystemService("activity");
        try {
            Method forceStop = ActivityManager.class.getDeclaredMethod("forceStopPackage", new Class[]{String.class});
            forceStop.setAccessible(true);
            forceStop.invoke(am, new Object[]{packagename});
        } catch (Exception e) {
            e.printStackTrace();
            LOG.mo8825d("...Exception...===" + e.toString());
        }
    }

    public void goBackPage() {
        LOG.mo8825d("******goBackPage*****HISTORY.size()===**" + HISTORY.size());
        for (int i = 0; i < HISTORY.size(); i++) {
            LOG.mo8825d("**HISTORY****cateType**" + HISTORY.get(i));
        }
        if (HISTORY != null && HISTORY.size() > 1) {
            int inx = HISTORY.size() - 2;
            int catey = ((Integer) HISTORY.get(inx)).intValue();
            LOG.mo8825d("******goBackPage*****catey**" + catey + "****inx==***" + inx);
            if (getBackPageIndex() == 4) {
                HISTORY.remove(inx);
                goBackPage();
                return;
            }
            LOG.mo8825d("-------catey---------->" + catey);
            if (catey == 3 && getBackPageIndex() != 4) {
                LOG.mo8825d("getBackPageIndex---->" + getBackPageIndex() + "-------catey---------->" + catey);
                boolean usbChecked = DownUtils.usbChecked();
                LOG.mo8825d("usbChecked------->" + usbChecked);
                if (usbChecked) {
                    HISTORY.remove(inx + 1);
                    this.cateType = catey;
                    this.mDBProcessor.setProp("cateType", new StringBuilder(String.valueOf(this.cateType)).toString());
                    backPage();
                    return;
                }
                HISTORY.remove(inx);
                goBackPage();
            } else if (catey != 4 || getBackPageIndex() == 4) {
                LOG.mo8825d("getBackPageIndex---->" + getBackPageIndex() + "-------catey---------->" + catey);
                HISTORY.remove(inx + 1);
                this.cateType = catey;
                this.mDBProcessor.setProp("cateType", new StringBuilder(String.valueOf(this.cateType)).toString());
                backPage();
            } else {
                LOG.mo8825d("getBackPageIndex---->" + getBackPageIndex() + "-------catey---------->" + catey);
                HISTORY.remove(inx);
                goBackPage();
            }
        } else if (HISTORY != null && HISTORY.size() == 1) {
            this.cateType = 0;
            loadLivePage();
        }
    }

    public void downLoadPageCallBack(int eventCode, Message MessageData) {
        LOG.mo8825d("*******downLoadPagecallBack*******eventCode===***" + eventCode);
        switch (eventCode) {
            case 1:
                Bundle bundle = MessageData.getData();
                setFullPlay(true);
                this.mPlayVideoBean.setmSeekName(bundle.getString("playSeekName"));
                this.mPlayVideoBean.setmSeekFileName(bundle.getString("playSeekFileName"));
                this.mPlayVideoBean.setmPlayUrl(bundle.getString("playUrl"));
                this.mPlayVideoBean.setChannelName(bundle.getString("channelName"));
                this.mPlayVideoBean.setChannelNo(bundle.getString("channelNo"));
                this.mPlayVideoBean.setChannelIconUrl(bundle.getString("channelIconUrl"));
                this.mPlayPage.doByMessage(MessageData, 0);
                this.cateType = 4;
                checkPage();
                return;
            case 2:
                LOG.mo8825d("getBackPageIndex()--->" + getBackPageIndex());
                if (getBackPageIndex() == 1 || getBackPageIndex() == 2) {
                    this.mEPGPage.doByMessage(null, 1);
                } else if (getBackPageIndex() == 5 || getBackPageIndex() == 6) {
                    this.mCategoryPage.doByMessage(null, 1);
                }
                goBackPage();
                return;
            case 3:
                if (this.mEPGPage != null) {
                    this.mEPGPage.doByMessage(null, 1);
                }
                checkPage();
                return;
            case 4:
                this.cateType = 0;
                checkPage();
                return;
            case 5:
                this.mPlayPage.doByMessage(null, 1);
                return;
            case 6:
                this.mPlayPage.doByMessage(null, 5);
                return;
            default:
                return;
        }
    }

    public void livePageCallBack(int eventCode, Message MessageData) {
        LOG.mo8825d("*******livePagecallBack*******eventCode===***" + eventCode);
        switch (eventCode) {
            case 1:
                setFullPlay(true);
                this.mPlayPage.doByMessage(MessageData, 0);
                this.cateType = 4;
                checkPage();
                return;
            case 2:
                this.mPlayPage.doByMessage(null, 1);
                return;
            case 3:
                checkPage();
                return;
            case 4:
                this.cateType = 3;
                checkPage();
                return;
            case 5:
                showInfoDialog(getResources().getString(C0395R.string.ts_no_usb), false, true);
                return;
            default:
                return;
        }
    }

    public void playPageCallBack(int eventCode, Message messageData) {
        LOG.mo8825d("*******playPagecallBack*******eventCode===***" + eventCode);
        switch (eventCode) {
            case 1:
                goBackPage();
                return;
            case 2:
                this.mContentLayout.bringChildToFront(this.mLivePage.getmContentView());
                this.mLivePage.doByMessage(messageData, 0);
                return;
            case 3:
                checkPage();
                if (messageData != null && this.mEPGPage != null) {
                    this.mEPGPage.doByMessage(messageData, 2);
                    return;
                }
                return;
            case 4:
                this.cateType = 0;
                checkPage();
                return;
            case 5:
                if (this.mLivePage != null) {
                    this.mLivePage.doByMessage(null, 1);
                    return;
                }
                return;
            case 6:
                if (this.mLivePage != null) {
                    this.mLivePage.doByMessage(null, 2);
                    return;
                }
                return;
            case 7:
                this.cateType = 3;
                checkPage();
                if (isPlaying()) {
                    this.mPlayPage.doByMessage(null, 5);
                    return;
                }
                return;
            case 8:
                showInfoDialog(getResources().getString(C0395R.string.ts_no_usb), true, true);
                return;
            case 9:
                checkPage();
                return;
            case 11:
                String type = null;
                if (this.mPlayPage != null) {
                    type = this.mPlayVideoBean.getCurPlayType();
                }
                LOG.mo8825d("---type=" + type + "&mEPGPage=" + this.mEPGPage + "&mLivePage=" + this.mLivePage);
                if (type == null || !type.equals("0")) {
                    if (this.mLivePage != null) {
                        this.mLivePage.doByMessage(null, 3);
                        return;
                    }
                    return;
                } else if (this.mEPGPage != null) {
                    this.mEPGPage.doByMessage(null, 3);
                    return;
                } else {
                    return;
                }
            case 12:
                showInfoDialog(getResources().getString(C0395R.string.have_reservation), true, false);
                return;
            case 13:
                showInfoDialog(getResources().getString(C0395R.string.ts_max), false, false);
                return;
            default:
                ((InputMethodManager) getSystemService("input_method")).hideSoftInputFromWindow(new EditText(this).getWindowToken(), 1);
                return;
        }
    }

    public void epgPageCallBack(int eventCode, Message MessageData) {
        LOG.mo8825d("*******EPGPageCallBack*******eventCode===***" + eventCode);
        switch (eventCode) {
            case 1:
                goBackPage();
                return;
            case 2:
                if (isPlaying()) {
                    stopMediaPlay();
                }
                setFullPlay(true);
                this.mPlayPage.doByMessage(MessageData, 0);
                this.cateType = 4;
                checkPage();
                return;
            case 3:
                this.cateType = 4;
                this.mPlayPage.doByMessage(null, 2);
                checkPage();
                return;
            case 4:
                if (isPlaying()) {
                    stopMediaPlay();
                }
                this.cateType = 0;
                checkPage();
                return;
            case 6:
                if (isPlaying()) {
                    this.mPlayPage.doByMessage(null, 5);
                }
                this.cateType = 3;
                checkPage();
                return;
            case 7:
                this.mPlayPage.doByMessage(null, 3);
                return;
            case 8:
                this.mPlayPage.doByMessage(null, 4);
                return;
            case 9:
                this.mPlayPage.doByMessage(null, 1);
                return;
            case 10:
                showInfoDialog(getResources().getString(C0395R.string.ts_no_usb), false, true);
                return;
            case 11:
                LOG.mo8825d("显示分类页面  + show category");
                checkPage();
                return;
            case 12:
                LOG.mo8825d("显示推荐页面  + show recommendation");
                checkPage();
                return;
            case 13:
                showInfoDialog(getResources().getString(C0395R.string.ts_max), false, false);
                return;
            case 14:
                showInfoDialog(getResources().getString(C0395R.string.have_reservation), false, false);
                return;
            default:
                return;
        }
    }

    public void categoryPageCallBack(int eventCode, Message MessageData) {
        LOG.mo8825d("*******categoryPageCallBack*******eventCode===***" + eventCode);
        switch (eventCode) {
            case 1:
                goBackPage();
                return;
            case 2:
                if (isPlaying()) {
                    stopMediaPlay();
                }
                setFullPlay(true);
                this.mPlayPage.doByMessage(MessageData, 0);
                this.cateType = 4;
                checkPage();
                return;
            case 3:
                this.cateType = 4;
                this.mPlayPage.doByMessage(null, 2);
                checkPage();
                return;
            case 6:
                if (isPlaying()) {
                    this.mPlayPage.doByMessage(null, 5);
                }
                this.cateType = 3;
                checkPage();
                return;
            case 7:
                this.mPlayPage.doByMessage(null, 3);
                return;
            case 8:
                this.mPlayPage.doByMessage(null, 4);
                return;
            case 9:
                this.mPlayPage.doByMessage(null, 1);
                return;
            case 10:
                showInfoDialog(getResources().getString(C0395R.string.ts_no_usb), false, true);
                return;
            case 11:
                checkPage();
                return;
            case 13:
                showInfoDialog(getResources().getString(C0395R.string.ts_max), false, false);
                return;
            case 14:
                if (isPlaying()) {
                    stopMediaPlay();
                }
                this.cateType = 0;
                checkPage();
                return;
            case 15:
                showInfoDialog(getResources().getString(C0395R.string.have_reservation), false, false);
                return;
            default:
                return;
        }
    }

    private void showInfoDialog(String text, boolean isTransparent, boolean isRes) {
        LOG.mo8825d("..showInfoDialog....." + isTransparent);
        Message msg = Message.obtain();
        msg.obj = text;
        if (isTransparent) {
            if (isRes) {
                msg.what = 4;
            } else {
                msg.what = 2;
            }
        } else if (isRes) {
            msg.what = 3;
        } else {
            msg.what = 1;
        }
        this.mUiHandler.sendMessage(msg);
    }

    public void showInfoDialog() {
        showInfoDialog(getResources().getString(C0395R.string.ts_no_usb), true, true);
    }
}
