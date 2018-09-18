package com.park254.app.park254.ui.repo

import android.arch.lifecycle.ViewModel
import android.net.Uri
import com.park254.app.park254.models.Lot
import com.park254.app.park254.models.Photo
import com.park254.app.park254.models.Rate
import javax.inject.Inject

class ParkingLotRegistrationViewModel @Inject
constructor( ) : ViewModel() {

    var MAX_STEP = 3

    var current_step  = 1
    var previous_step = 0
    var addresss = ""

    var lot: Lot = Lot()
    var rate1: Rate = Rate()
    var rate2: Rate = Rate()
    var rate3: Rate = Rate()
    var rate4: Rate = Rate()
    var rate5: Rate = Rate()

    var imageOneUri : Uri? = null
    var imageTwoUri : Uri? = null
    var imageThreeUri : Uri? = null

    var imageOneLabel = ""
    var imageTwoLabel = ""
    var imageThreeLabel = ""
    var photo:Photo = Photo()

}