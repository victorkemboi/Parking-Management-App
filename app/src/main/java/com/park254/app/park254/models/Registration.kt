package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*


@Entity(tableName = "registration")
@TypeConverters(DateTypeConverter::class)
data class Registration(
        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "userId")
        var userId: String = "",

        @ColumnInfo(name = "lotId")
        var parkinglotId: String = "",

        @ColumnInfo(name = "designation")
        var designation: String = "",

        @ColumnInfo(name = "commencingOn")
        var commencingOn: Date = Date(),

        @ColumnInfo(name = "expiresOn")
        var expiresOn: Date = Date())
{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
