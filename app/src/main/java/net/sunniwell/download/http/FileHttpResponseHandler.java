package net.sunniwell.download.http;

import android.os.Message;
import android.util.Log;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;
import net.sunniwell.download.exception.FileAlreadyExistException;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectTimeoutException;

public class FileHttpResponseHandler extends AsyncHttpResponseHandler {
    private static final int BUFFER_SIZE = 8192;
    private static final String TAG = "FileHttpResponseHandler";
    private static final String TEMP_SUFFIX = ".download";
    private static final int TIMERSLEEPTIME = 100;
    public static final int TIME_OUT = 30000;
    private File baseDirFile;
    /* access modifiers changed from: private */
    public long downloadSize;
    private File file;
    private boolean interrupt = false;
    /* access modifiers changed from: private */
    public long networkSpeed;
    private RandomAccessFile outputStream;
    /* access modifiers changed from: private */
    public long previousFileSize;
    /* access modifiers changed from: private */
    public long previousTime;
    private File tempFile;
    private Timer timer = new Timer();
    /* access modifiers changed from: private */
    public boolean timerInterrupt = false;
    /* access modifiers changed from: private */
    public long totalSize;
    /* access modifiers changed from: private */
    public long totalTime;
    private String url;

    private class ProgressReportingRandomAccessFile extends RandomAccessFile {
        private int progress = 0;

        public ProgressReportingRandomAccessFile(File file, String mode) throws FileNotFoundException {
            super(file, mode);
        }

        public void write(byte[] buffer, int offset, int count) throws IOException {
            super.write(buffer, offset, count);
            this.progress += count;
            FileHttpResponseHandler.this.totalTime = System.currentTimeMillis() - FileHttpResponseHandler.this.previousTime;
            FileHttpResponseHandler.this.downloadSize = ((long) this.progress) + FileHttpResponseHandler.this.previousFileSize;
            if (FileHttpResponseHandler.this.totalTime > 0) {
                FileHttpResponseHandler.this.networkSpeed = (long) (((double) (((long) this.progress) / FileHttpResponseHandler.this.totalTime)) / 1.024d);
            }
        }
    }

    public FileHttpResponseHandler(String url2, String rootFile, String fileName) {
        this.url = url2;
        this.baseDirFile = new File(rootFile);
        this.file = new File(rootFile, fileName);
        this.tempFile = new File(rootFile, new StringBuilder(String.valueOf(fileName)).append(TEMP_SUFFIX).toString());
        init();
    }

    public FileHttpResponseHandler(String rootFile, String fileName) {
        this.baseDirFile = new File(rootFile);
        this.file = new File(rootFile, fileName);
        this.tempFile = new File(rootFile, new StringBuilder(String.valueOf(fileName)).append(TEMP_SUFFIX).toString());
        init();
    }

    public FileHttpResponseHandler(String filePath) {
        this.file = new File(filePath);
        this.baseDirFile = new File(this.file.getParent());
        this.tempFile = new File(new StringBuilder(String.valueOf(filePath)).append(TEMP_SUFFIX).toString());
        init();
    }

    private void init() {
        if (!this.baseDirFile.exists()) {
            this.baseDirFile.mkdirs();
        }
    }

