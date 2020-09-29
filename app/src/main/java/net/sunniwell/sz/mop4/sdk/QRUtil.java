package net.sunniwell.sz.mop4.sdk;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;

/* renamed from: net.sunniwell.sz.mop4.sdk.QRUtil */
public class QRUtil {
    public static Bitmap encodeQRCode(String text, int width, int height) {
        try {
            QRCodeWriter writer = new QRCodeWriter();
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            String str = text;
            BitMatrix bitMatrix = writer.encode(str, BarcodeFormat.QR_CODE, (int) (((float) width) * 1.0f), (int) (((float) height) * 1.0f), hints);
            int width2 = bitMatrix.getWidth();
            int w = width2;
            int height2 = bitMatrix.getHeight();
            int h = height2;
            System.out.println("w:" + bitMatrix.getWidth() + "h:" + bitMatrix.getHeight());
            int offset_x = (w - width2) / 2;
            int offset_y = (h - height2) / 2;
            int[] pixels = new int[(width2 * height2)];
            for (int y = 0; y < height2; y++) {
                for (int x = 0; x < width2; x++) {
                    if (bitMatrix.get(x + offset_x, y + offset_y)) {
                        pixels[(y * height2) + x] = -16777216;
                    } else {
                        pixels[(y * height2) + x] = -1;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(width2, height2, Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width2, 0, 0, width2, height2);
            return bitmap;
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void saveBitmap(Bitmap bm, String name) {
        File f = new File(name);
        if (f.exists()) {
            f.delete();
        }
        try {
            f.createNewFile();
            FileOutputStream fOut = new FileOutputStream(f);
            bm.compress(CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
