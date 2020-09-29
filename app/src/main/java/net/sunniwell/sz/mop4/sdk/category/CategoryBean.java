package net.sunniwell.sz.mop4.sdk.category;

import java.io.Serializable;

/* renamed from: net.sunniwell.sz.mop4.sdk.category.CategoryBean */
public class CategoryBean implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: id */
    int f337id;
    String title;
    int total;

    public int getId() {
        return this.f337id;
    }

    public void setId(int id) {
        this.f337id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title2) {
        this.title = title2;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total2) {
        this.total = total2;
    }

    public String toString() {
        return new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("[CategoryBean=" + super.toString())).append("\n\tid=").append(this.f337id).toString())).append("\n\ttitle=").append(this.title).toString())).append("\n\ttotal=").append(this.total).toString())).append("]").toString();
    }
}
