package com.example.mysubmissiontwo

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mysubmissiontwo.databinding.ActivityMainBinding
import com.example.mysubmissiontwo.favorite.FavoriteActivity
import com.example.mysubmissiontwo.setting.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val listUsersAdapter = ListUsersAdapter()
    private lateinit var rvUsers: RecyclerView
    private var query = ""

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Github User's"

        val layoutManager = LinearLayoutManager(this)
        binding.listUser.layoutManager = layoutManager
        binding.listUser.adapter = listUsersAdapter
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listUser.addItemDecoration(itemDecoration)

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, SettingViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        mainViewModel.getThemeSettings().observe(this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        rvUsers = findViewById(R.id.listUser)
        rvUsers.setHasFixedSize(true)

        goToDetail()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_bar, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                findUserData(query)
                this@MainActivity.query = query
                Toast.makeText(this@MainActivity, query, Toast.LENGTH_SHORT).show()
                searchView.clearFocus()
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val i = Intent(this, FavoriteActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.settings -> {
                val i = Intent(this, SettingActivity::class.java)
                startActivity(i)
                return true
            }
            else -> return true
        }
    }

    fun findUserData(query:String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUser(query)
        client.enqueue(object : Callback<ListUsersResponse> {
            override fun onResponse(
                call: Call<ListUsersResponse>,
                response: Response<ListUsersResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        listUsersAdapter.setData(responseBody.items)
                    }
                } else {
                    Log.e("tes", "onResponse: ${response}")
                }
            }
            override fun onFailure(call: Call<ListUsersResponse>, t:  Throwable) {
                showLoading(false)
                Log.e(MainActivity.TAG, "onFailure: ${t.message}")
            }
        })
    }

private fun setListData(items: List<ItemsItem>) {
    Log.d("items content", items.toString())
    listUsersAdapter.setData(items)
}

    private fun goToDetail() {

        listUsersAdapter.setOnItemClickCallback(object : ListUsersAdapter.OnItemClickCallback{
            override fun onItemClicked(userData: ItemsItem) {
                val intentToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra("DATA", userData)
                startActivity(intentToDetail)
                }
            })
        }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            }
        }
}