package net.sunniwell.app.linktaro.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.view.ViewCompat;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.objectweb.asm.Opcodes;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeoutException;

public class ImageDownloader {

    /* renamed from: $SWITCH_TABLE$net$sunniwell$app$linktaro$tools$ImageDownloader$Mode */
    private static /* synthetic */ int[] f333x8c86370a = null;
    private static final int DELAY_BEFORE_PURGE = 10000;
    private static final int HARD_CACHE_CAPACITY = 12;
    private static final String LOG_TAG = "ImageDownloader";
    /* access modifiers changed from: private */
    public static final ConcurrentHashMap<String, SoftReference<Bitmap>> sSoftBitmapCache = new ConcurrentHashMap<>(6);
    /* access modifiers changed from: private */
    public Mode mode = Mode.NO_ASYNC_TASK;
    private final Handler purgeHandler = new Handler();
    private final Runnable purger = new Runnable() {
        public void run() {
            ImageDownloader.this.clearCache();
        }
    };
    private final HashMap<String, Bitmap> sHardBitmapCache = new LinkedHashMap<String, Bitmap>(6, 0.75f, true) {
        /* access modifiers changed from: protected */
        public boolean removeEldestEntry(Entry<String, Bitmap> eldest) {
            if (size() <= 12) {
                return false;
            }
            ImageDownloader.sSoftBitmapCache.put((String) eldest.getKey(), new SoftReference((Bitmap) eldest.getValue()));
            return true;
        }
    };

    class BitmapDownloaderTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        /* access modifiers changed from: private */
        public String url;

        public BitmapDownloaderTask(ImageView imageView) {
            this.imageViewReference = new WeakReference<>(imageView);
        }

