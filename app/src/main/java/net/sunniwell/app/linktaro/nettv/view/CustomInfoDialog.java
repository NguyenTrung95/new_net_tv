package net.sunniwell.app.linktaro.nettv.view;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CustomInfoDialog extends Dialog {
    private Button mBtnInfoCancel;
    private Context mContext;
    private boolean mCustomKey;
    private FrameLayout mFlResBg;
    private int mKeyCode;
    private TextView mTvResContent;

    public CustomInfoDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        setContentView(C0395R.layout.info_dialog);
        initUi();
    }

    public CustomInfoDialog(Context context, int theme, boolean customKey, int keyCode) {
        super(context, theme);
        this.mContext = context;
        this.mCustomKey = customKey;
        this.mKeyCode = keyCode;
        setContentView(C0395R.layout.info_dialog);
        initUi();
    }

    private void initUi() {
        this.mFlResBg = (FrameLayout) findViewById(C0395R.C0397id.fl_infodialog);
        this.mTvResContent = (TextView) findViewById(C0395R.C0397id.tv_infodialog);
        setBtnCancelClickListener(null);
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

    public void setBtnCancelBgResource(int bgResource) {
        if (this.mBtnInfoCancel != null) {
            this.mBtnInfoCancel.setBackgroundResource(bgResource);
        }
    }

    private void setBtnCancelClickListener(OnClickListener listener) {
        if (this.mBtnInfoCancel != null) {
            this.mBtnInfoCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    CustomInfoDialog.this.dismiss();
                }
            });
        }
    }

    public void show() {
        super.show();
        if (this.mBtnInfoCancel != null) {
            this.mBtnInfoCancel.requestFocus();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!this.mCustomKey || keyCode != this.mKeyCode) {
            return super.onKeyDown(keyCode, event);
        }
        dismiss();
        return false;
    }
}
