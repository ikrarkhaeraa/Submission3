package com.example.mysubmissiontwo.favorite

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    fun addFavorite(favorite: FavoriteEntity)

    @Query("SELECT * FROM favorite")
    fun getFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT count(*) FROM favorite WHERE favorite.id = :id")
    fun checkFavorite(id: Int): Int

    @Query("DELETE FROM favorite WHERE favorite.id = :id")
    fun removeFavorite(id: Int): Int
}