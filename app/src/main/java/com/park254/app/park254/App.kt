package com.park254.app.park254

import android.app.Activity
import android.app.Application
import android.util.Log
import com.park254.app.park254.di.AppComponent
import com.park254.app.park254.di.AppModule
import com.park254.app.park254.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject
import javax.inject.Singleton


class App :Application() {


    //lateinit var daggerAppComponent: AppComponent




    override fun onCreate() {
        super.onCreate()
       applicationInjector.provideApplication()



    }

    @Singleton
    companion object {
         val applicationInjector = DaggerAppComponent.builder()
                 .appModule(AppModule(App()))
                 .build()!!

    }



    //override fun applicationInjector() = applicationInjector




}


