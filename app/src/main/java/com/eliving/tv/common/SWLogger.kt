package com.eliving.tv.common

import android.util.Log
import org.objectweb.asm.Opcodes
import java.io.*
import java.util.*
import java.util.regex.Pattern

class SWLogger protected constructor(
    private val level: Level,
    private val TAG: String,
    private val FORMAT: String?
) {

    enum class Level(var intValue: Int) {
        debug(3), info(4), warn(5), error(6), none(7);

        companion object {
            fun getLevel(key: String?): Level? {
                val level: Level? = null
                return if (key == null) {
                    level
                } else try {
                    valueOf(key.toLowerCase())
                } catch (e: Exception) {
                    e.printStackTrace()
                    level
                }
            }

            fun getLevel(intValue2: Int): Level? {
                val levels =
                    values()
                for (l in levels) {
                    if (l.intValue == intValue2) {
                        return l
                    }
                }
                return null
            }
        }

    }

    /* renamed from: d */
    fun mo8825d(msg: String) {
        print(Level.debug, msg)
    }

    /* renamed from: i */
    fun mo8827i(msg: String) {
        print(Level.info, msg)
    }

    /* renamed from: w */
    fun mo8828w(msg: String) {
        print(Level.warn, msg)
    }

    /* renamed from: e */
    fun mo8826e(msg: String) {
        print(Level.error, msg)
    }

    private fun print(l: Level, msg: String) {
        val intValue = l.intValue
        if (intValue >= level.intValue) {
            Log.println(
                intValue,
                TAG,
                formatStr(intValue, stackTraceElement, msg)
            )
        }
    }

    private val stackTraceElement: StackTraceElement?
        private get() {
            val stackElements =
                Throwable().stackTrace ?: return null
            for (i in stackElements.indices) {
                if (stackElements[i].className != javaClass.name) {
                    return stackElements[i]
                }
            }
            return null
        }

    private fun formatStr(
        level_intValue: Int,
        ste: StackTraceElement?,
        msg: String
    ): String {
        var msg: String? = msg
        if (msg == null) {
            msg = "null"
        }
        val matcher =
            Pattern.compile("%[A-Za-z]").matcher(FORMAT)
        val sbr = StringBuffer()
        loop@ while (matcher.find()) {
            var str: String? = null
            when (matcher.group()[1]) {
                'F' -> if (ste != null) {
                    str = ste.fileName
                    break@loop
                }
                'M' -> if (ste != null) {
                    str = ste.methodName
                    break@loop
                }
                Opcodes.DADD.toChar() -> if (ste != null) {
                    str = ste.className
                    break@loop
                }
                'd' -> str = DateTime().format("yyyyMMddHHmmssSSS")
                'l' -> if (ste != null) {
                    str = StringBuilder(ste.lineNumber.toString()).toString()
                    break@loop
                }
                'm' -> str = msg.replace("\\", "\\\\")
                'n' -> str = System.getProperty("line.separator")
                Opcodes.IREM.toChar()-> str =
                    Level.getLevel(level_intValue)!!.name
                Opcodes.INEG.toChar() -> str = Thread.currentThread().name
            }
            if (str != null) {
                matcher.appendReplacement(sbr, str.replace("$", "\\$"))
            }
        }
        matcher.appendTail(sbr)
        return sbr.toString()
    }

    companion object {
        private const val DEFALUT_FORMAT = ""
        private const val FILE = "/data/log.properties"
        fun getLogger(cls: Class<*>?): SWLogger? {
            var value: String
            if (cls == null) {
                return null
            }
            val tag = cls.simpleName
            var useLevel =
                Level.none
            var format: String? = ""
            val p = Properties()
            var value2: InputStream? = null
            var inputStreamString: String? = ""
            val file = File(FILE)
            if (file.exists()) {
                try {
                    value2 = FileInputStream(file)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            } else {
                value2 = SWLogger::class.java.getResourceAsStream("/log.properties")
                if (value2 == null) {
                    value2 = SWLogger::class.java.getResourceAsStream("log.properties")
                }
            }
            if (value2 != null) {
                try {
                    p.load(value2)
                    if (!p.isEmpty) {
                        var packageName = cls.getPackage()!!.name
                        inputStreamString = p.getProperty(packageName)
                        if (value2 == null || Level.getLevel(
                                inputStreamString
                            ) == null
                        ) {
                            while (true) {
                                val pos = packageName.lastIndexOf(".")
                                if (pos > 0) {
                                    packageName = packageName.substring(0, pos)
                                    value = p.getProperty(packageName)
                                    if (value != null && Level.getLevel(
                                            value
                                        ) != null
                                    ) {
                                        break
                                    }
                                } else {
                                    break
                                }
                            }
                        }
                        val defaultLevel =
                            Level.getLevel(p.getProperty("level"))
                        if (defaultLevel != null) {
                            useLevel = defaultLevel
                        }
                        val level2 =
                            Level.getLevel(
                                inputStreamString
                            )
                        if (level2 != null) {
                            useLevel = level2
                        }
                        val f = p.getProperty("format")
                        if (f != null) {
                            format = f
                        }
                    }
                } catch (e2: Exception) {
                    e2.printStackTrace()
                    if (value2 != null) {
                        try {
                            value2.close()
                        } catch (e3: IOException) {
                            e3.printStackTrace()
                        }
                    }
                } finally {
                    if (value2 != null) {
                        try {
                            value2.close()
                        } catch (e4: IOException) {
                            e4.printStackTrace()
                        }
                    }
                }
            }
            if (value2 != null) {
                try {
                    value2.close()
                } catch (e5: IOException) {
                    e5.printStackTrace()
                }
            }
            return SWLogger(useLevel, tag, format)
        }
    }

}