package com.panjidwisatrio.github.data

/* memungkinkan kita untuk membuat sebuah kelas abstrak yang hanya bisa diwarisi oleh
   kelas-kelas tertentu di dalamnya (dalam hal ini disebut "sub-kelas"). */
sealed class Resource<T>(val data: T?  = null, val message: String? = null) {
    class Success<T>(data: T?): Resource<T>(data)
    class Error<T>(message: String?, data: T? = null): Resource<T>(data, message)
    class Loading<T>(data: T? = null): Resource<T>(data)
}