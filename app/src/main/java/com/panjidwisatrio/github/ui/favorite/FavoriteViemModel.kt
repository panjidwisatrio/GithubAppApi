package com.panjidwisatrio.github.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import com.panjidwisatrio.github.data.Repository
import kotlinx.coroutines.Dispatchers

class FavoriteViemModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository(application)

    suspend fun getFavoriteList() = repository.getFavoriteList()
    fun getThemeSetting() = repository.getThemeSetting().asLiveData(Dispatchers.IO)
}