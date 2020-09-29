package net.sunniwell.app.linktaro.launcher.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.sunniwell.app.linktaro.R;

public class CustomInfoDialog extends Dialog {
    private Context mContext;
    private boolean mCustomKey;
    private FrameLayout mFlResBg;
    private int mKeyCode;
    private TextView mTvResContent;

    public CustomInfoDialog(Context context, int theme) {
        super(context, theme);
        this.mContext = context;
        setContentView(R.layout.info_dialog);
        initUi();
    }

    public CustomInfoDialog(Context context, int theme, boolean customKey, int keyCode) {
        super(context, theme);
        this.mContext = context;
        this.mCustomKey = customKey;
        this.mKeyCode = keyCode;
        setContentView(R.layout.info_dialog);
        initUi();
    }

    private void initUi() {
        this.mFlResBg = (FrameLayout) findViewById(R.id.fl_infodialog);
        this.mTvResContent = (TextView) findViewById(R.id.tv_infodialog);
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

    public void show() {
        super.show();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (!this.mCustomKey || keyCode != this.mKeyCode) {
            return super.onKeyDown(keyCode, event);
        }
        return true;
    }
}
