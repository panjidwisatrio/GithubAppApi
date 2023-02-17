package com.panjidwisatrio.github.data.remote

import com.panjidwisatrio.github.util.Constanta.BASE_URL
import com.panjidwisatrio.github.util.Constanta.TOKEN
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitService {
    // Membuat fungsi client untuk mengirimkan permintaaan ke server
    private fun client(): OkHttpClient =
        OkHttpClient.Builder()
            // Menambahkan header ke setiap permintaan yang dikirimkan ke server.
            .addInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .addHeader("Authorization", TOKEN)
                val request = requestBuilder.build()
                it.proceed(request)
            }
            // Menentukan batas waktu koneksi yang dibutuhkan untuk mengirimkan permintaan ke server
            .connectTimeout(20, TimeUnit.SECONDS)
            // Menentukan batas waktu koneksi yang dibutuhkan untuk menerima respon dari server
            .readTimeout(20, TimeUnit.SECONDS)
            // Membuat objek OkHttpClient
            .build()

    // Membuat fungsi create untuk membuat objek retrofit
    fun create(): ApiService =
        Retrofit.Builder()
            // Menentukan base url dari server
            .baseUrl(BASE_URL)
            // Menentukan client yang akan digunakan untuk mengirimkan permintaan ke server
            .client(client())
            // Menentukan converter yang akan digunakan untuk mengubah data dari json ke objek
            .addConverterFactory(MoshiConverterFactory.create())
            // Membuat objek retrofit
            .build()
            // Membuat objek dari interface ApiService
            .create(ApiService::class.java)
}