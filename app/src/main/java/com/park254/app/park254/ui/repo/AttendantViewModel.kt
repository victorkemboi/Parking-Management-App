package com.park254.app.park254.ui.repo

import android.arch.lifecycle.ViewModel
import com.park254.app.park254.models.Employee
import com.park254.app.park254.models.LotResponse
import com.park254.app.park254.models.User
import javax.inject.Inject

class AttendantViewModel @Inject
constructor( ) : ViewModel() {

    var user: User = User()
    var employee: Employee = Employee()
    var lotResponse:LotResponse = LotResponse()

}