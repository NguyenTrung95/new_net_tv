package net.sunniwell.app.linktaro.nettv.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CustomResDialog extends Dialog {
    private Button mBtnResCancel;
    private Button mBtnResOk;
    private Context mContext;
    private FrameLayout mFlResBg;
    private TextView mTvResContent;

    public CustomResDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        setContentView(C0395R.layout.reservation_dialog);
        initUi();
    }

    private void initUi() {
        this.mFlResBg = (FrameLayout) findViewById(C0395R.C0397id.fl_resdialog_bg);
        this.mTvResContent = (TextView) findViewById(C0395R.C0397id.tv_res_content);
        this.mBtnResOk = (Button) findViewById(C0395R.C0397id.btn_res_ok);
        this.mBtnResCancel = (Button) findViewById(C0395R.C0397id.btn_res_cancel);
        Window dialogWindow = getWindow();
        LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(49);
        lp.y = 200;
        dialogWindow.setAttributes(lp);
    }

    public void setDialogBg(int bgResource) {
        this.mFlResBg.setBackgroundResource(bgResource);
    }

    public void setTextContent(String text) {
        this.mTvResContent.setText(text);
    }

    public void setOkBgResource(int bgResource) {
        this.mBtnResOk.setBackgroundResource(bgResource);
    }

    public void setCancelBgResource(int bgResource) {
        this.mBtnResCancel.setBackgroundResource(bgResource);
    }

    public void setOkClickListener(OnClickListener listener) {
        this.mBtnResOk.setOnClickListener(listener);
    }

    public void setCancelClickListener(OnClickListener listener) {
        this.mBtnResCancel.setOnClickListener(listener);
    }

    public void show() {
        super.show();
        this.mBtnResOk.requestFocus();
    }
}
