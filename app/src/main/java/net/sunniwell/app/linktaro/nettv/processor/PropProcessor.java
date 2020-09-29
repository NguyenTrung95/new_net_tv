package net.sunniwell.app.linktaro.nettv.processor;

public interface PropProcessor {
    String getProp(String str);

    boolean isSupport(String str);

    String setProp(String str, String str2);
}
