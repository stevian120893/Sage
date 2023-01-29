package com.mib.lib_util

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.util.*

object GsonUtils {

    /**
     * Gson that consider timezone to convert date object
     * use this if you need to convert the date from or to UTC + 0 (usually to communicate with server)
     * e.g :
     * AnObject(aDate=Tue Mar 29 15:12:36 WIB 2022) -> {"aDate":"2022-03-29T08:12:36Z"}
     * {"aDate":"2021-12-16T08:03:23Z"} -> AnObject(aDate=Thu Dec 16 15:03:23 WIB 2021)
     */
    val GSON_WITH_UTC: Gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, GsonDateFormatAdapter(DateConstant.UTC_FORMAT))
        .create()

    /**
     * Gson that doesn't consider timezone to convert date object
     * use this for saving to local database, or data that will be better using local timezone
     * e.g :
     * AnObject(aDate=Tue Mar 29 15:12:36 WIB 2022) -> {"aDate":"2022-03-29T15:12:36Z"}
     * {"aDate":"2021-12-16T08:03:23Z"} -> AnObject(aDate=Thu Dec 16 08:03:23 WIB 2021)
     */
    val GSON: Gson = GsonBuilder()
        .setDateFormat(DateConstant.UTC_FORMAT).create()

    inline fun <reified T> toJson(source: T): String = GSON.toJson(source, object : TypeToken<T>() {}.type)

    fun <T> toJson(source: T, cls: Class<T>): String = GSON.toJson(source, cls)

    inline fun <reified T> fromJson(json: String?): T = GSON.fromJson(json, object : TypeToken<T>() {}.type)

    fun <T> fromJson(json: String?, cls: Class<T>): T = GSON.fromJson(json, cls)

    inline fun <reified T> toJsonWithUtc(source: T): String = GSON_WITH_UTC.toJson(source, object : TypeToken<T>() {}.type)

    fun <T> toJsonWithUtc(source: T, cls: Class<T>): String = GSON_WITH_UTC.toJson(source, cls)

    inline fun <reified T> fromJsonWithUtc(json: String?): T = GSON_WITH_UTC.fromJson(json, object : TypeToken<T>() {}.type)

    fun <T> fromJsonWithUtc(json: String?, cls: Class<T>): T = GSON_WITH_UTC.fromJson(json, cls)
}