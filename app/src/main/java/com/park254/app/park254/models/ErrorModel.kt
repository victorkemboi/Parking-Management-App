package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*


@Entity(tableName = "error_model")
@TypeConverters(DateTypeConverter::class)
data class ErrorModel(
        @ColumnInfo(name = "code")
        var code: String = "",

        @ColumnInfo(name = "description")
        var description: String = "")

{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
