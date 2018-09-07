package com.park254.app.park254.di

import dagger.Provides
import javax.inject.Singleton
import android.arch.persistence.room.Room
import android.app.Application
import com.park254.app.park254.App
import com.park254.app.park254.models.Park254Database
import dagger.Module


@Module
class RoomModule(park254App: App) {

    private val park254Database: Park254Database

    init {
        park254Database = Room.databaseBuilder(park254App, Park254Database::class.java, DATABASE_NAME)
                .build()
    }

    companion object {
        private const val DATABASE_NAME = "park254DB"
    }

    @Singleton
    @Provides
    internal fun providesRoomDatabase(): Park254Database {
        return park254Database
    }
/*
    @Singleton
    @Provides
    internal fun providesProductDao(demoDatabase: DemoDatabase): ProductDao {
        return demoDatabase.getProductDao()
    }

    @Singleton
    @Provides
    internal fun productRepository(productDao: ProductDao): ProductRepository {
        return ProductDataSource(productDao)
    }
    */

}