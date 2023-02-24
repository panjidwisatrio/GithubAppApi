package com.panjidwisatrio.github.ui.detail

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.panjidwisatrio.github.R
import com.panjidwisatrio.github.data.Resource
import com.panjidwisatrio.github.databinding.ActivityDetailBinding
import com.panjidwisatrio.github.model.User
import com.panjidwisatrio.github.ui.adapter.SectionTabAdapter
import com.panjidwisatrio.github.util.Constanta.EXTRA_USER
import com.panjidwisatrio.github.util.Constanta.TAB_TITLES
import com.panjidwisatrio.github.util.ViewStateCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity(), ViewStateCallback<User> {
    // inisialisasi binding dan viewmodel
    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    // lifecycle method onCreate yang akan dipanggil pertama kali saat activity dibuat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarDetail.topAppBarDetail)
        supportActionBar?.title = null
        // set back button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // set theme dari shared preference ke activity
        viewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
            } else {
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_black)
            }
        }

        // mengambil data dari intent dengan key EXTRA_USER
        val username = intent.getStringExtra(EXTRA_USER).toString()

        setTabLayout(username)
        getUserWithCoroutine(username)
    }

    // method untuk mengambil data user dari api dengan coroutine melalui viewmodel
    private fun getUserWithCoroutine(username: String) {
        // menjalankan kode secara asynchronous di dalam thread utama.
        CoroutineScope(Dispatchers.Main).launch {
            // mengamati data dari viewmodel dengan memanggil method getDetailUser
            viewModel.getDetailUser(username).observe(this@DetailActivity) {
                // mengecek apakah data yang didapat adalah error, loading, atau success
                when (it) {
                    is Resource.Error -> onFailed(it.message)
                    is Resource.Loading -> onLoading()
                    is Resource.Success -> it.data?.let { it1 -> onSuccess(it1) }
                    else -> {}
                }
            }
        }
    }

    // method untuk menampilkan tab layout dan mengatur tab layout
    private fun setTabLayout(username: String) {
        // inisisalisasi adapter untuk tab layout
        val pageAdapter = SectionTabAdapter(this, username)

        // mengatur tab layout
        binding.apply {
            // set adapter untuk tab layout
            container.adapter = pageAdapter
            // set tab layout dengan tab layout mediator
            TabLayoutMediator(tabs, container) { tabs, position ->
                tabs.text = resources.getString(TAB_TITLES[position])
            }.attach()
        }
    }

    override fun onSuccess(data: User) {
        binding.apply {
            progressBarDetail?.visibility = invisible
            container.visibility = visible
            fabFav.visibility = visible
            placeholderImg?.visibility = invisible
            placeholderText?.visibility = invisible
            tabs.visibility = visible
            followerDetail.visibility = visible
            followingDetail.visibility = visible
            repoDetail.visibility = visible

            Glide.with(this@DetailActivity)
                .load(data.avatar)
                .into(ivAvatar)
            ivAvatar.visibility = visible

            tvUsername.text = data.username.toString()
            tvUsername.visibility = visible

            if (data.name == null) tvName.text = "-"
            else tvName.text = data.name.toString()
            tvName.visibility = visible

            if (data.company == null) tvCompany.text = getString(R.string.not_added_company)
            else tvCompany.text = data.company.toString()
            tvCompany.visibility = visible

            if (data.location == null) tvLocation.text = getString(R.string.not_added_location)
            else tvLocation.text = data.location.toString()
            tvLocation.visibility = visible

            repoDetail.text = data.repository.toString()
            followerDetail.text = data.follower.toString()
            followingDetail.text = data.following.toString()

            // inisialisasi warna fab button favorite
            val checked: ColorStateList = ColorStateList.valueOf(Color.RED)
            var unChecked: ColorStateList = ColorStateList.valueOf(Color.rgb(36, 41, 47))

            // mengamati data dari shared preference untuk menentukan warna fab button favorite
            viewModel.getThemeSetting().observe(this@DetailActivity) { isDarkModeActive ->
                // mengubah warna fab button favorite sesuai dengan theme yang dipilih
                unChecked = if (isDarkModeActive) {
                    ColorStateList.valueOf(Color.WHITE)
                } else {
                    ColorStateList.valueOf(Color.rgb(36, 41, 47))
                }

                // mengecek apakah user yang sedang dilihat sudah di favorite atau belum
                if (data.isFavorite) fabFav.imageTintList = checked
                else fabFav.imageTintList = unChecked
            }

            // menambahkan listener pada fab button favorite
            fabFav.setOnClickListener {
                // mengecek apakah user yang sedang dilihat sudah di favorite atau belum
                // DONE: 1/10/2021 - == true bisa dihilangkan
                if (data.isFavorite) {
                    // jika sudah di favorite, maka akan dihapus dari favorite
                    data.isFavorite = false
                    viewModel.deleteFavorite(data)
                    fabFav.imageTintList = unChecked
                } else {
                    // jika belum di favorite, maka akan ditambahkan ke favorite
                    data.isFavorite = true
                    data.let { it1 -> viewModel.insertFavorite(it1) }
                    fabFav.imageTintList = checked
                }
            }
        }
    }

    override fun onLoading() {
        with(binding) {
            followerDetail.visibility = invisible
            followingDetail.visibility = invisible
            repoDetail.visibility = invisible
            tvCompany.visibility = invisible
            tvLocation.visibility = invisible
            tvName.visibility = invisible
            tvUsername.visibility = invisible
            ivAvatar.visibility = invisible
            progressBarDetail?.visibility = visible
            container.visibility = invisible
            fabFav.visibility = invisible
            placeholderImg?.visibility = invisible
            placeholderText?.visibility = invisible
            tabs.visibility = invisible
        }
    }

    override fun onFailed(message: String?) {
        with(binding) {
            followerDetail.visibility = invisible
            followingDetail.visibility = invisible
            repoDetail.visibility = invisible
            tvCompany.visibility = invisible
            tvLocation.visibility = invisible
            tvName.visibility = invisible
            tvUsername.visibility = invisible
            ivAvatar.visibility = invisible
            progressBarDetail?.visibility = invisible
            container.visibility = invisible
            fabFav.visibility = invisible
            placeholderImg?.visibility = visible
            if (message != null) placeholderText?.text = message
            else placeholderText?.text = getString(R.string.something_went_wrong)
            placeholderText?.visibility = visible
            tabs.visibility = invisible
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
}