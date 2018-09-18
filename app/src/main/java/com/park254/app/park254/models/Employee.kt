package com.park254.app.park254.models

import java.util.*

data class Employee constructor(
        var userId : String = "",
        var id : String = "",
        var parkinglotId : String = "",
        var commencingOn : String = "",
        var designation : String  = "Owner",
        var expiresOn : String = ""
)