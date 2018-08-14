package com.park254.app.park254

import android.app.Application
import com.park254.app.park254.di.AppComponent
import com.park254.app.park254.di.AppModule
import com.park254.app.park254.di.DaggerAppComponent

class App : Application() {

    lateinit var park254Component: AppComponent

    private fun initDagger2(app: App): AppComponent =
            DaggerAppComponent.builder()
                    .appModule(AppModule(app))
                    .build()

    override fun onCreate() {
        super.onCreate()
        park254Component = initDagger2(this)
    }
}