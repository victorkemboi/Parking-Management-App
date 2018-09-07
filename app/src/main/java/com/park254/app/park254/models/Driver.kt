package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.selfmate.mes.selfmate.models.tc.DateTypeConverter
import java.util.*
@TypeConverters(DateTypeConverter::class)
@Entity(tableName = "driver")
data class Driver(

        @ColumnInfo(name = "firstName")
        var firstName: String = "",

        @ColumnInfo(name = "lastName")
        var lastName: String = "",

        @ColumnInfo(name = "dob")
        var dob: Date = Date())
{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
