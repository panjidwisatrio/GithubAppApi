package com.panjidwisatrio.github.ui.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.panjidwisatrio.github.R
import com.panjidwisatrio.github.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingBinding
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarSetting.topAppBarSetting)
        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // set theme setting from shared preferences to switch button and set theme mode to dark or light mode
        binding.swNightMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
        }

        // mengubah tema dari aplikasi sesuai dengan settingan yang disimpan di shared preferences
        viewModel.getThemeSetting().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                // jika isDarkModeActive bernilai true, maka tema aplikasi akan berubah menjadi dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_black)
                binding.swNightMode.isChecked = true
            } else {
                // jika isDarkModeActive bernilai false, maka tema aplikasi akan berubah menjadi light mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_white)
                binding.swNightMode.isChecked = false
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
}