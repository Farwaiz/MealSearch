package com.example.cw2

import androidx.room.TypeConverter

class Converter {

    @TypeConverter
    fun fromString(string: String?): List<String>? {
        return string?.split(",")
    }

    @TypeConverter
    fun toString(list: List<String>?): String? {
        return list?.joinToString(",")
    }
}