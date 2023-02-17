package com.panjidwisatrio.github.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.panjidwisatrio.github.model.User

// mendefinisikan tabel yang akan digunakan pada aplikasi menggunakan kelas User.
@Database(entities = [User::class], exportSchema = false, version = 1)
abstract class UserDatabase: RoomDatabase() {

    // Mendefinisikan DAO yang akan digunakan pada aplikasi.
    abstract fun userDao(): UserDao

    companion object {
        // Memastikan bahwa hanya ada satu instance dari database pada aplikasi.
        @Volatile
        // Menyimpan objek UserDatabase yang akan diakses oleh seluruh aplikasi.
        private var INSTANCE: UserDatabase? = null

        // fungsi tersebut akan diterjemahkan sebagai sebuah fungsi static di dalam kode bytecode Java,
        // sehingga dapat diakses tanpa harus membuat instance dari kelas UserDatabase.
        @JvmStatic
        fun getInstance(context: Context): UserDatabase {
            // Jika instance dari database belum ada, maka akan dibuat.
            if (INSTANCE == null) {
                // Memastikan bahwa hanya ada satu thread yang dapat mengakses database pada satu waktu.
                synchronized(UserDatabase::class.java) {
                    // Membuat instance dari database.
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        UserDatabase::class.java,
                        "User.db"
                    ).build()
                }
            }
            // Mengembalikan instance dari database.
            return INSTANCE as UserDatabase
        }
    }

}