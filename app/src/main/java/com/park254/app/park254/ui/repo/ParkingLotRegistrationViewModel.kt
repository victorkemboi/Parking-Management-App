package com.park254.app.park254.ui.repo

import android.arch.lifecycle.ViewModel
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.Rate
import javax.inject.Inject

class ParkingLotRegistrationViewModel @Inject
constructor( ) : ViewModel() {

    var MAX_STEP = 3

    var current_step  = 1
    var previous_step = 0
    var addresss = ""

    val lot: Lot = Lot()
    val rate: Rate = Rate()

}