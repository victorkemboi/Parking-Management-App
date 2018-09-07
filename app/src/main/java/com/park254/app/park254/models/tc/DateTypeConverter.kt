package com.selfmate.mes.selfmate.models.tc

import android.arch.persistence.room.TypeConverter
import java.util.*

/**
 * Created by vicki_mes on 11/16/2017.
 */


class DateTypeConverter {

    @TypeConverter
    fun toDate(timestamp: Long?): Date? = if (timestamp == null) null else Date(timestamp)

    @TypeConverter
    fun toTimestamp(date: Date?): Long? = date?.time
}

