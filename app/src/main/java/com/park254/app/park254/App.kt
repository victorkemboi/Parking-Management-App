package com.park254.app.park254

import android.app.Application
import com.park254.app.park254.di.*
import com.park254.app.park254.utils.UtilityClass
import javax.inject.Singleton


class App :Application() {

    lateinit var applicationInjector: AppComponent

    override fun onCreate() {
        super.onCreate()

        applicationInjector = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .roomModule(RoomModule(this))
                .netModule(NetModule(UtilityClass.BASE_URL))
                .build()


        applicationInjector.provideApplication()

    }

}


