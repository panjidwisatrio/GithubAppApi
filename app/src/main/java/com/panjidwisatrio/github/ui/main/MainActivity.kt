package com.panjidwisatrio.github.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.panjidwisatrio.github.R
import com.panjidwisatrio.github.data.Resource
import com.panjidwisatrio.github.databinding.ActivityMainBinding
import com.panjidwisatrio.github.model.User
import com.panjidwisatrio.github.ui.adapter.SearchAdapter
import com.panjidwisatrio.github.ui.favorite.FavoriteActivity
import com.panjidwisatrio.github.ui.setting.SettingActivity
import com.panjidwisatrio.github.util.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), ViewStateCallback<List<User>> {

    // Inisialisasi binding, userQuery, viewModel, dan searchAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var userQuery: String
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var searchAdapter: SearchAdapter

    // lifecycle onCreate pada activity yang akan dijalankan pertama kali
    override fun onCreate(savedInstanceState: Bundle?) {
        // inisialisasi activity
        super.onCreate(savedInstanceState)
        // inflate (menghubungkan) layout dengan activity
        binding = ActivityMainBinding.inflate(layoutInflater)
        // menampilkan layout atau menetapkan tampilan layout
        setContentView(binding.root)

        // set custom action bar
        setSupportActionBar(binding.toolbar.topAppBarMain)
        // set title action bar menjadi null
        supportActionBar?.title = null
        // inisialisasi adapter
        searchAdapter = SearchAdapter()

        showRecyclerView()
        searchUser()
    }

    // searchUser() digunakan untuk mencari pengguna
    private fun searchUser() {
        binding.toolbar.search.apply {
            // implementasi listener pada searchView
            setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                // ketika tombol search atau enter ditekan
                override fun onQueryTextSubmit(query: String?): Boolean {
                    userQuery = query.toString()
                    // menghilangkan keyboard
                    clearFocus()
                    // memanggil method searchUser pada viewModel
                    CoroutineScope(Dispatchers.Main).launch {
                        viewModel.searchUser(userQuery).observe(this@MainActivity) {
                            when (it) {
                                is Resource.Error -> onFailed(it.message)
                                is Resource.Loading -> onLoading()
                                is Resource.Success -> it.data?.let { it1 -> onSuccess(it1) }
                            }
                        }
                    }
                    return true
                }

                // ketika text pada searchView berubah
                override fun onQueryTextChange(newText: String?): Boolean {
                    // menghapus list pada adapter
                    searchAdapter.submitList(emptyList())
                    // menampilkan placeholder
                    with(binding) {
                        placeholderImg.apply {
                            viewModel.getThemeSetting().observe(this@MainActivity) { isDarkModeActive ->
                                if (isDarkModeActive) {
                                    setImageResource(R.drawable.ic_people_search)
                                } else {
                                    setImageResource(R.drawable.ic_people_search_black)
                                }
                            }
                            visibility = visible
                        }

                        placeholderText.apply {
                            text = getString(R.string.cari_pengguna)
                            visibility = visible
                        }
                    }
                    return true
                }

            })
        }
    }

    // menampilkan recyclerview
    private fun showRecyclerView() {
        // menampilkan recyclerview
        binding.rvListUser.apply {
            visibility = visible
            // set adapter
            adapter = searchAdapter
            // set layout manager
            layoutManager = LinearLayoutManager(
                this@MainActivity, LinearLayoutManager.VERTICAL, false
            )
        }
    }

    // onCreatedOptionMenu digunakan untuk menampilkan menu pada action bar
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // menghubungkan menu dengan layout
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        // mengubah icon menu sesuai dengan tema yang dipilih
        viewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                menu.getItem(0)?.setIcon(R.drawable.ic_favorite_black)
                menu.getItem(1)?.setIcon(R.drawable.ic_baseline_settings_black)
            } else {
                menu.getItem(0)?.setIcon(R.drawable.ic_baseline_favorite_24)
                menu.getItem(1)?.setIcon(R.drawable.ic_baseline_settings_24)
            }
        }

        return true
    }

    // onOptionsItemSelected digunakan untuk menangani aksi ketika menu pada action bar ditekan
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                // berpindah ke activity FavoriteActivity
                startActivity(
                    Intent(
                        this@MainActivity, FavoriteActivity::class.java
                    )
                )
                true
            }
            R.id.setting -> {
                // berpindah ke activity SettingActivity
                startActivity(
                    Intent(
                        this@MainActivity, SettingActivity::class.java
                    )
                )
                true
            }
            else -> false
        }
    }

    // implementasi method onSuccess dari ViewStateCallback
    override fun onSuccess(data: List<User>) {
        // mengirimkan data ke adapter
        searchAdapter.submitList(data)
        // menampilkan recyclerview dan menghilangkan placeholder
        with(binding) {
            rvListUser.visibility = visible
            progressBar.visibility = invisible
            placeholderImg.visibility = invisible
            placeholderText.visibility = invisible
        }
    }

    // implementasi method onLoading dari ViewStateCallback
    override fun onLoading() {
        // menampilkan progressbar dan menghilangkan recyclerview dan placeholder
        with(binding) {
            progressBar.visibility = visible
            rvListUser.visibility = invisible
            placeholderImg.visibility = invisible
            placeholderText.visibility = invisible
        }
    }

    // implementasi method onFailed dari ViewStateCallback
    override fun onFailed(message: String?) {
        // menampilkan placeholder dan menghilangkan recyclerview dan progressbar
        with(binding) {
            progressBar.visibility = invisible
            rvListUser.visibility = invisible
            // jika message null, maka akan menampilkan placeholder default
            if (message == null) {
                placeholderImg.apply {
                    // mengubah icon placeholder sesuai dengan tema yang dipilih
                    viewModel.getThemeSetting().observe(this@MainActivity) { isDarkModeActive ->
                        if (isDarkModeActive) {
                            setImageResource(R.drawable.ic_search_not_found)
                        } else {
                            setImageResource(R.drawable.ic_search_not_found_black)
                        }
                    }

                    visibility = visible
                }

                placeholderText.apply {
                    text = getString(R.string.user_not_found)
                    // mengubah font placeholder sesuai dengan tema yang dipilih
                    typeface = ResourcesCompat.getFont(
                        this@MainActivity, R.font.sf_compact_semibold
                    )
                    visibility = visible
                }
            } else {
                // jika message tidak null, maka akan menampilkan placeholder sesuai dengan message
                placeholderImg.apply {
                    // mengubah icon placeholder sesuai dengan tema yang dipilih
                    viewModel.getThemeSetting().observe(this@MainActivity) { isDarkModeActive ->
                        if (isDarkModeActive) {
                            setImageResource(R.drawable.ic_search_not_found)
                        } else {
                            setImageResource(R.drawable.ic_search_not_found_black)
                        }
                    }

                    visibility = visible
                }

                placeholderText.apply {
                    text = message
                    visibility = visible
                }
            }
        }
    }
}