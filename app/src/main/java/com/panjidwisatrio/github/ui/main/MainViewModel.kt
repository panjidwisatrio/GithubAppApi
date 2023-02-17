package com.panjidwisatrio.github.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.panjidwisatrio.github.data.Repository
import kotlinx.coroutines.Dispatchers

// menghubungkan repository dengan viewmodel (View)
class MainViewModel(application: Application): AndroidViewModel(application) {
    // inisialisasi repository
    private val repository = Repository(application)

    // mengambil data dari repository dan mengembalikan ke view
    fun searchUser(query: String) = repository.searchUser(query)
    // mengambil data dari repository dan mengembalikan ke view
    fun getThemeSetting() = repository.getThemeSetting().asLiveData(Dispatchers.IO)
}