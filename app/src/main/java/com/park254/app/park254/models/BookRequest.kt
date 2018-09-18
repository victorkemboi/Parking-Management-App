package com.park254.app.park254.models

import java.util.*

data class BookRequest  constructor(
        var sarting : String = "",
        var ending : String = "",
        var parkinglotId : String = "",
        var carRegistration : String = ""
)