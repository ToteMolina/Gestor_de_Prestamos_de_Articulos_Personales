package com.example.appprestamos.Conversiones;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    // de long a date
    @TypeConverter
    public static Date fromTimestamp(Long value){
        return value == null ? null : new Date(value);
    }
    // de date a long
    @TypeConverter
    public static Long dateToTimestamp(Date date){
        return date == null ? null : date.getTime();
    }
}
