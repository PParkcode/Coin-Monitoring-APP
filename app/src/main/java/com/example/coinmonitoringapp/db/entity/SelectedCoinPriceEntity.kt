package com.example.coinmonitoringapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date


@Entity(tableName = "selected_coin_price_table")
data class SelectedCoinPriceEntity(

    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val coinName:String,
    val transaction_date: String,
    val type: String,
    val units_traded: String,
    val price: String,
    val total: String,
    val timestamp: Date

)

class DateConverters {
    @TypeConverter
    fun fromTimeStamp(value : Long) : Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimeStamp(date:Date) :Long {
        return date.time
    }
}