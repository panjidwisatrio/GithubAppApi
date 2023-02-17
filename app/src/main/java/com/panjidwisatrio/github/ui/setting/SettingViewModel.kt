package com.panjidwisatrio.github.ui.setting

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.panjidwisatrio.github.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingViewModel(application: Application): AndroidViewModel(application) {
    // inisialisasi repository
    private val repository = Repository(application)

    // fungsi untuk menyimpan settingan tema ke datastore
    fun saveThemeSetting(isDarkModeActive: Boolean) = viewModelScope.launch {
        repository.saveThemeSetting(isDarkModeActive)
    }

    // fungsi untuk mengambil settingan tema dari datastore
    fun getThemeSetting() = repository.getThemeSetting().asLiveData(Dispatchers.IO)
}