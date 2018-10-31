package com.park254.app.park254.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors
import javax.inject.Singleton


@Module
class ThreadModule {
    @Singleton
    @Provides
    internal fun provideThreadPool(): ExecutorCoroutineDispatcher = Executors.newCachedThreadPool().asCoroutineDispatcher()


    @Singleton
    @Provides
    internal fun provideJob(): Job = Job()

}

