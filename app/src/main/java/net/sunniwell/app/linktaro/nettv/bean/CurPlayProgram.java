package net.sunniwell.app.linktaro.nettv.bean;

public class CurPlayProgram {
    private String channelName;
    private String channelNum;
    private String iconUrl;
    private String programDate;
    private String programName;
    private String programdescript;

    public String getIconUrl() {
        return this.iconUrl;
    }

    public void setIconUrl(String iconUrl2) {
        this.iconUrl = iconUrl2;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName2) {
        this.channelName = channelName2;
    }

    public String getProgramDate() {
        return this.programDate;
    }

    public void setProgramDate(String programDate2) {
        this.programDate = programDate2;
    }

    public String getProgramdescript() {
        return this.programdescript;
    }

    public void setProgramdescript(String programdescript2) {
        this.programdescript = programdescript2;
    }

    public String getProgramName() {
        return this.programName;
    }

    public void setProgramName(String programName2) {
        this.programName = programName2;
    }

    public String getChannelNum() {
        return this.channelNum;
    }

    public void setChannelNum(String channelNum2) {
        this.channelNum = channelNum2;
    }
}
