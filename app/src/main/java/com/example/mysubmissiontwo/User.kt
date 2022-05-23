package com.example.mysubmissiontwo

import android.app.Application
import android.os.Parcelable
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mysubmissiontwo.favorite.DatabaseFavorite
import com.example.mysubmissiontwo.favorite.FavoriteDao
import com.example.mysubmissiontwo.favorite.FavoriteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class User (application: Application) : AndroidViewModel(application){


    private var favDao: FavoriteDao?
    private var favDatabase: DatabaseFavorite?

    init {
        favDatabase = DatabaseFavorite.getDatabase(application)
        favDao = favDatabase?.favoriteDao()
    }

    fun addFavorite(login: String,
                    subscriptionUrl: String,
                    avatarUrl: String,
                    id: Int,
                    gistsUrl: String,
                    reposUrl: String,
                    followingUrl: String,
                    starredUrl: String,
                    followersUrl: String,
                    type: String,
                    url: String,
                    score: Double,
                    receivedEventsUrl: String,
                    eventsUrl: String,
                    htmlUrl: String,
                    siteAdmin: Boolean,
                    gravatarId: String,
                    nodeId: String,
                    organizationsUrl: String,
                    ) {
        CoroutineScope(Dispatchers.IO).launch {
            var user = FavoriteEntity(
                login,
                subscriptionUrl,
                avatarUrl,
                id,
                gistsUrl,
                reposUrl,
                followingUrl,
                starredUrl,
                followersUrl,
                type,
                url,
                score,
                receivedEventsUrl,
                eventsUrl,
                htmlUrl,
                siteAdmin,
                gravatarId,
                nodeId,
                organizationsUrl,
            )
            favDao?.addFavorite(user)
        }
    }
    fun checkFavorite(id: Int) = favDao?.checkFavorite(id)

    fun removeFromFavorite(id: Int){
        CoroutineScope(Dispatchers.IO).launch {
            favDao?.removeFavorite(id)
        }
    }
}

