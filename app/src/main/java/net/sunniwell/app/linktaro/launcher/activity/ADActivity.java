package net.sunniwell.app.linktaro.launcher.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;

import net.sunniwell.app.linktaro.R;
import net.sunniwell.download.manager.DownLoadConfigUtil;

public class ADActivity extends Activity {
    private ImageView mImageView;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        this.mImageView = (ImageView) findViewById(R.id.imageView);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        String url = getIntent().getStringExtra(DownLoadConfigUtil.KEY_URL);
        if (url != null) {
            Glide.with((Activity) this).load(url).placeholder((int) R.color.black).error((int) R.drawable.launcher_detil).into(this.mImageView);
        }
    }
}
