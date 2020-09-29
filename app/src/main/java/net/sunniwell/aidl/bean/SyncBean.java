package net.sunniwell.aidl.bean;

import java.io.Serializable;

public class SyncBean implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: id */
    private int f304id;
    private int sync;

    public SyncBean() {
    }

    public SyncBean(int id, int sync2) {
        this.f304id = id;
        this.sync = sync2;
    }

    public int getId() {
        return this.f304id;
    }

    public void setId(int id) {
        this.f304id = id;
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
        if (this.f304id == other.getId() && this.sync == other.getSync()) {
            return true;
        }
        return false;
    }
}
