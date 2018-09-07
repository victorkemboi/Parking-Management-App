package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverters
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*

@Entity(tableName = "fcm_registration")
@TypeConverters(DateTypeConverter::class)
data class FcmRegistration(
        @ColumnInfo(name = "registrationToken")
        var registrationToken: String = "",

        @ColumnInfo(name = "deviceId")
        var deviceId: String = "",

        @ColumnInfo(name = "osVersion")
        var osVersion: String = "",

        @ColumnInfo(name = "deviceName")
        var deviceName: String = "",

        @ColumnInfo(name = "userId")
        var userId: String = "")

{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
