package com.park254.app.park254.ui.repo

import android.arch.lifecycle.ViewModel
import com.park254.app.park254.models.BookRequest
import java.util.*
import javax.inject.Inject

class LotInfoViewModel @Inject
constructor( ) : ViewModel() {


    var bookRequest: BookRequest = BookRequest()
    val  checkInDate : Calendar = Calendar.getInstance()
    val  checkOutDate : Calendar = Calendar.getInstance()

}