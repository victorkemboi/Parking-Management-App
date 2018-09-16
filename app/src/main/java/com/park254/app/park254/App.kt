package com.park254.app.park254

import android.app.Application
import com.facebook.stetho.Stetho
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import com.park254.app.park254.di.*
import com.park254.app.park254.utils.UtilityClass


class App :Application() {

    lateinit var applicationInjector: AppComponent

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        applicationInjector = DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .roomModule(RoomModule(this))
                .netModule(NetModule(UtilityClass.BASE_URL, this))
                .build()


        applicationInjector.provideApplication()




}

}


