package com.panjidwisatrio.github.util

import androidx.annotation.StringRes
import com.panjidwisatrio.github.BuildConfig
import com.panjidwisatrio.github.R

object Constanta {
    // key untuk intent dari antar activity atau fragment
    const val EXTRA_USER = "EXTRA_USER"

    // anotasi @StringRes digunakan untuk menandakan bahwa variabel tersebut adalah id dari string
    @StringRes
    // array untuk tab layout dalam integer
    val TAB_TITLES = intArrayOf(
        R.string.tab1,
        R.string.tab2
    )

    // key untuk melakukan request ke github api dengan menggunakan token yang diambil dari build config
    const val TOKEN = BuildConfig.KEY

    // base url untuk melakukan request ke github api
    const val BASE_URL = "https://api.github.com"
}