package com.panjidwisatrio.github.data.datastore

import androidx.datastore.preferences.core.booleanPreferencesKey

// membuat kode menjadi lebih mudah dibaca, lebih mudah dikelola, dan lebih mudah di-maintain.
object DataStoreUtil {
    const val DATA_STORE_NAME = "USER_DATASTORE"
    // menginisialisasi key yang akan digunakan untuk menyimpan nilai boolean dari tema yang dipilih.
    val DATA_STORE_PREF_THEME_KEY = booleanPreferencesKey("THEME_PREF_KEY")
}