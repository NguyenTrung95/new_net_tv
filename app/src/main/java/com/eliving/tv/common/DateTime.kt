package com.eliving.tv.common

import org.codehaus.jackson.util.MinimalPrettyPrinter
import org.xmlpull.v1.XmlPullParser
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

class DateTime {
    var calendar: Calendar?
        private set

    constructor() {
        calendar = null
        calendar = Calendar.getInstance()
    }

    constructor(lMillis: Long) : this() {
        calendar!!.timeInMillis = lMillis
    }

    constructor(year: Int, month: Int, day: Int) : this() {
        calendar!![year, month] = day
    }

    constructor(year: Int, month: Int, day: Int, hourOfDay: Int, minute: Int) : this() {
        calendar!![year, month, day, hourOfDay] = minute
    }

    constructor(
        year: Int,
        month: Int,
        day: Int,
        hourOfDay: Int,
        minute: Int,
        second: Int
    ) : this() {
        calendar!![year, month, day, hourOfDay, minute] = second
    }

    constructor(strDateTime: String?) {
        calendar = null
        if (strDateTime == null || strDateTime.trim { it <= ' ' }.length == 0) {
            calendar = null
            return
        }
        val strDateTime2 = checkStr(strDateTime)
        calendar = Calendar.getInstance()
        calendar?.set(
            getInt(strDateTime2, 0, 4),
            getInt(strDateTime2, 4, 2) - 1,
            getInt(strDateTime2, 6, 2),
            getInt(strDateTime2, 8, 2),
            getInt(strDateTime2, 10, 2),
            getInt(strDateTime2, 12, 2)
        )
    }

    val year: Int
        get() = if (calendar == null) {
            -1
        } else calendar!![Calendar.YEAR]

    val month: Int
        get() = if (calendar == null) {
            -1
        } else calendar!![Calendar.MONTH] + 1

    val day: Int
        get() = if (calendar == null) {
            -1
        } else calendar!![Calendar.DAY_OF_MONTH]

    val week: Int
        get() = if (calendar == null) {
            -1
        } else calendar!![Calendar.DAY_OF_WEEK]

    val hour: Int
        get() = if (calendar == null) {
            -1
        } else calendar!![Calendar.HOUR_OF_DAY]

    val minute: Int
        get() = if (calendar == null) {
            -1
        } else calendar!![Calendar.MINUTE]

    val second: Int
        get() = if (calendar == null) {
            -1
        } else calendar!![Calendar.SECOND]

    val millisecond: Int
        get() = if (calendar == null) {
            -1
        } else calendar!![Calendar.MILLISECOND]

    val time: Long
        get() = if (calendar == null) {
            -1
        } else calendar!!.time.time

    val timeZone: Int
        get() = if (calendar == null) {
            -1
        } else calendar!![Calendar.ZONE_OFFSET] / 3600000

    fun formatDefault(): String {
        return format("yyyy-MM-dd hh:mm:ss.SSS")
    }

    fun format(format: String?): String {
        return if (format == null) {
            XmlPullParser.NO_NAMESPACE
        } else SimpleDateFormat(format).format(Date(time))
    }

    val timestamp: Timestamp
        get() = Timestamp(calendar!!.timeInMillis)

    var dateTime: Long
        get() = if (calendar == null) {
            -1
        } else calendar!!.timeInMillis
        set(lMillis) {
            if (calendar != null) {
                calendar!!.timeInMillis = lMillis
            }
        }

    val dateTimeInMillis: Long
        get() = if (calendar == null) {
            -1
        } else calendar!!.timeInMillis

    val formattedDateTime: String?
        get() = getFormattedDateTime("yyyy年MM月dd日HH时mm分ss秒")

    val formatDateTime: String?
        get() = getFormattedDateTime("yyyy-MM-dd HH:mm:ss")

    fun getFormattedDateTime(strPattern: String?): String? {
        if (calendar == null) {
            return XmlPullParser.NO_NAMESPACE
        }
        return if (strPattern == null || strPattern.trim { it <= ' ' }.length == 0) {
            null
        } else SimpleDateFormat(strPattern).format(calendar!!.time)
    }

    val firstDayOfMouth: DateTime
        get() {
            val dt =
                DateTime(dateTimeInMillis)
            dt[5] = calendar!!.getActualMinimum(Calendar.DAY_OF_MONTH)
            return dt
        }

    val lastDayOfMouth: DateTime
        get() {
            val dt =
                DateTime(dateTimeInMillis)
            dt[5] = calendar!!.getActualMaximum(Calendar.DAY_OF_MONTH)
            return dt
        }

    val firstDayOfWeek: DateTime
        get() {
            val iDayOfWeek = calendar!![Calendar.DAY_OF_WEEK]
            val dt =
                DateTime(dateTimeInMillis)
            dt.addDay(1 - iDayOfWeek)
            return dt
        }

    val lastDayOfWeek: DateTime
        get() {
            val dt = firstDayOfWeek
            dt.addDay(6)
            return dt
        }

    fun setDateTime(strDateTime: String) {
        if (calendar != null) {
            val strDateTime2 = checkStr(strDateTime)
            calendar!![getInt(strDateTime2, 0, 4), getInt(strDateTime2, 4, 2) - 1, getInt(
                strDateTime2,
                6,
                2
            ), getInt(
                strDateTime2,
                8,
                2
            ), getInt(strDateTime2, 10, 2)] = getInt(strDateTime2, 12, 2)
        }
    }