        /* access modifiers changed from: protected */
        public Bitmap doInBackground(String... params) {
            this.url = params[0];
            return ImageDownloader.this.downloadBitmap(this.url);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }
            ImageDownloader.this.addBitmapToCache(this.url, bitmap);
            if (this.imageViewReference != null) {
                ImageView imageView = (ImageView) this.imageViewReference.get();
                if (this == ImageDownloader.getBitmapDownloaderTask(imageView) || ImageDownloader.this.mode != Mode.CORRECT) {
                    imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
                }
            }
        }
    }

    static class DownloadedDrawable extends ColorDrawable {
        private final WeakReference<BitmapDownloaderTask> bitmapDownloaderTaskReference;

        public DownloadedDrawable(BitmapDownloaderTask bitmapDownloaderTask) {
            super(ViewCompat.MEASURED_STATE_MASK);
            this.bitmapDownloaderTaskReference = new WeakReference<>(bitmapDownloaderTask);
        }

        public BitmapDownloaderTask getBitmapDownloaderTask() {
            return (BitmapDownloaderTask) this.bitmapDownloaderTaskReference.get();
        }
    }

    static class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0;
            while (totalBytesSkipped < n) {
                long bytesSkipped = this.in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0) {
                    if (read() < 0) {
                        break;
                    }
                    bytesSkipped = 1;
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }

    public enum Mode {
        NO_ASYNC_TASK,
        NO_DOWNLOADED_DRAWABLE,
        CORRECT
    }

    /* renamed from: $SWITCH_TABLE$net$sunniwell$app$linktaro$tools$ImageDownloader$Mode */
    static /* synthetic */ int[] m320x8c86370a() {
        int[] iArr = f333x8c86370a;
        if (iArr == null) {
            iArr = new int[Mode.values().length];
            try {
                iArr[Mode.CORRECT.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[Mode.NO_ASYNC_TASK.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[Mode.NO_DOWNLOADED_DRAWABLE.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            f333x8c86370a = iArr;
        }
        return iArr;
    }

    public void download(String url, ImageView imageView) {
        Bitmap bitmap = getBitmapFromCache(url);
        if (bitmap == null) {
            forceDownload(url, imageView);
            return;
        }
        cancelPotentialDownload(url, imageView);
        imageView.setBackgroundDrawable(new BitmapDrawable(bitmap));
    }

    public Bitmap downLoad(String url) throws TimeoutException {
        Bitmap bitmap = null;
        try {
            return downloadBitmap(url);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    private void forceDownload(String url, ImageView imageView) {
        if (url == null) {
            imageView.setImageDrawable(null);
        } else if (cancelPotentialDownload(url, imageView)) {
            switch (m320x8c86370a()[this.mode.ordinal()]) {
                case 1:
                    Bitmap bitmap = downloadBitmap(url);
                    addBitmapToCache(url, bitmap);
                    imageView.setImageBitmap(bitmap);
                    return;
                case 2:
                    imageView.setMinimumHeight(Opcodes.IFGE);
                    new BitmapDownloaderTask(imageView).execute(new String[]{url});
                    return;
                case 3:
                    BitmapDownloaderTask task = new BitmapDownloaderTask(imageView);
                    imageView.setImageDrawable(new DownloadedDrawable(task));
                    imageView.setMinimumHeight(Opcodes.IFGE);
                    task.execute(new String[]{url});
                    return;
                default:
                    return;
            }
        }
    }

    private static boolean cancelPotentialDownload(String url, ImageView imageView) {
        BitmapDownloaderTask bitmapDownloaderTask = getBitmapDownloaderTask(imageView);
        if (bitmapDownloaderTask == null) {
            return true;
        }
        String bitmapUrl = bitmapDownloaderTask.url;
        if (bitmapUrl != null && bitmapUrl.equals(url)) {
            return false;
        }
        bitmapDownloaderTask.cancel(true);
        return true;
    }

    /* access modifiers changed from: private */
    public static BitmapDownloaderTask getBitmapDownloaderTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DownloadedDrawable) {
                return ((DownloadedDrawable) drawable).getBitmapDownloaderTask();
            }
        }
        return null;
    }

    /* access modifiers changed from: 0000 */
    public Bitmap downloadBitmap(String url) {
        HttpClient client;
        HttpEntity entity = null;
        InputStream inputStream = null;
        if (this.mode == Mode.NO_ASYNC_TASK) {
            client = new DefaultHttpClient();
        } else {
            client = AndroidHttpClient.newInstance("Android");
        }
        client.getParams().setIntParameter("http.socket.timeout", 5000);
        client.getParams().setIntParameter("http.connection.timeout", 5000);
        HttpGet getRequest = new HttpGet(url);
        try {
            HttpResponse response = client.execute(getRequest);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                Log.w(LOG_TAG, "Error " + statusCode + " while retrieving bitmap from " + url);
                if (!(client instanceof AndroidHttpClient)) {
                    return null;
                }
                ((AndroidHttpClient) client).close();
                return null;
            }
            entity = response.getEntity();
            if (entity != null) {
                inputStream = null;
                inputStream = entity.getContent();
                Bitmap decodeStream = BitmapFactory.decodeStream(new FlushedInputStream(inputStream));
                if (inputStream != null) {
                    inputStream.close();
                }
                entity.consumeContent();
                if (client instanceof AndroidHttpClient) {
                    ((AndroidHttpClient) client).close();
                }
                return decodeStream;
            } else if (!(client instanceof AndroidHttpClient)) {
                return null;
            } else {
                ((AndroidHttpClient) client).close();
                return null;
            }
        } catch (IOException e) {
            try {
                getRequest.abort();
                Log.w(LOG_TAG, "I/O error while retrieving bitmap from " + url, e);
            } finally {
                if (client instanceof AndroidHttpClient) {
                    ((AndroidHttpClient) client).close();
                }
            }
        } catch (IllegalStateException e2) {
            getRequest.abort();
            Log.w(LOG_TAG, "Incorrect URL: " + url);
            if (!(client instanceof AndroidHttpClient)) {
                return null;
            }
            ((AndroidHttpClient) client).close();
            return null;
        } catch (Exception e3) {
            getRequest.abort();
            Log.w(LOG_TAG, "Error while retrieving bitmap from " + url, e3);
            if (!(client instanceof AndroidHttpClient)) {
                return null;
            }
            ((AndroidHttpClient) client).close();
            return null;
        } catch (Throwable th) {
            try{
                if (inputStream != null) {
                    inputStream.close();
                }
                entity.consumeContent();
            }catch (Exception e){
                e.printStackTrace();
            }

            throw th;
        }
        return null;
    }

    public void setMode(Mode mode2) {
        this.mode = mode2;
        clearCache();
    }

    /* access modifiers changed from: private */
    public void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            synchronized (this.sHardBitmapCache) {
                this.sHardBitmapCache.put(url, bitmap);
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0023, code lost:
        if (r1 == null) goto L_0x0037;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0025, code lost:
        r0 = (android.graphics.Bitmap) r1.get();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x002b, code lost:
        if (r0 == null) goto L_0x0032;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0032, code lost:
        sSoftBitmapCache.remove(r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:?, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001b, code lost:
        r1 = (java.lang.ref.SoftReference) sSoftBitmapCache.get(r5);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap getBitmapFromCache(java.lang.String r5) {
        /*
            r4 = this;
            java.util.HashMap<java.lang.String, android.graphics.Bitmap> r3 = r4.sHardBitmapCache
            monitor-enter(r3)
            java.util.HashMap<java.lang.String, android.graphics.Bitmap> r2 = r4.sHardBitmapCache     // Catch:{ all -> 0x002f }
            java.lang.Object r0 = r2.get(r5)     // Catch:{ all -> 0x002f }
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0     // Catch:{ all -> 0x002f }
            if (r0 == 0) goto L_0x001a
            java.util.HashMap<java.lang.String, android.graphics.Bitmap> r2 = r4.sHardBitmapCache     // Catch:{ all -> 0x002f }
            r2.remove(r5)     // Catch:{ all -> 0x002f }
            java.util.HashMap<java.lang.String, android.graphics.Bitmap> r2 = r4.sHardBitmapCache     // Catch:{ all -> 0x002f }
            r2.put(r5, r0)     // Catch:{ all -> 0x002f }
            monitor-exit(r3)     // Catch:{ all -> 0x002f }
            r2 = r0
        L_0x0019:
            return r2
        L_0x001a:
            monitor-exit(r3)     // Catch:{ all -> 0x002f }
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.ref.SoftReference<android.graphics.Bitmap>> r2 = sSoftBitmapCache
            java.lang.Object r1 = r2.get(r5)
            java.lang.ref.SoftReference r1 = (java.lang.ref.SoftReference) r1
            if (r1 == 0) goto L_0x0037
            java.lang.Object r0 = r1.get()
            android.graphics.Bitmap r0 = (android.graphics.Bitmap) r0
            if (r0 == 0) goto L_0x0032
            r2 = r0
            goto L_0x0019
        L_0x002f:
            r2 = move-exception
            monitor-exit(r3)     // Catch:{ all -> 0x002f }
            throw r2
        L_0x0032:
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.ref.SoftReference<android.graphics.Bitmap>> r2 = sSoftBitmapCache
            r2.remove(r5)
        L_0x0037:
            r2 = 0
            goto L_0x0019
        */
        throw new UnsupportedOperationException("Method not decompiled: net.sunniwell.app.linktaro.tools.ImageDownloader.getBitmapFromCache(java.lang.String):android.graphics.Bitmap");
    }

    public void clearCache() {
        this.sHardBitmapCache.clear();
        sSoftBitmapCache.clear();
    }

    private void resetPurgeTimer() {
        this.purgeHandler.removeCallbacks(this.purger);
        this.purgeHandler.postDelayed(this.purger, 10000);
    }
}
