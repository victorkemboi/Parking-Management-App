package com.park254.app.park254

import android.app.Activity
import android.app.Application
import android.support.v4.app.Fragment
import android.support.v4.app.ListFragment
import android.support.v7.widget.RecyclerView
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.park254.app.park254.di.*
import com.park254.app.park254.utils.UtilityClass
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import io.fabric.sdk.android.Fabric
import javax.inject.Inject

class App : Application() , HasActivityInjector , HasSupportFragmentInjector{


    @Inject
    lateinit var  dispatchingActivityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var  dispatchingFragmentInjector: DispatchingAndroidInjector<Fragment>

    lateinit var applicationInjector: AppComponent

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)
        Fabric.with(this, Crashlytics())
        applicationInjector =  DaggerAppComponent.builder()
                .appModule(AppModule(this))
                .roomModule(RoomModule(this))
                .netModule(NetModule(UtilityClass.BASE_URL, this))
                .build()



       applicationInjector.inject(this)

    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return dispatchingActivityInjector
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
       return dispatchingFragmentInjector
    }




}


