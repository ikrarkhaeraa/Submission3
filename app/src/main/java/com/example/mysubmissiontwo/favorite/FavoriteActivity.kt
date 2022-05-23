package com.example.mysubmissiontwo.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mysubmissiontwo.*
import com.example.mysubmissiontwo.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: ListUsersAdapter
    private lateinit var favoriteModel : FavoriteViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ListUsersAdapter()

        binding.rvFavorite.layoutManager = LinearLayoutManager(this@FavoriteActivity)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.setHasFixedSize(true)

        supportActionBar?.title = "Favorite Github User's"

        favoriteModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        adapter.setOnItemClickCallback(object : ListUsersAdapter.OnItemClickCallback{
            override fun onItemClicked(userData: ItemsItem) {
                val intentToDetail = Intent(this@FavoriteActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra("DATA", userData)
                startActivity(intentToDetail)
            }
        })

        favoriteModel?.getFavorite()?.observe(this) {
            if (it != null) {
                val list = mapList(it)
                adapter.setData(list)
            }
        }
    }

    private fun mapList(users: List<FavoriteEntity>): ArrayList<ItemsItem> {
        val listUsers = ArrayList<ItemsItem>()
        for (user in users) {
            val userMapped = ItemsItem (
                user.gistsUrl,
                user.reposUrl,
                user.followingUrl,
                user.starredUrl,
                user.login,
                user.followersUrl,
                user.type,
                user.url,
                user.subscriptionUrl,
                user.score,
                user.receivedEventsUrl,
                user.avatarUrl,
                user.eventsUrl,
                user.htmlUrl,
                user.siteAdmin,
                user.id,
                user.gravatarId,
                user.nodeId,
                user.organizationsUrl,
            )
            listUsers.add(userMapped)
        }
        return listUsers
    }

}