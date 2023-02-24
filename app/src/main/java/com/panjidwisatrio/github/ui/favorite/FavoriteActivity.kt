package com.panjidwisatrio.github.ui.favorite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjidwisatrio.github.R
import com.panjidwisatrio.github.data.Resource
import com.panjidwisatrio.github.databinding.ActivityFavoriteBinding
import com.panjidwisatrio.github.model.User
import com.panjidwisatrio.github.ui.adapter.SearchAdapter
import com.panjidwisatrio.github.util.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity(), ViewStateCallback<List<User>> {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var searchAdapter: SearchAdapter
    private val viewModel: FavoriteViemModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarFavorite.topAppBarFavorite)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_black)
            } else {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
            }
        }

        searchAdapter = SearchAdapter()

        showRecycleView()
        getUserWithCoroutine()
    }

    private fun getUserWithCoroutine() {
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavoriteList().observe(this@FavoriteActivity) {
                when (it) {
                    is Resource.Error -> onFailed(it.message)
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> it.data?.let { it1 ->
                        onSuccess(it1)
                        searchAdapter.submitList(it1)
                    }
                }
            }
        }
    }

    private fun showRecycleView() {
        binding.rvListUser.apply {
            adapter = searchAdapter
            layoutManager = LinearLayoutManager(
                this@FavoriteActivity, LinearLayoutManager.VERTICAL, false
            )
        }
    }

    override fun onSuccess(data: List<User>) {
        binding.apply {
            progressBar.visibility = invisible
            placeholderImgFavorite.visibility = invisible
            placeholderTextFavorite.visibility = invisible
            rvListUser.visibility = visible
        }
    }

    override fun onLoading() {
        binding.apply {
            placeholderTextFavorite.visibility = invisible
            placeholderImgFavorite.visibility = invisible
            rvListUser.visibility = invisible
            progressBar.visibility = visible
        }
    }

    override fun onFailed(message: String?) {
        if (message == null) {
            binding.apply {
                progressBar.visibility = invisible
                rvListUser.visibility = invisible
                placeholderImgFavorite.visibility = visible
                placeholderTextFavorite.visibility = visible
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.getFavoriteList().observe(this@FavoriteActivity) {
                when (it) {
                    is Resource.Error -> onFailed(it.message)
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> it.data?.let { it1 ->
                        onSuccess(it1)
                        searchAdapter.submitList(it1)
                    }
                }
            }
        }
    }
}