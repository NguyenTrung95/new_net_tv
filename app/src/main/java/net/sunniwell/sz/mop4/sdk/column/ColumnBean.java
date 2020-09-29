package net.sunniwell.sz.mop4.sdk.column;

import java.io.Serializable;
import org.xmlpull.p019v1.XmlPullParser;

/* renamed from: net.sunniwell.sz.mop4.sdk.column.ColumnBean */
public class ColumnBean implements Serializable {
    private static final long serialVersionUID = 1;
    private String icon;

    /* renamed from: id */
    private int f338id;
    private boolean leaf;
    private int pid;
    private String title = XmlPullParser.NO_NAMESPACE;
    private String type = XmlPullParser.NO_NAMESPACE;

    public ColumnBean() {
    }

    public ColumnBean(int id, int pid2, String title2, String type2, boolean leaf2, String icon2) {
        this.f338id = id;
        this.pid = pid2;
        this.title = title2;
        this.type = type2;
        this.leaf = leaf2;
        this.icon = icon2;
    }

    public int getId() {
        return this.f338id;
    }

    public void setId(int id) {
        this.f338id = id;
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

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon2) {
        this.icon = icon2;
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
        if (this.f338id != other.getId() || this.pid != other.getPid()) {
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
        if (this.icon == null) {
            if (other.getIcon() != null) {
                return false;
            }
        } else if (!this.icon.equals(other.getIcon())) {
            return false;
        }
        return true;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[ColumnBean=" + super.toString())).append("\n\tid=").append(this.f338id).toString())).append("\n\tpid=").append(this.pid).toString())).append("\n\ttitle=").append(this.title).toString())).append("\n\ttype=").append(this.type).toString())).append("\n\tleaf=").append(this.leaf).toString())).append("\n\ticon=").append(this.icon).toString())).append("]").toString();
    }
}
