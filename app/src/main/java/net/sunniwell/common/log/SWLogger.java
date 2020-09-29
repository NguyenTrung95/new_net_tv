package net.sunniwell.common.log;

import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sunniwell.common.tools.DateTime;
import org.objectweb.asm.Opcodes;

public class SWLogger {
    private static final String DEFALUT_FORMAT = "";
    private static final String FILE = "/data/log.properties";
    private final String FORMAT;
    private final String TAG;
    private final Level level;

    enum Level {
        debug(3),
        info(4),
        warn(5),
        error(6),
        none(7);
        
        int intValue;

        private Level(int intValue2) {
            this.intValue = intValue2;
        }

        public static Level getLevel(String key) {
            Level level = null;
            if (key == null) {
                return level;
            }
            try {
                return valueOf(key.toLowerCase());
            } catch (Exception e) {
                e.printStackTrace();
                return level;
            }
        }

        public static Level getLevel(int intValue2) {
            Level[] levels = values();
            for (Level l : levels) {
                if (l.intValue == intValue2) {
                    return l;
                }
            }
            return null;
        }
    }

    protected SWLogger(Level level2, String TAG2, String Format) {
        this.level = level2;
        this.TAG = TAG2;
        this.FORMAT = Format;
    }

    /* renamed from: d */
    public void mo8825d(String msg) {
        print(Level.debug, msg);
    }

    /* renamed from: i */
    public void mo8827i(String msg) {
        print(Level.info, msg);
    }

    /* renamed from: w */
    public void mo8828w(String msg) {
        print(Level.warn, msg);
    }

    /* renamed from: e */
    public void mo8826e(String msg) {
        print(Level.error, msg);
    }

    private void print(Level l, String msg) {
        int intValue = l.intValue;
        if (intValue >= this.level.intValue) {
            Log.println(intValue, this.TAG, formatStr(intValue, getStackTraceElement(), msg));
        }
    }

    private StackTraceElement getStackTraceElement() {
        StackTraceElement[] stackElements = new Throwable().getStackTrace();
        if (stackElements == null) {
            return null;
        }
        for (int i = 0; i < stackElements.length; i++) {
            if (!stackElements[i].getClassName().equals(getClass().getName())) {
                return stackElements[i];
            }
        }
        return null;
    }

    private String formatStr(int level_intValue, StackTraceElement ste, String msg) {
        if (msg == null) {
            msg = "null";
        }
        Matcher matcher = Pattern.compile("%[A-Za-z]").matcher(this.FORMAT);
        StringBuffer sbr = new StringBuffer();
        while (matcher.find()) {
            String str = null;
            switch (matcher.group().charAt(1)) {
                case 'F':
                    if (ste != null) {
                        str = ste.getFileName();
                        break;
                    }
                    break;
                case 'M':
                    if (ste != null) {
                        str = ste.getMethodName();
                        break;
                    }
                    break;
                case Opcodes.DADD /*99*/:
                    if (ste != null) {
                        str = ste.getClassName();
                        break;
                    }
                    break;
                case 'd':
                    str = new DateTime().format("yyyyMMddHHmmssSSS");
                    break;
                case 'l':
                    if (ste != null) {
                        str = new StringBuilder(String.valueOf(ste.getLineNumber())).toString();
                        break;
                    }
                    break;
                case 'm':
                    str = msg.replace("\\", "\\\\");
                    break;
                case 'n':
                    str = System.getProperty("line.separator");
                    break;
                case Opcodes.IREM /*112*/:
                    str = Level.getLevel(level_intValue).name();
                    break;
                case Opcodes.INEG /*116*/:
                    str = Thread.currentThread().getName();
                    break;
            }
            if (str != null) {
                matcher.appendReplacement(sbr, str.replace("$", "\\$"));
            }
        }
        matcher.appendTail(sbr);
        return sbr.toString();
    }

    public static SWLogger getLogger(Class cls) {
        String value;
        if (cls == null) {
            return null;
        }
        String tag = cls.getSimpleName();
        Level useLevel = Level.none;
        String format = "";
        Properties p = new Properties();
        InputStream value2 = null;
        File file = new File(FILE);
        if (file.exists()) {
            try {
                value2 = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            value2 = SWLogger.class.getResourceAsStream("/log.properties");
            if (value2 == null) {
                value2 = SWLogger.class.getResourceAsStream("log.properties");
            }
        }
        if (value2 != null) {
            try {
                p.load(value2);
                if (!p.isEmpty()) {
                    String packageName = cls.getPackage().getName();
                    value2 = p.getProperty(packageName);
                    if (value2 == null || Level.getLevel(value2) == null) {
                        while (true) {
                            int pos = packageName.lastIndexOf(".");
                            if (pos > 0) {
                                packageName = packageName.substring(0, pos);
                                value = p.getProperty(packageName);
                                if (value != null && Level.getLevel(value) != null) {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                    Level defaultLevel = Level.getLevel(p.getProperty("level"));
                    if (defaultLevel != null) {
                        useLevel = defaultLevel;
                    }
                    Level level2 = Level.getLevel(value);
                    if (level2 != null) {
                        useLevel = level2;
                    }
                    String f = p.getProperty("format");
                    if (f != null) {
                        format = f;
                    }
                }
            } catch (Exception e2) {
                e2.printStackTrace();
                if (value2 != null) {
                    try {
                        value2.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                }
            } finally {
                if (value2 != null) {
                    try {
                        value2.close();
                    } catch (IOException e4) {
                        e4.printStackTrace();
                    }
                }
            }
        }
        if (value2 != null) {
            try {
                value2.close();
            } catch (IOException e5) {
                e5.printStackTrace();
            }
        }
        return new SWLogger(useLevel, tag, format);
    }
}
