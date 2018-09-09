package com.park254.app.park254.di

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.park254.app.park254.App
import com.park254.app.park254.ui.repo.LoginViewModel
import com.park254.app.park254.ui.repo.ParkingLotRegistrationViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelModule {
    @Singleton
    @Provides
    internal fun providesLoginViewModel(firebaseAuth:FirebaseAuth): LoginViewModel =
            LoginViewModel(firebaseAuth)

    @Singleton
    @Provides
    internal fun providesParkingLotRegistrationViewModel(): ParkingLotRegistrationViewModel =
            ParkingLotRegistrationViewModel()

}