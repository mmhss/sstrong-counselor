package com.hsd.avh.standstrong.data

import androidx.room.TypeConverter
import java.util.*

/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {
    @TypeConverter fun calendarToDatestamp(calendar: Calendar): Long = calendar.timeInMillis

    @TypeConverter fun datestampToCalendar(value: Long): Calendar =
            Calendar.getInstance().apply { timeInMillis = value }

    @TypeConverter fun fromTimestamp(value: Long?): Date? {
        return if (value == null) null else Date(value)
    }

    @TypeConverter fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}