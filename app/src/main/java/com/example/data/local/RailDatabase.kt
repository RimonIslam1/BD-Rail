package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        TrainEntity::class,
        TrainStopEntity::class,
        BookedTicketEntity::class,
        DelayAlertLogEntity::class,
        OfflineZoneSyncEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RailDatabase : RoomDatabase() {
    abstract fun railDao(): RailDao

    companion object {
        @Volatile
        private var INSTANCE: RailDatabase? = null

        fun getInstance(context: Context): RailDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RailDatabase::class.java,
                    "bd_rail_tracker.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
