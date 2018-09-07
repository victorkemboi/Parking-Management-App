package com.park254.app.park254.models

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [User::class,Driver::class], version = 1, exportSchema = false)
abstract class Park254Database : RoomDatabase() {


   // abstract fun moodLogDao(): MoodLogDao



}
