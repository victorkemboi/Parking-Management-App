package com.park254.app.park254.models

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [Driver::class, BaseEmployeeRegistration::class,
                        Booking::class, ErrorModel::class, FcmRegistration::class
                    ,Lot::class, Payment::class, Photo::class, Rate::class, Registration::class
                , Session::class, User::class], version = 1, exportSchema = false)
abstract class Park254Database : RoomDatabase() {


   // abstract fun moodLogDao(): MoodLogDao



}
