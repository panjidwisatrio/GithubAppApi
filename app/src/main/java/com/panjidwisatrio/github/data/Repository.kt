package com.panjidwisatrio.github.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.panjidwisatrio.github.data.datastore.UserDataStore
import com.panjidwisatrio.github.data.local.UserDao
import com.panjidwisatrio.github.data.local.UserDatabase
import com.panjidwisatrio.github.data.local.UserDatabase.Companion.getInstance
import com.panjidwisatrio.github.data.remote.ApiService
import com.panjidwisatrio.github.data.remote.RetrofitService
import com.panjidwisatrio.github.model.Search
import com.panjidwisatrio.github.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Repository(application: Application) {
    // inisialisasi retrofit, dao, dan dataStore
    private val retrofit: ApiService = RetrofitService.create()
    private val dao: UserDao
    private val dataStore: UserDataStore

    init {
        val database: UserDatabase = getInstance(application)
        dao = database.userDao()
        dataStore = UserDataStore.getInstance(application)
    }

    // fungsi untuk mengambil data dari api
    fun searchUser(query: String): LiveData<Resource<List<User>>> {

        // inisialisasi liveData
        val listUser = MutableLiveData<Resource<List<User>>>()

        // set liveData menjadi loading
        listUser.postValue(Resource.Loading())
        // mengambil data dari api
        retrofit.searchUsers(query).enqueue(object : Callback<Search> {
            override fun onResponse(
                call: Call<Search>, response: Response<Search>
            ) {
                // mengambil data dari response
                val list = response.body()?.items

                if (list.isNullOrEmpty())
                    // jika data kosong maka liveData akan menjadi error
                    listUser.postValue(Resource.Error(null))
                else
                    // jika data tidak kosong maka liveData akan menjadi success
                    listUser.postValue(Resource.Success(list))
            }

            override fun onFailure(call: Call<Search>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }

        })

        // mengembalikan liveData yang sudah di set
        return listUser
    }

    // fungsi untuk mengambil data dari database
    suspend fun getDetailUser(username: String): LiveData<Resource<User>> {

        // inisialisasi liveData
        val user = MutableLiveData<Resource<User>>()

        // cek apakah data sudah ada di database
        if (dao.getFavoriteDetailUser(username) != null) {
            // jika ada maka liveData akan menjadi success
            user.postValue(Resource.Success(dao.getFavoriteDetailUser(username)))
        } else {
            // jika tidak ada, maka akan mengambil data dari api
            retrofit.getDetailUser(username).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    // mengambil data dari response
                    val result = response.body()
                    // jika data tidak kosong maka liveData akan menjadi success
                    user.postValue(Resource.Success(result))
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    // jika data kosong maka liveData akan menjadi error
                    user.postValue(Resource.Error(t.message))
                }
            })
        }

        // mengembalikan liveData yang sudah di set
        return user
    }

    // fungsi untuk mengambil data dari api
    fun getUserFollowing(username: String): LiveData<Resource<List<User>>> {

        val listUser = MutableLiveData<Resource<List<User>>>()

        listUser.postValue(Resource.Loading())
        retrofit.getUserFollowing(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val list = response.body()
                if (list.isNullOrEmpty()) listUser.postValue(Resource.Error(null))
                else listUser.postValue(Resource.Success(list))
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }
        })
        return listUser
    }

    // fungsi untuk mengambil data dari api
    fun getUserFollowers(username: String): LiveData<Resource<List<User>>> {

        val listUser = MutableLiveData<Resource<List<User>>>()

        listUser.postValue(Resource.Loading())
        // TODO: 10/10/2021 - Coroutines
        retrofit.getUserFollowers(username).enqueue(object : Callback<List<User>> {
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                val list = response.body()
                if (list.isNullOrEmpty()) listUser.postValue(Resource.Error(null))
                else listUser.postValue(Resource.Success(list))
            }

            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                listUser.postValue(Resource.Error(t.message))
            }
        })

        return listUser
    }

    // fungsi untuk mengambil data dari database
    suspend fun getFavoriteList(): LiveData<Resource<List<User>>> {

        val listFavorite = MutableLiveData<Resource<List<User>>>()
        listFavorite.postValue(Resource.Loading())

        if (dao.getFavoriteListUser().isEmpty()) listFavorite.postValue(Resource.Error(null))
        else listFavorite.postValue(Resource.Success(dao.getFavoriteListUser()))

        return listFavorite
    }

    // fungsi untuk mengambil data dari database
    suspend fun insertFavoriteUser(user: User) = dao.insertFavoriteUser(user)

    // fungsi untuk mengambil data dari database
    suspend fun deleteFavoriteUser(user: User) = dao.deleteFavoriteUser(user)

    // fungsi untuk mengambil data dari database
    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        dataStore.saveThemeSetting(isDarkModeActive)

    // fungsi untuk mengambil data dari database
    fun getThemeSetting() = dataStore.getThemeSetting()
}