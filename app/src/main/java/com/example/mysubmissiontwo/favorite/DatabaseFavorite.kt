package com.example.mysubmissiontwo.favorite

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavoriteEntity::class], version = 1)

abstract class DatabaseFavorite: RoomDatabase() {
    companion object{
        var INSTANCE : DatabaseFavorite? = null
        fun getDatabase(context: Context): DatabaseFavorite?{
            if(INSTANCE == null) {
                synchronized(DatabaseFavorite::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, DatabaseFavorite::class.java, "db_user").build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun favoriteDao(): FavoriteDao
}
