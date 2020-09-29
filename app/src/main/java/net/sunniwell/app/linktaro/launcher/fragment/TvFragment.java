package net.sunniwell.app.linktaro.launcher.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import net.sunniwell.aidl.bean.AdBean;
import net.sunniwell.app.linktaro.R;
import net.sunniwell.app.linktaro.launcher.constans.Constans.SWAction;
import net.sunniwell.app.linktaro.launcher.db.MailDbHelper;
import net.sunniwell.common.log.SWLogger;
import net.sunniwell.download.manager.DownLoadConfigUtil;
import net.sunniwell.sz.mop4.sdk.log.LogBean1;

import java.util.List;

public class TvFragment extends Fragment implements OnFocusChangeListener, BaseFragment {
    private SWLogger LOG = SWLogger.getLogger(TvFragment.class);
    private final String NETTV_LIVE = "2";
    private final String NETTV_RECORD = "6";
    private final String NETTV_VOD = "1";
    private boolean isFirst = true;
    private RelativeLayout mAD1;
    private RelativeLayout mAD2;
    /* access modifiers changed from: private */
    public List<AdBean> mAdList;
    private boolean mIsHidden = true;
    private ImageView mIvAd2;
    private View mTvFragment;
    /* access modifiers changed from: private */
    public View[] relPosters = new View[5];

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater);
    }

    @SuppressLint({"InflateParams"})
    private View initView(LayoutInflater inflater) {
        this.mTvFragment = inflater.inflate(R.layout.fragment_tv, null);
        this.mTvFragment.setTag("nettv");
        for (int i = 0; i < this.relPosters.length; i++) {
            this.relPosters[i] = this.mTvFragment.findViewById(getActivity().getResources().getIdentifier("tv_" + (i + 1), "id", getActivity().getPackageName()));
            this.relPosters[i].setOnFocusChangeListener(this);
            final int finalI = i;
            this.relPosters[i].setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    if (TvFragment.this.relPosters[finalI].getTag().equals("record")) {
                        TvFragment.this.startNetTV("6");
                    } else if (TvFragment.this.relPosters[finalI].getTag().equals(LogBean1.TERMINAL_STATISTICS_VOD)) {
                        TvFragment.this.startNetTV("1");
                    } else if (TvFragment.this.relPosters[finalI].getTag().equals("live")) {
                        TvFragment.this.startNetTV("2");
                    } else if (TvFragment.this.relPosters[finalI].getTag().equals("app1")) {
                        TvFragment.this.startRadio();
                    } else {
                        TvFragment.this.relPosters[finalI].getTag().equals("app2");
                    }
                }
            });
        }
        this.mAD1 = (RelativeLayout) this.mTvFragment.findViewById(R.id.ad1);
        this.mAD1.setOnFocusChangeListener(this);
        this.mAD2 = (RelativeLayout) this.mTvFragment.findViewById(R.id.ad2);
        this.mAD2.setOnFocusChangeListener(this);
        this.mIvAd2 = (ImageView) this.mTvFragment.findViewById(R.id.iv_tv_ad2);
        this.mAD1.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(SWAction.NETTV);
                intent.putExtra(MailDbHelper.FLAG, "3");
                TvFragment.this.startActivity(intent);
            }
        });
        this.mAD2.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (TvFragment.this.mAdList != null && TvFragment.this.mAdList.size() > 1) {
                    Intent intent = new Intent();
                    intent.setAction("net.sunniwell.app.sakura.AD");
                    intent.putExtra(DownLoadConfigUtil.KEY_URL, ((AdBean) TvFragment.this.mAdList.get(1)).getContent());
                    TvFragment.this.startActivity(intent);
                }
            }
        });
        return this.mTvFragment;
    }

    /* access modifiers changed from: private */
    public void startNetTV(String type) {
        Intent intent = new Intent();
        intent.setAction(SWAction.NETTV);
        intent.putExtra(MailDbHelper.FLAG, type);
        startActivity(intent);
    }

    /* access modifiers changed from: private */
    public void startRadio() {
        Intent intent = new Intent();
        intent.setAction(SWAction.RADIO);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void onFocusChange(View view, boolean focused) {
        if (this.isFirst) {
            this.isFirst = false;
            this.mAD1.requestFocus();
            return;
        }
        this.LOG.mo8825d("view " + view.getTag());
        float toX = ((float) (view.getWidth() + 20)) / ((float) view.getWidth());
        float toY = ((float) (view.getHeight() + 20)) / ((float) view.getHeight());
        if (focused) {
            view.bringToFront();
            view.animate().scaleX(toX).scaleY(toY).setDuration(300).start();
            return;
        }
        view.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).start();
    }

    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.LOG.mo8825d("onHiddenChanged-----TvAdFragment--> " + hidden);
        this.mIsHidden = hidden;
        if (!hidden && this.mAdList != null && this.mAdList.size() > 0) {
            beginShowAd(this.mAdList);
        }
    }

    public void beginShowAd(List<AdBean> adList) {
        this.mAdList = adList;
        if (adList != null) {
            try {
                if (adList.size() > 0) {
                    this.LOG.mo8825d("tvad1 = " + ((AdBean) adList.get(0)).getContent() + "---tvad2" + ((AdBean) adList.get(0)).getContent());
                    if (adList.size() > 1) {
                        Glide.with((Fragment) this).load(((AdBean) adList.get(0)).getContent()).placeholder((int) R.drawable.ad_2).error((int) R.drawable.ad_2).into(this.mIvAd2);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
