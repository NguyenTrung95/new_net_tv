package net.sunniwell.app.linktaro.nettv.content;

import android.content.Context;
import android.os.Message;
import android.widget.LinearLayout;

public abstract class PageBase extends LinearLayout {
    public abstract void doByMessage(Message message, int i);

    public abstract void onCreate(Context context);

    public abstract void onDestroy();

    public abstract void onResume();

    public abstract void onStop();

    public PageBase(Context context) {
        super(context);
        onCreate(context);
    }
}
