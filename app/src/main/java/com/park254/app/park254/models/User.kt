package com.park254.app.park254.models

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.*

@Entity(tableName = "user")
data class User(
        @ColumnInfo(name = "userId")
        var userId: String = "",

        @ColumnInfo(name = "userName")
        var userName: String = "",
        @ColumnInfo(name = "password")
        var password: String = "")
{
    @PrimaryKey(autoGenerate = true)
    var uid: Long? = null
}
