package com.mib.lib_api.converter

import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type
import java.util.Date

class QueryConverterFactory : Converter.Factory() {
    override fun stringConverter(
        type: Type,
        annotations: Array<Annotation?>?,
        retrofit: Retrofit?
    ): Converter<*, String>? {
        return if (type === Date::class.java) {
            return DateQueryConverter.instance
        } else null
    }

    companion object {
        fun create(): QueryConverterFactory {
            return QueryConverterFactory()
        }
    }
}