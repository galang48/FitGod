package com.example.fitgod.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fitgod.data.local.dao.IstilahGymDao
import com.example.fitgod.data.local.dao.UserDao
import com.example.fitgod.data.local.entity.IstilahGymEntity
import com.example.fitgod.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, IstilahGymEntity::class],
    version = 1,
    exportSchema = false
)
abstract class FitGodDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun istilahGymDao(): IstilahGymDao

    companion object {
        @Volatile
        private var INSTANCE: FitGodDatabase? = null

        fun getInstance(context: Context): FitGodDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FitGodDatabase::class.java,
                    "fitgod_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
