package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*


@Entity(tableName = "photo")
@TypeConverters(DateTypeConverter::class)
data class Photo(
        @ColumnInfo(name = "parkingLotImageBase64Encoded")
        var parkingLotImageBase64Encoded: String = "",

        @ColumnInfo(name = "label")
        var label: String = "",

        @ColumnInfo(name = "parkingLotId")
        var parkingLotId: String = "",

        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "addedOn")
        var addedOn: Date = Date() )
{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}