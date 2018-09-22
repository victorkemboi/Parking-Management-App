package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*

@Entity(tableName = "booking")
@TypeConverters(DateTypeConverter::class)
data class Booking(
        @ColumnInfo(name = "userId")
        var userId: String = "",

        @ColumnInfo(name = "lotId")
        var lotId: String = "",

        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "starting")
        var starting: String = "",

        @ColumnInfo(name = "ending")
        var ending: String = "",

        @ColumnInfo(name = "cost")
        var cost: Double =  0.0,

        @ColumnInfo(name = "carRegistration")
        var carRegistration: String = "",
        @ColumnInfo(name = "bookedOn")
        var bookedOn: String = "")

{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
