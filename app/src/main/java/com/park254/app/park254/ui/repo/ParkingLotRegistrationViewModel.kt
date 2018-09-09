package com.park254.app.park254.ui.repo

import android.arch.lifecycle.ViewModel
import android.support.v4.app.Fragment
import com.park254.app.park254.models.Lot
import com.park254.app.park254.ui.RegistrationInterface
import javax.inject.Inject

class ParkingLotRegistrationViewModel @Inject
constructor( ) : ViewModel() {

    var MAX_STEP = 3

    var current_step  = 1
    var previous_step = 0
   val  MAP_BUTTON_REQUEST_CODE  = 305

    val lot: Lot = Lot()
    lateinit var registrationInterface: RegistrationInterface

    lateinit var visible_fragment: Fragment

    fun  setRegInterface(regInterface: RegistrationInterface){
        this.registrationInterface = regInterface
    }

}