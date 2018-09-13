package com.park254.app.park254.di

import dagger.Provides
import javax.inject.Singleton
import android.arch.persistence.room.Room
import android.app.Application
import android.content.Context
import com.park254.app.park254.App
import com.park254.app.park254.models.Park254Database
import com.park254.app.park254.models.dao.UserDao
import com.park254.app.park254.utils.UtilityClass
import dagger.Module
import javax.inject.Inject

@Module
class RoomModule(val app:Context)
{


    @Singleton
    @Provides
    internal fun providesRoomDatabase(): Park254Database {
        return Room.databaseBuilder(app, Park254Database::class.java, UtilityClass.DATABASE_NAME)
                .build()
    }



}