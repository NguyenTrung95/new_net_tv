package net.sunniwell.app.linktaro.launcher.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.core.os.EnvironmentCompat;
import androidx.fragment.app.FragmentActivity;

import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.tools.SWSysProp;

public class InfoActivity extends FragmentActivity {
    private TextView mSid;
    private TextView mUser;
    private TextView mValidto;

    public /* bridge */ /* synthetic */ View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(view, str, context, attributeSet);
    }

    public /* bridge */ /* synthetic */ View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return super.onCreateView(str, context, attributeSet);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_new);
        initViews();
        initData();
    }

    private void initViews() {
        this.mUser = (TextView) findViewById(R.id.user_edit);
        this.mSid = (TextView) findViewById(R.id.sid_edit);
        this.mValidto = (TextView) findViewById(R.id.validto_edit);
    }

    private void initData() {
        this.mUser.setText(SWSysProp.getStringParam("account_name", "sakura"));
        this.mValidto.setText(SWSysProp.getStringParam("account_time", "2000-01-01"));
        this.mSid.setText(SWSysProp.getStringParam("account_subscribe", EnvironmentCompat.MEDIA_UNKNOWN));
    }
}
