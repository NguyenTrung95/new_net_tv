package com.eliving.tv.linktaro.radio.bean;

public class Parameter {
    private String deleteable;
    private String isForce;
    private String name;
    private String systemable;
    private String updateable;
    private String value;

    public String getName() {
        return this.name;
    }

    public String getIsForce() {
        return this.isForce;
    }

    public void setIsForce(String isForce2) {
        this.isForce = isForce2;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value2) {
        this.value = value2;
    }

    public String getDeletable() {
        return this.deleteable;
    }

    public void setDeletable(String deleteable2) {
        this.deleteable = deleteable2;
    }

    public String getUpdateable() {
        return this.updateable;
    }

    public void setUpdateable(String updateable2) {
        this.updateable = updateable2;
    }

    public String getSystemable() {
        return this.systemable;
    }

    public void setSystemable(String systemable2) {
        this.systemable = systemable2;
    }
}
