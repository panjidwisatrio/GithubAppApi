package com.panjidwisatrio.github.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.panjidwisatrio.github.databinding.ActivitySplashBinding
import com.panjidwisatrio.github.ui.main.MainActivity
import com.panjidwisatrio.github.util.Constanta.SPLASH_SCREEN
import com.panjidwisatrio.github.util.Constanta.TIME_SPLASH
import java.lang.Exception

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        viewModel.getThemeSetting().observe(this@SplashActivity) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        // menjalankan thread untuk splash screen untuk proses async
        // TODO: 1/10/2021 1. Splash screen time activity
        object: Thread() {
            override fun run() {
                try {
                    // proses sleep selama 3 detik
                    sleep(TIME_SPLASH)
                    // pindah ke main activity
                    moveToMain()
                    // menutup activity
                    finish()
                } catch (e: Exception) {
                    Log.d(SPLASH_SCREEN, e.message.toString())
                }
            }
        }.start()
    }

    private fun moveToMain() {
        startActivity(
            Intent(this, MainActivity::class.java)
        )
    }
}