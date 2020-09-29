package net.sunniwell.app.linktaro.launcher.fragment;

import java.util.List;
import net.sunniwell.aidl.bean.AdBean;

public interface BaseFragment {
    void beginShowAd(List<AdBean> list);
}
