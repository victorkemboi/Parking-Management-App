package com.park254.app.park254.models

import android.arch.persistence.room.*
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*
import kotlin.collections.ArrayList

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
        var joinedOn: String = "",

        @ColumnInfo(name = "longitude")
        var longitude: Double = 0.0,

        @ColumnInfo(name = "name")
        var name: String = "",

        @ColumnInfo(name = "parkingSpaces")
        var parkingSpaces: Int =  0,

        @ColumnInfo(name = "paybillNumber")
        var paybillNumber: String = "",

        @ColumnInfo(name = "contactNumber")
        var contactNumber: String =  "",

        @ColumnInfo(name = "email")
        var email: String =  "",

        @ColumnInfo(name = "rating")
        var rating: Int =  0
        ,
        @ColumnInfo(name = "streetName")
        var streetName: String = "",

        @Ignore
        @ColumnInfo(name = "parkingLotPhotos")
        var parkingLotPhotos: ArrayList<LotImage> = ArrayList(),
        @Ignore
        @ColumnInfo(name = "parkingRates")
        var parkingRates: ArrayList<Rate> = ArrayList())
{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
