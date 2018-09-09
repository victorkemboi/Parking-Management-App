package com.park254.app.park254.ui.repo

import android.arch.lifecycle.ViewModel
import javax.inject.Inject

class ParkingLotRegistrationViewModel @Inject
constructor( ) : ViewModel() {

    var MAX_STEP = 3

    var current_step  = 1

}