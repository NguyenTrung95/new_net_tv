package net.sunniwell.aidl.bean;

import java.io.Serializable;
import java.util.ArrayList;

public class MediaListBean implements Serializable {
    private static final long serialVersionUID = 1;
    private ArrayList<MediaBean> list;
    private int pagecount;
    private int pageindex;
    private int pagesize;
    private int totalcount;

    public ArrayList<MediaBean> getList() {
        return this.list;
    }

    public void setList(ArrayList<MediaBean> list2) {
        this.list = list2;
    }

    public int getPagecount() {
        return this.pagecount;
    }

    public void setPagecount(int pagecount2) {
        this.pagecount = pagecount2;
    }

    public int getTotalcount() {
        return this.totalcount;
    }

    public void setTotalcount(int totalcount2) {
        this.totalcount = totalcount2;
    }

    public int getPageindex() {
        return this.pageindex;
    }

    public void setPageindex(int pageindex2) {
        this.pageindex = pageindex2;
    }

    public int getPagesize() {
        return this.pagesize;
    }

    public void setPagesize(int pagesize2) {
        this.pagesize = pagesize2;
    }
}
