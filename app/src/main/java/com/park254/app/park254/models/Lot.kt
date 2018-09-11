package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*

@Entity(tableName = "lot")
@TypeConverters(DateTypeConverter::class)
data class Lot(
        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "latitude")
        var latitude: Double = 0.0,

        @ColumnInfo(name = "ownerId")
        var ownerId: String = "",

        @ColumnInfo(name = "joinedOn")
        var joinedOn: Date = Date(),

        @ColumnInfo(name = "longitude")
        var longitude: Double = 0.0,

        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "parkingSpaces")
        var parkingSpaces: Int =  0,

        @ColumnInfo(name = "paybillNumber")
        var paybillNumber: String = "",

        @ColumnInfo(name = "contactNumber")
        var contactNumber: Int =  0,

        @ColumnInfo(name = "email")
        var email: String =  "",

        @ColumnInfo(name = "rating")
        var rating: Int =  0
        ,
        @ColumnInfo(name = "streetName")
        var streetName: String = "")
{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
