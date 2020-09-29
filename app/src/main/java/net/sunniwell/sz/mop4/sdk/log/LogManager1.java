package net.sunniwell.sz.mop4.sdk.log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* renamed from: net.sunniwell.sz.mop4.sdk.log.LogManager1 */
public class LogManager1 {
    private static ExecutorService executor;
    private static LogManager manager = new LogManager();

    public static LogManager getInstance() {
        executor = Executors.newSingleThreadExecutor();
        return manager;
    }

    public void sendLog(final LogBean1 logItem) {
        executor.execute(new Runnable() {
            public void run() {
                LogUtil1.sendLog(logItem);
            }
        });
    }
}
