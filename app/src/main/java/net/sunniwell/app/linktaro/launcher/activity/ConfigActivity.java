package net.sunniwell.app.linktaro.launcher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import net.sunniwell.app.linktaro.nettv.manager.DataManager;
import net.sunniwell.app.linktaro.tools.SWSysProp;

public class ConfigActivity extends Activity implements OnCheckedChangeListener {
    private DataManager mDataManager;
    private RadioGroup mQuality;
    private RadioButton mRadioHLS;
    private RadioButton mRadioHigh;
    private RadioButton mRadioLow;
    private RadioButton mRadioMid;
    private RadioButton mRadioP2P;
    private RadioGroup mTransport;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0395R.layout.activity_config);
        this.mTransport = (RadioGroup) findViewById(C0395R.C0397id.transport);
        this.mTransport.setOnCheckedChangeListener(this);
        this.mRadioP2P = (RadioButton) findViewById(C0395R.C0397id.transport_p2p);
        this.mRadioHLS = (RadioButton) findViewById(C0395R.C0397id.transport_hls);
        this.mQuality = (RadioGroup) findViewById(C0395R.C0397id.quality);
        this.mQuality.setOnCheckedChangeListener(this);
        this.mRadioLow = (RadioButton) findViewById(C0395R.C0397id.quality_low);
        this.mRadioMid = (RadioButton) findViewById(C0395R.C0397id.quality_mid);
        this.mRadioHigh = (RadioButton) findViewById(C0395R.C0397id.quality_high);
        initView();
    }

    private void initView() {
        switch (SWSysProp.getIntParam("play_quality", 4)) {
            case 2:
                this.mRadioLow.setChecked(true);
                break;
            case 3:
                this.mRadioMid.setChecked(true);
                break;
            case 4:
                this.mRadioHigh.setChecked(true);
                break;
        }
        String protocol = SWSysProp.getStringParam("transport_way", "transport_p2p");
        if (protocol.equals("transport_p2p")) {
            this.mRadioP2P.setChecked(true);
            this.mRadioP2P.requestFocus();
        } else if (protocol.equals("transport_hls")) {
            this.mRadioHLS.setChecked(true);
            this.mRadioHLS.requestFocus();
        }
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case C0395R.C0397id.transport_p2p /*2131361799*/:
                SWSysProp.setStringParam("transport_way", "transport_p2p");
                return;
            case C0395R.C0397id.transport_hls /*2131361800*/:
                SWSysProp.setStringParam("transport_way", "transport_hls");
                return;
            case C0395R.C0397id.quality_low /*2131361802*/:
                SWSysProp.setIntParam("play_quality", 2);
                return;
            case C0395R.C0397id.quality_mid /*2131361803*/:
                SWSysProp.setIntParam("play_quality", 3);
                return;
            case C0395R.C0397id.quality_high /*2131361804*/:
                SWSysProp.setIntParam("play_quality", 4);
                return;
            default:
                return;
        }
    }
}
