package com.panjidwisatrio.github.data.local

import androidx.room.*
import com.panjidwisatrio.github.model.User

/* Data Access Object pada Room.
   mengakses data dari database SQLite pada aplikasi Android dengan cara yang lebih mudah dan intuitif. */
@Dao
interface UserDao {
    @Query("SELECT * FROM user ORDER BY username ASC")
    suspend fun getFavoriteListUser(): List<User>

    @Query("SELECT * FROM user WHERE username = :username")
    suspend fun getFavoriteDetailUser(username: String): User?

    // Jika ada data yang sama, maka data yang lama akan diganti dengan data yang baru.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteUser(user: User)

    @Delete
    suspend fun deleteFavoriteUser(user: User)
}