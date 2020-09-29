package net.sunniwell.aidl.bean;

import java.io.Serializable;

public class ColumnBean implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: id */
    private int f300id;
    private boolean leaf;
    private int pid;
    private String title;
    private String type;

    public ColumnBean() {
    }

    public ColumnBean(int id, int pid2, String title2, String type2, boolean leaf2) {
        this.f300id = id;
        this.pid = pid2;
        this.title = title2;
        this.type = type2;
        this.leaf = leaf2;
    }

    public int getId() {
        return this.f300id;
    }

    public void setId(int id) {
        this.f300id = id;
    }

    public int getPid() {
        return this.pid;
    }

    public void setPid(int pid2) {
        this.pid = pid2;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type2) {
        this.type = type2;
    }

    public boolean isLeaf() {
        return this.leaf;
    }

    public void setLeaf(boolean leaf2) {
        this.leaf = leaf2;
    }

    public boolean equals(Object bean) {
        if (bean == null) {
            return false;
        }
        if (this == bean) {
            return true;
        }
        if (!(bean instanceof ColumnBean)) {
            return false;
        }
        ColumnBean other = (ColumnBean) bean;
        if (this.f300id != other.getId() || this.pid != other.getPid()) {
            return false;
        }
        if (this.title == null) {
            if (other.getTitle() != null) {
                return false;
            }
        } else if (!this.title.equals(other.getTitle())) {
            return false;
        }
        if (this.type == null) {
            if (other.getType() != null) {
                return false;
            }
        } else if (!this.type.equals(other.getType())) {
            return false;
        }
        return true;
    }

    public String toString() {
        return "id = " + this.f300id + " pid = " + this.pid + " title = " + this.title + " type  = " + this.type;
    }
}
