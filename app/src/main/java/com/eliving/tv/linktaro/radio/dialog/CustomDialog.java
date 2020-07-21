package com.eliving.tv.linktaro.radio.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.eliving.tv.R;


public class CustomDialog extends Dialog {
    private static int default_height = 120;
    private static int default_width = 160;
    private boolean cancelable;

    public CustomDialog(Context context, int layout, int style, boolean cancelable2) {
        this(context, default_width, default_height, layout, style, cancelable2);
    }

    public CustomDialog(Context context, int width, int height, int layout, int style, boolean cancelable2) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = height;
        params.gravity = 17;
        window.setAttributes(params);
        this.cancelable = cancelable2;
    }

    public void setMessage(CharSequence s) {
        TextView tv = (TextView) findViewById(R.id.message);
        if (tv != null) {
            tv.setText(s);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (this.cancelable && event.getKeyCode() == 4 && this != null) {
            dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }
}
