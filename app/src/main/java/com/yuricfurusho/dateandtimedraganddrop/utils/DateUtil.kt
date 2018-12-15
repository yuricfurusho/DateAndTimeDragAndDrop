package com.yuricfurusho.dateandtimedraganddrop.utils

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Yuri Furusho on 15/12/18.
 */
class DateUtil {
    companion object {
        const val DEFAULT_DATE_FORMAT = "YYYY-MM-dd HH:mm:ss.SSSSSS"

        fun getCurrentDateTime(format: String = DEFAULT_DATE_FORMAT, locale: Locale = Locale.getDefault()): String {
            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat(format, locale)
            return formatter.format(date)
        }
    }
}