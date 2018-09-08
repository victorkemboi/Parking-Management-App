package com.park254.app.park254

import android.app.Application
import com.park254.app.park254.di.AppModule
import com.park254.app.park254.di.DaggerAppComponent
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