    val dateTimeString: String
        get() {
            if (calendar == null) {
                return XmlPullParser.NO_NAMESPACE
            }
            val buffer = StringBuffer()
            buffer.append(calendar!![Calendar.YEAR])
            buffer.append(padChar(calendar!![Calendar.MONTH] + 1, 2))
            buffer.append(padChar(calendar!![Calendar.DAY_OF_MONTH], 2))
            buffer.append(padChar(calendar!![Calendar.HOUR_OF_DAY], 2))
            buffer.append(padChar(calendar!![Calendar.MINUTE], 2))
            buffer.append(padChar(calendar!![Calendar.SECOND], 2))
            return buffer.toString()
        }

    operator fun get(field: Int): Int {
        return calendar!![field]
    }

    operator fun set(field: Int, value: Int) {
        calendar!![field] = value
    }

    fun getActualMaximum(field: Int): Int {
        return calendar!!.getActualMaximum(field)
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    fun addYear(iYear: Int): DateTime? {
        if (calendar == null) {
            return null
        }
        calendar!!.add(Calendar.YEAR, iYear)
        return this
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    fun addMonth(iMonth: Int): DateTime? {
        if (calendar == null) {
            return null
        }
        calendar!!.add(Calendar.MONTH, iMonth)
        return this
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    fun addDay(iDay: Int): DateTime? {
        if (calendar == null) {
            return null
        }
        calendar!!.add(Calendar.DAY_OF_MONTH, iDay)
        return this
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    fun addHour(iHour: Int): DateTime? {
        if (calendar == null) {
            return null
        }
        calendar!!.add(Calendar.HOUR_OF_DAY, iHour)
        return this
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    fun addMinute(iMinute: Int): DateTime? {
        if (calendar == null) {
            return null
        }
        calendar!!.add(Calendar.MINUTE, iMinute)
        return this
    }

    /* Debug info: failed to restart local var, previous not found, register: 2 */
    fun addSecond(iSecond: Int): DateTime? {
        if (calendar == null) {
            return null
        }
        calendar!!.add(Calendar.SECOND, iSecond)
        return this
    }

    override fun toString(): String {
        return dateTimeString
    }

    private fun getInt(str: String?, iStart: Int, iWidth: Int): Int {
        val iLen = str!!.length
        return if (iLen < 1 || iLen < iStart || iLen < iStart + iWidth) {
            0
        } else str.substring(iStart, iStart + iWidth).toInt()
    }

    private fun checkStr(strDateTime: String): String? {
        return padChar(
            strDateTime.replace("-".toRegex(), XmlPullParser.NO_NAMESPACE).replace(
                MinimalPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR.toRegex(),
                XmlPullParser.NO_NAMESPACE
            ).replace(":".toRegex(), XmlPullParser.NO_NAMESPACE)
                .replace("/".toRegex(), XmlPullParser.NO_NAMESPACE)
                .trim { it <= ' ' },
            14,
            false
        )
    }

    private fun padChar(iInfo: Int, iWidth: Int): String? {
        val strInfo = iInfo.toString()
        val sbTmp = StringBuffer(strInfo)
        if (strInfo == null || strInfo.length > iWidth) {
            return strInfo
        }
        for (i in 0 until iWidth - strInfo.length) {
            sbTmp.insert(0, '0')
        }
        return sbTmp.toString()
    }

    private fun padChar(
        strInfo: String?,
        iWidth: Int,
        bAtBegin: Boolean
    ): String? {
        val sbTmp = StringBuffer(strInfo!!)
        if (strInfo == null || strInfo.length > iWidth) {
            return strInfo
        }
        for (i in 0 until iWidth - strInfo.length) {
            sbTmp.insert(if (bAtBegin) 0 else sbTmp.length, '0')
        }
        return sbTmp.toString()
    }

    companion object {
        /* renamed from: AM */
        const val f334AM = 0
        const val AM_PM = 9
        const val DATE = 5
        const val DAY_OF_MONTH = 5
        const val DAY_OF_WEEK = 7
        const val DAY_OF_WEEK_IN_MONTH = 8
        const val DAY_OF_YEAR = 6
        const val DST_OFFSET = 16
        const val ERA = 0
        const val HOUR = 10
        const val HOUR_OF_DAY = 11
        const val MILLISECOND = 14
        const val MILLIS_PER_DAY: Long = 86400000
        const val MILLIS_PER_HOUR: Long = 3600000
        const val MILLIS_PER_MINUTE: Long = 60000
        const val MINUTE = 12
        const val MONTH = 2
        const val SECOND = 13
        const val WEEK_OF_MONTH = 4
        const val WEEK_OF_YEAR = 3
        const val YEAR = 1
        const val ZONE_OFFSET = 15
        fun diffDays(strStartDate: String?, strEndDate: String?): Double {
            return (DateTime(strEndDate)
                .dateTime - DateTime(strStartDate)
                .dateTime).toDouble() / 8.64E7
        }

        fun diffDays(
            startDate: DateTime,
            endDate: DateTime
        ): Double {
            return (endDate.dateTime - startDate.dateTime).toDouble() / 8.64E7
        }

        fun diffHours(strStartDate: String?, strEndDate: String?): Double {
            return diffHours(
                DateTime(
                    strStartDate
                ), DateTime(strEndDate)
            )
        }

        fun diffHours(
            startDate: DateTime,
            endDate: DateTime
        ): Double {
            return (endDate.dateTime - startDate.dateTime).toDouble() * 1.0 / 3600000.0
        }

        fun diffMinutes(strStartDate: String?, strEndDate: String?): Double {
            return diffMinutes(
                DateTime(
                    strStartDate
                ), DateTime(strEndDate)
            )
        }

        fun diffMinutes(
            startDate: DateTime,
            endDate: DateTime
        ): Double {
            return (endDate.dateTime - startDate.dateTime).toDouble() * 1.0 / 60000.0
        }
    }
}