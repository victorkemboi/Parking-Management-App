package com.park254.app.park254.models.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.park254.app.park254.models.Lot


@Dao
interface LotDao {

    @Insert
    fun insertLot(lot: Lot) : Long

    @Update
    fun updateLot(lot: Lot)

    @get:Query("Select * from requestLot")
    val lots: LiveData<List<Lot>>


}