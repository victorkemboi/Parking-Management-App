package com.park254.app.park254.ui.repo

import android.arch.lifecycle.ViewModel
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.LotResponse
import java.util.ArrayList
import javax.inject.Inject

class HomeViewModel @Inject
constructor( ) : ViewModel() {

    var address = ""
    var longitude = 0.0
    var latitude  = 0.0

    var parsedLot:LotResponse? = null

}