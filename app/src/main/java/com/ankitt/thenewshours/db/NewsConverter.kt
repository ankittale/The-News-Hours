package com.ankitt.thenewshours.db

import androidx.room.TypeConverter
import com.ankitt.thenewshours.model.Source

class NewsConverter {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(name, name)
    }
}