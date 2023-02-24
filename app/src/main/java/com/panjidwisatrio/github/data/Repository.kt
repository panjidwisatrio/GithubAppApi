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
import com.panjidwisatrio.github.model.User
import kotlinx.coroutines.*

class Repository(application: Application) {
    // inisialisasi retrofit, dao, dan dataStore
    private val retrofit: ApiService = RetrofitService.create()
    private val dao: UserDao
    private val dataStore: UserDataStore
    private var job: Job? = null

    init {
        val database: UserDatabase = getInstance(application)
        dataStore = UserDataStore.getInstance(application)
        dao = database.userDao()
    }

    // fungsi untuk mengambil data dari api
    suspend fun searchUser(query: String): LiveData<Resource<List<User>>> {

        // inisialisasi liveData
        val listUser = MutableLiveData<Resource<List<User>>>()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            listUser.postValue(Resource.Error(throwable.message))
        }

        // set liveData menjadi loading
        listUser.postValue(Resource.Loading())
        // mengambil data dari api
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = retrofit.searchUsers(query)
            if (response.isSuccessful) {
                if (response.body()?.items?.isEmpty() == true) {
                    listUser.postValue(Resource.Error("User not found"))
                } else {
                    listUser.postValue(Resource.Success(response.body()?.items))
                }
            } else {
                if (response.code() == 403) {
                    listUser.postValue(Resource.Error("API limit exceeded"))
                } else if (response.code() == 422) {
                    listUser.postValue(Resource.Error("Query parameter is missing"))
                } else if (response.code() == 500) {
                    listUser.postValue(Resource.Error("Internal server error"))
                } else {
                    listUser.postValue(Resource.Error(response.message()))
                }
            }
        }

        // mengembalikan liveData yang sudah di set
        return listUser
    }

    // fungsi untuk mengambil data dari database
    suspend fun getDetailUser(username: String): LiveData<Resource<User>> {

        // inisialisasi liveData
        val user = MutableLiveData<Resource<User>>()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            user.postValue(Resource.Error(throwable.message))
        }

        // set liveData menjadi loading
        user.postValue(Resource.Loading())

        // cek apakah data sudah ada di database
        if (dao.getFavoriteDetailUser(username) != null) {
            // jika ada maka liveData akan menjadi success
            user.postValue(Resource.Success(dao.getFavoriteDetailUser(username)))
        } else {
            // jika tidak ada, maka akan mengambil data dari api
            job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = retrofit.getDetailUser(username)
                if (response.isSuccessful) {
                    user.postValue(Resource.Success(response.body()))
                } else {
                    if (response.code() == 403) {
                        user.postValue(Resource.Error("API limit exceeded"))
                    } else if (response.code() == 422) {
                        user.postValue(Resource.Error("Query parameter is missing"))
                    } else if (response.code() == 500) {
                        user.postValue(Resource.Error("Internal server error"))
                    } else {
                        user.postValue(Resource.Error(response.message()))
                    }
                }
            }
        }

        // mengembalikan liveData yang sudah di set
        return user
    }

    // fungsi untuk mengambil data dari api
    suspend fun getUserFollowing(username: String): LiveData<Resource<List<User>>> {

        val listUser = MutableLiveData<Resource<List<User>>>()
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            listUser.postValue(Resource.Error(throwable.message))
        }

        listUser.postValue(Resource.Loading())
        job = CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = retrofit.getUserFollowing(username)
            if (response.isSuccessful) {
                if (response.body()?.isEmpty() == true) {
                    listUser.postValue(Resource.Error(null))
                } else {
                    listUser.postValue(Resource.Success(response.body()))
                }
            } else {
                if (response.code() == 403) {
                    listUser.postValue(Resource.Error("API limit exceeded"))
                } else if (response.code() == 422) {
                    listUser.postValue(Resource.Error("Query parameter is missing"))
                } else if (response.code() == 500) {
                    listUser.postValue(Resource.Error("Internal server error"))
                } else {
                    listUser.postValue(Resource.Error(response.message()))
                }
            }
        }

        return listUser
    }

    // fungsi untuk mengambil data dari api
    suspend fun getUserFollowers(username: String): LiveData<Resource<List<User>>> {

        val listUser = MutableLiveData<Resource<List<User>>>()

        listUser.postValue(Resource.Loading())
        // DONE: 10/10/2021 - Coroutines
        job = CoroutineScope(Dispatchers.IO).launch {
            val response = retrofit.getUserFollowers(username)
            if (response.isSuccessful) {
                if (response.body()?.isEmpty() == true) {
                    listUser.postValue(Resource.Error(null))
                } else {
                    listUser.postValue(Resource.Success(response.body()))
                }
            } else {
                if (response.code() == 403) {
                    listUser.postValue(Resource.Error("API limit exceeded"))
                } else if (response.code() == 422) {
                    listUser.postValue(Resource.Error("Query parameter is missing"))
                } else if (response.code() == 500) {
                    listUser.postValue(Resource.Error("Internal server error"))
                } else {
                    listUser.postValue(Resource.Error(response.message()))
                }
            }
        }

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