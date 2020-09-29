package net.sunniwell.app.linktaro.radio.processor;

public interface PropProcessor {
    String getProp(String str);

    boolean isSupport(String str);

    String setProp(String str, String str2);
}
