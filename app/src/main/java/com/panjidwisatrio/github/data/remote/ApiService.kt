package com.panjidwisatrio.github.data.remote

import com.panjidwisatrio.github.model.Search
import com.panjidwisatrio.github.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// bagian dari penggunaan Retrofit untuk mengakses REST API.
interface ApiService {
    // Membuat fungsi untuk mengambil data user
    @GET("search/users")
    suspend fun searchUsers (
        // Membuat parameter untuk mengirimkan query pencarian
        @Query("q")
        query: String
    ): Response<Search>

    @GET("users/{username}")
    suspend fun getDetailUser (
        @Path("username")
        username: String
    ): Response<User>

    @GET("users/{username}/followers")
    suspend fun getUserFollowers (
        @Path("username")
        username: String
    ): Response<List<User>>

    @GET("users/{username}/following")
    suspend fun getUserFollowing (
        @Path("username")
        username: String
    ): Response<List<User>>
}