package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*

@Entity(tableName = "rate")
@TypeConverters(DateTypeConverter::class)
data class Rate(
        @ColumnInfo(name = "lotId")
        var parkingLotId: String = "",

        @ColumnInfo(name = "minimumTime")
        var minimumTime: Int = 0,

        @ColumnInfo(name = "maximumTime")
        var maximumTime: Int = 0,

        @ColumnInfo(name = "id")
        var id: String = "",


        @ColumnInfo(name = "cost")
        var cost: Double = 0.0 )
{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}