    private void startTimer() {
        this.timer.schedule(new TimerTask() {
            public void run() {
                while (!FileHttpResponseHandler.this.timerInterrupt) {
                    FileHttpResponseHandler.this.sendProgressMessage(FileHttpResponseHandler.this.totalSize, FileHttpResponseHandler.this.getDownloadSize(), FileHttpResponseHandler.this.networkSpeed);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, 0, 1000);
    }

    private void stopTimer() {
        this.timerInterrupt = true;
    }

    public File getFile() {
        return this.file;
    }

    public String getUrl() {
        return this.url;
    }

    public boolean isInterrupt() {
        return this.interrupt;
    }

    public void setInterrupt(boolean interrupt2) {
        this.interrupt = interrupt2;
    }

    public long getDownloadSize() {
        return this.downloadSize;
    }

    public long getTotalSize() {
        return this.totalSize;
    }

    public double getDownloadSpeed() {
        return (double) this.networkSpeed;
    }

    public void setPreviousFileSize(long previousFileSize2) {
        this.previousFileSize = previousFileSize2;
    }

    public long getTotalTime() {
        return this.totalTime;
    }

    public void onSuccess(byte[] binaryData) {
        onSuccess(new String(binaryData));
    }

    public void onSuccess(int statusCode, byte[] binaryData) {
        onSuccess(binaryData);
    }

    public void onFailure(Throwable error, byte[] binaryData) {
        onFailure(error);
    }

    /* access modifiers changed from: protected */
    public void sendSuccessMessage(int statusCode, byte[] responseBody) {
        sendMessage(obtainMessage(4, new Object[]{Integer.valueOf(statusCode), responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendFailureMessage(Throwable e, byte[] responseBody) {
        sendMessage(obtainMessage(1, new Object[]{e, responseBody}));
    }

    /* access modifiers changed from: protected */
    public void sendProgressMessage(long totalSize2, long currentSize, long speed) {
        sendMessage(obtainMessage(0, new Object[]{Long.valueOf(totalSize2), Long.valueOf(currentSize), Long.valueOf(speed)}));
    }

    /* access modifiers changed from: protected */
    public void handleSuccessMessage(int statusCode, byte[] responseBody) {
        onSuccess(statusCode, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleFailureMessage(Throwable e, byte[] responseBody) {
        onFailure(e, responseBody);
    }

    /* access modifiers changed from: protected */
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case 4:
                Object[] response = (Object[]) msg.obj;
                handleSuccessMessage(((Integer) response[0]).intValue(), (byte[]) response[1]);
                return;
            default:
                super.handleMessage(msg);
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void sendResponseMessage(HttpResponse response) {
        Throwable error = null;
        long result = -1;
        int statusCode = 0;
        try {
            statusCode = response.getStatusLine().getStatusCode();
            long contentLenght = response.getEntity().getContentLength();
            if (contentLenght == -1) {
                contentLenght = (long) response.getEntity().getContent().available();
            }
            this.totalSize = this.previousFileSize + contentLenght;
            Log.v(TAG, "totalSize: " + this.totalSize);
            if (!this.file.exists() || this.totalSize != this.file.length()) {
                if (this.tempFile.exists()) {
                    Log.v(TAG, "yahooo: " + response.getEntity().getContentLength());
                    this.previousFileSize = this.tempFile.length();
                    Log.v(TAG, "File is not complete, download now.");
                    Log.v(TAG, "File length:" + this.tempFile.length() + " totalSize:" + this.totalSize);
                }
                this.outputStream = new ProgressReportingRandomAccessFile(this.tempFile, "rw");
                InputStream input = response.getEntity().getContent();
                startTimer();
                int bytesCopied = copy(input, this.outputStream);
                if (this.previousFileSize + ((long) bytesCopied) == this.totalSize || this.totalSize == -1 || this.interrupt) {
                    Log.v(TAG, "Download completed successfully.");
                    result = (long) bytesCopied;
                    stopTimer();
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (result != -1 && !this.interrupt && error == null) {
                        this.tempFile.renameTo(this.file);
                        sendSuccessMessage(statusCode, "下载成功！".getBytes());
                    } else if (error != null) {
                        Log.v(TAG, "Download failed." + error.getMessage());
                        if (error instanceof FileAlreadyExistException) {
                            sendSuccessMessage(statusCode, "下载成功！".getBytes());
                        } else {
                            sendFailureMessage(error, "");
                        }
                    }
                } else {
                    throw new IOException("Download incomplete: " + bytesCopied + " != " + this.totalSize);
                }
            } else {
                Log.v(TAG, "Output file already exists. Skipping download.");
                throw new FileAlreadyExistException("Output file already exists. Skipping download.");
            }
        } catch (FileNotFoundException e2) {
            sendFailureMessage(e2, "");
            error = e2;
        } catch (FileAlreadyExistException e3) {
            error = e3;
        } catch (IllegalStateException e4) {
            error = e4;
        } catch (IOException e5) {
            error = e5;
        }
    }

    public int copy(InputStream input, RandomAccessFile out) throws IOException {
        if (input == null || out == null) {
            return -1;
        }
        byte[] buffer = new byte[8192];
        BufferedInputStream in = new BufferedInputStream(input, 8192);
        Log.v(TAG, "length" + out.length());
        int count = 0;
        long errorBlockTimePreviousTime = -1;
        try {
            out.seek(out.length());
            this.previousTime = System.currentTimeMillis();
            while (!this.interrupt) {
                int n = in.read(buffer, 0, 8192);
                if (n != -1) {
                    out.write(buffer, 0, n);
                    count += n;
                    if (this.networkSpeed != 0) {
                        errorBlockTimePreviousTime = -1;
                    } else if (errorBlockTimePreviousTime <= 0) {
                        errorBlockTimePreviousTime = System.currentTimeMillis();
                    } else if (System.currentTimeMillis() - errorBlockTimePreviousTime > 30000) {
                        throw new ConnectTimeoutException("connection time out.");
                    }
                }
            }
            try {
                return count;
            } catch (Exception e) {
                return count;
            }
        } finally {
            try {
                out.close();
            } catch (IOException e2) {
            }
        }
    }

    public File getTempFile() {
        return this.tempFile;
    }
}
