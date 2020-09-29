package net.sunniwell.app.linktaro.tools;

public class NetServiceException extends Exception {
    public NetServiceException() {
    }

    public NetServiceException(String message) {
        super(message);
    }

    public NetServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public NetServiceException(Throwable cause) {
        super(cause);
    }
}
