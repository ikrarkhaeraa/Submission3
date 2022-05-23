package com.example.mysubmissiontwo.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class FavoriteViewModel (application: Application): AndroidViewModel(application) {

    private var favDao: FavoriteDao?
    private var favDb: DatabaseFavorite?

    init {
        favDb = DatabaseFavorite.getDatabase(application)
        favDao = favDb?.favoriteDao()
    }

    fun getFavorite(): LiveData<List<FavoriteEntity>>? {
        return favDao?.getFavorite()
    }
}