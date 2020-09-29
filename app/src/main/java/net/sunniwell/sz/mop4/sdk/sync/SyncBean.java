package net.sunniwell.sz.mop4.sdk.sync;

import java.io.Serializable;

/* renamed from: net.sunniwell.sz.mop4.sdk.sync.SyncBean */
public class SyncBean implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: id */
    private int f343id;
    private int sync;

    public SyncBean() {
    }

    public SyncBean(int id, int sync2) {
        this.f343id = id;
        this.sync = sync2;
    }

    public int getId() {
        return this.f343id;
    }

    public void setId(int id) {
        this.f343id = id;
    }

    public int getSync() {
        return this.sync;
    }

    public void setSync(int sync2) {
        this.sync = sync2;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SyncBean)) {
            return false;
        }
        SyncBean other = (SyncBean) o;
        if (this.f343id == other.getId() && this.sync == other.getSync()) {
            return true;
        }
        return false;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[SyncBean=" + super.toString())).append("\n\tid=").append(this.f343id).toString())).append("\n\tsync=").append(this.sync).toString())).append("]").toString();
    }
}
