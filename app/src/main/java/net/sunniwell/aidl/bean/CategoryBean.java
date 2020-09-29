package net.sunniwell.aidl.bean;

import java.io.Serializable;

public class CategoryBean implements Serializable {
    private static final long serialVersionUID = 1;

    /* renamed from: id */
    int f299id;
    String title;
    int total;

    public int getId() {
        return this.f299id;
    }

    public void setId(int id) {
        this.f299id = id;
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
}
