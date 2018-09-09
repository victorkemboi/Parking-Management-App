package com.park254.app.park254.di

import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App
import com.park254.app.park254.ui.AddUserInfoActivity
import com.park254.app.park254.ui.LoginActivity
import com.park254.app.park254.ui.ParkingLotRegistrationActivity
import com.park254.app.park254.ui.repo.LoginViewModel
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import dagger.Component
import javax.inject.Singleton
import dagger.android.support.AndroidSupportInjectionModule




@Singleton
@Component(modules = [(AppModule::class), (RoomModule::class), (NetModule::class),
   (FirebaseModule::class),(ViewModelModule::class), (AndroidSupportInjectionModule::class)])
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
    fun inject(parkingLotRegistrationActivity: ParkingLotRegistrationActivity)

   fun provideApplication(): App

    fun provideFirebaseAuth(): FirebaseAuth

    fun providesLoginViewModel(): LoginViewModel

    fun providesParkingLotRegistrationViewModel(): ParkingLotRegistrationViewModel

}