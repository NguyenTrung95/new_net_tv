package net.sunniwell.app.linktaro.tools;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sunniwell.app.linktaro.nettv.download.DownloadTask;
import org.codehaus.jackson.util.MinimalPrettyPrinter;

public class SortList<E> {
    public void Sort(List<E> list, final String method, final String sort) {
        Collections.sort(list, new Comparator() {
            public int compare(Object a, Object b) {
                try {
                    Method m1 = a.getClass().getMethod(method, null);
                    Method m2 = b.getClass().getMethod(method, null);
                    if (sort == null || !"desc".equals(sort)) {
                        return m1.invoke(a, null).toString().compareTo(m2.invoke(b, null).toString());
                    }
                    return m2.invoke(b, null).toString().compareTo(m1.invoke(a, null).toString());
                } catch (NoSuchMethodException ne) {
                    System.out.println(ne);
                    return 0;
                } catch (IllegalAccessException ie) {
                    System.out.println(ie);
                    return 0;
                } catch (InvocationTargetException it) {
                    System.out.println(it);
                    return 0;
                }
            }
        });
    }

    public void Sort(List<DownloadTask> mWaitList) {
        Collections.sort(mWaitList, new Comparator() {
            public int compare(Object lhs, Object rhs) {
                DownloadTask downFile1 = (DownloadTask) lhs;
                DownloadTask downFile2 = (DownloadTask) rhs;
                return new StringBuilder(String.valueOf(StringUtils.getTimeMillions(new StringBuilder(String.valueOf(downFile1.getDate().trim())).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(downFile1.getStartTime().trim()).toString()))).toString().compareTo(new StringBuilder(String.valueOf(StringUtils.getTimeMillions(new StringBuilder(String.valueOf(downFile2.getDate().trim())).append(MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR).append(downFile2.getStartTime().trim()).toString()))).toString());
            }
        });
    }
}
