package net.sunniwell.download.exception;

public class FileAlreadyExistException extends Exception {
    private static final long serialVersionUID = 1;

    public FileAlreadyExistException(String message) {
        super(message);
    }
}
