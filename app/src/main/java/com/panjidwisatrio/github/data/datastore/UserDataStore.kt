package com.panjidwisatrio.github.data.datastore

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserDataStore(private val context: Context) {

    // objek yang terkait dengan kelas yang dapat diakses dimana saja tanpa harus membuat objek baru
    companion object {
        /* Menekan laporan warning mengenai leak memory (kebocoran memori)
           karena menempatkan konteks di dalam kelas singleton. */
        @SuppressLint("StaticFieldLeak")
        // memastikan bahwa instance mInstance selalu dapat diakses dengan benar di thread yang berbeda.
        @Volatile
        private var mInstance: UserDataStore? = null

        // memeriksa apakah instance dari mInstance sudah ada.
        fun getInstance(context: Context): UserDataStore =
            // (synchronized) memastikan bahwa variabel mInstance hanya dapat diakses oleh satu thread pada satu waktu
            mInstance ?: synchronized(this) {
                // Jika instance dari mInstance belum ada, maka akan dibuat.
                val newInstance = mInstance ?: UserDataStore(context).also { mInstance = it }
                // Mengembalikan instance yang baru dibuat.
                newInstance
            }
    }

    // Inisisasi DataStore dengan nama yang telah ditentukan di kelas DataStoreUtil.
    private val Context.userPreferenceDataStore: DataStore<Preferences> by preferencesDataStore(
        name = DataStoreUtil.DATA_STORE_NAME
    )

    // menyimpan data ke dalam DataStore.
    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        context.userPreferenceDataStore.edit {
            // Menyimpan preferensi tema yang telah ditentukan menggunakan kunci yang telah ditentukan di kelas DataStoreUtil.
            it[DataStoreUtil.DATA_STORE_PREF_THEME_KEY] = isDarkModeActive
        }
    }

    // Mengambil preferensi tema yang telah ditentukan menggunakan kunci yang telah ditentukan di kelas DataStoreUtil.
    fun getThemeSetting(): Flow<Boolean> =
        context.userPreferenceDataStore.data.map {
            //  Jika nilai tersebut null, maka fungsi map akan mengembalikan nilai false.
            it[DataStoreUtil.DATA_STORE_PREF_THEME_KEY] ?: false
        }
}