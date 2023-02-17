package com.panjidwisatrio.github.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.panjidwisatrio.github.data.Repository
import com.panjidwisatrio.github.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application): AndroidViewModel(application) {
    // inisiasi repository
    private val repository = Repository(application)

    // fungsi untuk mendapatkan detail user dari repository dan mengembalikan data ke activity
    suspend fun getDetailUser(username: String) = repository.getDetailUser(username)

    /* fungsi untuk menambahkan user ke database favorite user dan mengembalikan data ke activity
       viewmodelscope digunakan untuk menjalankan coroutine di background thread */
    fun insertFavorite(user: User) = viewModelScope.launch {
        repository.insertFavoriteUser(user)
    }

    // fungsi untuk menghapus user dari database favorite user dan mengembalikan data ke activity
    fun deleteFavorite(user: User) = viewModelScope.launch {
        repository.deleteFavoriteUser(user)
    }

    // fungsi untuk mendapatkan data theme dari repository dan mengembalikan data ke activity
    fun getThemeSetting() = repository.getThemeSetting().asLiveData(Dispatchers.IO)
}