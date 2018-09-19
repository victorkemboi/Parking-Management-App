package com.park254.app.park254.ui
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.park254.app.park254.App
import com.park254.app.park254.R
import com.park254.app.park254.ui.fragments.LotRegistrationStepOneFragment
import com.park254.app.park254.ui.repo.HomeViewModel
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import javax.inject.Inject

class UpdateInfoActivity : AppCompatActivity(), LotRegistrationStepOneFragment.OnFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    @Inject
    lateinit var homeViewModel: HomeViewModel

    @Inject
    lateinit var viewModel: ParkingLotRegistrationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_info)
        (application as App).applicationInjector.inject(this)
        viewModel.requestLot.name = homeViewModel.parsedLot!!.name
        viewModel.requestLot.streetName = homeViewModel.parsedLot!!.streetName
        viewModel.requestLot.email = homeViewModel.parsedLot!!.email
        viewModel.requestLot.contactNumber = homeViewModel.parsedLot!!.contactNumber
        viewModel.requestLot.paybillNumber = homeViewModel.parsedLot!!.paybillNumber
        viewModel.requestLot.parkingSpaces = homeViewModel.parsedLot!!.parkingSpaces
        viewModel.requestLot.latitude = homeViewModel.parsedLot!!.latitude
        viewModel.requestLot.longitude = homeViewModel.parsedLot!!.longitude

        var fragment: Fragment? = null
        val fragmentClass: Class<*> =   LotRegistrationStepOneFragment::class.java
        fragment = fragmentClass.newInstance() as Fragment
        val fragmentManager = supportFragmentManager
        fragmentManager.beginTransaction().replace(R.id.updateInfoFragment, fragment).commit()
    }
}
