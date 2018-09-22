package com.park254.app.park254.models

import java.util.*

data class BookRequest  constructor(
        var starting : String = "",
        var ending : String = "",
        var lotId : String = "",
        var carRegistration : String = ""
)