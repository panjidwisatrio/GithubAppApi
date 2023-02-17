package com.panjidwisatrio.github.data.remote

import com.panjidwisatrio.github.model.Search
import com.panjidwisatrio.github.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// bagian dari penggunaan Retrofit untuk mengakses REST API.
interface ApiService {
    // Membuat fungsi untuk mengambil data user
    @GET("search/users")
    fun searchUsers (
        // Membuat parameter untuk mengirimkan query pencarian
        @Query("q")
        query: String
    ): Call<Search> // Membuat objek Call dengan tipe data Search

    @GET("users/{username}")
    fun getDetailUser (
        @Path("username")
        username: String
    ): Call<User>

    @GET("users/{username}/followers")
    fun getUserFollowers (
        @Path("username")
        username: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    fun getUserFollowing (
        @Path("username")
        username: String
    ): Call<List<User>>
}