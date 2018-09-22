package com.park254.app.park254.models

data class AvailableSpaceResponse
constructor(
        var lotId: String = "",
        var userName: String = "",
        var reportedAt: String = "",
        var reportedBy: String = "",
        var available: Int = 0

)