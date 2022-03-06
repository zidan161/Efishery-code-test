package com.example.efisherypricelist.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.efisherypricelist.model.Area
import com.example.efisherypricelist.model.Fish
import com.example.efisherypricelist.model.Size

@Database(entities = [Fish::class], version = 1)
abstract class MainDatabase : RoomDatabase() {

    abstract fun fishDao(): FishDao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MainDatabase {
            if (INSTANCE == null) {
                synchronized(MainDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        MainDatabase::class.java, "efishery_db")
                        .build()
                }
            }
            return INSTANCE as MainDatabase
        }
    }
}