package net.sunniwell.app.linktaro.nettv.processor;

public interface DownLoadInterface {

    public interface DownFileCallBackListener {
        void onDownFileComplete(String str);

        void onDownFileErrorCallBack(String str, int i);

        void onDownFileInfoCallBack(String str, float f, long j);

        void onDownFilePaused(String str);

        void onDownFileResumed(String str);

        void onDownFileStarted(String str);

        void onDownFileStoped(String str);
    }

    void downFile(String str, String str2);

    float getDownLoadProgress(String str);

    String getDownLoadSpeed(String str);

    int getDownLoadStatus(String str);

    void pauseDownLoad(String str);

    void resumeDownLoad(String str);

    void stopDownLoad(String str);
}
