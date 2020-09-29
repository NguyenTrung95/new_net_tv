package net.sunniwell.app.linktaro.nettv.view;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.widget.TextView;

import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.nettv.entry.NettvActivity;
import net.sunniwell.common.log.SWLogger;

public class PromptDialogBox extends Dialog {
    private static PromptDialogBox mDialogBox = null;
    private final SWLogger LOG = SWLogger.getLogger(NettvActivity.class);
    private NettvActivity mP2pTv = new NettvActivity();

    private PromptDialogBox(Context context, int theme) {
        super(context, theme);
    }

    public static PromptDialogBox createDialog(Context context, String msg) {
        if (mDialogBox == null) {
            mDialogBox = new PromptDialogBox(context, R.style.PromptDialog);
        }
        mDialogBox.setContentView(R.layout.promptdialog);
        ((TextView) mDialogBox.findViewById(R.id.ts_info)).setText(msg);
        return mDialogBox;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        this.LOG.mo8825d("====keyCode===" + keyCode);
        if (keyCode == 4) {
            return true;
        }
        if (keyCode == 23 && mDialogBox != null) {
            mDialogBox.dismiss();
            notifiP2p();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void notifiP2p() {
        this.mP2pTv.showDownIcon();
    }

    public PromptDialogBox setMessage(String strMessage) {
        TextView tvMsg = (TextView) mDialogBox.findViewById(R.id.ts_info);
        if (tvMsg != null) {
            tvMsg.setText(strMessage);
        }
        return mDialogBox;
    }
}
