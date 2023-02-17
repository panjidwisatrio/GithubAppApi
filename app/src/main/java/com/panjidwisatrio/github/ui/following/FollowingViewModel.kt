package com.panjidwisatrio.github.ui.following

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.panjidwisatrio.github.data.Repository

class FollowingViewModel(application: Application): AndroidViewModel(application) {
    private val repository = Repository(application)

    fun getUserFollowing(username: String) = repository.getUserFollowing(username)
}