package com.mib.lib_api.converter

import com.mib.lib_util.DateConstant
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import retrofit2.Converter

class DateQueryConverter :
    Converter<Date, String> {
    override fun convert(date: Date): String {
        return dateFormat.get()?.format(date) ?: ""
    }

    companion object {
        val instance = DateQueryConverter()
        private val dateFormat: ThreadLocal<DateFormat> = object : ThreadLocal<DateFormat>() {
            public override fun initialValue(): DateFormat {
                return SimpleDateFormat(DateConstant.UTC_FORMAT, Locale.getDefault()).also {
                    it.timeZone = TimeZone.getTimeZone("GMT")
                }
            }
        }
    }
}