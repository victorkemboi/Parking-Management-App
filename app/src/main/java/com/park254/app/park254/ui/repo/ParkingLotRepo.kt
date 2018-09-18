package com.park254.app.park254.ui.repo

import android.arch.lifecycle.LiveData
import com.park254.app.park254.App
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.dao.LotDao
import com.park254.app.park254.network.RetrofitApiService
import javax.inject.Inject

class ParkingLotRepo @Inject
constructor(private val lotDao: LotDao, val retrofitApiService: RetrofitApiService, val app: App)  {
   val lots: LiveData<List<Lot>>
        get() = lotDao.lots



    fun insert(lot: Lot): Long {
        return lotDao.insertLot(lot)
    }

    fun updateLot(lot: Lot) {
        lotDao.updateLot(lot)
    }

}