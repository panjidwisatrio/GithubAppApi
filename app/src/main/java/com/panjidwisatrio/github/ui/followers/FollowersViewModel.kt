package com.panjidwisatrio.github.ui.followers

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.panjidwisatrio.github.data.Repository

class FollowersViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository(application)
    suspend fun getUserFollowers(username: String) = repository.getUserFollowers(username)
}