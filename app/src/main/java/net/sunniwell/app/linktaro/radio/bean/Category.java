package net.sunniwell.app.linktaro.radio.bean;

public class Category {
    private String name;
    private String num;
    private String url;

    public void setNum(String num2) {
        this.num = num2;
    }

    public String getNum() {
        return this.num;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String categoryName) {
        this.name = categoryName;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url2) {
        this.url = url2;
    }

    public String toString() {
        return "{name=" + this.name + ",url=" + this.url + ",num=" + this.num + "}";
    }
}
