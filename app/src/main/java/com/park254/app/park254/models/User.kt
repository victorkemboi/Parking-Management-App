package com.park254.app.park254.models

import android.arch.persistence.room.*
import com.park254.app.park254.models.tc.DateTypeConverter
import java.util.*

@Entity(tableName = "userProfile")
@TypeConverters(DateTypeConverter::class)
data class User(
        @ColumnInfo(name = "addedOn")
        var addedOn: Date = Date(),

        @ColumnInfo(name = "gender")
        var gender: String = "",

        @ColumnInfo(name = "phoneNumber")
        var phoneNumber: String = "",

        @PrimaryKey(autoGenerate = false)
        @ColumnInfo(name = "id")
        var id: String = "",

        @ColumnInfo(name = "name")
        var userName: String = "",

        @ColumnInfo(name = "email")
        var email: String = "")
