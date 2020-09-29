package net.sunniwell.app.linktaro.nettv.processor;

import java.util.ArrayList;
import net.sunniwell.aidl.bean.EPGBean;
import net.sunniwell.aidl.bean.MediaBean;
import net.sunniwell.aidl.bean.MediaListBean;

public interface ParserInterface {
    ArrayList<EPGBean> getCurProgramByChannelMark(String str, long j);

    MediaListBean getLiveList(int i);

    MediaBean getMediaDetail(String str, int i);

    ArrayList<EPGBean> getProgramByCategory(long j, long j2, String str);

    ArrayList<EPGBean> getProgramByChannelMark(String str, long j, long j2);
}
