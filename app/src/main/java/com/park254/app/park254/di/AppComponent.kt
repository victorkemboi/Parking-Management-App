package com.park254.app.park254.di

import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App
import com.park254.app.park254.models.Park254Database
import com.park254.app.park254.models.dao.UserDao
import com.park254.app.park254.network.RetrofitApiService
import com.park254.app.park254.ui.AddUserInfoActivity
import com.park254.app.park254.ui.HomeActivity
import com.park254.app.park254.ui.LoginActivity
import com.park254.app.park254.ui.ParkingLotRegistrationActivity
import com.park254.app.park254.ui.fragments.LotRegistrationStepOneFragment
import com.park254.app.park254.ui.fragments.LotRegistrationStepThreeFragment
import com.park254.app.park254.ui.fragments.LotRegistrationStepTwoFragment
import com.park254.app.park254.ui.repo.HomeViewModel
import com.park254.app.park254.ui.repo.LoginViewModel
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import dagger.Component
import javax.inject.Singleton
import dagger.android.support.AndroidSupportInjectionModule
import retrofit2.Retrofit


@Singleton
@Component(modules = [(AppModule::class), (RoomModule::class), (NetModule::class),
   (FirebaseModule::class),(ViewModelModule::class), (AndroidSupportInjectionModule::class),  (DaoModule::class)])
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

   fun provideApplication(): App

    fun provideFirebaseAuth(): FirebaseAuth

    fun providesLoginViewModel(): LoginViewModel

    fun providesParkingLotRegistrationViewModel(): ParkingLotRegistrationViewModel

    fun providesHomeViewModel(): HomeViewModel

    fun providesNetworkModule(): RetrofitApiService

    fun providesLocalDb(): Park254Database

    fun providesUserDao(): UserDao

}