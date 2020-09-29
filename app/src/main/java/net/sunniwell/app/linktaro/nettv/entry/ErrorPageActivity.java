package net.sunniwell.app.linktaro.nettv.entry;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.provider.Settings.System;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import java.lang.reflect.Method;

import net.sunniwell.app.linktaro.launcher.activity.HomeActivity;
import net.sunniwell.app.linktaro.nettv.processor.imp.DBProcessor;
import net.sunniwell.app.linktaro.nettv.view.PromptDialogBox;
import net.sunniwell.app.linktaro.tools.SWSysProp;
import net.sunniwell.common.log.SWLogger;

public class ErrorPageActivity extends Activity {
    private static final String[] CONTENT = {"サービス登録者ではありません。代理店にお問い合わせ下さい。", "ご契約のパッケージで本番組は視聴できません。代理店にお問い合わせ下さい。", "インターネット故障\n通信機器(モデムやルーター)の電源を一旦切り、しばらく時間をおいてから再度起動してください。", "ただいま、受信できません。代理店にお問い合わせ下さい。", "この番組は、録画制限がかかっているため収録できません。", "このチャンネルは契約されていません、ご覧のチャンネルのカスタマセンターへご連絡してください。     コード：A103", "クラウト録画視聴の権限がございません。"};
    private static final SWLogger LOG = SWLogger.getLogger(ErrorPageActivity.class);
    private TextView contenTv;
    private int errorCode = 1;
    private Button mBtYes;
    private DBProcessor mDBProcessor;
    private PromptDialogBox mDialogBox;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        LOG.mo8825d("...onCreate...");
        super.onCreate(savedInstanceState);
        setContentView(C0395R.layout.error);
        this.contenTv = (TextView) findViewById(C0395R.C0397id.content);
        this.mBtYes = (Button) findViewById(C0395R.C0397id.bt_error);
        this.mDBProcessor = DBProcessor.getDBProcessor(this);
        SWSysProp.init(this);
        this.errorCode = SWSysProp.getIntParam("error_code", 1);
        LOG.mo8825d("----error page----errorCode=" + this.errorCode);
        this.mBtYes.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ErrorPageActivity.this.gotoHomePgae();
            }
        });
        showConten(this.errorCode);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        LOG.mo8825d("...onResume...");
        System.putInt(getContentResolver(), "isHandleSettingKey", 1);
        this.mDBProcessor.setProp("isAtErrorPage", "1");
        Secure.putString(getContentResolver(), "isNeedCallLanucherCheckStb", "1");
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        LOG.mo8825d("...onPause...");
        this.mDBProcessor.setProp("isAtErrorPage", "0");
        super.onPause();
    }

    /* access modifiers changed from: protected */
    public void onStop() {
        LOG.mo8825d("...onStop...");
        super.onStop();
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        LOG.mo8825d("...onDestroy...");
        this.mDBProcessor.setProp("isAtErrorPage", "0");
        super.onDestroy();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LOG.mo8825d("...keyCode...===" + keyCode);
        if (keyCode != 23) {
            if (keyCode == 176) {
                gotoSettingPage();
            } else if (keyCode == 3) {
                gotoHomePgae();
            } else if (keyCode == 4 || keyCode != 27) {
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showConten(int value) {
        int code = value;
        try {
            LOG.mo8825d("===Error_Code===" + value);
            if (code == 1) {
                this.contenTv.setText(CONTENT[2]);
            } else if (code == 2) {
                this.contenTv.setText(CONTENT[0]);
            } else if (code == 3) {
                this.contenTv.setText(CONTENT[1]);
            } else if (code == 5) {
                this.contenTv.setText(CONTENT[6]);
            } else if (code == 4) {
                this.contenTv.setText(CONTENT[5]);
            } else if (code == 6) {
                this.contenTv.setText(CONTENT[4]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public void forceStopPackages(String packagename) {
        ActivityManager am = (ActivityManager) getSystemService("activity");
        try {
            Method forceStop = ActivityManager.class.getDeclaredMethod("forceStopPackage", new Class[]{String.class});
            forceStop.setAccessible(true);
            forceStop.invoke(am, new Object[]{packagename});
        } catch (Exception e) {
            LOG.mo8825d("==exception====" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void gotoSettingPage() {
        forceStopPackages("net.sunniwell.app.sakura.nettv");
        Intent settins = new Intent();
        settins.setComponent(new ComponentName("net.sunniwell.app.swsetting_zhongying", "net.sunniwell.app.settings.activities.MainActivity"));
        startActivity(settins);
        finish();
    }

    public void gotoHomePgae() {
        Intent home = new Intent(this, HomeActivity.class);
        home.setFlags(268435456);
        startActivity(home);
        finish();
    }

    private void gotoP2pPage(int cateType) {
        this.mDBProcessor.setProp("cateType", new StringBuilder(String.valueOf(cateType)).toString());
        Intent intent = new Intent(this, NettvActivity.class);
        intent.setFlags(268435456);
        startActivity(intent);
        finish();
    }

    private void showDownDialog(String msg) {
        if (this.mDialogBox == null) {
            this.mDialogBox = PromptDialogBox.createDialog(this, msg);
        }
        this.mDialogBox = this.mDialogBox.setMessage(msg);
        this.mDialogBox.show();
    }

    public void gotoDlna() {
        Intent settins = new Intent();
        settins.setComponent(new ComponentName("net.sunniwell.app.swlocalplayer.huawei", "net.sunniwell.app.swlocalplayer.huawei.mainpage.MainPageActivity"));
        startActivity(settins);
        finish();
    }
}
