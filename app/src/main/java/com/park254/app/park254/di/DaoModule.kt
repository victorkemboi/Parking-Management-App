package com.park254.app.park254.di

import com.park254.app.park254.models.Park254Database
import com.park254.app.park254.models.dao.UserDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DaoModule {
    @Singleton
    @Provides
    internal fun providesUserDao(db: Park254Database): UserDao {
        return db.userDao()
    }

}