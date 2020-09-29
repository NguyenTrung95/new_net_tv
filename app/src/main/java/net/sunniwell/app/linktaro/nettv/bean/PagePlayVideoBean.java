package net.sunniwell.app.linktaro.nettv.bean;

public class PagePlayVideoBean {
    private String channelIconUrl;
    private String channelName;
    private String channelNo;
    private String mCurPlayType;
    private String mPlayUrl;
    private String mSeekFileName;
    private String mSeekName;

    public String getCurPlayType() {
        return this.mCurPlayType;
    }

    public void setCurPlayType(String curPlayType) {
        this.mCurPlayType = curPlayType;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName2) {
        this.channelName = channelName2;
    }

    public String getChannelNo() {
        return this.channelNo;
    }

    public void setChannelNo(String channelNo2) {
        this.channelNo = channelNo2;
    }

    public String getChannelIconUrl() {
        return this.channelIconUrl;
    }

    public void setChannelIconUrl(String channelIconUrl2) {
        this.channelIconUrl = channelIconUrl2;
    }

    public String getmSeekFileName() {
        return this.mSeekFileName;
    }

    public void setmSeekFileName(String mSeekFileName2) {
        this.mSeekFileName = mSeekFileName2;
    }

    public String getmSeekName() {
        return this.mSeekName;
    }

    public void setmSeekName(String mSeekName2) {
        this.mSeekName = mSeekName2;
    }

    public String getmPlayUrl() {
        return this.mPlayUrl;
    }

    public void setmPlayUrl(String mPlayUrl2) {
        this.mPlayUrl = mPlayUrl2;
    }
}
