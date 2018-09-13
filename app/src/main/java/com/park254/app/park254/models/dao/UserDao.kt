package com.park254.app.park254.models.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.park254.app.park254.models.User

@Dao
interface UserDao {

    @Insert
    abstract fun insertUser(user: User)

    @Update
    abstract fun updateUser(user: User)


}