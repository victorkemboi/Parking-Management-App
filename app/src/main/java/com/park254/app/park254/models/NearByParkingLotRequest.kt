package com.park254.app.park254.models

data class NearByParkingLotRequest constructor(
        var  latitude: Double = 0.0,
        var longitude: Double = 0.0,
        var count: Int = 0

)