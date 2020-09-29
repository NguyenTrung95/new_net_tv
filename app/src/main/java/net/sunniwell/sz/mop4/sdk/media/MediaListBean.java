package net.sunniwell.sz.mop4.sdk.media;

import java.io.Serializable;
import java.util.ArrayList;

/* renamed from: net.sunniwell.sz.mop4.sdk.media.MediaListBean */
public class MediaListBean implements Serializable {
    private static final long serialVersionUID = 1;
    private ArrayList<MediaBean> list = null;
    private int pagecount = 0;
    private int pageindex = 0;
    private int pagesize = 20;
    private int totalcount = 0;

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

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[MediaListBean=" + super.toString())).append("\n\tpageCount=").append(this.pagecount).toString())).append("\n\tpageIndex=").append(this.pageindex).toString())).append("\n\tpageSize=").append(this.pagesize).toString())).append("\n\tlist=").append(this.list).toString())).append("]").toString();
    }
}
