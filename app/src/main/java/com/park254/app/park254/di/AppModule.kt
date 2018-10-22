package com.park254.app.park254.di

import android.app.Application
import android.content.Context
import com.park254.app.park254.App
import com.park254.app.park254.di.qualifier.ApplicationContext
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import javax.inject.Singleton

@Module
class AppModule(private val app: App) {



    @Provides
    @Singleton
    fun provideApplication(): App = app


}