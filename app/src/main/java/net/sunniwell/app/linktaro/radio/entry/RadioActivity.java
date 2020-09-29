package net.sunniwell.app.linktaro.radio.entry;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemProperties;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import net.sunniwell.aidl.SDKRemoteManager;
import net.sunniwell.aidl.bean.AdBean;
import net.sunniwell.aidl.bean.MediaBean;
import net.sunniwell.aidl.bean.MediaListBean;
import net.sunniwell.aidl.bean.UrlBean;
import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.radio.dialog.CustomDialog;
import net.sunniwell.app.linktaro.tools.ImageDownloader;
import net.sunniwell.app.linktaro.tools.ImageDownloader.Mode;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.app.linktaro.tools.StringUtils;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.sz.mop4.sdk.media.MediaManager;

import org.objectweb.asm.Opcodes;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RadioActivity extends Activity {
    public static final int BBRADIO = 4;
    private static final int DOWN_AD_DATA = 3;
    private static final int DOWN_BBRADIO_DATA = 2;
    private static final int FEFRESH_AD_IMAGE_VIEW = 1;
    private static final int FEFRESH_CHANNEL_IMAGE_VIEW = 0;
    private static final int FEFRESH_NUM_IMAGE_VIEW = 4;
    private static final int HIDE_FULL_AD_IMAGE_VIEW = 3;
    /* access modifiers changed from: private */
    public static final SWLogger LOG = SWLogger.getLogger(RadioActivity.class);
    private static final int PLAY = 1;
    private static final int SHOW_FULL_AD_IMAGE_VIEW = 2;
    private static final int UPDATE_AD_IMAGE = 6;
    private static final int UPDATE_CHANNEL_IMAGE = 5;
    Handler UiHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (RadioActivity.this.mProgressDialog != null) {
                        Toast.makeText(RadioActivity.this, R.string.connection_fail, Toast.LENGTH_LONG).show();
                        RadioActivity.this.mProgressDialog.dismiss();
                        RadioActivity.this.mProgressDialog = null;
                        RadioActivity.this.clickIndex = 0;
                        RadioActivity.this.mMediaPlayer.stop();
                        RadioActivity.this.mMediaPlayer.reset();
                        break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };
    /* access modifiers changed from: private */
    public ImageView ad_fullscreen_image;
    /* access modifiers changed from: private */
    public ImageView ad_logo_image;
    private AudioManager audioManager = null;
    /* access modifiers changed from: private */
    public ImageView channel_image;
    /* access modifiers changed from: private */
    public int clickIndex;
    private ImageView colorIcon1_image;
    private ImageView colorIcon2_image;
    private ImageView colorIcon3_image;
    private ImageView colorIcon4_image;
    /* access modifiers changed from: private */
    public List<ImageView> colorIconImageList = new ArrayList();
    /* access modifiers changed from: private */
    public ImageView curAdImageView;
    /* access modifiers changed from: private */
    public boolean isFullAdShow;
    /* access modifiers changed from: private */
    public ImageDownloader mAdImageDownloader;
    /* access modifiers changed from: private */
    public int mAdIndex = -1;
    /* access modifiers changed from: private */
    public List<AdBean> mAllAdList;
    /* access modifiers changed from: private */
    public ArrayList<MediaBean> mAllChannelList;
    /* access modifiers changed from: private */
    public int mChannelIndex;
    private DownHandler mDownHandler;
    private HandlerThread mHandlerThread;
    /* access modifiers changed from: private */
    public MediaPlayer mMediaPlayer;
    /* access modifiers changed from: private */
    public CustomDialog mProgressDialog = null;
    /* access modifiers changed from: private */
    public SDKRemoteManager mSdkRemoteManager;
    /* access modifiers changed from: private */
    public int[] mSelectcolorIconBg = {R.drawable.radio_red_sel, R.drawable.radio_green_sel, R.drawable.radio_yellow_sel,R.drawable.radio_blue_sel};
    private String mTerminalid;
    Handler mUiHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (RadioActivity.this.mAllChannelList != null) {
                        RadioActivity.this.mAdImageDownloader.download(((MediaBean) RadioActivity.this.mAllChannelList.get(RadioActivity.this.mChannelIndex)).getImage(), RadioActivity.this.channel_image);
                        break;
                    }
                    break;
                case 1:
                    RadioActivity.LOG.mo8825d("[AllAdList]" + RadioActivity.this.mAllAdList + "&mAdIndex=" + RadioActivity.this.mAdIndex + "&curAdImageView=" + RadioActivity.this.curAdImageView + "&mAdImageDownloader=" + RadioActivity.this.mAdImageDownloader);
                    if (RadioActivity.this.mAllAdList != null && RadioActivity.this.mAllAdList.size() >= RadioActivity.this.mAdIndex + 1) {
                        RadioActivity.this.mAdImageDownloader.download(((AdBean) RadioActivity.this.mAllAdList.get(RadioActivity.this.mAdIndex)).getContent(), RadioActivity.this.curAdImageView);
                        break;
                    }
                case 2:
                    if (RadioActivity.this.mAllAdList != null && RadioActivity.this.mAllAdList.size() >= RadioActivity.this.mAdIndex + 1) {
                        RadioActivity.this.isFullAdShow = true;
                        RadioActivity.this.curAdImageView = RadioActivity.this.ad_logo_image;
                        RadioActivity.this.mAdImageDownloader.download(((AdBean) RadioActivity.this.mAllAdList.get(RadioActivity.this.mAdIndex)).getContent(), RadioActivity.this.curAdImageView);
                        RadioActivity.this.mUiHandler.sendEmptyMessage(4);
                        break;
                    } else {
                        Toast.makeText(RadioActivity.this, R.string.no_ad_hint, Toast.LENGTH_SHORT).show();
                        break;
                    }
                case 3:
                    RadioActivity.this.curAdImageView = RadioActivity.this.ad_logo_image;
                    RadioActivity.this.ad_fullscreen_image.setBackgroundDrawable(null);
                    break;
                case 4:
                    for (int i = 0; i < RadioActivity.this.colorIconImageList.size(); i++) {
                        if (i == RadioActivity.this.mAdIndex) {
                            ((ImageView) RadioActivity.this.colorIconImageList.get(i)).setBackgroundResource(RadioActivity.this.mSelectcolorIconBg[i]);
                        } else {
                            ((ImageView) RadioActivity.this.colorIconImageList.get(i)).setBackgroundResource(RadioActivity.this.mUnSelectcolorIconBg[i]);
                        }
                    }
                    break;
                case 5:
                    RadioActivity.LOG.mo8825d("]UPDATE_CHANNEL_IMAGE]");
                    if (RadioActivity.this.mAllChannelList != null && RadioActivity.this.mAllChannelList.size() > 0) {
                        RadioActivity.this.mAdImageDownloader.download(((MediaBean) RadioActivity.this.mAllChannelList.get(RadioActivity.this.mChannelIndex)).getImage(), RadioActivity.this.channel_image);
                        break;
                    }
                case 6:
                    RadioActivity.LOG.mo8825d("[UPDATE_AD_IMAGE]");
                    if (RadioActivity.this.mAllAdList != null && RadioActivity.this.mAllAdList.size() > 0) {
                        RadioActivity.this.mAdImageDownloader.download(((AdBean) RadioActivity.this.mAllAdList.get(0)).getContent(), RadioActivity.this.ad_logo_image);
                        break;
                    }
            }
            super.handleMessage(msg);
        }
    };
    /* access modifiers changed from: private */
    public int[] mUnSelectcolorIconBg = {R.drawable.radio_red, R.drawable.radio_green, R.drawable.radio_yellow, R.drawable.radio_blue};

    private class DownHandler extends Handler {
        public DownHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (RadioActivity.this.mAllChannelList != null && RadioActivity.this.mAllChannelList.size() > 0) {
                        String playUrl = RadioActivity.this.mopUrl2RealUrl(((UrlBean) ((MediaBean) RadioActivity.this.mAllChannelList.get(RadioActivity.this.clickIndex)).getUrls().get(0)).getUrl());
                        try {
                            if (!StringUtils.isEmpty(playUrl)) {
                                if (RadioActivity.this.mMediaPlayer.isPlaying()) {
                                    RadioActivity.this.mMediaPlayer.stop();
                                    RadioActivity.this.mMediaPlayer.reset();
                                }
                                RadioActivity.this.mMediaPlayer.setAudioStreamType(3);
                                RadioActivity.this.mMediaPlayer.setDataSource(playUrl);
                                RadioActivity.this.mMediaPlayer.prepare();
                                RadioActivity.this.mMediaPlayer.start();
                                if (RadioActivity.this.mProgressDialog != null) {
                                    RadioActivity.this.mProgressDialog.dismiss();
                                    RadioActivity.this.mProgressDialog = null;
                                }
                                RadioActivity.this.UiHandler.removeMessages(0);
                                break;
                            } else {
                                return;
                            }
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                            break;
                        } catch (SecurityException e2) {
                            e2.printStackTrace();
                            break;
                        } catch (IllegalStateException e3) {
                            e3.printStackTrace();
                            break;
                        } catch (IOException e4) {
                            e4.printStackTrace();
                            break;
                        }
                    }
                case 2:
                    MediaListBean mediaListBean = RadioActivity.this.mSdkRemoteManager.getMediaList(4, -1, null, null, null, null, null, null, null, null, MediaManager.SORT_BY_CHNLNUM, 0, 100, "zh", "iptv");
                    if (mediaListBean != null) {
                        RadioActivity.this.mAllChannelList = mediaListBean.getList();
                        RadioActivity.LOG.mo8825d("[mediaList]" + RadioActivity.this.mAllChannelList);
                        RadioActivity.this.mUiHandler.sendEmptyMessage(5);
                        break;
                    }
                    break;
                case 3:
                    RadioActivity.this.mAllAdList = RadioActivity.this.mSdkRemoteManager.getAdData(4, "iptv");
                    RadioActivity.LOG.mo8825d("[mAllAdList]" + RadioActivity.this.mAllAdList);
                    RadioActivity.this.mUiHandler.sendEmptyMessage(6);
                    break;
            }
            super.handleMessage(msg);
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG.mo8825d("[onCreate]");
        setContentView(R.layout.radio_activity);
        this.mSdkRemoteManager = SDKRemoteManager.getInstance(this, null);
        this.mAdImageDownloader = new ImageDownloader();
        this.mAdImageDownloader.setMode(Mode.NO_DOWNLOADED_DRAWABLE);
        this.mHandlerThread = new HandlerThread("downRadioData");
        this.mHandlerThread.start();
        this.mDownHandler = new DownHandler(this.mHandlerThread.getLooper());
        this.audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        initUI();
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        LOG.mo8825d("[onResume]");
        this.mMediaPlayer = new MediaPlayer();
        this.ad_logo_image.setBackgroundResource(R.drawable.radio_ad1);
        this.mUiHandler.sendEmptyMessage(5);
        this.mUiHandler.sendEmptyMessage(6);
        this.mDownHandler.sendEmptyMessage(2);
        this.mDownHandler.sendEmptyMessage(3);
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LOG.mo8825d("[onPause]");
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        LOG.mo8825d("[onStop]");
        if (this.mMediaPlayer != null) {
            if (this.mMediaPlayer.isPlaying()) {
                this.mMediaPlayer.stop();
            }
            this.mMediaPlayer.release();
            this.mMediaPlayer = null;
        }
        super.onStop();
    }

    private void initUI() {
        this.colorIcon1_image = (ImageView) findViewById(R.id.colorIcon1_image);
        this.colorIcon2_image = (ImageView) findViewById(R.id.colorIcon2_image);
        this.colorIcon3_image = (ImageView) findViewById(R.id.colorIcon3_image);
        this.colorIcon4_image = (ImageView) findViewById(R.id.colorIcon4_image);
        this.colorIcon1_image.setBackgroundResource(this.mSelectcolorIconBg[0]);
        this.colorIconImageList.add(this.colorIcon1_image);
        this.colorIconImageList.add(this.colorIcon2_image);
        this.colorIconImageList.add(this.colorIcon3_image);
        this.colorIconImageList.add(this.colorIcon4_image);
        this.ad_logo_image = (ImageView) findViewById(R.id.ad_logo_image);
        this.ad_logo_image.setBackgroundResource(R.drawable.radio_ad1);
        this.channel_image = (ImageView) findViewById(R.id.channel_image);
        this.ad_fullscreen_image = (ImageView) findViewById(R.id.ad_fullscreen_image);
        this.curAdImageView = this.ad_logo_image;
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LOG.mo8825d("[onDestroy]");
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case 4:
                if (this.isFullAdShow) {
                    this.mUiHandler.sendMessageAtFrontOfQueue(this.mUiHandler.obtainMessage(3));
                    this.isFullAdShow = false;
                    return true;
                } else if (this.mMediaPlayer == null || !this.mMediaPlayer.isPlaying()) {
                    finish();
                    return true;
                } else {
                    this.mMediaPlayer.stop();
                    this.mMediaPlayer.reset();
                    return true;
                }
            case 19:
                LOG.mo8825d("mChannelIndex------------>" + this.mChannelIndex);
                if (this.mChannelIndex == 0) {
                    this.mChannelIndex = 11;
                } else {
                    this.mChannelIndex--;
                }
                this.mUiHandler.sendEmptyMessage(0);
                break;
            case 20:
                LOG.mo8825d("mChannelIndex------------>" + this.mChannelIndex);
                if (this.mChannelIndex == 11) {
                    this.mChannelIndex = 0;
                } else {
                    this.mChannelIndex++;
                }
                this.mUiHandler.sendEmptyMessage(0);
                break;
            case 21:
                this.audioManager.adjustStreamVolume(3, -1, 1);
                break;
            case 22:
                this.audioManager.adjustStreamVolume(3, 1, 1);
                break;
            case 131:
                this.mAdIndex = 1;
                this.mUiHandler.sendMessageAtFrontOfQueue(this.mUiHandler.obtainMessage(2));
                break;
            case 132:
                this.mAdIndex = 0;
                this.mUiHandler.sendMessageAtFrontOfQueue(this.mUiHandler.obtainMessage(2));
                break;
            case Opcodes.I2L /*133*/:
                this.mAdIndex = 2;
                this.mUiHandler.sendMessageAtFrontOfQueue(this.mUiHandler.obtainMessage(2));
                break;
            case Opcodes.I2F /*134*/:
                this.mAdIndex = 3;
                this.mUiHandler.sendMessageAtFrontOfQueue(this.mUiHandler.obtainMessage(2));
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void clickAmChannel(View view) {
        String clickIndexStr = (String) view.getTag();
        LOG.mo8825d("[clickAmChannel]" + clickIndexStr);
        if (clickIndexStr != null && StringUtils.isNumberStr(clickIndexStr)) {
            this.clickIndex = Integer.valueOf(clickIndexStr).intValue();
        }
        updateProgressDialog("Connecting ....");
        this.UiHandler.sendEmptyMessageDelayed(0, 20000);
        this.mDownHandler.sendEmptyMessage(1);
    }

    public void updateProgressDialog(String msg) {
        if (this.mProgressDialog == null) {
            this.mProgressDialog = new CustomDialog(this, 280, 100, R.layout.radio_progress_dialog, R.style.dialog2, false);
            this.mProgressDialog.getWindow().setType(2003);
            this.mProgressDialog.setCancelable(false);
            this.mProgressDialog.show();
        }
        this.mProgressDialog.setMessage(msg);
    }

    /* access modifiers changed from: private */
    public String mopUrl2RealUrl(String url) {
        LOG.mo8825d("url " + url);
        String ret = url.substring(6);
        if (StringUtils.isEmpty(this.mTerminalid)) {
            this.mTerminalid = SystemProperties.get("ro.serialno");
        }
        String user = SWSysProp.getStringParam("user_name", XmlPullParser.NO_NAMESPACE);
        LOG.mo8825d("user " + user);
        String curOisUrl = this.mSdkRemoteManager.getCurOisUrl();
        LOG.mo8825d("curOisUrl " + curOisUrl);
        String ret2 = "http://" + curOisUrl.replace("5001", "5000/") + ret + ".m3u8?protocal=hls&user=" + user + "&tid=" + this.mTerminalid + "&sid=" + ((MediaBean) this.mAllChannelList.get(this.clickIndex)).getId() + "&type=stb&token=" + this.mSdkRemoteManager.getOisToken();
        LOG.mo8825d("mopUrl2RealUrl " + ret2);
        return ret2;
    }
}
