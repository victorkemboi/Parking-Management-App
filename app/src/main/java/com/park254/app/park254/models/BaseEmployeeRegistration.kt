package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*


@Entity(tableName = "base_employee_registration")
@TypeConverters(DateTypeConverter::class)
data class BaseEmployeeRegistration(
        @ColumnInfo(name = "userId")
        var userId: String = "",

        @ColumnInfo(name = "lotId")
        var lotId: String = "",

        @ColumnInfo(name = "designation")
        var designation: String = "",

        @ColumnInfo(name = "commencingOn")
        var commencingOn: String = "",

        @ColumnInfo(name = "expiresOn")
        var expiresOn: String = "")

{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
