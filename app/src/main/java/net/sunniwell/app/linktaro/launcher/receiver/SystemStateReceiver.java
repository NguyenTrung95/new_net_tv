package net.sunniwell.app.linktaro.launcher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import net.sunniwell.app.linktaro.tools.SWSysProp;

public class SystemStateReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("songjinzhu", "action " + action);
        if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
            String mountPath = intent.getData().getPath();
            Log.d("songjinzhu", "mountPath " + mountPath);
            if (!mountPath.contains("sdcard")) {
                SWSysProp.setStringParam("mount_path", mountPath);
            }
        }
    }
}
