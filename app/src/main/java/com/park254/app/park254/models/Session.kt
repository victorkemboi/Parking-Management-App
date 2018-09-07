package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*

@Entity(tableName = "session")
@TypeConverters(DateTypeConverter::class)
data class Session(
        @ColumnInfo(name = "userId")
        var userId: String = "",

        @ColumnInfo(name = "lotId")
        var lotId: String = "",

        @ColumnInfo(name = "rating")
        var rating: Int = 0,

        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "start")
        var start: Date = Date(),

        @ColumnInfo(name = "end")
        var end: Date = Date(),

        @ColumnInfo(name = "cost")
        var cost: Double = 0.0,

        @ColumnInfo(name = "paidOn")
        var paidOn: Date = Date() )
{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
