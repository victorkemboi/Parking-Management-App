package com.park254.app.park254.di

import com.park254.app.park254.ui.*
import com.park254.app.park254.ui.fragments.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity (): HomeActivity

    @ContributesAndroidInjector
    abstract fun contributeAddUserIfoActivity (): AddUserInfoActivity

    @ContributesAndroidInjector
    abstract fun contributeBookingsActivity (): BookingsActivity

    @ContributesAndroidInjector
    abstract fun contributeEmployeeActivity (): EmployeeActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity (): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeLotInfoActivity (): LotInfoActivity

    @ContributesAndroidInjector
    abstract fun contributeOwnerLotInfoActivity (): OwnerLotInfoActivity

    @ContributesAndroidInjector
    abstract fun contributeParkingLotRegistrationActivity (): ParkingLotRegistrationActivity

    @ContributesAndroidInjector
    abstract fun contributePaymentsActivity (): PaymentsActivity

    @ContributesAndroidInjector
    abstract fun contributePaymentVerificationActivity (): PaymentVerificationActivity

    @ContributesAndroidInjector
    abstract fun contributeRegisterEmployeeActivity (): RegisterEmployeeActivity

    @ContributesAndroidInjector
    abstract fun contributeSplashActivity (): SplashActivity

    @ContributesAndroidInjector
    abstract fun contributeUpdateInfoActivity (): UpdateInfoActivity

    @ContributesAndroidInjector
    abstract fun contributeAttendantFragment (): AttendantFragment

    @ContributesAndroidInjector
    abstract fun contributeHomeFragment (): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeLotRegStepOneFragment (): LotRegistrationStepOneFragment

    @ContributesAndroidInjector
    abstract fun contributeLotRegStepTwoFragment (): LotRegistrationStepTwoFragment

    @ContributesAndroidInjector
    abstract fun contributeLotRegStepThreeFragment (): LotRegistrationStepThreeFragment

    @ContributesAndroidInjector
    abstract fun contributeMyPlacesFragment (): MyPlacesFragment

    @ContributesAndroidInjector
    abstract fun contributeOwnerFragment (): OwnerFragment

    @ContributesAndroidInjector
    abstract fun contributePaymentVerificationSuccessFragment (): PaymentVerificationSuccessFragment


}