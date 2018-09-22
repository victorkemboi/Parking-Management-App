package com.park254.app.park254.di

import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App
import com.park254.app.park254.models.Park254Database
import com.park254.app.park254.models.dao.UserDao
import com.park254.app.park254.network.FirebaseUserIdTokenInterceptor
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.*
import com.park254.app.park254.ui.adapters.*
import com.park254.app.park254.ui.fragments.*
import com.park254.app.park254.ui.repo.*
import com.park254.app.park254.utils.SharedPrefs
import dagger.Component
import javax.inject.Singleton
import dagger.android.support.AndroidSupportInjectionModule
import retrofit2.Retrofit


@Singleton
@Component(modules = [(AppModule::class), (RoomModule::class), (NetModule::class),
   (FirebaseModule::class),(ViewModelModule::class), (AndroidSupportInjectionModule::class),  (DaoModule::class), (ThreadModule::class)])
interface AppComponent{

/*
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun build(): AppComponent
    }

    */

    fun inject(loginActivity: LoginActivity)
     fun inject(addUserInfoActivity: AddUserInfoActivity)
    fun inject(homeActivity: HomeActivity)
    fun inject(parkingLotRegistrationActivity: ParkingLotRegistrationActivity)
    fun inject(lotRegistrationStepOneFragment: LotRegistrationStepOneFragment)
    fun inject(lotRegistrationStepTwoFragment: LotRegistrationStepTwoFragment)
    fun inject(lotRegistrationStepThreeFragment: LotRegistrationStepThreeFragment)
    fun inject(lotInfoActivity: LotInfoActivity)
    fun inject(ownerLotInfoActivity: OwnerLotInfoActivity)
    fun inject(updateInfoActivity: UpdateInfoActivity)
    fun inject(employeeActivity: EmployeeActivity)
    fun inject(registerEmployeeActivity: RegisterEmployeeActivity)
    fun inject(bookingsListAdapter: BookingsListAdapter)
    fun inject(bookingsActivity: BookingsActivity)
    fun inject(homeListAdapter: HomeListAdapter)
    fun inject(employeeListAdapter: EmployeeListAdapter)
    fun inject(ownerListAdapter: OwnerListAdapter)
    fun inject(splashActivity: SplashActivity)
    fun inject(mainHomeFragment: MainHomeFragment)
    fun inject(ownerFragment: OwnerFragment)
    fun inject(attendantFragment: AttendantFragment)
    fun inject(paymentsListAdapter:PaymentsListAdapter)
    fun inject(paymentsActivity: PaymentsActivity)

   fun provideApplication(): App

    fun provideFirebaseAuth(): FirebaseAuth

    fun providesLoginViewModel(): LoginViewModel

    fun providesParkingLotRegistrationViewModel(): ParkingLotRegistrationViewModel

    fun providesHomeViewModel(): HomeViewModel

    fun providesEmployeeViewModel(): EmployeeViewModel

    fun providesLotInfoViewModel(): LotInfoViewModel

    fun providesParkingLotViewModel(): ParkingLotViewModel

    fun providesNetworkModule(): RetrofitApiService

    fun providesSettings(): SharedPrefs

    fun providesLocalDb(): Park254Database

    fun providesUserDao(): UserDao

}