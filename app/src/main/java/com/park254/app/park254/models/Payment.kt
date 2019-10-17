package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*

@Entity(tableName = "payment")
@TypeConverters(DateTypeConverter::class)
data class   Payment(
        @ColumnInfo(name = "userId")
        var userId: String = "",

        @ColumnInfo(name = "amount")
        var amount: String = "",

        @ColumnInfo(name = "receivedOn")
        var receivedOn: String = "",

        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "paymentReference")
        var paymentReference: String = "",

        @ColumnInfo(name = "paidBy")
        var paidBy: String = "" ,

        @ColumnInfo(name = "currency")
        var currency: String = "" ,

        @ColumnInfo(name = "lotId")
        var lotId: String = "",
        @ColumnInfo(name = "bookingId")
        var bookingId: String = "")

{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
